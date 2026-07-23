package com.newzkl.platform.base.common.core.job.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * XXL-Job 执行器配置。
 *
 * <p>对应源 {@code XxlJobConfig} 的 {@code @Value} 注入项, 收敛为类型安全配置绑定,
 * 前缀 {@code xxl.job}。</p>
 *
 * @author KC
 */
@Data
@ConfigurationProperties(prefix = "xxl.job")
public class JobProperties {

    /**
     * 调度中心通讯 token。
     */
    private String accessToken;

    /**
     * 调度中心配置。
     */
    private Admin admin = new Admin();

    /**
     * 执行器配置。
     */
    private Executor executor = new Executor();

    /**
     * 调度中心。
     */
    @Data
    public static class Admin {

        /**
         * 调度中心地址 (多个逗号分隔), 形如 http://ip:port/xxl-job-admin。
         */
        private String addresses;
    }

    /**
     * 执行器。
     */
    @Data
    public static class Executor {

        /**
         * 执行器应用名。
         */
        private String appname;

        /**
         * 执行器注册地址 (空则自动获取)。
         */
        private String address;

        /**
         * 执行器 IP (空则自动获取)。
         */
        private String ip;

        /**
         * 执行器端口 (0 或负数则自动分配)。
         */
        private int port;

        /**
         * 执行器日志路径。
         */
        private String logpath = "./xxl-job/executor";

        /**
         * 执行器日志保留天数 (小于 3 则关闭清理)。
         */
        private int logretentiondays = 7;
    }
}
