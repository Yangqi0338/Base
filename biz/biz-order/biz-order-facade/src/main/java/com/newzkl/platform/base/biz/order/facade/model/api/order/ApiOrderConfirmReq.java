package com.newzkl.platform.base.biz.order.facade.model.api.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 确认收货参数
 * @author fang
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApiOrderConfirmReq  implements Serializable {
    /**
     * 外部订单号
     */
    @NotNull
    private String  outOrderNo;
    /**
     * skuId列表
     */
    @NotNull
    private List<Long> skuIdList;
}
