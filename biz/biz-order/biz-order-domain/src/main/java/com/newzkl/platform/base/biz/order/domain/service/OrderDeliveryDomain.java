package com.newzkl.platform.base.biz.order.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderDelivery;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryUpdateReq;

/**
 * 订单发货领域服务（原子业务能力）
 * @author sijiwang
 */
public interface OrderDeliveryDomain {

    /**
     * 创建发货单（仅主单+明细+SKU数量更新）
     */
    OrderDelivery createDelivery(OrderDeliveryCreateReq req);

    /**
     * 修改发货物流信息
     */
    OrderDelivery updateDelivery(OrderDeliveryUpdateReq req);

    /**
     * 根据ID查询发货详情（含明细）
     */
    OrderDelivery getDeliveryDetail(Long deliveryId);

    /**
     * 分页查询发货单列表
     */
    Page<OrderDelivery> pageDelivery(OrderDeliveryPageReq pageReq);
}