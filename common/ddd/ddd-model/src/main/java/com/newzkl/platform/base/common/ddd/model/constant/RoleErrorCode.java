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
public enum RoleErrorCode implements ErrorCode {

    CODE_ERROR(703, "验证码错误"),
    IS_LOCK(704, "该账号已停用"),
    PASSWORD(706, "密码错误，请重新输入"),
    EXPIRE(707, "登录过期,请重新登录"),
    NO_EXIST(1000, "该账号不存在, 请检查账号是否正确."),
    NO_TOKEN(1001, "缺少Token"),
    EXIST_ROLE(1002, "已拥有该角色"),
    AUTH_ERROR(1003, "认证异常"),
    NOT_OPEN_ROLE(1004, "主账号未开通该角色"),
    EXIST_Account(1005, "手机号已存在"),
    PARAM_YQM(1006, "邀请码输入错误"),
    NO_ROLE(1007, "请求缺少角色"),
    NO_CLIENT(1008, "请求缺少身份"),
    NO_INVITE(1009, "该邀请码无邀请权限"),
    NO_AUTH(1010, "权限不足"),
    PARAM_ERROR(1011, "参数错误"),
    WARN_ROLE(1011, "错误的角色"),
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
