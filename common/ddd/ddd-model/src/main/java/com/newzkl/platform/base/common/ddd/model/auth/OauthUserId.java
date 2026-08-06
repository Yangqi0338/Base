package com.newzkl.platform.base.common.ddd.model.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当前登录用户ID自动注入标记
 *
 * <p>标注在入参对象的 {@code Long}/{@code String} 字段上。当该对象随 {@code @Valid} 触发校验时,
 * 由 {@link OauthUserInjectionValidator} 取当前登录用户ID回填至此字段, 免于前端传入与后端手动 set。</p>
 *
 * <p>依赖宿主类(或其父类, 如 {@code BaseReq})标注 {@link OauthUserInjection} 方能生效, 因字段级
 * 校验器拿不到宿主对象, 注入须由类级校验器执行。</p>
 *
 * @author KC
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OauthUserId {

    /**
     * 未登录(取不到用户ID)时是否报错
     *
     * @return true 抛校验失败, false 静默跳过注入
     */
    boolean required() default true;
}
