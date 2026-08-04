package com.newzkl.platform.base.common.core.mq.model.notify;

import lombok.Data;

import java.io.Serializable;

/**
 * 开放平台通知事件入参
 *
 * <p>底层跨系统通知的统一入参。上移自 biz-order domain, 供 NotifyUtil 与各业务域共用。
 * {@code eventInfo} 为具体事件 DTO 的 JSON 文本, 由调用方按 serviceType/businessType 约定序列化。</p>
 *
 * @author KC
 */
@Data
public class NotifyEventCommand implements Serializable {

    /**
     * 服务类型
     *
     * @ext {@link NotifyEnums.ServiceType} 的 code
     */
    private Integer serviceType;

    /**
     * 业务类型
     *
     * @ext 依 serviceType 取 {@link NotifyEnums.OrderType} / {@link NotifyEnums.GoodsType} / {@link NotifyEnums.BossType} 的 code
     */
    private Integer businessType;

    /**
     * 事件内容
     *
     * @ext JSON 文本
     */
    private String eventInfo;
}
