package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.dto.RefundOperationRecordDTO;
import com.newzkl.platform.base.biz.order.model.req.query.RefundOperationRecordQuery;


import java.util.List;

/**
 * 售后操作记录 仓储接口
 *
 * @author 开发者名称
 * @since 2026-01-23
 */
public interface IRefundOperationRecordRepository {

    /**
     * 新增售后操作记录
     *
     * @param entity 领域模型
     * @return 新增后的领域模型（带ID）
     */
    Long save(RefundOperationRecordDTO entity);

    /**
     * 按售后单ID查询操作记录列表（按操作时间倒序）
     *
     * @param refundId 售后单ID
     * @return 操作记录列表
     */
    List<RefundOperationRecordDTO> listByRefundId(Long refundId);

    /**
     * 分页查询售后操作记录
     *
     * @param query          筛选条件
     * @return 分页结果（领域模型）
     */
    Page<RefundOperationRecordDTO> pageQuery(RefundOperationRecordQuery query);
}