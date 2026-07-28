package com.newzkl.platform.base.biz.order.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.req.OrderStateRecordCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderStateRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderStateRecordUpdateReq;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateRecordVO;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单状态记录 应用层服务接口
 * 封装领域服务调用，处理跨领域/第三方交互、事务编排等应用层逻辑
 *
 * @author sijiwang
 * @since 2026-01-30
 */
public interface OrderStateRecordService {

    /**
     * 新增订单状态记录
     * @param req 新增请求对象
     * @return 视图对象
     */
    OrderStateRecordVO create(OrderStateRecordCreateReq req);

    /**
     * 修改订单状态记录
     * @param req 修改请求对象
     * @return 视图对象
     */
    OrderStateRecordVO update(OrderStateRecordUpdateReq req);

    /**
     * 按ID查询订单状态记录
     * @param id 主键ID
     * @return 视图对象
     */
    OrderStateRecordVO getById(Long id);

    /**
     * 按订单ID查询所有状态记录（按操作时间倒序）
     *
     * @param orderNo@return 视图对象列表
     */
    List<OrderStateRecordVO> listByOrderNo(String orderNo);

    /**
     * 按订单ID和变更后状态查询记录
     *
     * @param orderNo
     * @param afterOrderState 变更后订单状态
     * @return 视图对象（Optional包装）
     */
    Optional<OrderStateRecordVO> findByOrderNoAndAfterOrderState(String orderNo, Integer afterOrderState);

    /**
     * 根据操作时间范围查询记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 返回结果数量限制
     * @return 视图对象列表
     */
    List<OrderStateRecordVO> findByOperateTimeBetween(LocalDateTime startTime, LocalDateTime endTime, int limit);

    /**
     * 分页查询订单状态记录
     * @param req 分页查询请求对象
     * @return 分页结果（视图对象）
     */
    Page<OrderStateRecordVO> pageQuery(OrderStateRecordPageReq req);
}