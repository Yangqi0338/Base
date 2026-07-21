package com.newzkl.platform.base.common.ddd.model.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * javax检查条件
 *
 * @author: dangzhenghui
 * @date: 2019年03月17日-下午9:37:16
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ValidConditionValidator.class})
@Repeatable(ValidConditions.class)
public @interface ValidCondition {
    /**
     * 条件表达式，如"license_validity_type==0"
     */
    String value();

    /**
     * 分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};
}
