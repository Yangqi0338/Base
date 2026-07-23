package com.newzkl.platform.base.biz.order.application.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkl.scm.finance.model.pay.res.TradeBaseRes;
import com.newzkl.platform.base.biz.order.application.service.IOrderService;
import com.newzkl.platform.base.biz.order.application.service.IQueryOrderService;
import com.newzkl.platform.base.biz.order.application.service.IUpdateOrderService;
import com.newzkl.platform.base.biz.order.domain.service.ICreateOrderDomain;
import com.newzkl.platform.base.biz.order.model.order.req.*;
import com.newzkl.platform.base.biz.order.model.order.res.CreateOrderRes;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateCountVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sijiwang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final ICreateOrderDomain createOrderDomain;
    private final IUpdateOrderService updateOrderDomain;
    private final IQueryOrderService queryOrderDomain;
    @Override
    public CreateOrderRes createOrderPre(CreateOrderReq req) {
        return createOrderDomain.createOrderPre(req);
    }

    @Override
    public CreateOrderRes commitOrderPre(CommitOrderPreReq req) {
        return createOrderDomain.commitOrderPre(req);
    }

    @Override
    public TradeBaseRes payOrder(PayOrderReq req) {
        return createOrderDomain.payOrder(req);
    }

    @Override
    public CreateOrderRes createOrderAgain(String spuOrderNo) {
        return createOrderDomain.createOrderAgain(spuOrderNo);
    }

    @Override
    public CreateOrderRes selectOrderPre(CommitOrderPreReq req) {
        return queryOrderDomain.selectOrderPre(req);
    }

    @Override
    public Page<SpuOrderVO> spuPage(SpuOrderPageReq req) {
        return queryOrderDomain.spuPage(req);
    }

    @Override
    public SpuOrderVO getSpuOrderDetail(String spuOrderNo) {
        return queryOrderDomain.getSpuOrderDetail(spuOrderNo);
    }

    @Override
    public List<OrderStateCountVO> countOrderStateByChannel(Long channelId) {
        return queryOrderDomain.countOrderStateByChannel(channelId);
    }

    @Override
    public List<OrderStateCountVO> countOrderStateByAccount(Long accountId) {
        return queryOrderDomain.countOrderStateByAccount(accountId);
    }

    @Override
    public void orderBalancePay(String... orderNos) {
        queryOrderDomain.orderBalancePay(orderNos);
    }

    @Override
    public void operatorOrderBalancePay(String orderNo) {
        queryOrderDomain.operatorOrderBalancePay(orderNo);
    }

    @Override
    public void orderDirectPay(String orderNo) {
        queryOrderDomain.orderDirectPay(orderNo);
    }

    @Override
    public Boolean changeOrderShip(OrderAddressUpdateReq req) {
        return updateOrderDomain.changeOrderShip(req);
    }

    @Override
    public Boolean cancelOrder(CancelOrderReq req) {
        return updateOrderDomain.cancelOrder(req);
    }

    @Override
    public Boolean confirmOrder(ConfirmOrderReq req) {
        return updateOrderDomain.confirmOrder(req);
    }

    @Override
    public Boolean completeOrder(ConfirmOrderReq req) {
        return updateOrderDomain.completeOrder(req);
    }
}
