package com.newzkl.platform.base.common.core.job.annotation;

import com.newzkl.platform.base.common.core.job.config.XxlJobConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用 XXL-Job 定时任务
 * 在启动类或配置类上添加此注解以启用 XXL-Job
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(XxlJobConfig.class)
public @interface EnableXXLJob {
}