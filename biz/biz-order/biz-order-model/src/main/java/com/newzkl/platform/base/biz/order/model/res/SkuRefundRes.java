package com.newzkl.platform.base.biz.order.model.res;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
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
    private Long spuOrderId;
    /**
     * sku订单id
     */
    private Long skuOrderId;
    /**
     * sku订单状态
     */
     private OrderEnum.State skuOrderState;
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
     * spu_name
     */
    private String spuName;
    /**
     * spu_img
     */
    private String spuImg;
}
