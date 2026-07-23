package com.newzkl.platform.base.biz.order.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fang
 */
public class AuditEnum {

    /**
     * 审核状态
     */
    @Getter
    @AllArgsConstructor
    public enum State {
        /** 待用户提交 */
        CUSTOM(0, "待用户提交"),
        /** 待审核 */
        AUDITING(1, "待审核"),
        /** 通过 */
        SUCCESS(2, "通过"),
        /** 未通过 */
        FAIL(3, "未通过"),
        /** 终止 */
        STOP(4, "终止"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        /**
         * 通过code获取枚举实例
         */
        public static State getByCode(Integer code) {
            if (code == null) {
                return null;
            }
            for (State state : State.values()) {
                if (state.getCode().equals(code)) {
                    return state;
                }
            }
            return null;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum Action {
        /** 拒绝 */
        REFUSE(0, "拒绝"),
        /** 通过 */
        PASS(1, "通过"),
        ;
        private final Integer code;
        private final String value;
    }

    /**
     * 审批状态枚举
     */
    @Getter
    @AllArgsConstructor
    public enum ApprovalStatus {
        /**
         * 待审核
         */
        PENDING("PENDING", "待审核"),
        /**
         * 已通过
         */
        APPROVED("APPROVED", "已通过"),
        /**
         * 已拒绝
         */
        REJECTED("REJECTED", "已拒绝"),
        ;
        /**
         * 英文状态值
         */
        private final String value;
        /**
         * 中文描述
         */
        private final String description;

        /**
         * 根据状态值获取枚举
         */
        public static ApprovalStatus fromValue(String value) {
            if (value == null || value.trim().isEmpty()) {
                return PENDING;
            }
            for (ApprovalStatus status : values()) {
                if (status.getValue().equalsIgnoreCase(value.trim())) {
                    return status;
                }
            }
            throw new IllegalArgumentException("无效的审批状态值: " + value);
        }
    }

    @Getter
    @AllArgsConstructor
    public enum WithdrawSate {
        /**
         * 待审核
         */
        AUDITING(0, "待审核"),
        /**
         * 通过
         */
        PASS(1, "通过"),
        /**
         * 拒绝
         */
        REFUSE(2, "拒绝"),
        /**
         * 退汇
         */
        SUCCESS(3, "退汇"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }
}
