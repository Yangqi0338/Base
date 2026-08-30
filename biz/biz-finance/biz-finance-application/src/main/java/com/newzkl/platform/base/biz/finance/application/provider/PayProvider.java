package com.newzkl.platform.base.biz.finance.application.provider;

import com.newzkl.platform.base.biz.finance.application.pay.service.CashPayService;
import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.domain.pay.service.OrderPayDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.facade.PayFacade;
import com.newzkl.platform.base.biz.finance.model.account.vo.ConfigSupplierVO;
import com.newzkl.platform.base.common.ddd.facade.SupplierSettleReq;
import com.newzkl.platform.base.common.ddd.facade.ChannelSettleReq;
import com.newzkl.platform.base.common.ddd.facade.MemberRefundRes;
import com.newzkl.platform.base.common.ddd.facade.SellAfterRefundReq;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.biz.finance.model.pay.res.TradeOrderInfoRes;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordReq;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.BalancePayReq;
import com.newzkl.platform.base.common.ddd.facade.BalancePayResult;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private final CashPayService cashPayService;

    /**
     * 拉起三方现金支付
     *
     * <p>2026-08-28 接线: 原实现 {@code PayBaseResult payBaseResult = null} 后立即取值, 任何调用必抛
     * {@code NullPointerException} —— 消费者下单走汇付这条链完全不通。现纯委派 {@code CashPayService.orderPay},
     * 后者已含幂等缓存、支付单落库、三方单号回填与状态缓存</p>
     *
     * <p>外层不加 {@code @Transactional} / {@code @DistributedLock}: 锁与事务边界归内层实现,
     * 外层再套会把「调用汇付」这个外部 IO 圈进事务</p>
     *
     * @param req 支付入参
     * @return 三方支付结果
     */
    @Override
    public PayBaseResult orderPay(OrderPayReq req) {
        return cashPayService.orderPay(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BalancePayResult balancePay(BalancePayReq req) {
        if (req.getPurseType() == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "扣款科目不能为空");
        }
        // 定义接口返回对象，返回支付状态
        BalancePayResult balancePayResult = new BalancePayResult();
        // 扣减账户余额 支付金额+服务费
        Money subTotalAmount = req.getPayAmount();
        AccountPurseAlterRecordReq subRecordReq = TransferUtils.transfer(req,AccountPurseAlterRecordReq.class);
        // 扣款科目显式 set: 不把「钱从哪个账户扣」交给反射静默拷贝, 上游改名会退化成 null 且编译零报错
        subRecordReq.setPurseType(req.getPurseType());
        subRecordReq.setAmount(subTotalAmount);
        subRecordReq.setAlterType(PurseEnum.AlterType.ORDER_PAY);
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
            recordReq.setAccountType(PurseEnum.User.CHANNEL);
            recordReq.setPurseType(PurseEnum.Type.PURCHASE);
            recordReq.setAmount(req.getRefundAmount());
            recordReq.setJoinRecordId(req.getOrderNo());
            recordReq.setAlterType(PurseEnum.AlterType.SELL_AFTER);
            purseDomain.addAmount(recordReq);
        }

        return refundRes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void channelSettle(ChannelSettleReq req) {
        AccountPurseAlterRecordReq recordReq = TransferUtils.transfer(req, AccountPurseAlterRecordReq.class);
        recordReq.setAccountType(PurseEnum.User.CHANNEL);
        recordReq.setPurseType(PurseEnum.Type.GOODS_INCOME);
        recordReq.setAmount(req.getSettleAmount());
        recordReq.setJoinRecordId(req.getJoinSettleOrderNo());
        recordReq.setAlterType(PurseEnum.AlterType.CHANNEL_SETTLE);
        purseDomain.addAmount(recordReq);
    }

    /**
     * 供应商结算
     *
     * <p>🔴 2026-08-28 修资损: 原实现四处错 —— ① PROMISE 入账额误用 {@code settleAmount} 而非按比例算出的
     * {@code supplierDeposit}(10% 比例下 10 倍入账); ② 紧随其后又对 {@code TOTAL} 做一笔同额**入账**(本意是冲抵,
     * 方向反了); ③ 结算余额落 {@code TOTAL} 而非收益账户; ④ 余额那笔 alterType 仍是被污染的
     * {@code SUPPLIER_SETTLE_SUB_DEPOSIT}
     *
     * <p>现口径: {@code settleAmount × depositSettleSub%} → {@code PROMISE(3)} 保证金,
     * 其余 → {@code SUPPLIER_INCOME(13)} 收益账户。两笔 purseType 均 {@code isTotalRelation() == true},
     * 由 {@code AccountPurseRepositoryImpl} 自动镜像进 {@code TOTAL}(不变式 {@code TOTAL = Σ(totalRelation 科目)}),
     * 故本方法**零显式 TOTAL 操作**
     *
     * @param req 结算请求, 含 settleAmount / joinSettleOrderNo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void supplierSettle(SupplierSettleReq req) {
        Money supplierDeposit = Money.ZERO;
        ConfigSupplierVO supplierConfigVO = purseConfigDomain.querySupplierConfig();
        AccountPurseAlterRecordReq recordReq = TransferUtils.transfer(req, AccountPurseAlterRecordReq.class);
        // 关键字段全部显式 set: TransferUtils 是同名浅拷贝, 不匹配静默丢弃, 不可依赖
        recordReq.setAccountType(PurseEnum.User.SUPPLIER);
        recordReq.setJoinRecordId(req.getJoinSettleOrderNo());
        recordReq.setAlterType(PurseEnum.AlterType.SUPPLIER_SETTLE);
        if (supplierConfigVO.getDepositSettleSub() > 0) {
            // 按全局比例抽一部分结算款补充保证金账户
            supplierDeposit = req.getSettleAmount().multiply(supplierConfigVO.getDepositSettleSub())
                    .divide(Money.HUNDRED);
            recordReq.setPurseType(PurseEnum.Type.PROMISE);
            recordReq.setAmount(supplierDeposit);
            purseDomain.addAmount(recordReq);
        }
        // 其余进收益账户, 可提现
        recordReq.setPurseType(PurseEnum.Type.SUPPLIER_INCOME);
        recordReq.setAmount(req.getSettleAmount().subtract(supplierDeposit));
        purseDomain.addAmount(recordReq);
    }

}
