package com.newzkl.platform.base.biz.order.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderDelivery;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryUpdateReq;

/**
 * 订单发货应用服务（业务编排）
 * @author sijiwang
 */
public interface OrderDeliveryAppService {

    /**
     * 订单发货（全流程）
     */
    OrderDelivery deliverOrder(OrderDeliveryCreateReq req);

    /**
     * 修改发货信息
     */
    OrderDelivery updateDelivery(OrderDeliveryUpdateReq req);

    /**
     * 查询发货详情
     */
    OrderDelivery getDeliveryDetail(Long deliveryId);

    /**
     * 分页查询发货列表
     */
    Page<OrderDelivery> pageDelivery(OrderDeliveryPageReq pageReq);
}