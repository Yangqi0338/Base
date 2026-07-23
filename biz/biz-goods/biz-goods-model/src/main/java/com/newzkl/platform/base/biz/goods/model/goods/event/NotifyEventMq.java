package com.newzkl.platform.base.biz.goods.model.goods.event;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于MQ发送的消息体
 * @author fang
 */
@Data
public class NotifyEventMq implements Serializable {
    /**
     * 需要推送的事件详情
     */
    private String eventContent;
    /**
     * 开发者ID
     */
    private String appId;
    /**
     * 回调地址
     */
    private String notifyAddress;
}
