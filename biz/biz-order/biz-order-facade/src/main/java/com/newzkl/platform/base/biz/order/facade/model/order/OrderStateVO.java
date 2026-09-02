package com.newzkl.platform.base.biz.order.facade.model.order;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;

import java.io.Serializable;

/**
* SPU订单
* @author fang
*/
@Data
public class OrderStateVO implements Serializable {
    /**
     * 订单号
     */
    private Long id;
    /**
     * 订单状态
     */
    private OrderEnum.State orderState;
}
