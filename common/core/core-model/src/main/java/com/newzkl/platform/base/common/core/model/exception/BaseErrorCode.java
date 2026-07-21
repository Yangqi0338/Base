package com.newzkl.platform.base.common.core.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 86176
 */
@Getter
@AllArgsConstructor
public enum BaseErrorCode implements ErrorCode {
    /**/
    SUCCESS(200, "请求成功"),
    INVALID_UPDATE(400, "无可用的更新数据"),
    SERVER(500, "服务器端错误"),
    UNKNOWN(600, "未知错误"),
    PARAM_JSON(601, "参数解析失败"),
    PARAM(602, "参数异常:{}"),
    DATABASE_ERR(604, "数据库错误"),
    NODATA(604, "{}记录不存在"),
    EXIST_DATA(605, "{}记录已存在"),
    UPDATE(606, "更新失败"),
    REMOTE(607, "远程调用失败 {}"),
    REPEAT(608, "重复操作"),
    BUSY(609, "服务繁忙,请稍后再试"),
    EXECUTE(610, "{}"),
    USER_NOT_LOGIN(611, "用户未登录:{}"),
    OPERATE_FAIL(612, "操作失败:{}"),
    USER_NOT_FOUND(700, "用户不存在:{}"),
    USER_DISABLED(701, "用户已禁用:{}"),
    PASSWORD_ERROR(702, "密码错误:{}"),
    NOT_SERVICE(999, "无此服务"),
    CUSTOM(999, "{}"),
    NOT_FILE(998, "非文件或文件不存在"),
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
