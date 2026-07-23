package com.newzkl.platform.base.biz.goods.model.goods.req.spu;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Description: 提交预支付订单请求对象
 * @Author: niu
 * @Date: 2023/4/19 14:43
 */
@Data
public class OrderItemCommand {
    /**
     * skuId
     */
    @NotNull(message = "skuId不能为空")
    private Long skuId;
    /**
     * 购买数量
     */
    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "商品数量不能小于1")
    private Integer count;
}
