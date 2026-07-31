package com.newzkl.platform.base.biz.order.facade.model.api.refund;

import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
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
    private RefundEnum.State refundState;
}
