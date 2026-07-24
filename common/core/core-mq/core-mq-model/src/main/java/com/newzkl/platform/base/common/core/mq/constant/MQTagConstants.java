package com.newzkl.platform.base.common.core.mq.constant;

/**
 * MQ 消息 Tag 常量 — 仅技术 / 基建 tag。
 *
 * <p>业务 tag (订单超时 / 支付回调 等) 归各 biz 模块自行定义, 不进 core。</p>
 */
public final class MQTagConstants {

    private MQTagConstants() {
    }

    /**
     * JobContext clear。
     * @ext 触发各实例 ScanJob reload (BROADCASTING)
     */
    public static final String JOB_CONTEXT_CLEAR = "JOB_CONTEXT_CLEAR";

    /**
     * ExecuteLog 补录 / 作废。
     * @ext CLUSTERING 仅一实例消费, 避免重复 INSERT
     */
    public static final String EXECUTE_LOG = "EXECUTE_LOG";
}
