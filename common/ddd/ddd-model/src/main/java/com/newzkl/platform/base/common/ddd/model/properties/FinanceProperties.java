package com.newzkl.platform.base.common.ddd.model.properties;

import cn.hutool.core.map.MapUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 财务域配置根
 *
 * <p>密钥类配置一律不落代码默认值, 由 Nacos {@code platform-share-config.yml} 注入:</p>
 * <pre>
 * scm:
 *   fi:
 *     huifu:
 *       public-key: ${HUIFU_PUBLIC_KEY}
 *       private-key: ${HUIFU_PRIVATE_KEY}
 * </pre>
 *
 * @author KC
 */
@Configuration
@ConfigurationProperties(prefix = "platform.fi")
public class FinanceProperties {

    /**
     * 汇付支付配置
     *
     * <p>静态字段 + 实例 setter 模式: Spring 绑定走实例 setter, 业务侧静态引用免注入。</p>
     *
     * @author KC
     */
    @Configuration
    @ConfigurationProperties(prefix = "platform.fi.huifu")
    @Getter
    public static class HuiFuProperties {
        /* ============= 通知Notify ============= */
        // 绑卡通知
        private static final Map<HuiFuNotifyEnum, String> notifyUri = new HashMap<>();
        /**
         * 汇付平台RSA公钥
         */
        public static String publicKey;
        /**
         * 商户RSA私钥
         */
        public static String privateKey;
        public static String productId;
        public static String sysId;
        public static String baseUrl;
        /* ============= 业务参数 ============= */
        // 结算周期
        public static String settleCycle = "T1";
        // 测试环境价格装饰模式
        public static Integer devDecorateMode = 0;
        private static String notifyPreUrl;

        public void setPublicKey(String publicKey) {
            HuiFuProperties.publicKey = publicKey;
        }

        public void setPrivateKey(String privateKey) {
            HuiFuProperties.privateKey = privateKey;
        }

        public void setBaseUrl(String baseUrl) {
            HuiFuProperties.baseUrl = baseUrl;
        }

        public void setNotifyPreUrl(String notifyPreUrl) {
            HuiFuProperties.notifyPreUrl = notifyPreUrl;
        }

        public void setNotifyUri(Map<String, String> notifyUri) {
            notifyUri.forEach((key, value) -> HuiFuProperties.notifyUri.put(HuiFuNotifyEnum.valueOf(key), value));
        }

        public static String getUrl(HuiFuNotifyEnum notifyEnum){
            String uri = notifyUri.get(notifyEnum);
            if (uri == null){
                return null;
            }
            return notifyPreUrl + uri;
        }

        public void setProductId(String productId) {
            HuiFuProperties.productId = productId;
        }

        public void setSysId(String sysId) {
            HuiFuProperties.sysId = sysId;
        }

        public void setSettleCycle(String settleCycle) {
            HuiFuProperties.settleCycle = settleCycle;
        }

        public void setDevDecorateMode(Integer devDecorateMode) {
            HuiFuProperties.devDecorateMode = devDecorateMode;
        }
    }

    @AllArgsConstructor
    @Getter
    public static enum HuiFuNotifyEnum { bindCard,pay,refund,withdraw; }

}
