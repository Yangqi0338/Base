package com.newzkl.platform.base.biz.order.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @author sijiwang
 */
@Data
public class PayMemberOrderCommand {
    /**
     * 订单id
     */
    @NotNull(message = "订单id不能为空")
    private Long orderId;

    /**
     * 支付类型
     */
    @NotNull(message = "paymentType不能为空")
    private OrderEnum.PayType paymentType;
}
