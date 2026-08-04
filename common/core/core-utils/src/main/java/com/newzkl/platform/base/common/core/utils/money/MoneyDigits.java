package com.newzkl.platform.base.common.core.utils.money;

import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum.YesOrNo;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 金额校验注解，作用于 {@link com.newzkl.platform.base.common.core.model.dto.Money} 字段
 * <p>min/max 以分为单位。positive=YES 时不允许 0 与负数。null 视为通过校验，配合 {@link jakarta.validation.constraints.NotNull} 使用。</p>
 * @ext 含端点
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = MoneyDigitsValidator.class)
public @interface MoneyDigits {

    /**
     * 最小值，含
     * @ext 分
     */
    long min() default Long.MIN_VALUE;

    /**
     * 最大值，含
     * @ext 分
     */
    long max() default Long.MAX_VALUE;

    /**
     * 是否必须为正数
     * @ext >0
     */
    YesOrNo positive() default YesOrNo.NO;

    String message() default "金额不符合范围";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
