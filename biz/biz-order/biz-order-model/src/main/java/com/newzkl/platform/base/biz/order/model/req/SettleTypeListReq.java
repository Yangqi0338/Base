package com.newzkl.platform.base.biz.order.model.req;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

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