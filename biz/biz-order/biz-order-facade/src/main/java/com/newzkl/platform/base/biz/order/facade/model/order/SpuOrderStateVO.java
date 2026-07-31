package com.newzkl.platform.base.biz.order.facade.model.order;

import lombok.Data;

import java.io.Serializable;

/**
* SPU订单
* @author fang
*/
@Data
public class SpuOrderStateVO implements Serializable {
    /**
     * 订单号
     */
    private Long orderId;
    /**
     * 订单状态
     */
    private Integer orderState;
}
