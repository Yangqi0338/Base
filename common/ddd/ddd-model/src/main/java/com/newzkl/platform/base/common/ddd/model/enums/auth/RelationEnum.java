package com.newzkl.platform.base.common.ddd.model.enums.auth;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限关系枚举组
 *
 * <p>account/role/permission 三方多态关联, 以 {@link Type} 区分关系语义,
 * ACCOUNT_PERMISSION 派生关系以 {@link Source} 记录来源</p>
 *
 * @author KC
 */
public final class RelationEnum {

    private RelationEnum() {
    }

    /**
     * 关系类型
     */
    @Getter
    @AllArgsConstructor
    public enum Type implements IEnum<String> {
        /** 账号-角色 */
        ACCOUNT_ROLE("ACCOUNT_ROLE", "账号-角色"),
        /** 角色-权限 */
        ROLE_PERMISSION("ROLE_PERMISSION", "角色-权限"),
        /** 账号-权限 */
        ACCOUNT_PERMISSION("ACCOUNT_PERMISSION", "账号-权限"),
        ;

        @EnumValue
        @JsonValue
        private final String code;
        private final String value;
    }

    /**
     * 账号-权限来源
     */
    @Getter
    @AllArgsConstructor
    public enum Source implements IEnum<String> {
        /** 角色派生 */
        ROLE_DERIVED("ROLE_DERIVED", "角色派生"),
        /** 直接授权 */
        DIRECT("DIRECT", "直接授权"),
        ;

        @EnumValue
        @JsonValue
        private final String code;
        private final String value;
    }
}
