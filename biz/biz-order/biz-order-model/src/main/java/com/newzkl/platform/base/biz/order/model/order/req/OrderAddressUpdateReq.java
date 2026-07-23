package com.newzkl.platform.base.biz.order.model.order.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 订单地址更新请求
 * @author sijiwang
 */
@Data
public class OrderAddressUpdateReq {

    @NotNull(message = "orderNo不能为空")
    private String orderNo;

    /**
     * 收货地址ID（关联地址表主键）
     */
    @NotNull(message = "收货地址ID不能为空")
    @Min(value = 1, message = "收货地址ID必须为正整数")
    private Long shipId;

    /**
     * 账号ID（关联账号表主键）
     */
    private Long accountId;
}
