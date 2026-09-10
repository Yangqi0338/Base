package com.newzkl.platform.base.biz.order.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.facade.PayFacade;
import com.newzkl.platform.base.biz.order.domain.adapt.api.*;
import com.newzkl.platform.base.common.ddd.facade.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.newzkl.platform.base.common.ddd.infrastructure.rpc.RpcReference;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author niu
 * @description: 余额支付api
 * @date 2024/1/22 10:10
 */
@Component("orderPayApi")
@Slf4j
@RequiredArgsConstructor
public class PayApiImpl implements PayApi {

    @RpcReference
    private PayFacade payFacade;

    @Override
    public PayBaseResult orderPay(OrderPayReq req) {
        return payFacade.orderPay(req);
    }

    @Override
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
