package com.newzkl.platform.base.biz.order.model.order.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/714:01
 */
@Data
public class RefundItemReq {
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
