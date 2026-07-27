package com.newzkl.platform.base.biz.order.model.order.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 售后操作记录-分页查询请求对象。
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.domain.refund.model.req.RefundOperationRecordPageReq}。
 * 沿用旧 {@code current}/{@code size} 分页字段 (与同域 {@code OrderStateRecordPageReq} 一致,
 * 不改前端契约); {@code spuOrderId} (Long) 改为业务单号 {@code spuOrderNo} (String)。</p>
 *
 * @author sijiwang
 * @since 2026-01-23
 */
@Data
public class RefundOperationRecordPageReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码 (默认 1)
     */
    private Long current = 1L;

    /**
     * 页大小 (默认 10)
     */
    private Long size = 10L;

    /**
     * SPU 订单号 (检索条件)
     */
    private String spuOrderNo;

    /**
     * 售后单 refund 表的主键 (检索条件)
     */
    private Long refundId;

    /**
     * 操作人 ID (检索条件)
     */
    private Long operatorId;

    /**
     * 操作方角色编码 (检索条件)
     */
    private Long operatorRoleCode;
}
