package com.newzkl.platform.base.biz.account.model.auth.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 员工密码登录请求
 *
 * <p>字段名逐字沿用旧 {@code com.zkl.scm.user.domain.emp.model.EmpPasswordLoginReq},
 * 不改前端契约。{@code parentUsername} 传 {@code "0"} 表示登录主账号自身</p>
 *
 * @author KC
 */
@Data
public class EmpPasswordLoginReq {

    /**
     * 父用户名
     */
    @NotEmpty(message = "parentUsername?")
    private String parentUsername;

    /**
     * 用户名
     */
    @NotEmpty(message = "username?")
    private String username;

    /**
     * 用户密码
     */
    @NotEmpty(message = "password?")
    private String password;
}
