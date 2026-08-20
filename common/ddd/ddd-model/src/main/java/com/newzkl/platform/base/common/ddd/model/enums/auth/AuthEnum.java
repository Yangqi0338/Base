package com.newzkl.platform.base.common.ddd.model.enums.auth;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认证枚举组
 *
 * @author KC
 */
public final class AuthEnum {


    /**
     * 认证类型
     */
    @Getter
    @AllArgsConstructor
    public enum Type implements IEnum<Integer> {
        /** 密码 */
        PASSWORD(0, "密码"),
        /** 验证码 */
        CODE(1, "验证码"),
        /** 自动刷新 */
        REFRESH(2, "自动刷新", false),
        /** 切换身份 */
        TOGGLE(3, "切换身份", false),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
        private final boolean needLog;

        Type(Integer code, String value) {
            this.code = code;
            this.value = value;
            this.needLog = true;
        }
    }
}
