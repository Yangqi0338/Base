package com.newzkl.platform.base.biz.order.model.order.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 订单发货明细DTO
 * @author sijiwang
 */
@Data
public class OrderDeliveryItem {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 发货单号
     */
    private String deliveryNo;

    /**
     * 主订单号
     */
    private String orderNo;

    /**
     * SPU订单号
     */
    private String spuOrderNo;

    /**
     * SKU订单号
     */
    private String skuOrderNo;

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
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 铺货ID
     */
    private Long distributionId;

    /**
     * 购买数量
     */
    private Integer buyNum;

    /**
     * 本次发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 商品单价（分）
     */
    private Long skuPrice;

    /**
     * 商品小计（分）
     */
    private Long skuTotalAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}