package com.newzkl.platform.base.biz.account.model.auth.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SubPasswordLoginReq {
    /**
     * 主账号名称
     */
    @NotEmpty(message = "mainUsername?")
    private String mainUsername;
    /**
     * 子账号名称
     */
    @NotEmpty(message = "username?")
    private String username;
    /**
     * 用户密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;
}