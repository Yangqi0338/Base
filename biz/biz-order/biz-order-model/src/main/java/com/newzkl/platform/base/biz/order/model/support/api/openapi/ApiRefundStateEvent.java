package com.newzkl.platform.base.biz.order.model.support.api.openapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 售后状态变更通知事件
 *
 * <p>迁移自 new-scm scm-message-rpc {@code ApiRefundStateEvent}, 序列化进 NotifyEventCommand.eventInfo。</p>
 *
 * @author muc_fang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiRefundStateEvent implements Serializable {

    /**
     * 售后单ID
     */
    private Long refundId;

    /**
     * 原售后单状态
     */
    private Integer sourceState;

    /**
     * 新售后单状态
     */
    private Integer newState;
}
