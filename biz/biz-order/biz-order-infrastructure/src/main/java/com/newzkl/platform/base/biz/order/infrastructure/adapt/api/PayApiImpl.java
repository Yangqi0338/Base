package com.newzkl.platform.base.biz.order.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.facade.PayFacade;
import com.newzkl.platform.base.biz.finance.facade.model.pay.*;
import com.newzkl.platform.base.biz.order.domain.adapt.api.*;
import com.newzkl.platform.base.common.ddd.facade.*;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.FinanceEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author niu
 * @description: 余额支付api
 * @date 2024/1/22 10:10
 */
@DubboService
@Component
@Slf4j
@RequiredArgsConstructor
public class PayApiImpl implements PayApi {

    @DubboReference
    private PayFacade payFacade;

    @Override
    public PayBaseResult orderPay(OrderPayReq req) {
        return payFacade.orderPay(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BalancePayResult balancePay(BalancePayReq req) {
        return payFacade.balancePay(req);
    }

    @Override
    public MemberRefundRes sellAfterRefund(SellAfterRefundReq req) {
        return payFacade.sellAfterRefund(req);
    }

    @Override
    public void channelSettle(ChannelSettleReq req) {
        payFacade.channelSettle(req);
    }

    @Override
    public void supplierSettle(SupplierSettleReq req) {
        payFacade.supplierSettle(req);
    }
}
