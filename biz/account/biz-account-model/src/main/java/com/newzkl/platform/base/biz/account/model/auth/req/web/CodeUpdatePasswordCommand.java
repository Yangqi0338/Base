package com.newzkl.platform.base.biz.account.model.auth.req.web;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 验证码更新密码
 *
 * @author ruoyi
 */
@Data
public class CodeUpdatePasswordCommand {
    /**
     * 验证码
     */
    @NotEmpty(message = "code?")
    private String code;
    /**
     * 新密码
     */
    @NotEmpty(message = "newPassword?")
    private String newPassword;
}
