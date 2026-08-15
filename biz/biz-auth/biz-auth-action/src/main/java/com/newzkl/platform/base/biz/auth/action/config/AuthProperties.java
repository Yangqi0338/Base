package com.newzkl.platform.base.biz.auth.action.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限过滤
 */
@Data
@RefreshScope
@ConfigurationProperties("platform.secure")
public class AuthProperties {
    /**
     * 放行API集合
     */
    public static List<String> skipUrl = new ArrayList<>();

    public static List<String> skipRoleUrl = new ArrayList<>();

    public void setSkipUrl(List<String> skipUrl) {
        AuthProperties.skipUrl = skipUrl;
    }

    public void setSkipRoleUrl(List<String> skipRoleUrl) {
        AuthProperties.skipRoleUrl = skipRoleUrl;
    }
}
