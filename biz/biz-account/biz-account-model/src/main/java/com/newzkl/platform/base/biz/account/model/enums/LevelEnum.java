package com.newzkl.platform.base.biz.account.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fang
 */
public class LevelEnum {

    public static final String USER_DEFAULT_PASSWORD = "123456";

    /**
     * 业绩升级条件包含范围
     */
    @Getter
    @AllArgsConstructor
    public enum AmountConditionScope {
        /**
         * 直属下级
         */
        DIRECT(0, "直属下级"),
        /** 非直属下级 */
        NOT_DIRECT(1, "非直属下级"),
        ;
        private Integer code;
        private String value;
    }

    /**
     * 条件满足类型
     */
    @Getter
    @AllArgsConstructor
    public enum ConditionJudgeType {
        /** 满足任意一项 */
        OR(0, "满足任意一项"),
        /** 全部满足 */
        AND(1, "全部满足"),
        ;
        private final Integer code;
        private final String value;
    }
}
