package com.newzkl.platform.base.biz.order.facade.model.api.refund;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 售后明细参数
 * @author muc_fang
 */
@Data
public class ApiRefundSubmitGoodsReq  implements Serializable {
    /**
     * SKU_ID
     */
    @NotNull
    private Long skuId;
}
