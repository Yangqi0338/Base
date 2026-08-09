package com.newzkl.platform.base.biz.order.model.req;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 结算类型列表请求对象
 *
 * @author fang
 */
@Data
public class SettleTypeListReq {
    /**
     * 结算单ID
     */
    @NotNull
    private Long settleRecordId;
    /**
     * 结算类型
     * @ext 0 商品 1 运费 2 售后冲正; 无对应枚举, 保留 Integer (参见 deferred D-55)
     */
    @NotNull
    private Integer type;
}