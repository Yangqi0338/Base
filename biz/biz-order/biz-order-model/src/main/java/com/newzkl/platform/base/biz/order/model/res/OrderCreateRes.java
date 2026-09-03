package com.newzkl.platform.base.biz.order.model.res;

import com.newzkl.platform.base.biz.order.model.dto.OrderAgg;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/1210:01
 */
@Data
@NoArgsConstructor
public class OrderCreateRes implements Serializable {
    /**
     * 交易单ID
     */
    private Long orderId;
    private String orderNo;
    /***
     *
     * 交易单状态
     */
    private OrderEnum.State orderState;
    /**
     * 交易单聚合
     */
    private OrderAgg orderAgg;

    /**
     * 支付倒计时
     */
    private Long remainTime;


    public OrderCreateRes(Long orderId, String orderNo, OrderEnum.State orderState, OrderAgg orderAgg) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.orderState = orderState;
        this.orderAgg = orderAgg;
    }
}
