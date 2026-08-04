package com.newzkl.platform.base.common.core.mq.model.notify;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 开放平台通知 MQ 载体
 *
 * <p>NotifyUtil 发往 MQ 的消息体。开发者 appId / 回调地址(notifyAddress) 的解析
 * 与 HTTP 回调下沉到 message 域消费方处理, 本载体仅承接接收方 accountId 列表与事件内容。</p>
 *
 * @author KC
 */
@Data
public class NotifyEventMq implements Serializable {

    /**
     * 接收方账户ID列表
     */
    private List<Long> accountIds;

    /**
     * 服务类型
     *
     * @ext {@link NotifyEnums.ServiceType} 的 code
     */
    private Integer serviceType;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 需要推送的事件详情
     *
     * @ext JSON 文本
     */
    private String eventContent;
}
