package com.newzkl.platform.base.common.core.model.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 备注标记 (类 TODO, 供人读)
 *
 * <p>标注契约/行为变更等需下游知悉的点, 不影响运行期。典型用途: 枚举加 @JsonValue 后
 * JSON 输出由枚举名变为 code, 在相关字段标 NOTE 备注给前端</p>
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
public @interface NOTE {

    /**
     * 备注内容
     *
     * @return 说明文本
     */
    String value();
}
