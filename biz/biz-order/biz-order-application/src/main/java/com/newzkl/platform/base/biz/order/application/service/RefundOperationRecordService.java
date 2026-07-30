package com.newzkl.platform.base.biz.order.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundOperationRecordUpdateReq;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundOperationRecordVO;

import java.util.List;

/**
 * 售后操作记录 (协商记录) 应用服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.application.service.IRefundOperationRecordService}, 去 I 前缀。
 * 薄编排层: Req 转领域模型 -> 委托 {@code RefundOperationRecordDomain} -> 转 VO。</p>
 *
 * @author sijiwang
 * @since 2026-01-23
 */
public interface RefundOperationRecordService {

    /**
     * 新增售后操作记录
     *
     * @param req 新增入参
     * @return 视图对象
     */
    RefundOperationRecordVO create(RefundOperationRecordCreateReq req);

    /**
     * 修改售后操作记录
     *
     * @param req 修改入参
     * @return 视图对象
     */
    RefundOperationRecordVO update(RefundOperationRecordUpdateReq req);

    /**
     * 删除售后操作记录
     *
     * @param id 主键 ID
     */
    void delete(Long id);

    /**
     * 按售后单 ID 查询操作记录列表
     *
     * @param refundId 售后单 ID
     * @return 视图对象列表, 永不为 null
     */
    List<RefundOperationRecordVO> listByRefundId(Long refundId);

    /**
     * 分页查询售后操作记录
     *
     * @param req 分页查询入参
     * @return 分页结果 (视图对象)
     */
    Page<RefundOperationRecordVO> pageQuery(RefundOperationRecordPageReq req);
}
