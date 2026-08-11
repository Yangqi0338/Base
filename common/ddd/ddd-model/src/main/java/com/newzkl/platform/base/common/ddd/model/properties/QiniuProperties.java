package com.newzkl.platform.base.common.ddd.model.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛云对象存储配置属性
 *
 * <p>迁移说明: 源 {@code com.zkl.scm.admin.interfaces.config.QiConfigProperties}。前缀
 * {@code qiniuyun.config} 与字段名逐字沿用旧配置, 既有 nacos {@code scm-admin.yml} /
 * {@code scm-share-config.yml} 无需改动即可继续生效</p>
 *
 * @author KC
 */
@Configuration
@ConfigurationProperties(prefix = "qiniuyun.config")
public class QiniuProperties {

    /**
     * 七牛云AccessKey
     */
    public static String accessKey = "OtWjVh3OdkGtbk-CEW6FDZmso7U_i80JFxLVS7Js";

    /**
     * 七牛云SecretKey
     */
    public static String secretKey = "AL1Cu1KHHFRytsAWxCqVX31DQJni1lrv_vcLSPh2";

    /**
     * 存储空间名称
     */
    public static String bucket = "zhongzetp";

    /**
     * 访问域名
     */
    public static String domain = "https://oss.jiufucloud.com";

    /**
     * 签名过期时间
     * @ext 秒
     */
    public static Long expires = 3600L;

    public void setAccessKey(String accessKey) {
        QiniuProperties.accessKey = accessKey;
    }
    public void setSecretKey(String secretKey) {
        QiniuProperties.secretKey = secretKey;
    }
    public void setBucket(String bucket) {
        QiniuProperties.bucket = bucket;
    }
    public void setDomain(String domain) {
        QiniuProperties.domain = domain;
    }
    public void setExpires(Long expires) {
        QiniuProperties.expires = expires;
    }
}
