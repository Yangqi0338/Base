package com.newzkl.platform.base.biz.finance.application.pay.service.impl;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.application.pay.service.CashPayService;
import com.newzkl.platform.base.biz.finance.application.pay.service.GoodsSeatSupplierService;
import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.domain.adapt.api.SeatPackageApi;
import com.newzkl.platform.base.biz.finance.domain.pay.service.PurchaseRecordDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.GoodsSeatDomain;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordReq;
import com.newzkl.platform.base.biz.finance.model.pay.res.SeatPackageOrderInfo;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuPayRes;
import com.newzkl.platform.base.biz.finance.model.purse.req.SupplierPurchaseGoodsSeatReq;
import com.newzkl.platform.base.biz.finance.model.support.SeatPackageApiVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.base.common.ddd.facade.BalancePayResult;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 供应商购买商品位编排实现
 *
 * <p>对齐渠道商 {@code GoodsSeatChannelServiceImpl}: 跨域取套餐 (biz-store)、读供应商配置
 * 校验最小量与单价、按支付方式走营销金即时结算或三方拉起支付, 落购买记录。</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsSeatSupplierServiceImpl implements GoodsSeatSupplierService {

    private final SeatPackageApi seatPackageApi;
    private final AccountPurseConfigDomain accountPurseConfigDomain;
    private final PurchaseRecordDomain purchaseRecordDomain;
    private final CashPayService cashPayService;
    private final GoodsSeatDomain goodsSeatDomain;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayBaseResult supplierPurchaseGoodsSeat(SupplierPurchaseGoodsSeatReq req) {
        Long supplierId = req.getSupplierId();
        Long seatPackageId = req.getSeatPackageId();
        PaymentEnum.PayType payType = req.getPayType();
        Integer purchaseNum = req.getPurchaseNum();

        if (seatPackageId != null && seatPackageId != 0L) {
            // 选定套餐: 数量与价格取套餐 (跨域 biz-goods)
            SeatPackageApiVO seatPackage = seatPackageApi.seatPackageVO(seatPackageId);
            if (seatPackage == null) {
                throw new PlatformException(BaseErrorCode.PARAM, "错误的席位套餐");
            }
            req.setPurchaseNum(seatPackage.getSeatNum());
            req.setPurchasePrice(seatPackage.getPackagePrice());
        } else {
            // 自定义套餐: 校验最小量, 单价取供应商配置商品位费用
            if (purchaseNum == null || purchaseNum <= 0) {
                throw new PlatformException(BaseErrorCode.PARAM, "购买数量不大于0");
            }
            ConfigSupplierVO supplierConfig = accountPurseConfigDomain.querySupplierConfig();
            if (supplierConfig == null) {
                log.info("supplierPurchaseGoodsSeat - 未配置SUPPLIER_CONFIG全局配置");
                throw new PlatformException(BaseErrorCode.SERVER);
            }
            Integer purchaseMinimumNum = supplierConfig.getPurchaseMinimumNum();
            if (purchaseMinimumNum != null && purchaseNum < purchaseMinimumNum) {
                throw new PlatformException(BaseErrorCode.PARAM, "购买数量不能小于最小购买数量" + purchaseMinimumNum);
            }
            req.setSeatPackageName("自定义套餐");
            req.setPurchasePrice(Money.of(supplierConfig.getSkuSpaceFee()).multiply(purchaseNum));
        }

        Money totalFee = req.getPurchasePrice();
        if (!totalFee.greaterThanZero()) {
            throw new PlatformException(BaseErrorCode.PARAM, "实付金额不大于0");
        }

        PurchaseRecordReq saveCommand = buildPurchaseRecordSaveCommand(totalFee, req);
        PayBaseResult res;
        if (PaymentEnum.PayType.PURCHASE == payType) {
            // 营销金即时结算: 扣营销金 + 加商品位额度 (领域动账)
            if (!Boolean.TRUE.equals(goodsSeatDomain.supplierPurchaseGoodsSeat(req))) {
                throw new PlatformException(BaseErrorCode.PARAM, "营销金余额不足");
            }
            BalancePayResult payResult = new BalancePayResult();
            payResult.setPayState(true);
            res = payResult;

            purchaseRecordDomain.add(saveCommand);
        } else {
            // 微信 / 支付宝: 拉起三方支付, 落待付款记录
            OrderPayReq orderPayReq = new OrderPayReq();
            orderPayReq.setOrderNo(SnowflakeGenerator.getSnowflakeId());
            orderPayReq.setConsumeType(EarningsEnum.ConsumeType.GOODS_SEAT);
            orderPayReq.setOrderAmount(totalFee);
            orderPayReq.setPayAmount(totalFee);
            orderPayReq.setOrderInfo(JSONUtil.toJsonStr(TransferUtils.transfer(req, SeatPackageOrderInfo::new)));
            orderPayReq.setGoodsInfo("商品席位购买");
            orderPayReq.setAccountId(supplierId);
            orderPayReq.setAccountName(SecurityUtils.getUsername());
            orderPayReq.setPayType(payType);
            HuiFuPayRes payRes = cashPayService.orderPay(orderPayReq);
            res = payRes;

            saveCommand.setOrderNo(orderPayReq.getOrderNo());
            saveCommand.setTradeNo(payRes.getTradeNo());
            saveCommand.setPayState(OrderEnum.State.CHANNEL_WAIT_PAY);
            saveCommand.setTripartiteTradeNo(payRes.getThirdTradeNo());

            purchaseRecordDomain.add(saveCommand);
        }

        return res;
    }

    /**
     * 组装供应商席位购买记录入参
     *
     * @param totalFee 实付金额
     * @param req      购买入参
     * @return 购买记录入参
     */
    private PurchaseRecordReq buildPurchaseRecordSaveCommand(Money totalFee, SupplierPurchaseGoodsSeatReq req) {
        String orderInfo = JSONUtil.toJsonStr(TransferUtils.transfer(req, SeatPackageOrderInfo::new));

        PurchaseRecordReq saveCommand = new PurchaseRecordReq();
        saveCommand.setPurchaseNo(BusinessCodeUtil.generate(BusinessType.ORDER_SEAT_PACKAGE));
        saveCommand.setType(PurseEnum.PurchaseRecordType.SEAT_PACKAGE.getCode());
        saveCommand.setTradeNo(BusinessCodeUtil.generate(BusinessType.ORDER_SEAT_PACKAGE));
        saveCommand.setOrderNo(0L);
        saveCommand.setAccountId(req.getSupplierId());
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
