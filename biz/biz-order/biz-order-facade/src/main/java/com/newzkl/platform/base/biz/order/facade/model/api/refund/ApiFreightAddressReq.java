package com.newzkl.platform.base.biz.order.facade.model.api.refund;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 退货地址查询参数
 * @author muc_fang
 */
@Data
public class ApiFreightAddressReq  implements Serializable {
    /**
     * 外部订单号
     */
    @NotNull
    private String outOrderNo;
    /**
     * SpuId
     */
    @NotNull
    private Long spuId;
}
