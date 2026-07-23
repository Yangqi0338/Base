package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.Order;
import com.newzkl.platform.base.biz.order.model.order.res.CreateOrderRes;

import java.util.List;

/**
 * 订单仓储接口（DDD领域层）
 * 仅依赖领域模型，完全隔离DO/MyBatisPlus等技术细节
 * @author sijiwang
 */
public interface OrderRepository {

    /**
     * 保存订单（参数为领域模型）
     * @param order 订单领域模型
     * @return 保存结果
     */
    boolean save(Order order);

    /**
     * 更新订单（参数为领域模型）
     * @param order 订单领域模型
     * @return 更新结果
     */
    boolean updateById(Order order);

    /**
     * 根据ID查询订单（返回领域模型）
     * @param id 订单ID
     * @return 订单领域模型
     */
    Order getById(Long id);

    /**
     * 根据交易单号查询订单（返回领域模型）
     *
     * @param orderNo 交易单号
     * @return 订单领域模型
     */
    Order getByOrderNo(String orderNo);

    /**
     * 分页查询订单（参数/返回值均为领域模型）
     * @param page 分页参数
     * @param order 查询条件（领域模型）
     * @return 分页结果（领域模型）
     */
    Page<Order> pageQuery(Page<Order> page, Order order);

    /**
     * 批量保存订单（参数为领域模型列表）
     * @param orderList 订单领域模型列表
     * @return 保存结果
     */
    boolean batchSave(List<Order> orderList);

    /**
     * 订单聚合保存
     * @param createOrderRes
     */
    void orderAggSave(CreateOrderRes createOrderRes);

    /**
     * 根据订单号更新订单状态（主订单+SPU+SKU订单）
     * @param orderNo 订单号
     * @param sourceState 原状态（可为null，null时不校验原状态）
     * @param toState 目标状态
     * @param closeReason SPU订单拓展字段（可为null）
     */
    void updateOrderChainStateByOrderNo(String orderNo, Integer sourceState, Integer toState, String closeReason);

    /**
     * 单订单号更新主订单状态
     *
     * @param orderNo     订单号
     * @param sourceState 原状态
     * @param toState     目标状态
     * @param closeReason
     * @return 影响行数
     */
    int updateMainOrderStateByOrderNo(String orderNo, Integer sourceState, Integer toState, String closeReason);

}