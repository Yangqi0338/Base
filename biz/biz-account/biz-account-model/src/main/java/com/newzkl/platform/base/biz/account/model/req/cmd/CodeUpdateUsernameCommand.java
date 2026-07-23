package com.newzkl.platform.base.biz.account.model.req.cmd;

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
    @NotEmpty(message = "code?")
    private String code;

    /**
     * 新用户名
     */
    @NotEmpty(message = "newUsername?")
    private String newUsername;
}
