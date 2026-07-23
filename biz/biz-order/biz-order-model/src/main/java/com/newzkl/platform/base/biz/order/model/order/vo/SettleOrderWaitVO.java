package com.newzkl.platform.base.biz.order.model.order.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 待结算订单信息表
 * @author fang
 */
@Data
public class SettleOrderWaitVO {
     /**
     * ID
     */
     private Long id;
     /**
     * 供应商ID
     */
     private Long supplierId;
     /**
     * SPU_订单ID
     */
     private String spuOrderNo;
     /**
     * sku订单ID
     */
     private String skuOrderNo;
     /**
      * 类型 0 商品 1 运费 2 售后冲正
      */
     private Integer type;
     /**
     * 订单结算金额
     */
     private Integer orderMoney;
     /**
     * SPU_ID
     */
     private Long spuId;
     /**
     * SKU_ID
     */
     private Long skuId;
     /**
     * sku数量
     */
     private Integer skuCount;
     /**
     * 结算状态
     */
     private Integer settleState;
     /**
     * 结算时间
     */
     private LocalDateTime settleTime;

     private String spuImg;

     private String spuName;

     private String skuName;
     /**
      * 售后状态
      */
     private Integer refundState;
     private Long refundId;
     /**
      * 下单时间
      */
     private LocalDateTime createOrderTime;
}