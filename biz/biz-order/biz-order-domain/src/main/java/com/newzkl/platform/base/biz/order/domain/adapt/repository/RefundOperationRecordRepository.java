package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.RefundOperationRecord;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordPageReq;

import java.util.List;

/**
 * 售后操作记录 仓储端口
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.domain.refund.repository.IRefundOperationRecordRepository},
 * 去 I 前缀; 分页入参由旧 {@code (Page, refundId, operatorRoleCode)} 收敛为
 * {@code RefundOperationRecordPageReq} 单参。</p>
 *
 * @author sijiwang
 * @since 2026-01-23
 */
public interface RefundOperationRecordRepository {

    /**
     * 新增售后操作记录
     *
     * @param record 领域模型
     * @return 新增后的领域模型 (带 ID)
     */
    RefundOperationRecord save(RefundOperationRecord record);

    /**
     * 按 ID 修改售后操作记录
     *
     * @param record 领域模型
     * @return 修改后的领域模型
     */
    RefundOperationRecord updateById(RefundOperationRecord record);

    /**
     * 按 ID 删除售后操作记录
     *
     * @param id 主键 ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 按 ID 查询售后操作记录
     *
     * @param id 主键 ID
     * @return 领域模型, 不存在返回 null
     */
    RefundOperationRecord findById(Long id);

    /**
     * 按售后单 ID 查询操作记录列表 (按操作时间倒序)
     *
     * @param refundId 售后单 ID
     * @return 操作记录列表, 永不为 null
     */
    List<RefundOperationRecord> listByRefundId(Long refundId);

    /**
     * 按 SPU 订单号查询操作记录列表 (按操作时间倒序)
     *
     * @param spuOrderNo SPU 订单号
     * @return 操作记录列表, 永不为 null
     */
    List<RefundOperationRecord> listBySpuOrderNo(String spuOrderNo);

    /**
     * 分页查询售后操作记录
     *
     * @param query 分页查询入参
     * @return 分页结果 (领域模型)
     */
    Page<RefundOperationRecord> pageByQuery(RefundOperationRecordPageReq query);

    /**
     * 检查 ID 是否存在
     *
     * @param id 主键 ID
     * @return 是否存在
     */
    boolean existsById(Long id);
}
