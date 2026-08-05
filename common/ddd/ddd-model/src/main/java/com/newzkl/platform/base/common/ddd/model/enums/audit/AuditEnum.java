package com.newzkl.platform.base.common.ddd.model.enums.audit;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核相关枚举
 */
public class AuditEnum {

    public static final String END_STEP_CODE = "end";

    @Getter
    @AllArgsConstructor
    public enum TemplateType implements IEnum<Long> {
        ROLE_APPLY(1L, "角色申请审批模板"),
        PROMISE_FLOW(2L, "保证金缴纳审批模板"),
        SPU_CREATE(3L, "SPU上传审批模板"),
        BRAND_CREATE(4L, "品牌申请审批模板"),
        SPU_WORK_TABLE(5L, "SPU工单审批模板"),
        NAME_AUTH(6L, "实名认证审批模板"),
        ;
        @EnumValue
        @JsonValue
        private Long code;
        private String value;
    }

    /**
     * 审核状态
     */
    @Getter
    @AllArgsConstructor
    public enum State implements IEnum<Integer> {
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
    public enum Action implements IEnum<Integer> {
        /** 拒绝 */
        REFUSE(0, "拒绝"),
        /** 通过 */
        PASS(1, "通过"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    /**
     * 审批状态枚举
     */
    @Getter
    @AllArgsConstructor
    public enum ApprovalStatus implements IEnum<String> {
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
        @EnumValue
        @JsonValue
        private final String value;
        /**
         * 中文描述
         */
        private final String description;

        @Override
        public String getCode() {
            return value;
        }

        @Override
        public String getValue() {
            return description;
        }

        /**
         * 根据状态值获取枚举
         */
        public static ApprovalStatus fromValue(String value) {
            if (value == null || value.trim().isEmpty()) {
                return PENDING;
            }
            for (ApprovalStatus status : values()) {
                if (status.getCode().equalsIgnoreCase(value.trim())) {
                    return status;
                }
            }
            throw new IllegalArgumentException("无效的审批状态值: " + value);
        }
    }

    @Getter
    @AllArgsConstructor
    public enum WithdrawSate implements IEnum<Integer> {
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
