package com.newzkl.platform.base.biz.order.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fang
 */
public class LocalMessageEnum {

    /**
     * 本地消息ID key
     */
    public final static String LOCAL_MESSAGE_ID_KEY = "local_message_id";

    @Getter
    @AllArgsConstructor
    public enum SendState {
        /**
         * 待发送
         */
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

    @Getter
    @AllArgsConstructor
    public enum ConsumeState {
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

    @Getter
    @AllArgsConstructor
    public enum ConsumeMode {
        /**
         * 使用线程池并发消费
         */
        CONCURRENTLY("CONCURRENTLY", "多线程"),
        /**
         * 单线程消费
         */
        ORDERLY("ORDERLY", "单线程");

        private final String mode;
        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum MessageMode {
        /**
         * 集群
         */
        CLUSTERING("CLUSTERING", "集群"),
        /** 广播 */
        BROADCASTING("BROADCASTING", "广播");

        private final String mode;
        private final String value;
    }
}
