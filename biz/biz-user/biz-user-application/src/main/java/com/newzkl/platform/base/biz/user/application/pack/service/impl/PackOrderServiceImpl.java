package com.newzkl.platform.base.biz.user.application.pack.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.newzkl.platform.base.biz.user.application.pack.service.PackOrderService;
import com.newzkl.platform.base.biz.user.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.user.domain.adapt.api.PackUpCheckCommand;
import com.newzkl.platform.base.biz.user.domain.adapt.api.PayApi;
import com.newzkl.platform.base.biz.user.domain.adapt.api.PayResultDTO;
import com.newzkl.platform.base.biz.user.domain.pack.entity.PackOrder;
import com.newzkl.platform.base.biz.user.domain.service.PackGoodsDomain;
import com.newzkl.platform.base.biz.user.domain.service.PackOrderDomain;
import com.newzkl.platform.base.biz.user.model.pack.enums.PackOrderStateEnum;
import com.newzkl.platform.base.biz.user.model.pack.query.PackOrderQuery;
import com.newzkl.platform.base.biz.user.model.pack.req.PackOrderCommand;
import com.newzkl.platform.base.biz.user.model.pack.req.PackOrderPayReq;
import com.newzkl.platform.base.biz.user.model.pack.res.PackGoodsRes;
import com.newzkl.platform.base.biz.user.model.pack.res.PackOrderPreRes;
import com.newzkl.platform.base.biz.user.model.pack.res.PackOrderRes;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.common.ddd.model.constant.PackOrderErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * 入会礼包订单应用服务实现（跨域编排）
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.application.service.impl.PackOrderServiceImpl}。</p>
 *
 * <p>迁移说明：</p>
 * <ul>
 *   <li>收货地址查询：源经 {@code IShipAddressFacade} 跨域，本仓收货同域，此处沿源实际逻辑传空
 *       {@code ShipAddressVO}（源亦注释掉了地址装配，仅 new 空对象）。</li>
 *   <li>账号升级校验/账号查询/支付：经本域出站端口 {@code AccountLevelApi}/{@code AccountQueryApi}/
 *       {@code OrderPayApi}（默认兜底，starter 远程接线）。</li>
 *   <li>预单缓存：源 {@code RedisClient.setCacheObject} 换 {@code RedisUtil.set}（30 分钟 TTL）。</li>
 *   <li>{@code BeanUtil.copyProperties} 换 {@code TransferUtils}。</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PackOrderServiceImpl implements PackOrderService {

    /**
     * 预单缓存 TTL（分钟）
     */
    private static final long PRE_ORDER_CACHE_MINUTES = 30L;

    /**
     * mustSingle 去重窗口（分钟）
     */
    private static final int SINGLE_DEDUP_MINUTES = 10;

    private final PackGoodsDomain packGoodsDomain;

    private final PackOrderDomain packOrderDomain;

    private final AccountApi accountApi;

    private final PayApi payApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PackOrderPreRes packOrderCreate(PackOrderCommand command) {
        Long packId = command.getId();
        PackGoodsRes packGoods = packGoodsDomain.packGoodsVO(packId);
        if (packGoods == null) {
            throw new PlatformException(PackOrderErrorCode.GOODS_DOWN);
        }

        // 升级校验（出站端口，默认放行）
        PackUpCheckCommand checkCommand = new PackUpCheckCommand();
        checkCommand.setAccountId(SecurityUtils.getAccountId());
        checkCommand.setPackGoodsId(packGoods.getId());
        checkCommand.setAmount(packGoods.getAmount());
        checkCommand.setLevel(packGoods.getLevel());
        checkCommand.setType(packGoods.getType());
        Integer checkState = accountApi.packUpCheck(checkCommand);
        if (checkState != null && checkState == -1) {
            throw new PlatformException(PackOrderErrorCode.ALREADY_LEVEL);
        }
        if (checkState == null || checkState != 1) {
            throw new PlatformException(PackOrderErrorCode.LEVEL_UP_ERROR);
        }

        // mustSingle 去重：10 分钟内同 packId 待支付单直接复用
        if (command.isMustSingle()) {
            PackOrderQuery query = new PackOrderQuery();
            query.setAccountId(SecurityUtils.getAccountId());
            query.setState(PackOrderStateEnum.WAIT_PAY.getCode());
            query.setPackId(packId);
            query.setCreateTimeGreater(DateUtil.offset(DateUtil.date(), DateField.MINUTE, -SINGLE_DEDUP_MINUTES).toLocalDateTime());
            query.resetQuerySingle();
            PackOrderRes exist = CollUtil.getFirst(packOrderDomain.packOrderVOList(query).getRecords());
            if (exist != null) {
                return TransferUtils.transfer(exist, PackOrderPreRes::new);
            }
        }

        // 组装预单（收货同源逻辑传空，源亦仅 new 空 ShipAddressOutVO）
        PackOrder packOrder = packOrderDomain.packOrderCreate(command, null, CollUtil.newArrayList(packGoods));
        RedisUtil.set(packOrder.getId().toString(), packOrder, PRE_ORDER_CACHE_MINUTES, TimeUnit.MINUTES);
        return TransferUtils.transfer(packOrder, PackOrderPreRes::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long packOrderSubmit(Long orderId) {
        PackOrder packOrder = RedisUtil.get(orderId.toString());
        if (packOrder == null) {
            throw new PlatformException(PackOrderErrorCode.ORDER_TIME_OUT);
        }
        packOrderDomain.packOrderCreate(packOrder);
        RedisUtil.del(orderId.toString());
        return orderId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayResultDTO packOrderPay(PackOrderPayReq payReq) {
        Long orderId = payReq.getOrderId();
        Money amount;
        Integer packLevel;
        Integer packType;
        String levelName;
        Long accountId;
        if (orderId == null) {
            PackOrderCommand command = new PackOrderCommand();
            command.setId(payReq.getPackId());
            command.setMustSingle(true);
            PackOrderPreRes preRes = packOrderCreate(command);
            orderId = preRes.getId();
            payReq.setOrderId(orderId);
            packOrderSubmit(orderId);
            amount = preRes.getAmount();
            packLevel = preRes.getPackLevel();
            packType = preRes.getPackType();
            levelName = preRes.getLevelName();
            accountId = preRes.getAccountId();
        } else {
            PackOrderRes orderRes = packOrderDomain.packOrderVO(orderId);
            amount = orderRes.getAmount();
            packLevel = orderRes.getPackLevel();
            packType = orderRes.getPackType();
            levelName = orderRes.getLevelName();
            accountId = orderRes.getAccountId();
        }

        AccountGroupVO accountInfo = accountApi.accountInfo(accountId);

        OrderPayReq apiPayReq = new OrderPayReq();
        apiPayReq.setOrderNo(orderId);
        apiPayReq.setConsumeType(EarningsEnum.ConsumeType.PICK_PACK);
        apiPayReq.setOrderAmount(amount);
        apiPayReq.setPayAmount(amount);
        RoleEnum.CompanyRole companyRole = packType == null ? null : RoleEnum.CompanyRole.getByCode(packType.longValue());
        apiPayReq.setOrderInfo((companyRole == null ? "" : companyRole.getValue()) + "礼包");
        apiPayReq.setGoodsInfo(String.format("等级: %s, 名称:%s", packLevel, levelName));
        apiPayReq.setAccountId(accountId);
        if (accountInfo != null) {
            apiPayReq.setAccountMobile(accountInfo.getPhone());
            apiPayReq.setRegisterTime(accountInfo.getCreateTime() == null ? null
                    : DateUtil.format(java.util.Date.from(accountInfo.getCreateTime()
                    .atZone(java.time.ZoneId.systemDefault()).toInstant()), "yyyyMMddHHmmss"));
            apiPayReq.setAccountName(accountInfo.getNickname());
        }
        apiPayReq.setPayType(payReq.getPayType());

        return payApi.packOrderPay(apiPayReq);
    }
}
