package com.newzkl.platform.base.biz.order.domain.service;


import com.newzkl.platform.base.biz.order.model.dto.OrderAgg;
import com.newzkl.platform.base.biz.order.model.req.MemberOrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.OrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckV2Res;

/**
 * 新订单领域
 * @author sijiwang
 */
public interface IOrderDomainNew {
    /**
     * 创建订单
     * @param data
     * @param orderCreateCommand
     * @return
     */
    OrderCreateRes createOrder(OrderGoodsCheckV2Res data, OrderCreateCommand orderCreateCommand);

    /**
     * 保存预支付单
     * @param order
     * @param memberOrderCreateCommand
     */
    void savePrePayOrder(OrderCreateRes order, MemberOrderCreateCommand memberOrderCreateCommand);

    /**
     * 订单聚合保存
     * @param orderAgg
     */
    void orderAggSave(OrderAgg orderAgg);
}
