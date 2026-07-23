package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderStateRecord;
import com.newzkl.platform.base.biz.order.model.order.req.OrderStateRecordPageReq;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单状态记录仓储接口
 *
 * @author sijiwang
 * @since 2026-01-30
 */
public interface OrderStateRecordRepository {

    /**
     * 保存/更新订单状态记录
     */
    OrderStateRecord save(OrderStateRecord record);

    /**
     * 根据订单ID查询所有状态记录
     */
    List<OrderStateRecord> findBySpuOrderNo(String spuOrderNo);

    /**
     * 根据订单ID和状态查询记录
     */
    Optional<OrderStateRecord> findBySpuOrderNoAndOrderState(String spuOrderNo, Integer orderState);

    /**
     * 根据操作时间范围查询记录
     */
    List<OrderStateRecord> findByOperateTimeBetween(LocalDateTime startTime, LocalDateTime endTime, int limit);

    /**
     * 分页查询订单状态记录
     */
    Page<OrderStateRecord> findPage(OrderStateRecordPageReq req);

    /**
     * 按订单ID查询所有状态记录（按操作时间倒序）
     *
     * @param spuOrderNo@return 视图对象列表
     */
    List<OrderStateRecord> listBySpuOrderId(String spuOrderNo);
}
