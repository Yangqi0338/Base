package com.newzkl.platform.base.biz.goods.model.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统配置
 * @author fang
 */
@Data
@Component
@ConfigurationProperties(prefix = "scm.sys")
public class ScmSysProperties {

    /**
     * 网关地址
     */
    public static String gatewayUrl;
    /**
     * 当前应用的数据库名称
     */
    public static String db;
    /**
     * 同类目能有的数量
     */
    public static Integer sameCategoryCount = 90;

    public void setGatewayUrl(String gatewayUrl) {
        ScmSysProperties.gatewayUrl = gatewayUrl;
    }

    public void setDb(String db) {
        ScmSysProperties.db = db;
    }

    public void setSameCategoryCount(Integer sameCategoryCount) {
        ScmSysProperties.sameCategoryCount = sameCategoryCount;
    }
}

