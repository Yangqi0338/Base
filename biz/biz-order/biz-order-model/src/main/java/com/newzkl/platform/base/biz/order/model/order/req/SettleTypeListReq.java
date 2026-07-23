package com.newzkl.platform.base.biz.order.model.order.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SettleTypeListReq {
    /**
     * 结算单ID
     */
    @NotNull
    private Long settleRecordId;
    /**
     * 类型 0 商品 1 运费 2 售后冲正
     */
    @NotNull
    private Integer type;
}