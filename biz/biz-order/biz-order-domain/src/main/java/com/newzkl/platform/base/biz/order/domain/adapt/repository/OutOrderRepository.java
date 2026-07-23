package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.OutOrder;
import com.newzkl.platform.base.biz.order.model.order.req.OutOrderPageReq;

import java.util.List;
import java.util.Optional;

/**
 * 外部订单仓储接口
 *
 * @author sijiwang
 * @since 2026-07-04
 */
public interface OutOrderRepository {

    /**
     * 保存/更新外部订单
     */
    OutOrder save(OutOrder outOrder);

    /**
     * 批量保存外部订单
     */
    boolean batchSave(List<OutOrder> outOrders);

    /**
     * 根据ID查询外部订单
     */
    Optional<OutOrder> findById(Long id);

    /**
     * 根据外部订单号查询
     */
    Optional<OutOrder> findByOrderSn(String orderSn);

    /**
     * 根据内部订单ID查询
     */
    List<OutOrder> findByOrderId(Long orderId);

    /**
     * 删除外部订单
     */
    boolean deleteById(Long id);

    /**
     * 分页查询外部订单
     */
    Page<OutOrder> findPage(OutOrderPageReq req);

    /**
     * 根据外部订单号批量查询
     */
    List<OutOrder> findByOrderSns(List<String> orderSns);

}