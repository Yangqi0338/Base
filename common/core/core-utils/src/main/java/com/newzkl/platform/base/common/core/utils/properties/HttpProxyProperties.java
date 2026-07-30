package com.newzkl.platform.base.common.core.utils.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 出站 HTTP 代理配置属性
 *
 * <p>迁移说明: 源 {@code com.zkl.scm.web.config.HttpProxyProperties} 位于 common-web,
 * 是跨域的技术性配置 (内网出口需经代理才能访问外部三方 API), 故落 core-utils
 * 与 {@code SysProperties} 同级, 不归属任何 biz</p>
 *
 * <p>前缀 {@code scm.web.proxy} 与字段名逐字沿用旧配置, 既有 nacos
 * {@code scm-share-config.yml} 无需改动即可继续生效 (生产环境该项为 {@code enabled: true})</p>
 *
 * @author KC
 */
@Data
@Component
@ConfigurationProperties(prefix = "scm.web.proxy")
public class HttpProxyProperties {

    /**
     * 是否测试环境
     */
    private Boolean isTest;

    /**
     * 是否启用代理
     */
    private Boolean enabled;

    /**
     * 代理 ip
     */
    private String ip;

    /**
     * 代理端口
     */
    private Integer port;
}
