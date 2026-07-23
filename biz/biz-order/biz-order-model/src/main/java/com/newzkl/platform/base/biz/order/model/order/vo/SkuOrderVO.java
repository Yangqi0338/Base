package com.newzkl.platform.base.biz.order.model.order.vo;

import com.newzkl.platform.base.biz.order.model.order.dto.OrderDeliveryItem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 * SKU级订单明细表 DTO
 * 纯业务属性，无数据库注解和技术细节
 *
 * @author sijiwang
 */
@Data
public class SkuOrderVO implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * sku单号
     */
    private String skuOrderNo;

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
     * 订单状态流转日志（逗号隔开）
     */
    private String orderStateLog;

    /**
     * 结算节点
     */
    private Integer settleOrderType;

    /**
     * 结算配置（JSON格式）
     */
    private String settlementConfig;

    /**
     * 运费结算发送状态
     */
    private Integer settleSendState;

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 外部SKU ID
     */
    private Long outSkuId;

    /**
     * SPU ID
     */
    private Long spuId;

    /**
     * 铺货ID
     */
    private Long distributionId;

    /**
     * SKU商品快照（JSON格式：名称/图片/规格/重量/体积等）
     */
    private String skuSnapshot;

    /**
     * 购买数量
     */
    private Integer buyNum;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 售后中数量
     */
    private Integer refundingQuantity;

    /**
     * 已售后数量
     */
    private Integer refundedQuantity;

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
     * 配送信息列表
     */
    private List<OrderDeliveryItem> deliveryItemList;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}