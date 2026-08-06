package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.core.model.money.Money;

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
     private Long skuOrderId;
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
     private Money refundAmount;
     /**
      * 货款金额
      */
     private Money supplierAmount;

     /**
      * 铺货金额 门店销售单价
      */
     private Money skuStorePrice;
}