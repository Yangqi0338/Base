package com.newzkl.platform.base.common.core.job.annotation;

import com.newzkl.platform.base.common.core.job.config.JobAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用 XXL-Job 执行器自动装配。
 *
 * <p>标注入口 starter 的配置类或启动类, 主动激活 {@link JobAutoConfiguration},
 * 装配 {@code XxlJobSpringExecutor}。仿 core-rocketmq {@code @EnableMQConfiguration} 主动激活范式,
 * 不走 SPI 自动发现, 避免无 xxl-job 配置的模块误装配。</p>
 *
 * <p>业务侧任务方法直接使用原生 {@code com.xxl.job.core.handler.annotation.XxlJob} 标注,
 * 执行器自动扫描注册。</p>
 *
 * @author KC
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(JobAutoConfiguration.class)
public @interface EnableJobConfiguration {
}
