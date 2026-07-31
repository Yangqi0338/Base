package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.dto.RefundOperationRecordEntity;


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
    RefundOperationRecordEntity save(RefundOperationRecordEntity entity);

    /**
     * 按售后单ID查询操作记录列表（按操作时间倒序）
     *
     * @param refundId 售后单ID
     * @return 操作记录列表
     */
    List<RefundOperationRecordEntity> listByRefundId(Long refundId);

    /**
     * 分页查询售后操作记录
     *
     * @param page          分页参数
     * @param refundId      售后单ID（可选）
     * @param operatorRoleCode 操作方角色编码（可选）
     * @return 分页结果（领域模型）
     */
    IPage<RefundOperationRecordEntity> pageQuery(Page<RefundOperationRecordEntity> page, Long refundId, Long operatorRoleCode);
}