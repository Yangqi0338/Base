package com.newzkl.platform.base.common.ddd.model.annotation;

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
public @interface ValidConditions {
    // 存储多个@ValidCondition注解
    ValidCondition[] value();
}
