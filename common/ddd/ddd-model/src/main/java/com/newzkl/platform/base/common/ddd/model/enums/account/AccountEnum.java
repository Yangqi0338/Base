package com.newzkl.platform.base.common.ddd.model.enums.account;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author fang
 */
@Data
public class AccountEnum {

    public static final String USER_DEFAULT_PASSWORD = "123456";

    /**
     * 账号状态
     */
    @Getter
    @AllArgsConstructor
    public enum State implements IEnum<Integer> {
        /** 已销毁 */
        DESTROY(-1, "已销毁"),
        /** 禁用,冻结 */
        DISABLE(0, "禁用,冻结"),
        /** 正常 */
        ENABLE(1, "正常"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    /**
     * 登录类型
     */
    @Getter
    @AllArgsConstructor
    public enum LoginType implements IEnum<Integer> {
        /** 密码 */
        PASSWORD(0, "密码"),
        /** 验证码 */
        CODE(1, "验证码"),
        /**
         * 自动刷新
         */
        REFRESH(2, "自动刷新", false),
        /** 切换身份 */
        TOGGLE(3, "切换身份", false),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
        private final boolean needLog;

        LoginType(Integer code, String value) {
            this.code = code;
            this.value = value;
            this.needLog = true;
        }
    }

}
