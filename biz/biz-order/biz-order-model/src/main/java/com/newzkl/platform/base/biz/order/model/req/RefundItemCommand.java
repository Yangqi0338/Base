package com.newzkl.platform.base.biz.order.model.req;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/714:01
 */
@Data
public class RefundItemCommand {
    /**
     * SKU_ID
     */
    @NotNull
    private Long skuId;
    /**
     * 售后数量
     */
    private Integer count;
}
