package com.newzkl.platform.base.common.ddd.model.enums.account;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 供应商枚举
 * @author fang
 */
@Data
public class SupplierEnum {

    /**
     * 性别
     */
    @Getter
    @AllArgsConstructor
    public enum State implements IEnum<Integer> {
        /**
         * 未开通
         */
        INIT(0, "未开通"),
        /**
         * 审核申请中
         */
        AUDITING(1, "审核申请中"),
        /**
         * 已入驻
         */
        NORMAL(2, "已入驻"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        /**
         * 按 code 反查供应商状态
         *
         * @param code 状态码
         * @return 匹配的状态, 无匹配返回 null
         */
        public static State getByCode(Integer code) {
            if (code == null) {
                return null;
            }
            for (State state : values()) {
                if (state.code.equals(code)) {
                    return state;
                }
            }
            return null;
        }
    }

    /**
     * 保证金缴纳配置
     */
    @Getter
    @AllArgsConstructor
    public enum PromisePayConfig implements IEnum<Integer> {
        /**
         * 即时缴纳
         */
        IMMEDIATE(0, "即时缴纳"),
        /**
         * 延迟缴纳
         */
        DELAY(1, "延迟缴纳"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        /**
         * 按 code 反查保证金缴纳配置
         *
         * @param code 配置码
         * @return 匹配的配置, 无匹配返回 null
         */
        public static PromisePayConfig getByCode(Integer code) {
            if (code == null) {
                return null;
            }
            for (PromisePayConfig config : values()) {
                if (config.code.equals(code)) {
                    return config;
                }
            }
            return null;
        }
    }
}
