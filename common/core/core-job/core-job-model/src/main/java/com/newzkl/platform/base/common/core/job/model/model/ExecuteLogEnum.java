package com.newzkl.platform.base.common.core.job.model.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 执行日志枚举集合
 */
public final class ExecuteLogEnum {

    private ExecuteLogEnum() {
    }

    /**
     * 业务类型
     */
    @Getter
    @AllArgsConstructor
    public enum BizType implements IEnum<String> {

        /** 资产 */
        ASSET("ASSET", "资产", true),
        SYSTEM("SYSTEM", "系统"),
        ;

        @EnumValue
        @JsonValue
        private final String code;
        private final String value;
        private final Boolean secondHandle;

        BizType(String code, String value) {
            this.code = code;
            this.value = value;
            this.secondHandle = false;
        }
    }

    /**
     * 状态
     */
    @Getter
    @AllArgsConstructor
    public enum Status implements IEnum<Integer> {

        /** 待执行 */
        PENDING(0, "待执行"),
        /** 执行中 */
        RUNNING(1, "执行中"),
        /** 已完成 */
        DONE(2, "已完成"),
        /** 已失败 */
        FAILED(-1, "已失败"),
        /** 已跳过 */
        SKIP(-2, "已跳过");

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        @Override
        public Integer getCode() { return code; }

        @Override
        public String getValue() { return value; }
    }

    /**
     * 来源
     */
    @Getter
    @AllArgsConstructor
    public enum Source implements IEnum<Integer> {

        /** 系统 — 半事务 MQ 写入 */
        MQ(0, "系统"),
        /** 定时器 — XXL-Job 触发 */
        JOB(1, "定时器"),
        /** 人为 — 后台手动 */
        MANUAL(2, "人为");

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        @Override
        public Integer getCode() { return code; }

        @Override
        public String getValue() { return value; }
    }

    /**
     * 动作
     */
    @Getter
    @AllArgsConstructor
    public enum Action implements IEnum<Integer> {

        /** 预定时间 */
        SCHEDULED(0, "预定时间"),
        /** 立即执行 */
        IMMEDIATE(1, "立即执行");

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        @Override
        public Integer getCode() { return code; }

        @Override
        public String getValue() { return value; }
    }
}
