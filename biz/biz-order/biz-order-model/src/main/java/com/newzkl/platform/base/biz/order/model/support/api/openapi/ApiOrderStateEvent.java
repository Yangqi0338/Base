package com.newzkl.platform.base.biz.order.model.support.api.openapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单状态变更通知事件
 *
 * <p>迁移自 new-scm scm-message-rpc {@code ApiOrderStateEvent}, 序列化进 NotifyEventCommand.eventInfo。</p>
 *
 * @author muc_fang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiOrderStateEvent implements Serializable {

    /**
     * 外部订单号
     */
    private String outOrderNo;

    /**
     * 原订单状态
     */
    private Integer sourceState;

    /**
     * 新订单状态
     */
    private Integer newState;
}
