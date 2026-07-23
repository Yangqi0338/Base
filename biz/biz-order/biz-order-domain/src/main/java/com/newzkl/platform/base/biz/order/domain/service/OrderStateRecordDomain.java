package com.newzkl.platform.base.biz.order.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderStateRecord;
import com.newzkl.platform.base.biz.order.model.order.req.OrderStateRecordPageReq;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单状态记录 领域服务接口
 * 封装核心业务逻辑，仅处理领域内的规则校验和操作
 *
 * @author sijiwang
 * @since 2026-01-30
 */
public interface OrderStateRecordDomain {

    /**
     * 新增订单状态记录（含领域规则校验）
     * @param entity 订单状态记录领域模型
     * @return 新增后的领域模型
     */
    OrderStateRecord create(OrderStateRecord entity);

    /**
     * 修改订单状态记录（含领域规则校验）
     * @param entity 订单状态记录领域模型
     * @return 修改后的领域模型
     */
    OrderStateRecord update(OrderStateRecord entity);

    /**
     * 根据订单ID查询所有状态记录（按操作时间倒序）
     *
     * @param spuOrderNo@return 订单状态记录列表
     */
    List<OrderStateRecord> listByOrderNo(String spuOrderNo);

    /**
     * 根据订单ID和变更后状态查询记录
     *
     * @param spuOrderNo
     * @param afterOrderState 变更后订单状态
     * @return 订单状态记录（Optional包装，避免空指针）
     */
    Optional<OrderStateRecord> findByOrderIdAndAfterOrderState(String spuOrderNo, Integer afterOrderState);

    /**
     * 根据操作时间范围查询订单状态记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 返回结果数量限制
     * @return 订单状态记录列表
     */
    List<OrderStateRecord> findByOperateTimeBetween(LocalDateTime startTime, LocalDateTime endTime, int limit);

    /**
     * 分页查询订单状态记录
     *
     * @param req@return 分页结果（包含总条数、分页数据，数据按创建时间降序排列）
     */
    Page<OrderStateRecord> findPage(OrderStateRecordPageReq req);

    /**
     * 按订单ID查询所有状态记录（按操作时间倒序）
     *
     * @param spuOrderNo@return 视图对象列表
     */
    List<OrderStateRecord> listBySpuOrderId(String spuOrderNo);
}