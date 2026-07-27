package com.newzkl.platform.base.biz.order.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.RefundOperationRecord;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordPageReq;

import java.util.List;

/**
 * 售后操作记录 (协商记录) 领域服务。
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.domain.refund.service.IRefundOperationRecordDomainService},
 * 去 I 前缀。领域规则校验 (必填/存在性) 沿用旧实现, 校验失败改抛 {@code ScmException}。</p>
 *
 * @author sijiwang
 * @since 2026-01-23
 */
public interface RefundOperationRecordDomain {

    /**
     * 新增售后操作记录 (含领域规则校验)。
     *
     * @param record 领域模型
     * @return 新增后的领域模型
     */
    RefundOperationRecord create(RefundOperationRecord record);

    /**
     * 修改售后操作记录 (含领域规则校验)。
     *
     * @param record 领域模型
     * @return 修改后的领域模型
     */
    RefundOperationRecord update(RefundOperationRecord record);

    /**
     * 删除售后操作记录。
     *
     * @param id 主键 ID
     */
    void delete(Long id);

    /**
     * 按 ID 查询售后操作记录。
     *
     * @param id 主键 ID
     * @return 领域模型, 不存在返回 null
     */
    RefundOperationRecord findById(Long id);

    /**
     * 按售后单 ID 查询操作记录列表 (按操作时间倒序)。
     *
     * @param refundId 售后单 ID
     * @return 操作记录列表, 永不为 null
     */
    List<RefundOperationRecord> listByRefundId(Long refundId);

    /**
     * 按 SPU 订单号查询操作记录列表 (按操作时间倒序)。
     *
     * @param spuOrderNo SPU 订单号
     * @return 操作记录列表, 永不为 null
     */
    List<RefundOperationRecord> listBySpuOrderNo(String spuOrderNo);

    /**
     * 分页查询售后操作记录。
     *
     * @param query 分页查询入参
     * @return 分页结果
     */
    Page<RefundOperationRecord> pageQuery(RefundOperationRecordPageReq query);
}
