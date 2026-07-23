package com.newzkl.platform.base.biz.order.model.order.dto;

import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * SPU级订单明细表 DTO
 * 纯业务属性，无数据库注解和技术细节
 *
 * @author sijiwang
 */
@Data
public class SpuOrder implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * spu单号
     */
    private String spuOrderNo;

    /**
     * 交易单号（关联交易订单主表）
     */
    private String orderNo;

    /**
     * 订单状态
     */
    private Integer orderState;

    /**
     * 订单类型
     */
    private OrderEnum.OrderType orderType;

    /**
     * 外部单号（第三方系统订单号）
     */
    private String outOrderNo;

    /**
     * 收益三方账号
     */
    private String benefitTripartiteId;

    /**
     * 是否外部订单：0-否 1-是
     */
    private Integer isExternalOrder;

    /**
     * 来源类型
     */
    private Integer sourceType;

    /**
     * 订单状态流转日志（逗号隔开）
     */
    private String orderStateLog;

    /**
     * 运费结算发送状态
     */
    private Integer settleFreightState;

    /**
     * SPU ID
     */
    private Long spuId;

    /**
     * 外部SPU ID
     */
    private Long outSpuId;

    /**
     * 商品数量
     */
    private Integer goodsQuantity;

    /**
     * 商品快照（JSON格式：SPU名称/图片/规格等）
     */
    private String goodsSnapshot;

    /**
     * 订单地址ID（关联地址表）
     */
    private Long shipAddressId;

    /**
     * 收货信息（JSON格式）
     */
    private String receiptInfo;

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 平台进货总金额（单位：分）
     */
    private Long platformPurchaseAmount;

    /**
     * 平台铺货总金额（单位：分）
     */
    private Long platformDistributionAmount;

    /**
     * 渠道商进货总金额（单位：分）
     */
    private Long channelPurchaseAmount;

    /**
     * 渠道商铺货总金额（单位：分）
     */
    private Long channelDistributionAmount;

    /**
     * 门店销售总金额（单位：分）
     */
    private Long storeSalesAmount;

    /**
     * 订单应付总额（单位：分）
     */
    private Long orderPayableAmount;

    /**
     * 订单实付总额（单位：分）
     */
    private Long orderActualAmount;

    /**
     * 运费金额（单位：分）
     */
    private Long freightAmount;

    /**
     * 优惠金额（单位：分）
     */
    private Long discountAmount;

    /**
     * 平台服务费（单位：分）
     */
    private Long platformServiceFee;

    /**
     * 平台服务费率
     */
    private Double platformServiceRatio;

    /**
     * 运营商服务费（单位：分）
     */
    private Long operatorServiceFee;

    /**
     * 运营商服务费率
     */
    private Double operatorServiceRatio;

    /**
     * 总服务费（单位：分）
     */
    private Long totalServiceFee;

    /**
     * 总服务费率
     */
    private Double totalServiceRatio;

    /**
     * 用户支付金额（单位：分）
     */
    private Long userPayAmount;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 支付流水号
     */
    private String payFlowNo;

    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 确认收货时间
     */
    private LocalDateTime receiveTime;

    /**
     * 是否正在售后：0-否 1-是
     */
    private Integer isRefunding;

    /**
     * 售后数量
     */
    private Integer refundQuantity;

    /**
     * 订单关闭原因
     */
    private String closeReason;

    /**
     * 拓展字段（JSON格式）
     */
    private String extendInfo;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}