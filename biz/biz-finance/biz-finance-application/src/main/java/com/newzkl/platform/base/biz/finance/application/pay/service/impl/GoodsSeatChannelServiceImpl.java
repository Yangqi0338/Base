package com.newzkl.platform.base.biz.finance.application.pay.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.application.pay.service.CashPayService;
import com.newzkl.platform.base.biz.finance.application.pay.service.GoodsSeatChannelService;
import com.newzkl.platform.base.biz.finance.application.pay.service.PurchaseRecordService;
import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.StorePackageApi;
import com.newzkl.platform.base.biz.finance.domain.pay.service.PurchaseRecordDomain;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordReq;
import com.newzkl.platform.base.biz.finance.model.pay.res.SeatPackageOrderInfo;
import com.newzkl.platform.base.common.ddd.facade.BalancePayResult;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuPayRes;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;
import com.newzkl.platform.base.biz.finance.model.purse.req.ChannelPurchaseGoodsSeatReq;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;
import com.newzkl.platform.base.biz.finance.model.support.SeatPackageApiVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 渠道商购买商品位编排实现
 *
 * <p>逐行迁移自 new-scm {@code BalancePayApiImpl.channelPurchaseGoodsSeat}。跨域件映射:
 * 源 {@code storeFacade.seatPackageVO} → {@code StorePackageApi} 出站端口;
 * 源 {@code dictFacade.get(CHANNEL_CONFIG)} → {@code AccountPurseConfigDomain.defaultChannelConfig};
 * 源 {@code purchaseRecordAction.seatPackageSaveOrUpdate} → {@code PurchaseRecordService};
 * 源 {@code iOrderPayApi.orderPay} → {@code CashPayService.orderPay};
 * 源 {@code purchaseRecordDomain.add} → {@code PurchaseRecordDomain.add}</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsSeatChannelServiceImpl implements GoodsSeatChannelService {

    private final StorePackageApi storePackageApi;
    private final AccountPurseConfigDomain accountPurseConfigDomain;
    private final PurchaseRecordService purchaseRecordService;
    private final PurchaseRecordDomain purchaseRecordDomain;
    private final CashPayService cashPayService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayBaseResult channelPurchaseGoodsSeat(ChannelPurchaseGoodsSeatReq req) {
        Long channelId = req.getChannelId();
        Long seatPackageId = req.getSeatPackageId();
        PaymentEnum.PayType payType = req.getPayType();
        Integer purchaseNum = req.getPurchaseNum();

        if (seatPackageId != null && seatPackageId != 0L) {
            // 选定套餐: 数量与价格取套餐 (跨域 biz-store)
            SeatPackageApiVO seatPackage = storePackageApi.seatPackageVO(seatPackageId);
            if (seatPackage == null) {
                throw new PlatformException(BaseErrorCode.PARAM, "错误的席位套餐");
            }
            req.setPurchaseNum(seatPackage.getSeatNum());
            req.setPurchasePrice(seatPackage.getPackagePrice());
        } else {
            // 自定义套餐: 校验最小量, 单价取渠道配置原价
            if (purchaseNum == null || purchaseNum <= 0) {
                throw new PlatformException(BaseErrorCode.PARAM, "购买数量不大于0");
            }
            ChannelConfigVO channelConfig = accountPurseConfigDomain.defaultChannelConfig();
            if (channelConfig == null) {
                log.info("channelPurchaseGoodsSeat - 未配置CHANNEL_CONFIG全局配置");
                throw new PlatformException(BaseErrorCode.SERVER);
            }
            Integer purchaseMinimumNum = channelConfig.getPurchaseMinimumNum();
            Money seatOriginalPrice = channelConfig.getSeatOriginalPrice();
            if (NumberUtil.compare(purchaseNum, purchaseMinimumNum) < 0) {
                throw new PlatformException(BaseErrorCode.PARAM, "购买数量不能小于最小购买数量" + purchaseMinimumNum);
            }
            req.setSeatPackageName("自定义套餐");
            req.setPurchasePrice(seatOriginalPrice.multiply(purchaseNum));
        }

        Money totalFee = req.getPurchasePrice();
        if (!totalFee.greaterThanZero()) {
            throw new PlatformException(BaseErrorCode.PARAM, "实付金额不大于0");
        }

        PurchaseRecordReq saveCommand = buildPurchaseRecordSaveCommand(totalFee, req);
        PayBaseResult res;
        if (PaymentEnum.PayType.PURCHASE == payType) {
            // 采购金抵扣: 即时结算成功
            BalancePayResult payResult = new BalancePayResult();
            payResult.setPayState(true);
            res = payResult;

            purchaseRecordService.seatPackageSaveOrUpdate(saveCommand);
        } else {
            // 微信 / 支付宝: 拉起三方支付, 落待付款记录
            OrderPayReq orderPayReq = new OrderPayReq();
            orderPayReq.setOrderNo(SnowflakeGenerator.getSnowflakeId());
            orderPayReq.setConsumeType(EarningsEnum.ConsumeType.GOODS_SEAT);
            orderPayReq.setOrderAmount(totalFee);
            orderPayReq.setPayAmount(totalFee);
            // totalFee 已为 Money, orderAmount/payAmount 同为 Money, 直传
            orderPayReq.setOrderInfo(JSONUtil.toJsonStr(TransferUtils.transfer(req, SeatPackageOrderInfo::new)));
            orderPayReq.setGoodsInfo("商品席位购买");
            orderPayReq.setAccountId(channelId);
            orderPayReq.setAccountName(SecurityUtils.getUsername());
            orderPayReq.setPayType(payType);
            HuiFuPayRes payRes = cashPayService.orderPay(orderPayReq);
            res = payRes;

            // 修正购买记录数据
            saveCommand.setOrderNo(orderPayReq.getOrderNo());
            saveCommand.setTradeNo(payRes.getTradeNo());
            saveCommand.setPayState(OrderEnum.State.CHANNEL_WAIT_PAY);
            saveCommand.setTripartiteTradeNo(payRes.getThirdTradeNo());

            purchaseRecordDomain.add(saveCommand);
        }

        return res;
    }

    /**
     * 组装席位购买记录入参
     *
     * @param totalFee 实付金额
     * @param req      购买入参
     * @return 购买记录入参
     */
    private PurchaseRecordReq buildPurchaseRecordSaveCommand(Money totalFee, ChannelPurchaseGoodsSeatReq req) {
        String orderInfo = JSONUtil.toJsonStr(TransferUtils.transfer(req, SeatPackageOrderInfo::new));

        PurchaseRecordReq saveCommand = new PurchaseRecordReq();
        saveCommand.setPurchaseNo(BusinessCodeUtil.generate(BusinessType.ORDER_SEAT_PACKAGE));
        saveCommand.setType(PurseEnum.PurchaseRecordType.SEAT_PACKAGE.getCode());
        saveCommand.setTradeNo(0L);
        saveCommand.setOrderNo(0L);
        saveCommand.setAccountId(req.getChannelId());
        saveCommand.setAccountName(SecurityUtils.getUsername());
        saveCommand.setPayAmount(totalFee);
        saveCommand.setGoodsAmount(req.getPurchasePrice().multiply(req.getPurchaseNum()));
        saveCommand.setPayType(req.getPayType());
        saveCommand.setPayState(OrderEnum.State.SUCCESS);
        saveCommand.setTripartiteTradeNo("0");
        saveCommand.setOrderInfo(orderInfo);
        return saveCommand;
    }
}

