package com.newzkl.platform.base.biz.order.model.order.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 结算记录表
 * @author fang
 */
@Data
public class SettleRecordVO {
     /**
     * ID
     */
     private Long id;
     /**
     * 供应商ID
     */
     private Long supplierId;
     /**
     * 结算时间(版本号)
     */
     private LocalDateTime settleTime;
     /**
     * 结算金额
     */
     private Integer settleMoney;
     /**
     * 结算商品数量
     */
     private Integer settleGoodsNum;
     /**
      * 货款金额
      */
     private Integer goodsAmount;
     /**
      * 运费金额
      */
     private Integer freightAmount;
     /**
      * 售后金额
      */
     private Integer refundAmount;
     /**
      * 标签
      */
     private String label;
}