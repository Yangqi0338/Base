package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderDelivery;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryPageReq;

import java.util.List;
import java.util.Optional;

/**
 * 订单发货主单仓储接口
 * @author sijiwang
 */
public interface OrderDeliveryRepository {

    /**
     * 保存/更新发货单
     */
    OrderDelivery save(OrderDelivery delivery);

    /**
     * 根据ID查询
     */
    Optional<OrderDelivery> findById(Long id);

    /**
     * 根据发货单号查询
     */
    Optional<OrderDelivery> findByDeliveryNo(String deliveryNo);

    /**
     * 根据订单号查询列表
     */
    List<OrderDelivery> findByOrderNo(String orderNo);

    /**
     * 分页条件查询
     */
    Page<OrderDelivery> pageQuery(OrderDeliveryPageReq pageReq);
}