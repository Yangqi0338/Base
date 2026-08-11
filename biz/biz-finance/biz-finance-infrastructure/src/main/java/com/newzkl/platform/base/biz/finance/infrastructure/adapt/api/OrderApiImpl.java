package com.newzkl.platform.base.biz.finance.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.finance.domain.adapt.api.OrderApi;
import com.newzkl.platform.base.biz.order.facade.OrderFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderApiImpl implements OrderApi {

    @Autowired
    private OrderFacade orderFacade;

    @Override
    public void orderChannelPay(Long orderNo) {
        orderFacade.orderChannelPay(orderNo);
    }
}
