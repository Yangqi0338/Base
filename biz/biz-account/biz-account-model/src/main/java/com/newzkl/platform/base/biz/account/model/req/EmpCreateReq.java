package com.newzkl.platform.base.biz.account.model.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 员工创建请求参数
 */
@Data
public class EmpCreateReq {
    /** 员工ID */
    private Long id;
    /** 账号 */
    @NotNull(message = "username?")
    private String username;
    /** 密码 */
    @NotNull(message = "password?")
    private String password;
    /** 岗位ID */
    @NotNull(message = "roleId?")
    private Long roleId;
    /** 企业角色ID */
    private List<Long> companyRoleId;
}
