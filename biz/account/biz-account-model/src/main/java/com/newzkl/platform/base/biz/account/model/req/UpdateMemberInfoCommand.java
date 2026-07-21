package com.newzkl.platform.base.biz.account.model.req;

import lombok.Data;

/**
 * @author sijiwang
 */
@Data
public class UpdateMemberInfoCommand {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 密码
     */
    private String oldPassword;

    /**
     * 密码
     */
    private String newPassword;

    /**
     * 变更的手机号
     */
    private String newPhone;

    /**
     * 发验证码的手机号（现在绑定的手机号）
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;
}
