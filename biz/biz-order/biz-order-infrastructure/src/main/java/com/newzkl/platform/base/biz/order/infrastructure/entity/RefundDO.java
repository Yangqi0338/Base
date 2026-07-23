package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.entity.BaseDO;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

import java.time.LocalDateTime;

/**
 * 售后单表 DO
 *
 * @author sijiwang
 * @since 2026-07-04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class RefundDO extends BaseDO {

    /**
     * 售后单状态
     */
    @Index
    private RefundEnum.State refundState;

    /**
     * 售后类型 0仅退款 1退货退款
     */
    private RefundEnum.RefundType refundType;

    /**
     * 订单类型
     */
    private OrderEnum.OrderType orderType;

    /**
     * 商品渠道类型
     */
    private Integer spuChannelType;

    /**
     * 供应商ID
     */
    @Index
    private Long supplierId;

    /**
     * 渠道商ID
     */
    @Index
    private Long channelId;

    /**
     * 订单ID
     */
    @Index
    private String orderNo;

    /**
     * SPU订单ID
     */
    private String spuOrderNo;

    /**
     * 售后运费金额
     */
    private Integer freightAmount;

    /**
     * 售后金额
     */
    private Integer refundAmount;

    /**
     * 铺货商品金额
     */
    private Integer supplierAmount;

    /**
     * 选品商品金额
     */
    private Integer goodsAmount;

    /**
     * 铺货商品金额
     */
    private Integer storeAmount;

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
     * 审核完成时间
     */
    private LocalDateTime auditTime;

    /**
     * 售后完成时间
     */
    private LocalDateTime refundTime;

    /**
     * 来源状态
     */
    private Integer fromState;

    /**
     * 售后明细
     */
    private String item;

    /**
     * 来源订单状态
     */
    private Integer fromOrderState;

    /**
     * 售后状态流转日志
     */
    private String refundStateLog;

    /**
     * C端ID
     */
    private Long memberId;

    /**
     * 商户ID
     */
    private Long merchantId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 拒绝原因
     */
    private String refuseReason;

    /**
     * 外部售后单ID
     */
    private String outRefundId;

    /**
     * 状态时间
     */
    private LocalDateTime stateTime;

    /**
     * 退货货流信息拓展
     */
    private String freightExt;

    /**
     * 商家自动确认截止时间
     */
    private LocalDateTime storeAutoTime;

    /**
     * 渠道商服务费
     */
    private Integer serviceAmount;

    /**
     * 收货地址
     */
    private String receiveAddress;

    /**
     * 外部售后地址 ApiRefundFreightAddressVO.class
     */
    private String outRefundAddress;

}