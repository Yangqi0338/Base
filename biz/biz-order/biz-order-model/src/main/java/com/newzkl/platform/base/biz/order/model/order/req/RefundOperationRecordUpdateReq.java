package com.newzkl.platform.base.biz.order.model.order.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 售后操作记录-修改请求对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.domain.refund.model.req.RefundOperationRecordUpdateReq}。
 * 仅开放描述性字段修改, 业务主数据 (售后单/操作人/状态) 由领域层从原记录回填, 与旧实现一致;
 * 旧 hibernate {@code @Length} 改为标准 {@code jakarta.validation.constraints.Size}。</p>
 *
 * @author sijiwang
 * @since 2026-01-23
 */
@Data
public class RefundOperationRecordUpdateReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @NotNull(message = "记录ID不能为空")
    private Long id;

    /**
     * 操作内容描述
     */
    @Size(max = 512, message = "操作内容描述长度不能超过512位")
    private String operationContent;

    /**
     * 本次操作涉及的退款金额
     */
    private Integer refundAmount;

    /**
     * 物流公司名称
     */
    @Size(max = 64, message = "物流公司名称长度不能超过64位")
    private String freightCompanyName;

    /**
     * 物流单号
     */
    @Size(max = 64, message = "物流单号长度不能超过64位")
    private String freightNo;

    /**
     * 操作原因/备注
     */
    @Size(max = 512, message = "操作原因长度不能超过512位")
    private String reason;

    /**
     * 拓展字段 (JSON 格式)
     */
    @Size(max = 1024, message = "拓展字段长度不能超过1024位")
    private String ext;
}
