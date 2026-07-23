package com.newzkl.platform.base.common.core.utils.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统全局配置属性。
 *
 * <p>静态字段供无 Spring 上下文处静态读取; 值由 setter 注入。</p>
 *
 * @author fang
 */
@Data
@Component
@ConfigurationProperties(prefix = "scm.sys")
public class ScmSysProperties {

    /**
     * 网关地址。
     */
    public static String gatewayUrl;

    /**
     * 官方渠道门店 ID (默认门店)。
     */
    public static Long officialChannelId;

    /**
     * 设置网关地址。
     *
     * @param gatewayUrl 网关地址
     */
    public void setGatewayUrl(String gatewayUrl) {
        ScmSysProperties.gatewayUrl = gatewayUrl;
    }

    /**
     * 设置官方渠道门店 ID。
     *
     * @param officialChannelId 官方渠道门店 ID
     */
    public void setOfficialChannelId(Long officialChannelId) {
        ScmSysProperties.officialChannelId = officialChannelId;
    }
}
