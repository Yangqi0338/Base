package com.newzkl.platform.base.biz.order.model.event;

import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 售后单状态变更业务事件
 *
 * <p>由 biz-order 在售后单状态迁移成功后无条件发布, 订阅方自行按 orderType 过滤。
 * 承载收件人与订单类型是为了让订阅方零回查即可路由 —— Base 不感知任何订阅方</p>
 *
 * @author KC
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundStateEvent implements Serializable {

    /**
     * 售后单ID
     */
    private Long refundId;

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
    private RefundEnum.State sourceState;

    /**
     * 变更后状态
     */
    private RefundEnum.State newState;

}
