package com.newzkl.platform.base.biz.order.model.order.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 售后单信息
* @author fang
*/
@Data
public class ApiRefundVO implements Serializable {
    /**
     * 售后单号
     */
    private Long id;
    @NotNull
    /**
     * 订单ID
     */
    @NotNull
    private Long orderId;
    /**
     * 渠道商ID
     */
    @NotNull
    private Long channelId;
    /**
     * (0,"待渠道商审核"),(2,"待供应商审核"),(4,"待提交物流"),(6,"待确认收货"),(7,"待平台介入"),(8,"平台介入中"),(9,"退款中"),(10,"已完成"),(-2,"已拒绝"),(-4,"已关闭"),
     */
    @NotNull
    private Integer refundState;
    /**
     * 售后类型 0仅退款 1退货退款
     */
    @NotNull
    private Integer refundType;
    /**
     * 售后运费金额
     */
    private Integer freightAmount;
    /**
     * 售后金额
     */
    @NotNull
    private Integer refundAmount;
    /**
     * 售后原因
     */
    private String reason;
    /**
     * 申请说明
     */
    private String remark;
    /**
     * 申请图片
     */
    private String images;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 物流公司名称
     */
    private String freightCompanyName;
    /**
     * 物流单号
     */
    private String freightNo;
    /**
     * 收货状态
     */
    private Integer takeDeliveryState;
    /**
     * 退款状态
     */
    private Integer payState;
    /**
     * 售后创建时间
     */
    @NotNull
    private LocalDateTime createTime;
    /**
     * 审核完成时间
     */
    private LocalDateTime auditTime;
    /**
     * 售后完成时间
     */
    private LocalDateTime refundTime;
}
