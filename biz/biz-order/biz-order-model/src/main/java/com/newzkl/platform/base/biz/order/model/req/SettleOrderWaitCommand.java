package com.newzkl.platform.base.biz.order.model.req;

import com.newzkl.platform.base.common.core.model.dto.Money;

import lombok.Data;

/**
* 待结算订单信息表
* @author fang
*/
@Data
public class SettleOrderWaitCommand {
    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * SPU_订单ID
     */
    private Long spuOrderId;
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
     * 售后单ID
     */
    private Long refundId;
}
