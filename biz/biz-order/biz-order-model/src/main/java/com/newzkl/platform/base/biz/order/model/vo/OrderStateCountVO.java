package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
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
    private OrderEnum.State orderState;
    
    /**
     * 该状态的订单数量
     */
    private Integer count;
}
