package com.newzkl.platform.base.biz.finance.model.enums.activity;

import lombok.Getter;

/**
 * @Description: 活动相关枚举
 * @Author: niu
 * @Date: 2024/1/15 15:32
 */
public class ActivityEnum {


    @Getter
    public enum ActivityState {
        /**
         * 暂不开启
         */
        CLOSE(0, "暂不开启  "),

        /** 立即开启 */
        OPEN(1, "立即开启");

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
    }


    @Getter
    public enum ExecuteState {
        /** 未生效 */
        PENDING("PENDING", "未生效"),

        /** 生效中 */
        ACTIVE("ACTIVE", "生效中"),
        /** 已作废 */
        CANCELLED("CANCELLED", "已作废");

        private final String code;

        private final String desc;

        ExecuteState(String code, String desc) {
            this.code = code;
            this.desc = desc;
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
    public enum SettleState {
        /** 待确认 */
        PENDING_CONFIRMATION("PENDING_CONFIRMATION", "待确认"),

        /** 已确认 */
        CONFIRMED("CONFIRMED", "已确认"),
        /** 已删除 */
        DELETED("DELETED", "已删除");

        private final String code;

        private final String desc;

        SettleState(String code, String desc) {
            this.code = code;
            this.desc = desc;
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
    public enum DividendStatus {
        /** 未分红 */
        NOT_DIVIDEND("NOT_DIVIDEND", "未分红"),

        /** 已分红 */
        DIVIDEND_PAID("DIVIDEND_PAID", "已分红");

        private final String code;

        private final String desc;

        DividendStatus(String code, String desc) {
            this.code = code;
            this.desc = desc;
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
    public enum StrategyMode {
        /** 贡献值分配 */
        CONTRIBUTE(0, "贡献值分配"),

        /** 甄选师等级分配 */
        SELECTOR_LEVEL(1, "甄选师等级分配");

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
    }

    @Getter
    public enum DividendCycle {
        /** 周结算 */
        WEEKLY("WEEKLY", "周结算"),

        /** 月结算 */
        MONTHLY("MONTHLY", "月结算");


        private final String code;

        private final String desc;

        DividendCycle(String code, String desc) {
            this.code = code;
            this.desc = desc;
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
    public enum SettlementStrategy {
        /** 周期循环 */
        CYCLE("CYCLE", "周期循环"),

        /** 单次结算后关闭 */
        ONCE("ONCE", "单次结算后关闭");


        private final String code;

        private final String desc;

        SettlementStrategy(String code, String desc) {
            this.code = code;
            this.desc = desc;
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
    public enum DividendMethod {
        /** 平均分红 */
        AVERAGE("AVERAGE", "平均分红"),

        /** 加权分红 */
        WEIGHT("WEIGHT", "加权分红");


        private final String code;

        private final String desc;

        DividendMethod(String code, String desc) {
            this.code = code;
            this.desc = desc;
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
