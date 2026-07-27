package com.newzkl.platform.base.common.ddd.model.res;

import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author niu
 */
@Setter
@Getter
public class PlatformResult<T> implements Serializable {

    private static final long serialVersionUID = 6439723870077111495L;

    private Integer code;
    private String message;
    private T data;
    private Boolean success;

    /**
     * 返回R
     *
     * @param <T> T 泛型标记
     * @return
     */
    public static <T> PlatformResult<T> success() {
        return restResult(BaseErrorCode.SUCCESS.getCode(), "操作成功", null, Boolean.TRUE);
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> PlatformResult<T> success(T data) {
        return restResult(BaseErrorCode.SUCCESS.getCode(), data == null ? "暂无数据" : "操作成功", data, Boolean.TRUE);
    }

    /**
     * 返回R
     *
     * @param data 数据
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> PlatformResult<T> fail(T data, String msg) {
        return restResult(BaseErrorCode.CUSTOM.getCode(), msg, data, Boolean.FALSE);
    }

    /**
     * 返回R
     *
     * @param <T> T 泛型标记
     * @return
     */
    public static <T> PlatformResult<T> fail() {
        return restResult(BaseErrorCode.CUSTOM.getCode(), "操作失败", null, Boolean.FALSE);
    }

    /**
     * 返回R
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return R
     */
    public static <T> PlatformResult<T> fail(String msg) {
        return restResult(BaseErrorCode.CUSTOM.getCode(), msg, null, Boolean.FALSE);
    }

    /**
     * 返回R
     *
     * @param errorCode 消息
     * @param <T>       T 泛型标记
     * @return R
     */
    public static <T> PlatformResult<T> fail(ErrorCode errorCode) {
        return restResult(errorCode.getCode(), errorCode.getMessage(), null, Boolean.FALSE);
    }

    /**
     * 返回R
     *
     * @param code 状态码
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return R
     */
    public static <T> PlatformResult<T> fail(Integer code, String msg) {
        return restResult(code, msg, null, Boolean.FALSE);
    }

    public static <T> PlatformResult<T> restResult(Integer code, String message, T data, Boolean success) {
        PlatformResult<T> r = new PlatformResult<>();
        r.setCode(code);
        r.setData(data);
        r.setSuccess(success);
        r.setMessage(message);
        return r;
    }

    /**
     * 请求成功是否
     *
     * @return
     */
    public boolean isSuccess() {
        return this.code.equals(BaseErrorCode.SUCCESS.getCode());
    }
}
