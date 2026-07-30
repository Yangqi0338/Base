package com.newzkl.platform.base.biz.sys.model.config.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * sys 域系统配置属性
 *
 * <p>迁移说明: 源 {@code com.zkl.scm.web.config.ScmSysProperties} 是 common-web 的
 * 全局配置类, 混装网关地址/官方渠道商 id 等跨域项; Base 已把跨域部分下沉到
 * {@code common.core.utils.properties.SysProperties}。此处只承接 sys 域端点
 * {@code /admin/config/cdkPhone} 实际用到的 {@code cdkPhone} 一项,
 * 前缀 {@code scm.sys} 与旧配置逐字一致, 故既有配置文件无需改动。</p>
 *
 * @author KC
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "scm.sys")
public class AdminSysProperties {

    /**
     * CDK 权限手机号, 多个以逗号隔开
     */
    private String cdkPhone;
}
