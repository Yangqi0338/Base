package com.newzkl.platform.base.biz.auth.model.oauth.req;

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
    @NotEmpty
    private String code;
    /**
     * 新密码
     */
    @NotEmpty
    private String newPassword;
}
