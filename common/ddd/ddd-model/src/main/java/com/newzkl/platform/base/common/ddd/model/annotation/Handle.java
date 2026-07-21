package com.newzkl.platform.base.common.ddd.model.annotation;

import java.lang.annotation.*;

/**
 * 通过此注解声明的接口，自动实现字典翻译
 *
 * @Author scott
 * @email jeecgos@163.com
 * @Date 2022年01月05日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Handle {

    String[] value() default {};

}
