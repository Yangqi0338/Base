package com.newzkl.platform.base.common.core.logistics;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 物流配置类
 *
 * <p>凭证只在入口 starter (building-scm / building-mmt) 配一次, 各 biz 域只依赖本模块不重复配置</p>
 */
@Configuration
@ConfigurationProperties(prefix = "platform.logistics")
public class LogisticsConfig {

    /**
     * 轨迹缓存秒数
     */
    public static Integer trackCacheSeconds = 3600;

    /**
     * 单号识别结果缓存秒数
     *
     * <p>单号与快递公司的对应关系不会变, 故可长缓存</p>
     */
    public static Integer companyCacheSeconds = 86400;

    public void setTrackCacheSeconds(Integer trackCacheSeconds) {
        LogisticsConfig.trackCacheSeconds = trackCacheSeconds;
    }

    public void setCompanyCacheSeconds(Integer companyCacheSeconds) {
        LogisticsConfig.companyCacheSeconds = companyCacheSeconds;
    }

    /**
     * 快递100 平台配置
     */
    @Configuration
    @ConfigurationProperties(prefix = "platform.logistics.kuaidi100")
    public static class Kuaidi100Config {

        /**
         * 授权 key
         */
        public static String key;

        /**
         * 公司编号
         */
        public static String customer;

        /**
         * 实时轨迹查询地址
         */
        public static String queryUrl = "https://poll.kuaidi100.com/poll/query.do";

        /**
         * 单号识别地址, 占位 1 = 单号, 占位 2 = 授权 key
         */
        public static String autoNumUrl = "https://www.kuaidi100.com/autonumber/auto?num={}&key={}";

        /**
         * 请求超时毫秒数
         */
        public static Integer timeout = 10000;

        public void setKey(String key) {
            Kuaidi100Config.key = key;
        }

        public void setCustomer(String customer) {
            Kuaidi100Config.customer = customer;
        }

        public void setQueryUrl(String queryUrl) {
            Kuaidi100Config.queryUrl = queryUrl;
        }

        public void setAutoNumUrl(String autoNumUrl) {
            Kuaidi100Config.autoNumUrl = autoNumUrl;
        }

        public void setTimeout(Integer timeout) {
            Kuaidi100Config.timeout = timeout;
        }
    }
}
