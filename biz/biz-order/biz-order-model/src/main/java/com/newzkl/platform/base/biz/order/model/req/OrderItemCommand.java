package com.newzkl.platform.base.biz.order.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * @Description: 提交预支付订单请求对象
 * @Author: niu
 * @Date: 2023/4/19 14:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemCommand {

    /**
     * 铺货表idID
     */
    @NotNull(message = "storeDistributionId不能为空")
    private Long storeDistributionId;
    /**
     * skuId
     */
    private Long skuId;
    /**
     * 购买数量
     */
    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "商品数量不能小于1")
    private Integer count;
}
