package com.newzkl.platform.base.common.core.job.annotation;



import com.newzkl.platform.base.common.core.job.model.ExecuteLogEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记 XXL-Job handler 为秒级扫描的 reload 入口
 * 与 @XxlJob 同一方法上共存：@XxlJob 由 XXL-Job 框架触发执行，@SecondLevelJob 用于本平台扫描注册。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SecondLevelJob {

    /**
     * 业务类型 code
     * @ext 首版仅 ASSET，对应 ExecuteLogEnum.BizType
     */
    ExecuteLogEnum.BizType bizType() default ExecuteLogEnum.BizType.SYSTEM;
}
