package com.newzkl.platform.base.common.core.mq.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 本地消息枚举集合。
 *
 * <p>移植自 adopt-chicken common-model {@code MessageEnum}; {@code IEnum} 指向本仓 core-model。</p>
 *
 * @author fang
 */
public class MessageEnum {

    /**
     * 本地消息 ID key。
     * @ext 消息附加属性中携带的本地消息主键
     */
    public static final String LOCAL_MESSAGE_ID_KEY = "local_message_id";

    private MessageEnum() {
    }

    /**
     * 发送状态。
     */
    @Getter
    @AllArgsConstructor
    public enum SendState implements IEnum<Integer> {
        /** 待发送 */
        WAIT(0, "待发送"),
        /** 发送中 */
        SENDING(1, "发送中"),
        /** 发送成功 */
        SUCCESS(2, "发送成功"),
        /** 发送失败 */
        FAIL(3, "发送失败"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    /**
     * 消费状态。
     */
    @Getter
    @AllArgsConstructor
    public enum ConsumeState implements IEnum<Integer> {
        /** 待消费 */
        WAIT(0, "待消费"),
        /** 消费成功 */
        SUCCESS(1, "消费成功"),
        /** 消费失败 */
        FAIL(2, "消费失败"),
        /** 异常:需人工处理 */
        ERROR(3, "异常:需人工处理"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    /**
     * 消费模式。
     */
    @Getter
    @AllArgsConstructor
    public enum ConsumeMode implements IEnum<String> {
        /** 使用线程池并发消费 */
        CONCURRENTLY("CONCURRENTLY", "多线程"),
        /** 单线程消费 */
        ORDERLY("ORDERLY", "单线程");

        @EnumValue
        @JsonValue
        private final String mode;
        private final String value;

        @Override
        public String getCode() {
            return mode;
        }
    }

    /**
     * 消息模式。
     */
    @Getter
    @AllArgsConstructor
    public enum Mode implements IEnum<String> {
        /** 集群 */
        CLUSTERING("CLUSTERING", "集群"),
        /** 广播 */
        BROADCASTING("BROADCASTING", "广播");

        @EnumValue
        private final String mode;
        private final String value;

        @Override
        public String getCode() {
            return mode;
        }
    }

    /**
     * 延迟级别。
     * @ext 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    @Getter
    @AllArgsConstructor
    public enum DelayTimeLevel {
        /** 1 秒 */
        SECOND_1(1),
        /** 5 秒 */
        SECOND_5(2),
        /** 10 秒 */
        SECOND_10(3),
        /** 30 秒 */
        SECOND_30(4),
        /** 1 分钟 */
        MINUTE_1(5),
        /** 2 分钟 */
        MINUTE_2(6),
        /** 3 分钟 */
        MINUTE_3(7),
        /** 4 分钟 */
        MINUTE_4(8),
        /** 5 分钟 */
        MINUTE_5(9),
        /** 6 分钟 */
        MINUTE_6(10),
        /** 7 分钟 */
        MINUTE_7(11),
        /** 8 分钟 */
        MINUTE_8(12),
        /** 9 分钟 */
        MINUTE_9(13),
        /** 10 分钟 */
        MINUTE_10(14),
        /** 20 分钟 */
        MINUTE_20(15),
        /** 30 分钟 */
        MINUTE_30(16),
        /** 1 小时 */
        HOUR_1(17),
        /** 2 小时 */
        HOUR_2(18),
        ;

        private final int level;
    }
}
