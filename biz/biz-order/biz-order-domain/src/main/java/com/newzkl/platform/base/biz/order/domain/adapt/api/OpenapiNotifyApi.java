package com.newzkl.platform.base.biz.order.domain.adapt.api;

import java.util.List;

/**
 * 开放平台通知出站端口
 *
 * @author KC
 */
public interface OpenapiNotifyApi {

    /**
     * 批量发送开发者通知
     *
     * @param accountIds 接收方账户ID列表
     * @param command    通知事件入参
     */
    void batchSend(List<Long> accountIds, NotifyEventCommand command);
}
