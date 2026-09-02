package com.newzkl.platform.base.common.ddd.model.enums.audit;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核相关枚举
 *
 * <p>审核引擎(audit_flow/audit_template/DAG)拆除后只余审核态与动作语义:
 * 各业务链把审核态直接落主数据, 不再有审批模板与流转节点概念</p>
 *
 * @author god
 */
public class AuditEnum {

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
        /** 待审核 */
        PENDING("PENDING", "待审核"),
        /** 已通过 */
        APPROVED("APPROVED", "已通过"),
        /** 已拒绝 */
        REJECTED("REJECTED", "已拒绝"),
        ;

        @EnumValue
        @JsonValue
        private final String code;
        private final String value;
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
