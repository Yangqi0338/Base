package com.newzkl.platform.base.common.core.job.config;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * XXL-Job 属性
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    public static String addresses;
    public static String accessToken;
    public static String username;
    public static String password;
    public static String groupIds;

    public void setAddresses(String addresses) {
        XxlJobProperties.addresses = addresses;
    }

    public void setAccessToken(String accessToken) {
        XxlJobProperties.accessToken = accessToken;
    }

    public void setUsername(String username) {
        XxlJobProperties.username = username;
    }

    public void setPassword(String password) {
        XxlJobProperties.password = password;
    }

    public void setGroupIds(String groupIds) {
        XxlJobProperties.groupIds = groupIds;
    }

    /**
     * 汇付天下支付网关配置
     */
    @Configuration
    @ConfigurationProperties(prefix = "xxl.job.executor")
    public static class Executor {
        public static String appName;
        public static String address;
        public static String ip;
        public static int port;
        public static String logPath;
        public static int logRetentionDays;

        public void setAppName(String appName) {
            Executor.appName = appName;
        }

        public void setAddress(String address) {
            Executor.address = address;
        }

        public void setIp(String ip) {
            Executor.ip = ip;
        }

        public void setPort(int port) {
            Executor.port = port;
        }

        public void setLogPath(String logPath) {
            Executor.logPath = logPath;
        }

        public void setLogRetentionDays(int logRetentionDays) {
            Executor.logRetentionDays = logRetentionDays;
        }
    }

    public static List<Integer> parseGroupIds() {
        return StrUtil.split(groupIds,',', -1 , true, NumberUtil::parseInt);
    }
}
