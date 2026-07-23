package com.newzkl.platform.base.biz.order.model.order.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 订单状态统计VO
 * @author AI
 */
@Data
@Accessors(chain = true)
public class OrderStateCountVO {
    
    /**
     * 订单状态
     */
    private Integer orderState;
    
    /**
     * 该状态的订单数量
     */
    private Integer count;
}
