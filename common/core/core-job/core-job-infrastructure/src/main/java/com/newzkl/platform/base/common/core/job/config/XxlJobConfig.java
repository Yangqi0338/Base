package com.newzkl.platform.base.common.core.job.config;

import com.newzkl.platform.base.common.core.job.XxlJobAdminClient;
import com.newzkl.platform.base.common.core.job.annotation.EnableXXLJob;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

/**
 * XXL-Job 配置, 仅通过 {@code EnableXXLJob} 注解引入
 */
@Slf4j
@DependsOn({"xxlJobProperties"})
public class XxlJobConfig {

    /**
     * 装配 XXL-Job 执行器
     *
     * @return XxlJobSpringExecutor 实例
     */
    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        String addresses = XxlJobProperties.addresses;
        String appName = XxlJobProperties.Executor.appName;
        log.info(">>>>>>>>>>> xxl-job config init, adminAddresses={}, appName={}", addresses, appName);
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        executor.setAdminAddresses(addresses);
        executor.setAppname(appName);
        executor.setAddress(XxlJobProperties.Executor.address);
        executor.setIp(XxlJobProperties.Executor.ip);
        executor.setPort(XxlJobProperties.Executor.port);
        executor.setLogPath(XxlJobProperties.Executor.logPath);
        executor.setLogRetentionDays(XxlJobProperties.Executor.logRetentionDays);
        executor.setAccessToken(XxlJobProperties.accessToken);
        return executor;
    }

    /**
     * 装配 XXL-Job admin REST 客户端
     *
     * @return XxlJobAdminClient 实例
     */
    @Bean
    public XxlJobAdminClient xxlJobAdminClient() {
        return new XxlJobAdminClient();
    }
}
