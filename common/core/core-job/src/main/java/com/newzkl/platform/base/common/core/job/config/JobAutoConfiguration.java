package com.newzkl.platform.base.common.core.job.config;

import cn.hutool.core.util.StrUtil;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XXL-Job 执行器自动装配。
 *
 * <p>由 {@code @EnableJobConfiguration} 主动激活 (via {@code @Import})。
 * 装配 {@link XxlJobSpringExecutor}, 执行器启动后自动扫描 Spring 容器内所有原生
 * {@code @XxlJob} 标注方法并注册到调度中心。</p>
 *
 * <p>迁移自源各模块 {@code XxlJobConfig} (统一模板), {@code @Value} 注入改为
 * {@link JobProperties} 类型安全绑定。</p>
 *
 * @author KC
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JobProperties.class)
public class JobAutoConfiguration {

    private final JobProperties jobProperties;

    /**
     * 构建 XXL-Job Spring 执行器。
     *
     * @return 执行器实例
     */
    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        executor.setAdminAddresses(jobProperties.getAdmin().getAddresses());
        executor.setAppname(jobProperties.getExecutor().getAppname());
        executor.setAddress(jobProperties.getExecutor().getAddress());
        executor.setIp(jobProperties.getExecutor().getIp());
        executor.setPort(jobProperties.getExecutor().getPort());
        executor.setAccessToken(jobProperties.getAccessToken());
        String logpath = jobProperties.getExecutor().getLogpath();
        if (StrUtil.isNotBlank(logpath)) {
            executor.setLogPath(logpath);
        }
        executor.setLogRetentionDays(jobProperties.getExecutor().getLogretentiondays());
        return executor;
    }
}
