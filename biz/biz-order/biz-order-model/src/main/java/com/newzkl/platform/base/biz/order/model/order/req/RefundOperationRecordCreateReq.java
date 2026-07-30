package com.newzkl.platform.base.biz.order.model.order.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 售后操作记录-新增请求对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.domain.refund.model.req.RefundOperationRecordCreateReq}。
 * 旧 {@code spuOrderId} (Long) 改为业务单号 {@code spuOrderNo} (String), 与中台订单模型对齐;
 * 旧 hibernate {@code @Length} 改为标准 {@code jakarta.validation.constraints.Size}。</p>
 *
 * @author sijiwang
 * @since 2026-01-23
 */
@Data
public class RefundOperationRecordCreateReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * SPU 订单号
     */
    @NotNull(message = "SPU订单号不能为空")
    private String spuOrderNo;

    /**
     * 售后单 refund 表的主键
     */
    @NotNull(message = "售后单ID不能为空")
    private Long refundId;

    /**
     * 操作人ID
     */
    @NotNull(message = "操作人ID不能为空")
    private Long operatorId;

    /**
     * 操作方角色编码 (对应 RoleEnum.CompanyRole 的 code)
     */
    @NotNull(message = "操作方角色编码不能为空")
    private Long operatorRoleCode;

    /**
     * 操作方客户端类型 (对应 CommonEnum.Client 的 code)
     */
    @NotNull(message = "操作方客户端类型不能为空")
    @Size(max = 32, message = "操作方客户端类型长度不能超过32位")
    private String operatorClient;

    /**
     * 操作方名称
     */
    @NotNull(message = "操作方名称不能为空")
    @Size(max = 64, message = "操作方名称长度不能超过64位")
    private String operatorName;

    /**
     * 操作前售后单状态 (对应 RefundEnum.State 的 code)
     */
    @NotNull(message = "操作前售后单状态不能为空")
    private Integer beforeState;

    /**
     * 操作后售后单状态 (对应 RefundEnum.State 的 code)
     */
    @NotNull(message = "操作后售后单状态不能为空")
    private Integer afterState;

    /**
     * 操作类型 (0=发起退款申请, 1=审核通过, 2=审核拒绝, 3=提交物流信息, 4=确认收货, 5=退款完成, 6=关闭售后, 7=平台介入)
     */
    @NotNull(message = "操作类型不能为空")
    private Integer operationType;

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
