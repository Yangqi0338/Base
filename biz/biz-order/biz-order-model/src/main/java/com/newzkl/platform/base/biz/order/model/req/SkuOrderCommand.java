package com.newzkl.platform.base.biz.order.model.req;

import com.newzkl.platform.base.common.core.model.dto.Money;

import lombok.Data;

import java.time.LocalDateTime;

/**
* SKU订单
* @author fang
*/
@Data
public class SkuOrderCommand {
    /**
     * 订单ID
     */
    private Long id;
    /**
     * 商品金额
     */
    private Money goodsAmount;
    /**
     * 运费金额
     */
    private Money freightAmount;
    /**
     * 优惠金额
     */
    private Money discountAmount;
    /**
     * 订单金额
     */
    private Money totalAmount;
    /**
     * 订单状态 (0, "新订单"),(2,"待付款"),(4, "派发中"),(6,"待发货"),(8,"待收货"),(10,"已收货"),(12,"已完成"),(99,"已关闭")
     */
    private Integer orderState;
    /**
     * 发货完成时间
     */
    private LocalDateTime deliveredTime;
    /**
     * 确认收货时间
     */
    private LocalDateTime receiveTime;
}
