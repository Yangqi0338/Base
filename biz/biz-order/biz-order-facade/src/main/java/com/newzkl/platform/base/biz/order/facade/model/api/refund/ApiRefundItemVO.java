package com.newzkl.platform.base.biz.order.facade.model.api.refund;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
* 售后单明细
* @author fang
*/
@Data
public class ApiRefundItemVO implements Serializable {
    /**
     * skuId
     */
    @NotNull
    private Long skuId;
    /**
     * 退款数量
     */
    @NotNull
    private Integer count;
    /**
     * 售后金额
     */
    @NotNull
    private Integer refundAmount;
}
