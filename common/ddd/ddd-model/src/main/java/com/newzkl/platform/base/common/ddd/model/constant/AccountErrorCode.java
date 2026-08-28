package com.newzkl.platform.base.common.ddd.model.constant;

import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/7/2816:16
 */
@Getter
@AllArgsConstructor
public enum AccountErrorCode implements ErrorCode {

    CODE_ERROR(703, "验证码错误"),
    IS_LOCK(704, "该账号已停用"),
    PASSWORD(706, "密码错误，请重新输入"),
    EXPIRE(707, "登录过期,请重新登录"),
    NO_EXIST(1000, "该账号不存在, 请检查账号是否正确."),
    NO_TOKEN(1001, "缺少Token"),
    EXIST_ROLE(1002, "已拥有该角色"),
    AUTH_ERROR(1003, "认证异常"),
    NOT_OPEN_ROLE(1004, "主账号未开通该角色"),
    EXIST_USERNAME(1005, "账号已存在"),
    PARAM_YQM(1006, "邀请码输入错误"),
    NO_ROLE(1007, "请求缺少角色"),
    NO_CLIENT(1008, "请求缺少身份"),
    NO_INVITE(1009, "该邀请码无邀请权限"),
    NO_AUTH(1010, "权限不足"),
    PARAM_ERROR(1011, "参数错误：{}"),
    IN_BLOCKLIST(1012, "该账号已被封禁,不可{}"),
    NO_AUTHENTICATION(1013, "未实名认证"),
    PHONE_NOT_SET(1014, "账号不存在手机号"),
    NOT_AVAIL_ROLE(1015, "无可用的角色"),
    REGISTER_CERTIFICATE_ERROR(1016, "错误{}凭证"),
    CERTIFICATE_MISSING(1017, "错误{}凭证"),
    PASSWORD_NOT_SET(1018, "未设置密码，请验证码登录后手动设置密码"),
    ;

    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 状态码对应说明文案
     */
    private final String message;
}
