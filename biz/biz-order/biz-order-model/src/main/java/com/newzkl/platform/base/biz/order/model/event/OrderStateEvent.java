package com.newzkl.platform.base.biz.order.model.event;

import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单状态变更业务事件
 *
 * <p>由 biz-order 在交易单状态迁移成功后无条件发布, 订阅方自行按 orderType 过滤。
 * 承载收件人与订单类型是为了让订阅方零回查即可路由 —— Base 不感知任何订阅方</p>
 *
 * @author KC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStateEvent implements Serializable {

    /**
     * 外部订单号
     */
    private String outOrderNo;

    /**
     * 下单主体账户ID 乐态订单即渠道商
     */
    private Long channelId;

    /**
     * 订单类型
     */
    private OrderEnum.OrderType orderType;

    /**
     * 变更前状态
     */
    private OrderEnum.State sourceState;

    /**
     * 变更后状态
     */
    private OrderEnum.State newState;

}
