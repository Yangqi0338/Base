package com.newzkl.platform.base.common.core.model.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * BASE
 *
 * @Description: 定义终端异常类型
 * @Author: niu
 * @Date: 2022/4/6 13:27
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class PlatformException extends RuntimeException {

    /**
     * 业务错误码
     */
    private String code;
    /**
     * 错误提示
     */
    private String message;

    public PlatformException(Integer code, String message) {
        this.code = code + "";
        this.message = message;
    }

    public PlatformException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    public PlatformException(ErrorCode errorCode, String... msg) {
        this(errorCode.getCode(), errorCode.getMessage(msg));
    }

    public boolean equalsCode(ErrorCode code) {
        return this.code.equals(code.getCode().toString());
    }
}
