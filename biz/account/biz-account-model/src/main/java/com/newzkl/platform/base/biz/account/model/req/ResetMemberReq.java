package com.newzkl.platform.base.biz.account.model.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 找回密码
 *
 * @author sijiwang
 */
@Data
public class ResetMemberReq {

    /**
     * 密码
     */
    private String newPassword;

    /**
     * 发验证码的手机号（现在绑定的手机号）
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 签名时间戳
     */
    private Long timestamp;

    /**
     * 前端生成的随机nonce
     */
    private String nonce;

    /**
     * 加密字符串
     */
    @NotBlank(message = "sign不能为空")
    private String sign;
}
