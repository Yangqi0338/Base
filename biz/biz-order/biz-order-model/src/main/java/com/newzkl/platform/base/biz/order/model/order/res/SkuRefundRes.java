package com.newzkl.platform.base.biz.order.model.order.res;

import lombok.Data;

/**
 * @author muc_fang
 * @Description: 订单商品发货信息
 * @date 2023/5/914:58
 */
@Data
public class SkuRefundRes {
    /**
     * SkuID
     */
    private Long skuId;
    /**
     * spuId
     */
    private Long spuId;
    /**
     * spu订单id
     */
    private String spuOrderNo;
    /**
     * sku订单id
     */
    private String skuOrderNo;
    /**
     * sku订单状态
     */
    private Integer skuOrderState;
    /**
     * 购买数量
     */
    private Integer orderCount;
    /**
     * 售后中数量
     */
    private Integer refundingCount;
    /**
     * 已售后数量
     */
    private Integer refundedCount;
    /**
     * 已发货数量
     */
    private Integer deliverCount;
    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * SKU商品快照（JSON格式：名称/图片/规格/重量/体积等）
     */
    private String skuSnapshot;
}
