package com.newzkl.platform.base.common.core.redis.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 重复提交校验注解
 *
 * @author 孔祥基
 */
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface DuplicationCheck {
    /**
     * 字段判断代码(spel) （仅 type > 1 时生效）
     *
     * @return spel
     */
    String value() default "";

    /**
     * 是否开启重复提交校验
     *
     * @return 是否开启
     */
    boolean enabled() default true;

    /**
     * 重复提交校验时间间隔
     *
     * @return 间隔
     */
    long time() default 1;

    /**
     * 重复提交校验提示信息
     *
     * @return 提示信息
     */
    String message() default "请勿重复提交";

    /**
     * 校验类型: 1-校验请求地址; 2-校验请求地址 + 请求参数
     *
     * @return 类型
     */
    int type() default 2;

}
