package com.newzkl.platform.base.common.ddd.model.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
@ConfigurationProperties(prefix = "scm.fi")
public class FinanceProperties {

    /**
     * 汇付支付配置
     *
     * <p>静态字段 + 实例 setter 模式: Spring 绑定走实例 setter, 业务侧静态引用免注入。</p>
     *
     * @author KC
     */
    @Configuration
    @ConfigurationProperties(prefix = "scm.fi.huifu")
    @Getter
    public static class HuiFuProperties {
        /* ============= 通知Notify ============= */
        // 绑卡通知
        private static final String bindCardNotifyUri = "/notify/huifuBindCard";
        // 支付通知
        private static final String payNotifyUri = "/notify/consumeNotify";
        // 退款通知
        private static final String refundNotifyUri = "/notify/refundNotify";
        // 转出通知
        private static final String withdrawNotifyUri = "/notify/rollOutNotify";
        /**
         * 汇付公钥。安全基线: 禁止在代码内保留默认值, 必须由 Nacos {@code scm.fi.huifu.public-key} 注入。
         */
        public static String publicKey;
        /**
         * 汇付商户私钥。安全基线: 禁止在代码内保留默认值, 必须由 Nacos {@code scm.fi.huifu.private-key} 注入。
         */
        public static String privateKey;
        public static String productId = "EDUARK";
        public static String sysId = "6666000168676220";
        public static String testPreUrl = "https://api.huifu.com";
        public static String preUrl = "https://api.huifu.com";
        public static String bindCardNotifyUrl;
        public static String payNotifyUrl;
        public static String refundNotifyUrl;
        public static String withdrawNotifyUrl;
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

        public void setProductId(String productId) {
            HuiFuProperties.productId = productId;
        }

        public void setSysId(String sysId) {
            HuiFuProperties.sysId = sysId;
        }

        public void setTestPreUrl(String testPreUrl) {
            HuiFuProperties.testPreUrl = testPreUrl;
        }

        public void setPreUrl(String preUrl) {
            HuiFuProperties.preUrl = preUrl;
        }

        public void setSettleCycle(String settleCycle) {
            HuiFuProperties.settleCycle = settleCycle;
        }

        public void setDevDecorateMode(Integer devDecorateMode) {
            HuiFuProperties.devDecorateMode = devDecorateMode;
        }

        public void setNotifyPreUrl(String notifyPreUrl) {
            HuiFuProperties.notifyPreUrl = notifyPreUrl;
            HuiFuProperties.bindCardNotifyUrl = notifyPreUrl + bindCardNotifyUri;
            HuiFuProperties.payNotifyUrl = notifyPreUrl + payNotifyUri;
            HuiFuProperties.refundNotifyUrl = notifyPreUrl + refundNotifyUri;
            HuiFuProperties.withdrawNotifyUrl = notifyPreUrl + withdrawNotifyUri;
        }

        public void setBindCardNotifyUrl(String bindCardNotifyUrl) {
            HuiFuProperties.bindCardNotifyUrl = bindCardNotifyUrl;
        }

        public void setPayNotifyUrl(String huiFuPayNotifyUrl) {
            HuiFuProperties.payNotifyUrl = huiFuPayNotifyUrl;
        }

        public void setPayRefundNotifyUrl(String huiFuPayRefundNotifyUrl) {
            HuiFuProperties.refundNotifyUrl = huiFuPayRefundNotifyUrl;
        }

        public void setWithdrawNotifyUrl(String huiFuWithdrawNotifyUrl) {
            HuiFuProperties.withdrawNotifyUrl = huiFuWithdrawNotifyUrl;
        }
    }

}
