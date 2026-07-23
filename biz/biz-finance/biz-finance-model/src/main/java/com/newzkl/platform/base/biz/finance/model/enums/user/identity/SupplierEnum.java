package com.newzkl.platform.base.biz.finance.model.enums.user.identity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
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
    public enum State {
        /**
         * 未开通
         */
        INIT(0, "未开通"),
        /**
         * 已开通
         */
        AUDITING(1, "已开通"),
        /**
         * 已入驻
         */
        NORMAL(2, "已入驻"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }
}
