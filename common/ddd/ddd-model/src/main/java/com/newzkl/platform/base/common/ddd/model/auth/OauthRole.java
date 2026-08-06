package com.newzkl.platform.base.common.ddd.model.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当前登录用户角色自动注入标记
 *
 * <p>标注在入参对象的字段上, 随 {@code @Valid} 触发校验时由校验器取当前登录用户角色回填。
 * 字段类型支持:</p>
 * <ul>
 *     <li>{@code Long}/{@code String} — 回填角色ID({@code SecurityUtils.getRoleId()})</li>
 *     <li>{@code RoleEnum.CompanyRole} — 回填角色枚举({@code SecurityUtils.getRole()})</li>
 * </ul>
 *
 * <p>依赖宿主类(或其父类, 如 {@code BaseReq})标注 {@link OauthUserInjection} 方能生效, 因字段级
 * 校验器拿不到宿主对象, 注入须由类级校验器执行。</p>
 *
 * @author KC
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OauthRole {

    /**
     * 未登录(取不到角色)时是否报错
     *
     * @return true 抛校验失败, false 静默跳过注入
     */
    boolean required() default true;
}
