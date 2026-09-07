package com.newzkl.platform.base.biz.order.model.support.api.openapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单状态变更通知事件
 *
 * <p>plugin-openapi 的开发者回调出站体 由 {@code AbstractDeveloperNotifyConsumer} 序列化后 POST 给开发者</p>
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
