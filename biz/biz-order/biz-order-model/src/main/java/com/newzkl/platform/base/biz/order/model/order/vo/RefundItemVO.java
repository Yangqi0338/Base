package com.newzkl.platform.base.biz.order.model.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 售后单明细
 * @author fang
 */
@Data
public class RefundItemVO implements Serializable {
     /**
      * sku订单ID
      */
     private String skuOrderNo;
     private String spuImg;
     private String spuName;
     private String skuSaleAttribute;
     /**
      * spuId
      */
     private Long spuId;
     private String outSkuId;
     /**
     * skuId
     */
     private Long skuId;
     /**
     * 退款数量
     */
     private Integer count;
     /**
      * 购买数量
      */
     private Integer orderCount;
     /**
      * 已售后数量
      */
     private Integer refundedCount;
     /**
     * 售后金额
     */
     private Integer refundAmount;
     /**
      * 货款金额
      */
     private Integer supplierAmount;

     /**
      * 铺货金额 门店销售单价
      */
     private Integer skuStorePrice;
}