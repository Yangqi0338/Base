package com.newzkl.platform.base.common.ddd.action.config;

import com.newzkl.platform.base.common.ddd.model.res.ScmResult;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author fang
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ScmException.class)
    public ScmResult<?> handleScmException(ScmException e) {
        return ScmResult.fail(e.getCode(), e.getMessage());
    }

    /**
     * 校验异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ScmResult<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ScmResult.fail(HttpStatus.BAD_REQUEST.value(), String.format("缺少必要参数[%s]", e.getParameterName()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ScmResult<?> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            if (errors != null) {
                errors.forEach(p -> {
                    FieldError fieldError = (FieldError) p;
                    stringBuilder.append(fieldError.getDefaultMessage());
                });
            }
        }
        String message = stringBuilder.toString().replace("?", "不能为空!");
        return ScmResult.fail(BaseErrorCode.PARAM_JSON.getCode(), message);
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ScmResult<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ScmResult.fail("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ScmResult<?> handleException(Exception e) {
        return ScmResult.fail(BaseErrorCode.SERVER.getCode(), "网络繁忙，请重试或联系客服");
    }
}
