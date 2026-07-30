package com.newzkl.platform.base.biz.sys.model.config.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 七牛云对象存储配置属性
 *
 * <p>迁移说明: 源 {@code com.zkl.scm.admin.interfaces.config.QiConfigProperties}。前缀
 * {@code qiniuyun.config} 与字段名逐字沿用旧配置, 既有 nacos {@code scm-admin.yml} /
 * {@code scm-share-config.yml} 无需改动即可继续生效</p>
 *
 * @author KC
 */
@Data
@Component
@ConfigurationProperties(prefix = "qiniuyun.config")
public class QiniuProperties {

    /**
     * 七牛 AccessKey
     */
    private String accessKey;

    /**
     * 七牛 SecretKey
     */
    private String secretKey;

    /**
     * 目标存储空间名
     */
    private String bucket;

    /**
     * 资源访问域名
     */
    private String domain;

    /**
     * 凭证有效期, 单位秒
     */
    private Long expires;
}
