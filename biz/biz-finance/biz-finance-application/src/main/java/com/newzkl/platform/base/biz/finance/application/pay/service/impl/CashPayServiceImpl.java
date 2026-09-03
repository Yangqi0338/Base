package com.newzkl.platform.base.biz.finance.application.pay.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.application.pay.service.CashPayService;
import com.newzkl.platform.base.biz.finance.application.pay.service.PurchaseRecordService;
import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.CourseApi;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.NotifyApi;
import com.newzkl.platform.base.biz.finance.domain.hf.HuiFuMethod;
import com.newzkl.platform.base.biz.finance.domain.pay.service.OrderPayDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.common.ddd.facade.ChannelRegisterReq;
import com.newzkl.platform.base.biz.finance.model.pay.req.StoreRegisterReq;
import com.newzkl.platform.base.biz.finance.model.pay.res.StoreOrderInfo;
import com.newzkl.platform.base.common.core.model.enums.CacheKey;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.common.core.redis.aspect.DistributedLock;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordReq;
import com.newzkl.platform.base.biz.finance.model.pay.req.huifu.HuiFuPayReq;
import com.newzkl.platform.base.biz.finance.model.pay.res.TradeOrderInfoRes;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuPayRes;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordReq;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.HuifuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * 现金支付编排实现
 *
 * @author niu
 */
@Service
@RequiredArgsConstructor
public class CashPayServiceImpl implements CashPayService {

    /**
     * 支付单待回调状态过期分钟数
     */
    private static final long PAY_STATE_EXPIRE_MINUTES = 15L;

    /**
     * 支付结果缓存过期分钟数
     */
    private static final long PAY_CACHE_EXPIRE_MINUTES = 60L;
    private final OrderPayDomain orderPayService;
    private final AccountPurseDomain accountPurseService;
    private final AccountPurseConfigDomain accountPurseConfigService;
    private final PurchaseRecordService purchaseRecordAction;
    private final NotifyApi notifyApi;
    private final CourseApi courseApi;
    private final AccountApi accountApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterPayState(String tradeNo, String thirdOrderNo) {
        // 1、更新支付状态,flag表示是否更新成功
        boolean flag = orderPayService.alterPayState(tradeNo, thirdOrderNo);
        if (flag) {
            TradeOrderInfoRes tradeOrder = orderPayService.tradeOrderQuery(tradeNo);
            Long accountId = tradeOrder.getAccountId();
            EarningsEnum.ConsumeType consumeType = tradeOrder.getConsumeType();
            switch (consumeType) {
                case RECHARGE:
                    // 2、增加账户余额 + 变动记录修改
                    AccountPurseAlterRecordReq recharge = buildAccountPurseAlterRecord(tradeOrder,
                            PurseEnum.User.CHANNEL,
                            PurseEnum.Type.PURCHASE,
                            PurseEnum.AlterType.RECHARGE);
                    accountPurseService.addAmount(recharge);
                    // 3、渠道商采购金充值后，更新服务费
                    accountPurseConfigService.alterChannelNowChargeConfig(accountId, (int) tradeOrder.getPayAmount().getCent());
                    break;
                case SUPPLIER_RECHARGE:
                    // 2、增加收益余额 + 变动记录修改
                    AccountPurseAlterRecordReq supplierRecharge = buildAccountPurseAlterRecord(tradeOrder,
                            PurseEnum.User.SUPPLIER,
                            PurseEnum.Type.MARKETING,
                            PurseEnum.AlterType.SUPPLIER_OPERATOR_RECHARGE);
                    accountPurseService.addAmount(supplierRecharge);
                    break;
                case GOODS_SEAT:
                    // 供应商购买商品位只能扣采购金,所以不会到这里
                    PurchaseRecordReq saveCommand = new PurchaseRecordReq();
                    saveCommand.setPayState(OrderEnum.State.SUCCESS);
                    saveCommand.setTradeNo(null);
                    purchaseRecordAction.seatPackageSaveOrUpdate(saveCommand);
                    break;
                case STORE:
                    StoreOrderInfo storeOrderInfo = JSONUtil.toBean(tradeOrder.getOrderInfo(), StoreOrderInfo.class);
                    if (storeOrderInfo == null) {
                        return;
                    }
                    String storeName = storeOrderInfo.getStoreName();
                    Long storeType = storeOrderInfo.getStoreType();
                    String address = storeOrderInfo.getAddress();

                    // 创建渠道商
                    ChannelRegisterReq req = new ChannelRegisterReq();
                    req.setAccountId(accountId);
                    req.setIdentity(storeOrderInfo.getIsChannel() != Boolean.TRUE
                            ? AccountEnum.Identity.MEMBER
                            : AccountEnum.Identity.CHANNEL);
                    req.setStorePermission(CommonEnum.YesOrNo.YES);
                    req.setContactName(storeOrderInfo.getContactName());
                    req.setStoreName(storeName);
                    req.setContactPhone(storeOrderInfo.getContactPhone());

                    accountApi.registerChannel(req);
//                    roleFacade.registerChannel(req);

                    // 创建门店
                    StoreRegisterReq storeRegisterReq = new StoreRegisterReq();
                    storeRegisterReq.setChannelId(accountId);
                    storeRegisterReq.setStoreType(storeType);
                    storeRegisterReq.setStoreName(storeName);
                    storeRegisterReq.setAddress(address);
//                    storeFacade.openStore(storeRegisterReq);
                    break;
                case COURSE:
                    courseApi.paySuccess(tradeOrder.getOrderNo(),thirdOrderNo);
                    break;
                default:
                    // 2、其他消费支付成功后发送mq消息
                    notifyApi.paySuccess(tradeNo);
                    break;
            }
        }
    }

    @Override
    @DistributedLock(key = "'orderPay:' + #req.orderNo")
    @Transactional(rollbackFor = Exception.class)
    public HuiFuPayRes orderPay(OrderPayReq req) {
        // 1、命中支付结果缓存则直接返回, 避免同一订单重复拉起三方支付
        HuiFuPayRes cached = RedisUtil.get(RedisEnum.Key.ORDER_PAY_CACHE_PRE.getCode(req.getOrderNo()));
        if (cached != null) {
            return cached;
        }
        // 2、落库业务支付单; 收款方分账信息暂不启用, 传 null
        String tradeNo = orderPayService.saveOrderPayRecord(req, null);
        // 3、请求汇付聚合正扫
        HuiFuPayRes payRes = HuiFuMethod.pay(buildHuiFuPayReq(req, tradeNo));
        // 4、三方受理成功则回填三方交易号
        if (StrUtil.isNotBlank(payRes.getTripartiteNo())) {
            orderPayService.resetTripartiteTradeNo(tradeNo, payRes.getTripartiteNo());
        }
        // 5、标记支付单待回调状态, 15 分钟过期
        RedisUtil.set(StrUtil.format(CacheKey.PAYMENT_STATE, req.getConsumeType().getType(), payRes.getTradeNo()),
                1L, PAY_STATE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        // 6、缓存三方支付结果
        RedisUtil.set(RedisEnum.Key.ORDER_PAY_CACHE_PRE.getCode(req.getOrderNo()), payRes, PAY_CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        return payRes;
    }

    /**
     * 组装汇付支付入参
     *
     * <p>汇付侧的日期、金额换算、回调地址已由 {@code HuiFuMethod} 内部处理, 此处只做支付方式映射。</p>
     */
    private HuiFuPayReq buildHuiFuPayReq(OrderPayReq req, String tradeNo) {
        HuiFuPayReq huiFuPayReq = new HuiFuPayReq();
        huiFuPayReq.setTradeNo(tradeNo);
        huiFuPayReq.setGoodsInfo(req.getGoodsInfo());
        // HuiFu 边界: Money → 分 Integer
        huiFuPayReq.setPayAmount((int) req.getPayAmount().getCent());
        huiFuPayReq.setTradeType(switch (req.getPayType()) {
            case WX -> HuifuEnum.HuiFuTradeType.T_NATIVE;
            case ALIPAY -> HuifuEnum.HuiFuTradeType.A_NATIVE;
            default -> throw new PlatformException(BaseErrorCode.PARAM);
        });
        return huiFuPayReq;
    }

    private AccountPurseAlterRecordReq buildAccountPurseAlterRecord(TradeOrderInfoRes tradeOrder,
                                                                    PurseEnum.User accountType,
                                                                    PurseEnum.Type purseType,
                                                                    PurseEnum.AlterType alterType) {
        AccountPurseAlterRecordReq req = new AccountPurseAlterRecordReq();
        req.setAccountId(tradeOrder.getAccountId());
        req.setPurseType(purseType);
        req.setAccountType(accountType);
        req.setAlterType(alterType);
        req.setAmount(tradeOrder.getPayAmount());
        req.setJoinRecordId(tradeOrder.getId());
        req.setRemark(JSONUtil.toJsonStr(tradeOrder));
        return req;
    }
}
