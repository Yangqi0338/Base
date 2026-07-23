package com.newzkl.platform.base.biz.order.model.order.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 支付订单参数
 * @author sijiwang
 */
@Data
public class PayOrderReq {

    /**
     * 订单号
     */
    @NotNull(message = "orderNo不能为空")
    private String orderNo;
    /**
     * 支付类型
     */
    @NotNull(message = "paymentType不能为空")
    private Integer paymentType;
}
