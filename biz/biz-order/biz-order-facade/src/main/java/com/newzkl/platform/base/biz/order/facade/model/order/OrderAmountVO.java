package com.newzkl.platform.base.biz.order.facade.model.order;


import lombok.Data;

@Data
public class OrderAmountVO implements java.io.Serializable{



    /**
     * 订单总
     */
    private Integer orderCount;


    /**
     * 订单总金额
     */
    private Long totalOrderAmount;
}
