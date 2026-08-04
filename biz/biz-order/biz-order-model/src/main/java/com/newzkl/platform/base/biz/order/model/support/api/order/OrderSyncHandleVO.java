package com.newzkl.platform.base.biz.order.model.support.api.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 渠道订单支付后同步处理消息体
 *
 * <p>下单成功后投递到 {@code scm_order/orderSyncHandle}, 由消费端做扣减库存、
 * 扣采购金、外部供应链创建订单等后置动作。移植自模板 {@code OrderSyncHandleVO}</p>
 *
 * @author KC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSyncHandleVO implements Serializable {

    /**
     * 订单 id
     */
    private Long orderId;

}
