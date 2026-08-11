package com.newzkl.platform.base.biz.socialbang.model.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 活动ID格式校验注解
 *
 * <p>约束格式为 {@code FHC + yyyyMMddHHmmss + 6位自编码}, 如 FHC20250831103030000001</p>
 *
 * @author niu
 */
@Documented
@Constraint(validatedBy = ActivityIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityIdValid {

    String message() default "活动ID格式不正确，应为 FHC+yyyyMMddHHmmss+6位数字";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
