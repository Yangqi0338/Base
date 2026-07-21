package com.newzkl.platform.base.common.core.rocketmq;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RocketMQ 通用枚举与常量。
 *
 * <p>迁移说明: 原 {@code LocalMessageEnum} 中与本地消息业务无关的
 * 消费模式 / 消息模式 / 消息ID key 属通用 MQ 概念，抽取至此；
 * 本地消息业务枚举 (SendState/ConsumeState) 未迁移。</p>
 *
 * @author fang
 */
public class MQEnum {

    /**
     * 本地消息ID key。
     */
    public final static String LOCAL_MESSAGE_ID_KEY = "local_message_id";

    /**
     * 消费模式。
     */
    @Getter
    @AllArgsConstructor
    public enum ConsumeMode {
        /**
         * 使用线程池并发消费。
         */
        CONCURRENTLY("CONCURRENTLY", "多线程"),
        /**
         * 单线程消费。
         */
        ORDERLY("ORDERLY", "单线程");

        private final String mode;
        private final String value;
    }

    /**
     * 消息模式。
     */
    @Getter
    @AllArgsConstructor
    public enum MessageMode {
        /**
         * 集群。
         */
        CLUSTERING("CLUSTERING", "集群"),
        /**
         * 广播。
         */
        BROADCASTING("BROADCASTING", "广播");

        private final String mode;
        private final String value;
    }
}
