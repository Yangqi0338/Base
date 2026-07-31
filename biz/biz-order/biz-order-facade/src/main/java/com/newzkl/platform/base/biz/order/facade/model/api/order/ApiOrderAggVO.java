package com.newzkl.platform.base.biz.order.facade.model.api.order;


import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
* 订单信息
* @author fang
*/
@Data
public class ApiOrderAggVO implements Serializable {
    /**
     * 订单
     */
    @NotNull
    private ApiOrderVO order;
    /**
     * 订单明细
     */
    @NotNull
    private List<ApiSkuOrderVO> orderItem;
}
