package com.newzkl.platform.base.biz.order.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 开放平台通知事件入参
 *
 * @author KC
 */
@Data
public class NotifyEventCommand implements Serializable {

    /**
     * 服务类型
     */
    private Integer serviceType;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 事件内容
     *
     * @ext JSON 文本
     */
    private String eventInfo;
}
