package com.newzkl.platform.base.common.ddd.model.enums.auth;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限领域枚举组
 *
 * <p>Function(功能点) 与 Menu(菜单) 合并为统一 permission, 以 {@link Type} 区分</p>
 *
 * @author KC
 */
public final class PermissionEnum {

    /**
     * 权限类型
     */
    @Getter
    @AllArgsConstructor
    public enum Type implements IEnum<String> {
        /** 菜单 */
        MENU("MENU", "菜单"),
        /** 功能 */
        FUNC("FUNC", "功能"),
        ;

        @EnumValue
        @JsonValue
        private final String code;
        private final String value;
    }

    /**
     * 关系类型
     */
    @Getter
    @AllArgsConstructor
    public enum RelationType implements IEnum<String> {
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
