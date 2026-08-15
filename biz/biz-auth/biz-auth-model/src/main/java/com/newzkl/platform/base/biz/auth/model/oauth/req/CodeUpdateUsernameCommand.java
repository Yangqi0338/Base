package com.newzkl.platform.base.biz.auth.model.oauth.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 验证码修改用户名
 *
 * @author ruoyi
 */
@Data
public class CodeUpdateUsernameCommand {
    /**
     * 验证码
     */
    @NotEmpty
    private String code;

    /**
     * 新用户名
     */
    @NotEmpty
    private String newUsername;
}
