package com.newzkl.platform.base.biz.finance.application.pay.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.finance.application.pay.service.CashPayService;
import com.newzkl.platform.base.biz.finance.application.pay.service.PurchaseRecordService;
import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.domain.hf.HuiFuMethod;
import com.newzkl.platform.base.biz.finance.domain.pay.service.OrderPayDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.model.enums.CacheKey;
import com.newzkl.platform.base.biz.finance.model.event.PaySuccessEvent;
import com.newzkl.platform.base.biz.finance.model.pay.req.OrderPayReq;
import com.newzkl.platform.base.biz.finance.model.pay.req.PurchaseRecordReq;
import com.newzkl.platform.base.biz.finance.model.pay.req.huifu.HuiFuPayReq;
import com.newzkl.platform.base.biz.finance.model.pay.res.TradeOrderInfoRes;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuPayRes;
import com.newzkl.platform.base.biz.finance.model.purse.req.AccountPurseAlterRecordReq;
import com.newzkl.platform.base.common.core.mq.utils.MQUtil;
import com.newzkl.platform.base.biz.finance.model.support.MQ;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PayEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.finance.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.redis.lock.impl.RedissonLockUtil;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * 现金支付编排实现。
 *
 * @author niu
 */
@Service
@RequiredArgsConstructor
public class CashPayServiceImpl implements CashPayService {

    /**
     * 支付结果缓存 key 前缀 (拼订单号)。
     */
    private static final String ORDER_PAY_CACHE_PRE = "orderPayCache:";

    /**
     * 支付幂等锁 key 前缀 (拼订单号)。
     */
    private static final String ORDER_PAY_LOCK_PRE = "orderPayLock:";

    /**
     * 支付单待回调状态过期分钟数。
     */
    private static final long PAY_STATE_EXPIRE_MINUTES = 15L;

    /**
     * 支付结果缓存过期分钟数。
     */
    private static final long PAY_CACHE_EXPIRE_MINUTES = 60L;

    private final OrderPayDomain orderPayService;

    private final AccountPurseDomain accountPurseService;

    private final AccountPurseConfigDomain accountPurseConfigService;

    private final PurchaseRecordService purchaseRecordAction;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void alterPayState(Long tradeNo, String thirdOrderNo) {
        // 1、更新支付状态,flag表示是否更新成功
        boolean flag = orderPayService.alterPayState(tradeNo, thirdOrderNo);
        if (flag) {
            TradeOrderInfoRes tradeOrder = orderPayService.tradeOrderQuery(tradeNo);
            EarningsEnum.ConsumeType consumeType = tradeOrder.getConsumeType();
            switch (consumeType) {
                case RECHARGE:
                    // 2、增加账户余额 + 变动记录修改
                    AccountPurseAlterRecordReq recharge = buildAccountPurseAlterRecord(tradeOrder,
                            PurseEnum.FinanceUser.CHANNEL,
                            PurseEnum.PurseType.PURCHASE,
                            PurseEnum.PurseAlterType.RECHARGE);
                    accountPurseService.addAmount(recharge);
                    // 3、渠道商采购金充值后，更新服务费
                    accountPurseConfigService.alterChannelNowChargeConfig(tradeOrder.getAccountId(), tradeOrder.getPayAmount());
                    break;
                case SUPPLIER_RECHARGE:
                    // 2、增加收益余额 + 变动记录修改
                    AccountPurseAlterRecordReq supplierRecharge = buildAccountPurseAlterRecord(tradeOrder,
                            PurseEnum.FinanceUser.SUPPLIER,
                            PurseEnum.PurseType.MARKETING,
                            PurseEnum.PurseAlterType.SUPPLIER_OPERATOR_RECHARGE);
                    accountPurseService.addAmount(supplierRecharge);
                    break;
                case GOODS_SEAT:
                    // 供应商购买商品位只能扣采购金,所以不会到这里
                    PurchaseRecordReq saveCommand = new PurchaseRecordReq();
                    saveCommand.setPayState(OrderEnum.State.SUCCESS);
                    saveCommand.setTradeNo(tradeNo);
                    purchaseRecordAction.seatPackageSaveOrUpdate(saveCommand);
                    break;
                default:
                    // 2、其他消费支付成功后发送mq消息
                    MQUtil.send(MQ.Tag.PAYMENT_PAY_SUCCESS, new PaySuccessEvent(tradeNo));
                    break;
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HuiFuPayRes orderPay(OrderPayReq req) {
        RLock lock = RedissonLockUtil.lock(ORDER_PAY_LOCK_PRE + req.getOrderNo());
        try {
            // 1、命中支付结果缓存则直接返回, 避免同一订单重复拉起三方支付
            HuiFuPayRes cached = RedisUtil.get(ORDER_PAY_CACHE_PRE + req.getOrderNo());
            if (cached != null) {
                return cached;
            }
            // 2、落库业务支付单; 收款方分账信息暂不启用, 传 null
            Long tradeNo = orderPayService.saveOrderPayRecord(req, null);
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
            RedisUtil.set(ORDER_PAY_CACHE_PRE + req.getOrderNo(), payRes, PAY_CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            return payRes;
        } finally {
            RedissonLockUtil.unlock(lock);
        }
    }

    /**
     * 组装汇付支付入参。
     *
     * <p>汇付侧的日期、金额换算、回调地址已由 {@code HuiFuMethod} 内部处理, 此处只做支付方式映射。</p>
     */
    private HuiFuPayReq buildHuiFuPayReq(OrderPayReq req, Long tradeNo) {
        HuiFuPayReq huiFuPayReq = new HuiFuPayReq();
        huiFuPayReq.setTradeNo(tradeNo);
        huiFuPayReq.setGoodsInfo(req.getGoodsInfo());
        huiFuPayReq.setPayAmount(req.getPayAmount());
        huiFuPayReq.setTradeType(switch (req.getPayType()) {
            case WX -> PayEnum.HuiFuTradeType.T_NATIVE;
            case ALIPAY -> PayEnum.HuiFuTradeType.A_NATIVE;
            default -> throw new PlatformException(BaseErrorCode.PARAM);
        });
        return huiFuPayReq;
    }

    private AccountPurseAlterRecordReq buildAccountPurseAlterRecord(TradeOrderInfoRes tradeOrder,
                                                                    PurseEnum.FinanceUser accountType,
                                                                    PurseEnum.PurseType purseType,
                                                                    PurseEnum.PurseAlterType alterType) {
        AccountPurseAlterRecordReq req = new AccountPurseAlterRecordReq();
        req.setAccountId(tradeOrder.getAccountId());
        req.setPurseType(purseType);
        req.setAccountType(accountType);
        req.setAlterType(alterType);
        req.setAmount(tradeOrder.getPayAmount());
        req.setJoinRecordId(tradeOrder.getTradeNo());
        req.setRemark(JSONUtil.toJsonStr(tradeOrder));
        return req;
    }
}
