package com.newzkl.platform.base.biz.order.application.service;


import com.newzkl.platform.base.biz.order.model.dto.OrderAgg;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;

import java.util.List;

/**
 * @author niu
 * @description: 订单提交接口
 * @date 2024/5/7 10:17
 */
public interface CommitOrder {

    /**
     * 提交订单
     * @param orderCreateCommand
     * @param memberOrderCreateCommand
     */
    OrderCreateRes commitOrder(OrderCreateCommand orderCreateCommand, MemberOrderCreateCommand memberOrderCreateCommand);

    /**
     * 创建预订单 （存redis）
     * @param orderCreateCommand
     * @param memberOrderCreateCommand
     * @return
     */
    OrderCreateRes createMemberPrePayOrder(OrderCreateCommand orderCreateCommand, MemberOrderCreateCommand memberOrderCreateCommand);

    OrderCreateRes getOrderCreateResByRedis(MemberOrderCreateCommand memberOrderCreateCommand);
    /**
     * 再来一单
     * @param spuOrderId
     * @return
     */
    OrderCreateRes createOrderAgain(List<Long> spuOrderId);
    /**
     * 提交预订单 （从redis中删除）
     *
     * @param consumerPaymentCommand
     * @return
     */
    OrderAgg commitMemberPrePayOrder(CommitMemberOrderCommand consumerPaymentCommand);

    /**
     * 支付
     * @param command
     * @return
     */
    PayBaseResult memberPayOrder(PayMemberOrderCommand command);

    /**
     * 修改订单收货信息
     * @param command
     * @return
     */
    Boolean changeOrderShip(OrderShipCommand command);
}
