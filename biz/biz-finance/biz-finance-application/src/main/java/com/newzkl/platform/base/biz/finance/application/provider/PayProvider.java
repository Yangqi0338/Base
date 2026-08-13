package com.newzkl.platform.base.biz.finance.application.provider;

import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.domain.pay.service.OrderPayDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.facade.PayFacade;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.common.ddd.facade.SupplierSettleReq;
import com.newzkl.platform.base.common.ddd.facade.ChannelSettleReq;
import com.newzkl.platform.base.common.ddd.facade.MemberRefundRes;
import com.newzkl.platform.base.common.ddd.facade.SellAfterRefundReq;
import com.newzkl.platform.base.common.core.model.enums.CacheKey;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.biz.finance.model.pay.res.TradeOrderInfoRes;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordReq;
import com.newzkl.platform.base.biz.finance.model.purse.vo.AccountPurseAlterRecordVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.redis.aspect.DistributedLock;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.BalancePayReq;
import com.newzkl.platform.base.common.ddd.facade.BalancePayResult;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;
import com.newzkl.platform.base.common.ddd.model.enums.finance.FinanceEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author niu
 * @description: 余额支付api
 * @date 2024/1/22 10:10
 */
@DubboService
@Component
@Slf4j
@RequiredArgsConstructor
public class PayProvider implements PayFacade {

    private final OrderPayDomain orderPayDomain;
    private final AccountPurseDomain purseDomain;
    private final AccountPurseConfigDomain purseConfigDomain;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(key = "'orderPay:' + #req.orderNo")
    public PayBaseResult orderPay(OrderPayReq req) {
        Long tradeNo = orderPayDomain.saveOrderPayRecord(req, null);

        PayBaseResult payBaseResult = null;

        String thirdTradeNo = payBaseResult.getThirdTradeNo();
        if (StrUtil.isNotBlank(thirdTradeNo)) {
            orderPayDomain.resetTripartiteTradeNo(tradeNo, thirdTradeNo);
        }

        String redisKey = StrUtil.format(CacheKey.PAYMENT_STATE, req.getConsumeType().getType(), payBaseResult.getTradeNo());
        RedisUtil.set(redisKey, 1L);
        RedisUtil.expire(redisKey, 15, TimeUnit.MINUTES);

        return payBaseResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BalancePayResult balancePay(BalancePayReq req) {
        // 是否渠道商支付
        boolean isChannelPay = req.getAccountType().getType().equals(FinanceEnum.FinanceUser.CHANNEL.getType());
        // 定义接口返回对象，返回支付状态
        BalancePayResult balancePayResult = new BalancePayResult();
        // 扣减账户余额 支付金额+服务费
        Money subTotalAmount = req.getPayAmount();
        AccountPurseAlterRecordReq subRecordReq = TransferUtils.transfer(req,AccountPurseAlterRecordReq.class);
        subRecordReq.setAmount(subTotalAmount);
        subRecordReq.setAlterType(PurseEnum.PurseAlterType.ORDER_PAY);
        subRecordReq.setJoinRecordId(req.getOrderNo());
        boolean flag = purseDomain.subAmount(subRecordReq);
        balancePayResult.setPayState(flag);
        return balancePayResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemberRefundRes sellAfterRefund(SellAfterRefundReq req) {
        MemberRefundRes refundRes = new MemberRefundRes();
        TradeOrderInfoRes tradeOrderInfoRes = orderPayDomain.tradeOrderQuery(req.getOrderNo());
        if (tradeOrderInfoRes != null) {
            // 客户退款 同步即可
            refundRes.setRefundNo(tradeOrderInfoRes.getTradeNo());
            try {
//                HuiFuRefundRes huiFuRefundRes = memberRefund(req, tradeOrderInfoRes.getTradeNo(), tradeOrderInfoRes.getPayTime());
//                refundRes.setThirdTradeNo(huiFuRefundRes.getHf_seq_id());
//                if (!huiFuRefundRes.isSuccess()) {
//                    refundRes.setRefundWarnMsg(huiFuRefundRes.getResp_desc());
//                }
            } catch (Exception e) {
                refundRes.setRefundWarnMsg(e.getMessage());
                return refundRes;
            }
        }

        // 渠道商退款金额修改为 退款金额+服务费
        req.setRefundAmount(req.getRefundAmount().add(req.getServiceAmount()));
        if (req.getRefundAmount().greaterThanZero()) {
            // 渠道商退款
            AccountPurseAlterRecordReq recordReq = TransferUtils.transfer(req, AccountPurseAlterRecordReq.class);
            recordReq.setAccountType(PurseEnum.FinanceUser.CHANNEL);
            recordReq.setPurseType(PurseEnum.PurseType.PURCHASE);
            recordReq.setAmount(req.getRefundAmount());
            recordReq.setJoinRecordId(req.getOrderNo());
            recordReq.setAlterType(PurseEnum.PurseAlterType.SELL_AFTER);
            purseDomain.addAmount(recordReq);
        }

        return refundRes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void channelSettle(ChannelSettleReq req) {
        AccountPurseAlterRecordReq recordReq = TransferUtils.transfer(req, AccountPurseAlterRecordReq.class);
        recordReq.setAccountType(PurseEnum.FinanceUser.CHANNEL);
        recordReq.setPurseType(PurseEnum.PurseType.GOODS_INCOME);
        recordReq.setAmount(req.getSettleAmount());
        recordReq.setJoinRecordId(req.getJoinSettleOrderNo());
        recordReq.setAlterType(PurseEnum.PurseAlterType.CHANNEL_SETTLE);
        purseDomain.addAmount(recordReq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void supplierSettle(SupplierSettleReq req) {
        // 扣除部分补充到保证金账户
        Money supplierDeposit = Money.ZERO;
        ConfigSupplierVO supplierConfigVO = purseConfigDomain.querySupplierConfig();
        // 如果保证金扣除比例大于0，则进行扣除
        AccountPurseAlterRecordReq recordReq = TransferUtils.transfer(req, AccountPurseAlterRecordReq.class);
        recordReq.setAccountType(PurseEnum.FinanceUser.SUPPLIER);
        if(supplierConfigVO.getDepositSettleSub() > 0){
            supplierDeposit = req.getSettleAmount().multiply(supplierConfigVO.getDepositSettleSub())
                    .divide(Money.HUNDRED);
            // 将计算出的保证金存入保证金账户（类型为1）
            recordReq.setPurseType(PurseEnum.PurseType.PROMISE);
            recordReq.setAmount(req.getSettleAmount());
            recordReq.setJoinRecordId(req.getJoinSettleOrderNo());
            recordReq.setAlterType(PurseEnum.PurseAlterType.SUPPLIER_SETTLE);

            purseDomain.addAmount(recordReq);
            recordReq.setAlterType(PurseEnum.PurseAlterType.SUPPLIER_SETTLE_SUB_DEPOSIT);
            recordReq.setPurseType(PurseEnum.PurseType.TOTAL);
            purseDomain.addAmount(recordReq);
        }
        recordReq.setAmount(req.getSettleAmount().subtract(supplierDeposit));
        recordReq.setPurseType(PurseEnum.PurseType.TOTAL);
        purseDomain.addAmount(recordReq);
    }

}
