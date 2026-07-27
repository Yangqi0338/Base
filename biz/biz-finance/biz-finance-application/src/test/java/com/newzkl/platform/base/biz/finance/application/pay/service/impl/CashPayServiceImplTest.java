package com.newzkl.platform.base.biz.finance.application.pay.service.impl;

import com.newzkl.platform.base.biz.finance.application.pay.service.PurchaseRecordService;
import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.biz.finance.domain.hf.HuiFuMethod;
import com.newzkl.platform.base.biz.finance.domain.pay.service.OrderPayDomain;
import com.newzkl.platform.base.biz.finance.domain.purse.service.AccountPurseDomain;
import com.newzkl.platform.base.biz.finance.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.finance.model.enums.finance.PayEnum;
import com.newzkl.platform.base.biz.finance.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.finance.model.pay.req.OrderPayReq;
import com.newzkl.platform.base.biz.finance.model.pay.req.huifu.HuiFuPayReq;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuPayRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.redis.lock.DistributedLocker;
import com.newzkl.platform.base.common.core.redis.lock.impl.RedissonLockUtil;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.redisson.api.RLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link CashPayServiceImpl#orderPay} 幂等与汇付入参映射测试。
 *
 * <p>静态门面 {@code RedisUtil} / {@code HuiFuMethod} 用 {@code mockStatic} 隔离;
 * 分布式锁走 {@link RedissonLockUtil#setLocker} 注入 mock, 无需静态桩。</p>
 *
 * @author KC
 */
class CashPayServiceImplTest {

    private static final Long ORDER_NO = 730001L;

    private static final Long TRADE_NO = 990001L;

    private OrderPayDomain orderPayDomain;

    private CashPayServiceImpl cashPayService;

    private DistributedLocker locker;

    private RLock lock;

    private MockedStatic<RedisUtil> redisUtil;

    private MockedStatic<HuiFuMethod> huiFuMethod;

    @BeforeEach
    void setUp() {
        orderPayDomain = Mockito.mock(OrderPayDomain.class);
        cashPayService = new CashPayServiceImpl(orderPayDomain,
                Mockito.mock(AccountPurseDomain.class),
                Mockito.mock(AccountPurseConfigDomain.class),
                Mockito.mock(PurchaseRecordService.class));

        lock = Mockito.mock(RLock.class);
        locker = Mockito.mock(DistributedLocker.class);
        when(locker.lock(anyString())).thenReturn(lock);
        RedissonLockUtil.setLocker(locker);

        redisUtil = Mockito.mockStatic(RedisUtil.class);
        huiFuMethod = Mockito.mockStatic(HuiFuMethod.class);
    }

    @AfterEach
    void tearDown() {
        redisUtil.close();
        huiFuMethod.close();
        RedissonLockUtil.setLocker(null);
    }

    @Test
    @DisplayName("命中支付结果缓存时直接返回, 不重复落库也不拉起三方")
    void orderPayShouldReturnCachedResultWithoutCallingThirdParty() {
        HuiFuPayRes cached = new HuiFuPayRes();
        redisUtil.when(() -> RedisUtil.get("orderPayCache:" + ORDER_NO)).thenReturn(cached);

        HuiFuPayRes result = cashPayService.orderPay(req(OrderEnum.PayType.WX));

        assertSame(cached, result);
        verify(orderPayDomain, never()).saveOrderPayRecord(any(), any());
        huiFuMethod.verifyNoInteractions();
        verify(locker).unlock(lock);
    }

    @Test
    @DisplayName("未命中缓存时落库 → 拉起汇付 → 回填三方单号 → 写状态与结果缓存")
    void orderPayShouldPlaceOrderAndCacheResult() {
        when(orderPayDomain.saveOrderPayRecord(any(), eq(null))).thenReturn(TRADE_NO);
        HuiFuPayRes payRes = new HuiFuPayRes();
        payRes.setTradeNo(TRADE_NO);
        payRes.setTripartiteNo("HF20260726001");
        huiFuMethod.when(() -> HuiFuMethod.pay(any())).thenReturn(payRes);

        HuiFuPayRes result = cashPayService.orderPay(req(OrderEnum.PayType.WX));

        assertSame(payRes, result);
        verify(orderPayDomain).resetTripartiteTradeNo(TRADE_NO, "HF20260726001");
        redisUtil.verify(() -> RedisUtil.set(eq("orderPayCache:" + ORDER_NO), eq(payRes), anyLong(), any()));
        redisUtil.verify(() -> RedisUtil.set(anyString(), eq(1L), anyLong(), any()));
        verify(locker).unlock(lock);
    }

    @Test
    @DisplayName("三方未返回交易号时不回填, 避免写入空单号")
    void orderPayShouldSkipTripartiteBackfillWhenNoTradeNo() {
        when(orderPayDomain.saveOrderPayRecord(any(), eq(null))).thenReturn(TRADE_NO);
        HuiFuPayRes payRes = new HuiFuPayRes();
        payRes.setTradeNo(TRADE_NO);
        huiFuMethod.when(() -> HuiFuMethod.pay(any())).thenReturn(payRes);

        cashPayService.orderPay(req(OrderEnum.PayType.WX));

        verify(orderPayDomain, never()).resetTripartiteTradeNo(anyLong(), anyString());
    }

    @Test
    @DisplayName("微信/支付宝分别映射汇付正扫交易类型")
    void orderPayShouldMapPayTypeToHuiFuTradeType() {
        when(orderPayDomain.saveOrderPayRecord(any(), eq(null))).thenReturn(TRADE_NO);
        HuiFuPayRes payRes = new HuiFuPayRes();
        payRes.setTradeNo(TRADE_NO);
        huiFuMethod.when(() -> HuiFuMethod.pay(any())).thenReturn(payRes);

        cashPayService.orderPay(req(OrderEnum.PayType.WX));
        assertEquals(PayEnum.HuiFuTradeType.T_NATIVE, captureHuiFuReq().getTradeType());

        huiFuMethod.clearInvocations();
        cashPayService.orderPay(req(OrderEnum.PayType.ALIPAY));
        HuiFuPayReq alipayReq = captureHuiFuReq();
        assertEquals(PayEnum.HuiFuTradeType.A_NATIVE, alipayReq.getTradeType());
        assertEquals(TRADE_NO, alipayReq.getTradeNo());
        assertEquals(10000, alipayReq.getPayAmount());
    }

    @Test
    @DisplayName("非现金支付方式拒绝走汇付通道, 且锁必须释放")
    void orderPayShouldRejectNonCashPayTypeAndReleaseLock() {
        when(orderPayDomain.saveOrderPayRecord(any(), eq(null))).thenReturn(TRADE_NO);

        PlatformException ex = assertThrows(PlatformException.class,
                () -> cashPayService.orderPay(req(OrderEnum.PayType.PURCHASE)));

        assertTrue(ex.equalsCode(BaseErrorCode.PARAM));
        huiFuMethod.verifyNoInteractions();
        verify(locker).unlock(lock);
    }

    private HuiFuPayReq captureHuiFuReq() {
        ArgumentCaptor<HuiFuPayReq> captor = ArgumentCaptor.forClass(HuiFuPayReq.class);
        huiFuMethod.verify(() -> HuiFuMethod.pay(captor.capture()));
        return captor.getValue();
    }

    private OrderPayReq req(OrderEnum.PayType payType) {
        OrderPayReq req = new OrderPayReq();
        req.setOrderNo(ORDER_NO);
        req.setConsumeType(EarningsEnum.ConsumeType.RECHARGE);
        req.setOrderAmount(10000);
        req.setPayAmount(10000);
        req.setPayType(payType);
        req.setGoodsInfo("采购金");
        return req;
    }
}
