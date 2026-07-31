package com.newzkl.platform.base.biz.order.domain.adapt.api;

import com.newzkl.platform.base.biz.order.facade.model.order.RefundOperationRecordRPC;

/**
 * 售后操作记录事件出站端口
 *
 * <p>迁移: 原 domain 直连 {@code @DubboReference ILocalMessageFacade} 发 MQ 违依赖硬线,
 * 抽为出站端口, 由 infra 实现(本地消息表 + MQ 投递)
 *
 * @author sijiwang
 */
public interface RefundOperationRecordApi {

    /**
     * 发送售后操作记录事件
     *
     * @param recordRPC 售后操作记录消息体
     */
    void sendRefundOperationRecord(RefundOperationRecordRPC recordRPC);
}
