package com.newzkl.platform.base.biz.order.model.dto;

import lombok.Data;

@Data
public class OutOrder {
    private Long id;

    // 三方订单编号
    private String orderSn;

    private Long orderId;

    private String skuIds;
}
