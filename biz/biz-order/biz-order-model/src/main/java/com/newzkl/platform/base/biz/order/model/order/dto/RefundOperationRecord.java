package com.newzkl.platform.base.biz.order.model.order.dto;

import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 售后操作记录 (协商记录) 领域模型。
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.domain.refund.model.entity.RefundOperationRecordEntity},
 * {@code spuOrderId} 改为业务单号 {@code spuOrderNo}, 状态收敛为枚举。</p>
 *
 * @author sijiwang
 * @since 2026-01-23
 */
@Data
public class RefundOperationRecord {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * SPU 订单号
     */
    private String spuOrderNo;

    /**
     * 售后单 refund 表的主键
     */
    private Long refundId;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作方角色编码
     */
    private Long operatorRoleCode;

    /**
     * 操作方客户端类型
     */
    private String operatorClient;

    /**
     * 操作方名称
     */
    private String operatorName;

    /**
     * 操作前售后单状态
     */
    private RefundEnum.State beforeState;

    /**
     * 操作后售后单状态
     */
    private RefundEnum.State afterState;

    /**
     * 操作类型（0=发起退款申请，1=审核通过，2=审核拒绝，3=提交物流信息，4=确认收货，5=退款完成，6=关闭售后，7=平台介入）
     */
    private Integer operationType;

    /**
     * 操作内容描述
     */
    private String operationContent;

    /**
     * 本次操作涉及的退款金额
     */
    private Integer refundAmount;

    /**
     * 物流公司名称
     */
    private String freightCompanyName;

    /**
     * 物流单号
     */
    private String freightNo;

    /**
     * 操作原因/备注
     */
    private String reason;

    /**
     * 拓展字段（JSON格式）
     */
    private String ext;

    /**
     * 操作时间 (创建时间)
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
