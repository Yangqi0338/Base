package com.newzkl.platform.base.common.ddd.model.enums.activity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.Getter;

/**
 * @Description: 活动相关枚举
 * @Author: niu
 * @Date: 2024/1/15 15:32
 */
public class ActivityEnum {


    @Getter
    public enum ActivityState implements IEnum<Integer> {
        /**
         * 关闭
         */
        CLOSE(0, "暂不开启  "),

        /**
         * 开启
         */
        OPEN(1, "立即开启");

        @EnumValue
        @JsonValue
        private Integer state;
        private String info;

        ActivityState(Integer state, String info) {
            this.state = state;
            this.info = info;
        }

        public void setState(Integer state) {
            this.state = state;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        @Override
        public Integer getCode() {
            return state;
        }

        @Override
        public String getValue() {
            return info;
        }
    }


    @Getter
    public enum ExecuteState implements IEnum<String> {
        /**
         * 关闭
         */
        PENDING("PENDING", "未生效"),

        /**
         * 开启
         */
        ACTIVE("ACTIVE", "生效中"),
        /**
         * 暂停
         */
        CANCELLED("CANCELLED", "已作废");

        @EnumValue
        @JsonValue
        private final String code;

        private final String desc;

        ExecuteState(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getValue() {
            return desc;
        }

        public static String getDescByCode(String code) {
            if (code == null) return null;
            try {
                return ExecuteState.valueOf(code).desc;
            } catch (IllegalArgumentException e) {
                return null; // 或者返回默认值
            }
        }

    }


    @Getter
    public enum SettleState implements IEnum<String> {
        /**
         * 待确认
         */
        PENDING_CONFIRMATION("PENDING_CONFIRMATION", "待确认"),

        /**
         * 开启
         */
        CONFIRMED("CONFIRMED", "已确认"),
        /**
         * 已删除
         */
        DELETED("DELETED", "已删除");

        @EnumValue
        @JsonValue
        private final String code;

        private final String desc;

        SettleState(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getValue() {
            return desc;
        }

        public static String getDescByCode(String code) {
            if (code == null) return null;
            try {
                return SettleState.valueOf(code).desc;
            } catch (IllegalArgumentException e) {
                return null; // 或者返回默认值
            }
        }

    }

    @Getter
    public enum DividendStatus implements IEnum<String> {
        /**
         * 未分红
         */
        NOT_DIVIDEND("NOT_DIVIDEND", "未分红"),

        /**
         * 已分红
         */
        DIVIDEND_PAID("DIVIDEND_PAID", "已分红");

        @EnumValue
        @JsonValue
        private final String code;

        private final String desc;

        DividendStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getValue() {
            return desc;
        }

        public static String getDescByCode(String code) {
            if (code == null) return null;
            try {
                return DividendStatus.valueOf(code).desc;
            } catch (IllegalArgumentException e) {
                return null; // 或者返回默认值
            }
        }

    }

    @Getter
    public enum StrategyMode implements IEnum<Integer> {
        /**
         * 贡献值分配
         */
        CONTRIBUTE(0, "贡献值分配"),

        /**
         * 关闭contribute
         */
        SELECTOR_LEVEL(1, "甄选师等级分配");

        @EnumValue
        @JsonValue
        private Integer mode;
        private String info;

        StrategyMode(Integer mode, String info) {
            this.mode = mode;
            this.info = info;
        }

        public void setMode(Integer mode) {
            this.mode = mode;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        @Override
        public Integer getCode() {
            return mode;
        }

        @Override
        public String getValue() {
            return info;
        }
    }

    @Getter
    public enum DividendCycle implements IEnum<String> {

        WEEKLY("WEEKLY", "周结算"),

        MONTHLY("MONTHLY", "月结算");


        @EnumValue
        @JsonValue
        private final String code;

        private final String desc;

        DividendCycle(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getValue() {
            return desc;
        }

        public static String getDescByCode(String code) {
            if (code == null) return null;
            try {
                return DividendCycle.valueOf(code).desc;
            } catch (IllegalArgumentException e) {
                return null; // 或者返回默认值
            }
        }
    }

    @Getter
    public enum SettlementStrategy implements IEnum<String> {

        CYCLE("CYCLE", "周期循环"),

        ONCE("ONCE", "单次结算后关闭");


        @EnumValue
        @JsonValue
        private final String code;

        private final String desc;

        SettlementStrategy(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getValue() {
            return desc;
        }

        public static String getDescByCode(String code) {
            if (code == null) return null;
            try {
                return SettlementStrategy.valueOf(code).desc;
            } catch (IllegalArgumentException e) {
                return null; // 或者返回默认值
            }
        }
    }

    @Getter
    public enum DividendMethod implements IEnum<String> {

        AVERAGE("AVERAGE", "平均分红"),

        WEIGHT("WEIGHT", "加权分红");


        @EnumValue
        @JsonValue
        private final String code;

        private final String desc;

        DividendMethod(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getValue() {
            return desc;
        }

        public static String getDescByCode(String code) {
            if (code == null) return null;
            try {
                return DividendMethod.valueOf(code).desc;
            } catch (IllegalArgumentException e) {
                return null; // 或者返回默认值
            }
        }


    }


}
