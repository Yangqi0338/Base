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
    public static class HuiFuProperties extends PalletProperties.Properties {
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
         * 汇付平台RSA公钥
         */
        public static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2iLK7Ln7hYbLWjU1yjvIL3PmnrtYGFwD78oUa8uNcTZmbYbwz6aZHkPo3+xtZvHsgkD+d1aZyf8cJr/4AWHj+4o5xWxajlMGZsV1fUKYk54b7pncorI5V3Pvc9kTE51a5uU6cuyItotgkUaeFCyXKheKcRBXLnMhUPPuSzwGxz2D9YtKCYAyE2g+VAUrw0ajIgqqybzlEv96QsWRsWFMO4lcqwUaK+60uDpzaDYG4kiJZLGPmI1OOLlPPxMeSWqAFn9WEMz6aZ6dLy/+WYc9CD0lBQ2K2uVRRMyVvlj6OkXakB4jsB+b8aQVDddBMVvvvLDcHApm6Yj1YKLy18zr5QIDAQAB";
        /**
         * 商户RSA私钥
         */
        public static String privateKey = "MIIEuwIBADANBgkqhkiG9w0BAQEFAASCBKUwggShAgEAAoIBAQDaIsrsufuFhstaNTXKO8gvc+aeu1gYXAPvyhRry41xNmZthvDPppkeQ+jf7G1m8eyCQP53VpnJ/xwmv/gBYeP7ijnFbFqOUwZmxXV9QpiTnhvumdyisjlXc+9z2RMTnVrm5Tpy7Ii2i2CRRp4ULJcqF4pxEFcucyFQ8+5LPAbHPYP1i0oJgDITaD5UBSvDRqMiCqrJvOUS/3pCxZGxYUw7iVyrBRor7rS4OnNoNgbiSIlksY+YjU44uU8/Ex5JaoAWf1YQzPppnp0vL/5Zhz0IPSUFDYra5VFEzJW+WPo6RdqQHiOwH5vxpBUN10ExW++8sNwcCmbpiPVgovLXzOvlAgMBAAECggEBAIKeMNq9pxHWnC/hCtuVHpLjmOXVkrPLbYQJgl7l3UU8aOsO0WXWAXvw+CIVxZDOLmv0lStKjP46p1XAv7W8MzLtGxjPgA4XQw8JEAg4d3p1Q46FTWuSwulN/8Vj55toLwVxn6UvY9HC9ckn+wJjjQTkYb7AJSQoRnveBQPB5uEoJi7Om757k3IAESZl46JvH8db4jMlPscsEeuCIL1By1PkkwQBTvgDu9hz0vhUxU4UiwUkaFbCCqwYjoBdeDb3rRj54c+PfuoP1fw7NQMJFTxQLJT1jW29XXONUz15GMVVUjIl2v6Fyry2w5EzlAZKoWFIvXE55GC+QooxIgejXIUCgYEA+2cyHF2AVHGZgpZC5dEzWbJ0wzjfNoJXwGtqeDJ4SaASo4cPM4MijWfKZiRZDPPd1BbLDx/sfFWDYdkyldFsuLi2RPLt4XZOUGJNOVNadPNppulLbxeP6dKp4gY+JOs0DPCWDqRUf70mqZInJcCaVyzM3nao01NhOIYlr1USxw8CgYEA3h/f/r3VH+FqGFNcbcaVxIRuIkiH7M8k9MU2vHmrGvRxwL/jfEd9MCOkriiadX/lvW2VAEwYuzK0OV020KeI9M+er3N9Zio60WVKxYFZp5eKXYeICLtxCu/vctaS3P2d+CIrg3CnaYEz1wdyDE6QAbvZ8/cmh10s2aolZ4PdvcsCgYBhL7CQhJjSjCPS9rGf1DKsry8yNO8dTGAN87hyBNi5ZZcu/kwjFsOptIDq4YxHVJLhpXoUO7wZCJnEnslOX2pPMqDLoLnTGgAGVSoTSiTC50JlkvRlWs94jca8qLsnXIF/qxXnTSGZTA8BKI3Xq1A++QOt0GNNZoND7Z/t2s5qgwKBgHju/8Qw1HU8A8hksmDuCqJou5GczaxHh0ZgjRGGaHsPdVNM5ezG+0iXT1SmtJmeXZWJsOLti1V4IJlOv8ZQQIeQ9kNt7GsQON/Cdzga2ZYeMm4DmTOv4bbjtQlf+6unxTbQW8J/NhaCCpha7GP47fyTqvFhsS9nskB7m0vFhpeLAn8cRqCDJuYZkRHSUFXd2BytPnmCEIpSPpccabdhxIW5BGOeUfRFL0uN6uuTZKAJ8Nfjam00YQlPSOp/js3aZcREowN/rBJ+3BQ4wj+8NYQxn0hff1wuQUo1LHWFrI/Oudhr3AUunDUWKWrs6jaaGOonkZsqnjU+mRVViAygqHbA";
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
