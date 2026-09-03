package com.newzkl.platform.base.biz.order.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @author sijiwang
 */
@Data
public class PayMemberOrderCommand {
    /**
     * 订单号
     */
    @NotNull
    private String orderNo;

    /**
     * 支付类型
     */
    @NotNull
    private PaymentEnum.PayType paymentType;
}
