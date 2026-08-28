package com.newzkl.platform.base.common.ddd.action.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * <p>参数校验异常统一由此转 HTTP 响应, 并经 QDox 解析被校验类字段的 javadoc 首行,
 * 拼成 "[字段业务名]默认文案"(如 "[所属课程ID]不能为null"), 使各 Req 字段免于手写 message。</p>
 *
 * @author fang
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * javadoc 首行截断符 — 逗号/句号/换行
     */
    private static final char[] JAVADOC_HEAD_STOPS = {',', '，', '。', '\n', ';', '；'};

    /**
     * 业务异常
     *
     * @param e 业务异常
     * @return 失败响应
     */
    @ExceptionHandler(PlatformException.class)
    public PlatformResult<?> handlePlatformException(PlatformException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return PlatformResult.fail(e.getCode(), e.getMessage());
    }

    /**
     * 缺少必要请求参数
     *
     * @param e 缺参异常
     * @return 失败响应
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public PlatformResult<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return PlatformResult.fail(HttpStatus.BAD_REQUEST.value(), String.format("缺少必要参数[%s]", e.getParameterName()));
    }

    /**
     * 请求体参数校验失败(@RequestBody + @Valid)
     *
     * <p>逐字段取 javadoc 首行作业务名, 与 hibernate-validator 默认文案拼接。</p>
     *
     * @param e 校验异常
     * @return 失败响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public PlatformResult<?> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Object target = e.getBindingResult().getTarget();
        Class<?> targetClass = target == null ? null : target.getClass();
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> prependFieldComment(targetClass, fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(";\n"));
        log.warn("参数校验失败: {}", message);
        return PlatformResult.fail(BaseErrorCode.PARAM_JSON.getCode(), message);
    }

    /**
     * 单参校验失败(@Validated 标注类 + 方法级 @NotNull 等)
     *
     * @param e 约束违反异常
     * @return 失败响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public PlatformResult<?> handleConstraintViolation(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = violations.stream()
                .map(violation -> {
                    Class<?> rootClass = violation.getRootBeanClass();
                    String propertyPath = violation.getPropertyPath().toString();
                    String fieldName = StrUtil.subAfter(propertyPath, ".", true);
                    return prependFieldComment(rootClass, fieldName, violation.getMessage());
                })
                .collect(Collectors.joining(";\n"));
        log.warn("参数校验失败: {}", message);
        return PlatformResult.fail(BaseErrorCode.PARAM_JSON.getCode(), message);
    }

    /**
     * 表单/查询参数绑定校验失败
     *
     * @param e 绑定异常
     * @return 失败响应
     */
    @ExceptionHandler(BindException.class)
    public PlatformResult<?> handleBindException(BindException e) {
        Object target = e.getBindingResult().getTarget();
        Class<?> targetClass = target == null ? null : target.getClass();
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> prependFieldComment(targetClass, fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(";\n"));
        log.warn("参数绑定失败: {}", message);
        return PlatformResult.fail(BaseErrorCode.PARAM_JSON.getCode(), message);
    }

    /**
     * 请求方式不支持
     *
     * @param e 请求方式不支持异常
     * @return 失败响应
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public PlatformResult<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return PlatformResult.fail("不支持'" + e.getMethod() + "'请求");
    }

    /**
     * 未登录 — sa-token 校验登录态失败
     *
     * @param e 未登录异常
     * @return 失败响应
     */
    @ExceptionHandler(NotLoginException.class)
    public PlatformResult<?> handleNotLogin(NotLoginException e) {
        log.warn("未登录: {}", e.getMessage());
        return PlatformResult.fail(BaseErrorCode.USER_NOT_LOGIN.getCode(), "登录状态失效, 请重新登录");
    }

    /**
     * 无功能权限 — sa-token 鉴权失败, 直出所缺功能显示名
     *
     * @param e 权限不足异常
     * @return 失败响应
     */
    @ExceptionHandler(NotPermissionException.class)
    public PlatformResult<?> handleNotPermission(NotPermissionException e) {
        log.warn("权限不足: {}", e.getPermission());
        return PlatformResult.fail(BaseErrorCode.NO_AUTH.getCode(), StrUtil.format("无操作权限: {}", e.getPermission()));
    }

    /**
     * 兜底异常
     *
     * @param e       异常
     * @param request 请求
     * @return 失败响应
     */
    @ExceptionHandler(Exception.class)
    public PlatformResult<?> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常", e);
        return PlatformResult.fail(BaseErrorCode.SERVER.getCode(), "网络繁忙，请重试或联系客服");
    }

    /**
     * 取字段 javadoc 首行业务名, 拼在默认校验文案前
     *
     * <p>解析失败(无源码/找不到字段)时降级为仅默认文案, 不影响主流程。</p>
     *
     * @param targetClass 被校验类, 为 null 时不拼业务名
     * @param fieldName   字段名
     * @param defaultMsg  校验默认文案(手写 message 或 i18n 默认)
     * @return 拼接后的错误文案
     */
    private String prependFieldComment(Class<?> targetClass, String fieldName, String defaultMsg) {
        if (targetClass == null) {
            return defaultMsg;
        }
        String comment = "";
        try {
            JavaClass javaClass = CommonUtil.findJavaClass(targetClass);
            JavaField javaField = CommonUtil.findJavaField(javaClass, fieldName);
            if (javaField != null) {
                comment = firstLine(javaField.getComment());
            }
        } catch (Exception ignored) {
            // 解析失败降级为仅默认文案
        }
        return StrUtil.isBlank(comment) ? defaultMsg : StrUtil.format("[{}]{}", comment, defaultMsg);
    }

    /**
     * 取 javadoc 注释首行业务语义(截到首个逗号/句号/分号/换行)
     *
     * @param comment 完整 javadoc 注释体
     * @return 首行业务名, 空注释返回空串
     */
    private String firstLine(String comment) {
        if (StrUtil.isBlank(comment)) {
            return "";
        }
        String head = comment;
        for (char stop : JAVADOC_HEAD_STOPS) {
            head = StrUtil.subBefore(head, stop, false);
        }
        return head.trim();
    }
}
