package com.newzkl.platform.base.common.core.utils.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务码自动填充注解
 *
 * <p>标注 DO 的 String 字段,mybatis-plus insert 时若字段为空,由自动填充 handler 按 {@link #value()}
 * 生成业务码填入。已有非空值则保留(兼容手动指定)</p>
 *
 * @author KC
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@TableField(fill = FieldFill.INSERT)
public @interface BusinessCode {

    /**
     * 业务类型,决定前缀与发号器
     *
     * @return 业务类型
     */
    BusinessType value();

    /**
     * 序列号长度,0 表示用发号器默认
     *
     * @return 序列号长度
     */
    int length() default 0;
}
