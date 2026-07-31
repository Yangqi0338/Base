package com.newzkl.platform.base.biz.order.facade.model.api.order;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 下单明细参数
 * @Author: fang
 * @Date: 2023/4/19 14:43
 */
@Data
public class ApiOrderSubmitItemReq implements Serializable {
    /**
     * SkuId
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
