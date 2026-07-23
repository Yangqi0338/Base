package com.newzkl.platform.base.biz.order.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;

/**
 * 订单发货明细表
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class OrderDeliveryItemDO extends BaseDO {

    /**
     * 发货单号
     */
    @Index
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
    @Index
    private Long skuId;

    /**
     * 外部SKU ID
     */
    private Long outSkuId;

    /**
     * SPU ID
     */
    @Index
    private Long spuId;

    /**
     * 供应商ID
     */
    @Index
    private Long supplierId;

    /**
     * 铺货ID
     */
    @Index
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
     * 商品单价（单位：分）
     */
    private Long skuPrice;

    /**
     * 商品小计金额（单位：分）
     */
    private Long skuTotalAmount;
}