package com.newzkl.platform.base.biz.order.model.order.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
* 售后单状态
* @author fang
*/
@Data
public class ApiRefundStateVO  implements Serializable {
    /**
     * 售后单号
     */
    @NotNull
    private Long refundId;
    /**
     * 售后单状态
     */
    @NotNull
    private Integer refundState;
}
