package com.newzkl.platform.base.biz.finance.action.controller;

import com.newzkl.platform.base.biz.finance.application.pay.service.CashPayService;
import com.newzkl.platform.base.biz.finance.domain.account.service.AccountPurseConfigDomain;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.constant.FinanceErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.biz.finance.model.pay.res.huifu.HuiFuPayRes;
import com.newzkl.platform.base.common.ddd.facade.ChannelConfigVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.constants.TokenConstants;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@code PayOrderController} 充值入口测试
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class PayOrderControllerTest {

    private static final Long ACCOUNT_ID = 8801L;

    private static final String ACCOUNT_NAME = "渠道商甲";

    @Mock
    private CashPayService cashPayService;

    @Mock
    private AccountPurseConfigDomain accountPurseConfigDomain;

    @InjectMocks
    private PayOrderController payOrderController;

    @BeforeEach
    void setUpLoginState() {
        SecurityContextHolder.set(TokenConstants.DETAILS_ACCOUNT_ID, ACCOUNT_ID.toString());
        SecurityContextHolder.set(TokenConstants.DETAILS_USERNAME, ACCOUNT_NAME);
    }

    @AfterEach
    void clearLoginState() {
        SecurityContextHolder.remove();
    }

    @Test
    @DisplayName("渠道商充值: 金额低于配置阈值直接抛业务异常, 不拉起三方支付")
    void channelRechargeShouldRejectAmountBelowThreshold() {
        when(accountPurseConfigDomain.defaultChannelConfig()).thenReturn(channelConfig(10000));

        PlatformException ex = assertThrows(PlatformException.class, () -> payOrderController.channelRecharge(9999, 1));

        assertTrue(ex.equalsCode(FinanceErrorCode.LESS_THAN_MINIMUM_RECHARGE_AMOUNT));
        verify(cashPayService, never()).orderPay(any());
    }

    @Test
    @DisplayName("渠道商充值: 达到阈值时按 RECHARGE 类型组装入参并回填登录态")
    void channelRechargeShouldBuildRechargeRequest() {
        when(accountPurseConfigDomain.defaultChannelConfig()).thenReturn(channelConfig(10000));
        when(cashPayService.orderPay(any())).thenReturn(new HuiFuPayRes());

        PlatformResult<HuiFuPayRes> result = payOrderController.channelRecharge(10000, 1);

        assertTrue(result.isSuccess());
        OrderPayReq req = captureOrderPayReq();
        assertNotNull(req.getOrderNo());
        assertEquals(EarningsEnum.ConsumeType.RECHARGE, req.getConsumeType());
        // orderAmount/payAmount 已 Money; 入参 10000 分, getCent() 取回分值
        assertEquals(10000, req.getOrderAmount().getCent());
        assertEquals(10000, req.getPayAmount().getCent());
        assertEquals(ACCOUNT_ID, req.getAccountId());
        assertEquals(ACCOUNT_NAME, req.getAccountName());
        assertEquals(PaymentEnum.PayType.WX, req.getPayType());
        assertEquals("采购金", req.getGoodsInfo());
        assertEquals("{\"level\":1}", req.getOrderInfo());
    }

    @Test
    @DisplayName("渠道商充值: 支付方式 2 映射为支付宝")
    void channelRechargeShouldMapAlipayPayMethod() {
        when(accountPurseConfigDomain.defaultChannelConfig()).thenReturn(channelConfig(0));
        when(cashPayService.orderPay(any())).thenReturn(new HuiFuPayRes());

        payOrderController.channelRecharge(500, 2);

        assertEquals(PaymentEnum.PayType.ALIPAY, captureOrderPayReq().getPayType());
    }

    @Test
    @DisplayName("供应商充值: 不校验阈值, 按 SUPPLIER_RECHARGE 类型组装入参")
    void supplierRechargeShouldBuildSupplierRequest() {
        when(cashPayService.orderPay(any())).thenReturn(new HuiFuPayRes());

        PlatformResult<HuiFuPayRes> result = payOrderController.supplierRecharge(2000, 1);

        assertTrue(result.isSuccess());
        OrderPayReq req = captureOrderPayReq();
        assertEquals(EarningsEnum.ConsumeType.SUPPLIER_RECHARGE, req.getConsumeType());
        assertEquals("2000", req.getOrderInfo());
        assertEquals(EarningsEnum.ConsumeType.SUPPLIER_RECHARGE.getInfo(), req.getGoodsInfo());
        assertEquals(ACCOUNT_ID, req.getAccountId());
        verify(accountPurseConfigDomain, never()).defaultChannelConfig();
    }

    private OrderPayReq captureOrderPayReq() {
        ArgumentCaptor<OrderPayReq> captor = ArgumentCaptor.forClass(OrderPayReq.class);
        verify(cashPayService).orderPay(captor.capture());
        return captor.getValue();
    }

    private ChannelConfigVO channelConfig(Integer minimumWithdrawalAmount) {
        ChannelConfigVO config = new ChannelConfigVO();
        // 入参为分, Money.of(Integer)=分
        config.setMinimumWithdrawalAmount(Money.of(minimumWithdrawalAmount));
        return config;
    }
}
