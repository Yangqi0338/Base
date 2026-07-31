package com.newzkl.platform.base.biz.order.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * @Description: 提交预支付订单请求对象
 * @Author: niu
 * @Date: 2023/4/19 14:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ChannelOrderCreateCommand {
    /**
     * 门店ID
     */
    @NotNull(message = "storeId?")
    private Long storeId;
    /**
     * C端ID
     */
    private Long memberId;
}
