package com.newzkl.platform.base.biz.order.application.service;

import com.newzkl.platform.base.biz.order.model.order.req.CancelOrderReq;
import com.newzkl.platform.base.biz.order.model.order.req.ConfirmOrderReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderAddressUpdateReq;
import com.newzkl.platform.base.biz.order.model.order.res.TripSpuOrderChangeRes;

import java.util.List;

/**
 * @author sijiwang
 */
public interface UpdateOrderService {

    /**
     * 修改订单收货地址
     * @param req 地址修改请求
     * @return 是否修改成功
     */
    Boolean changeOrderShip(OrderAddressUpdateReq req);

    /**
     * 取消订单
     * @param req 取消订单请求
     * @return 是否取消成功
     */
    Boolean cancelOrder(CancelOrderReq req);

    /**
     * 确认收货
     * @param req 确认收货请求
     * @return 是否确认成功
     */
    Boolean confirmOrder(ConfirmOrderReq req);

    /**
     * 完成订单
     * @param req 完成订单请求
     * @return 是否完成成功
     */
    Boolean completeOrder(ConfirmOrderReq req);

    /**
     * 订单行程变更
     * @param orderNos 订单编号
     * @param spuOrderNos spu订单编号
     * @param skuOrderNos sku订单编号
     * @return 是否成功
     */
    TripSpuOrderChangeRes tripSpuOrderChange(List<String> orderNos, List<String> spuOrderNos, List<String> skuOrderNos);
}
