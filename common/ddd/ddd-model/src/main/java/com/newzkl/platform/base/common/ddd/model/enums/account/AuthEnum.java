package com.newzkl.platform.base.common.ddd.model.enums.account;

import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账号侧认证枚举集
 *
 * <p>biz-auth 切分时按语义拆分: RBAC 侧 (SystemType / RelationType / RelationObjectType / FunctionType)
 * 迁至 {@code com.newzkl.platform.base.biz.auth.model.enums.AuthEnum}; 本类保留账号语义的员工类型。</p>
 *
 * @author fang
 */
public class AuthEnum {

    //员工类型
    @Getter
    @AllArgsConstructor
    public enum EmpType {
        MANAGER(0, "管理员"),
        SIMPLE(1, "普通"),
        ;
        private Integer code;
        private String value;

        public static EmpType getByRole(RoleEnum.CompanyRole role) {
            if (role == null || (role != RoleEnum.CompanyRole.PLATFORM && role != RoleEnum.CompanyRole.EMP)) {
                return null;
            }
            return role == RoleEnum.CompanyRole.PLATFORM ? MANAGER : SIMPLE;
        }
    }
}

