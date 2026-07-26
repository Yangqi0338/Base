package com.newzkl.platform.base.biz.finance.model.support;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 财务域配置根。
 *
 * <p>密钥类配置一律不落代码默认值, 由 Nacos {@code platform-share-config.yml} 注入:</p>
 * <pre>
 * scm:
 *   fi:
 *     huifu:
 *       public-key: ${HUIFU_PUBLIC_KEY}
 *       private-key: ${HUIFU_PRIVATE_KEY}
 *     lianlian:
 *       public-key: ${LIANLIAN_PUBLIC_KEY}
 *       private-key: ${LIANLIAN_PRIVATE_KEY}
 *       oid-partner: ${LIANLIAN_OID_PARTNER}
 * </pre>
 *
 * @author KC
 */
@Configuration
@ConfigurationProperties(prefix = "scm.fi")
public class FinanceProperties {

    /**
     * 汇付支付配置。
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

    /**
     * 连连支付 (个人钱包) 配置。
     *
     * <p>迁移自 new-scm {@code SignUtils} / {@code TripartitePayMethod} 内的硬编码常量。
     * 密钥与商户号一律不落代码默认值, 由 Nacos {@code platform-share-config.yml} 的
     * {@code scm.fi.lianlian.*} 注入。</p>
     *
     * @author KC
     */
    @Configuration
    @ConfigurationProperties(prefix = "scm.fi.lianlian")
    @Getter
    public static class LianLianProperties {

        /**
         * 连连平台公钥 (验签用)。必须由 Nacos {@code scm.fi.lianlian.public-key} 注入。
         */
        public static String publicKey;

        /**
         * 商户私钥 (签名用)。必须由 Nacos {@code scm.fi.lianlian.private-key} 注入。
         */
        public static String privateKey;

        /**
         * 商户号 (旧码 {@code MERCHANT_NO})。必须由 Nacos {@code scm.fi.lianlian.oid-partner} 注入。
         */
        public static String oidPartner;

        /**
         * ACCP 主接口前缀, 旧值 {@code https://accpapi.lianlianpay.com/v1/}。
         */
        public static String apiUrl = "https://accpapi.lianlianpay.com/v1";

        /**
         * ACCP 网关前缀, 旧值 {@code https://accpgw.lianlianpay.com/v1/}。
         */
        public static String gwUrl = "https://accpgw.lianlianpay.com/v1";

        /**
         * ACCP 文件上传前缀, 旧值 {@code https://accpfile.lianlianpay.com/v1/}。
         */
        public static String fileUrl = "https://accpfile.lianlianpay.com/v1";

        /**
         * 个人钱包异步通知回调地址 (旧码 {@code PersonPayController.notify} 硬编码)。
         */
        public static String notifyUrl;

        public void setPublicKey(String publicKey) {
            LianLianProperties.publicKey = publicKey;
        }

        public void setPrivateKey(String privateKey) {
            LianLianProperties.privateKey = privateKey;
        }

        public void setOidPartner(String oidPartner) {
            LianLianProperties.oidPartner = oidPartner;
        }

        public void setApiUrl(String apiUrl) {
            LianLianProperties.apiUrl = apiUrl;
        }

        public void setGwUrl(String gwUrl) {
            LianLianProperties.gwUrl = gwUrl;
        }

        public void setFileUrl(String fileUrl) {
            LianLianProperties.fileUrl = fileUrl;
        }

        public void setNotifyUrl(String notifyUrl) {
            LianLianProperties.notifyUrl = notifyUrl;
        }
    }

}
