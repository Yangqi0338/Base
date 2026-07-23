package com.newzkl.platform.base.biz.account.model.vo;

import lombok.Data;

/**
 * 忘记密码VO
 *
 * @author sijiwang
 */
@Data
public class ResetMemberVO {
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
     * 设备码
     */
    private String deviceCode;

    /**
     * 签名时间戳
     */
    private Long timestamp;

    /**
     * 前端生成的随机nonce
     */
    private String nonce;
}
