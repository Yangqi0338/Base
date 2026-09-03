package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.core.model.money.Money;

import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 待结算订单信息表
 * @author fang
 */
@Data
public class SettleOrderWaitVO extends BaseRes {
     /**
     * ID
     */
     private Long id;
     /**
     * 供应商ID
     */
     private Long supplierId;
     /**
     * 交易单号
     */
     private String orderNo;
     /**
     * sku订单ID
     */
     private Long skuOrderId;
     /**
      * 类型 0 商品 1 运费 2 售后冲正
      */
     private Integer type;
     /**
     * 订单结算金额
     */
     private Money orderMoney;
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
     private RefundEnum.State refundState;
     private Long refundId;
     /**
      * 下单时间
      */
     private LocalDateTime createOrderTime;
}