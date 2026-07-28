package com.newzkl.platform.base.biz.auth.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RBAC 权限枚举集。
 *
 * <p>原 {@code com.newzkl.platform.base.biz.account.model.enums.AuthEnum} 在 biz-auth 切分时按语义拆分:
 * 本类保留 RBAC 侧 (系统 / 权限关系 / 关系对象 / 功能点类型); 员工类型 {@code EmpType} 属账号语义,
 * 留在 biz-account-model 原处。</p>
 *
 * @author fang
 */
public class AuthEnum {

    /**
     * 系统类型。
     */
    @Getter
    @AllArgsConstructor
    public enum SystemType {
        OVERALL_PLATFORM(1, "总平台"),
        ;
        private Integer code;
        private String value;
    }

    /**
     * 权限关系类型。
     */
    @Getter
    @AllArgsConstructor
    public enum RelationType {
        ROLE_MENU("角色与菜单关系"),
        ROLE_API("角色与权限关系"),
        USER_ROLE("用户与角色关系"),
        MENU_API("菜单与权限关系"),
        SYS_MENU("系统与菜单关系"),
        SYS_ROLE("系统与角色关系"),
        ;
        private String value;
    }

    /**
     * 关系对象类型。
     */
    @Getter
    @AllArgsConstructor
    public enum RelationObjectType {
        ROLE("角色"),
        API("权限"),
        USER("用户"),
        MENU("菜单"),
        SYS("系统"),
        ;
        private String value;
    }

    /**
     * 功能点类型。
     */
    @Getter
    @AllArgsConstructor
    public enum FunctionType {
        FUNCTION("权限"),
        DEFAULT("默认"),
        ;
        private String value;
    }
}
