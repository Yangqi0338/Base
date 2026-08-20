package com.newzkl.platform.base.common.core.sms;

import cn.hutool.core.collection.CollUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * 短信配置类
 */
@Configuration
@ConfigurationProperties(prefix = "platform.sms")
public class SmsConfig {

    /**
     * 发送长度
     */
    public static Integer length = 6;

    /**
     * 每分钟最多发送验证码次数
     */
    public static Integer codeMinuteLimit = 1;

    /**
     * 每小时最多发送验证码次数
     */
    public static Integer codeHourLimit = 5;

    public void setCodeMinuteLimit(Integer codeMinuteLimit) {
        SmsConfig.codeMinuteLimit = codeMinuteLimit;
    }
    public void setCodeHourLimit(Integer codeHourLimit) {
        SmsConfig.codeHourLimit = codeHourLimit;
    }
    public void setLength(Integer length) {
        SmsConfig.length = length;
    }

    /**
     * 联麓短信平台配置
     */
    @Configuration
    @ConfigurationProperties(prefix = "platform.sms.lianlu")
    public static class LianLuSmsConfig {

        /**
         * 商户ID
         */
        public static String MchId = "1067933";
        /**
         * 应用ID
         */
        public static String AppId = "10011756086551601";
        /**
         * 应用密钥
         */
        public static String AppKey = "906b07cbf47442fc9197286c20daf041";
        /**
         * API版本号
         */
        public static String Version = "1.1.0";
        /**
         * 签名算法类型
         */
        public static String SignType = "MD5";
        /**
         * 短信类型
         */
        public static Integer Type = 3;

        /**
         * 签名时需排除的字段名列表
         */
        public static List<String> signExcludeFields = CollUtil.newArrayList(
                "PhoneNumberSet", "TemplateParamSet"
        );

        public void setSignExcludeFields(List<String> signExcludeFields) {
            LianLuSmsConfig.signExcludeFields = signExcludeFields;
        }
        public void setMchId(String mchId) {
            LianLuSmsConfig.MchId = mchId;
        }
        public void setAppId(String appId) {
            LianLuSmsConfig.AppId = appId;
        }
        public void setAppKey(String appKey) {
            LianLuSmsConfig.AppKey = appKey;
        }
        public void setVersion(String version) {
            LianLuSmsConfig.Version = version;
        }
        public void setSignType(String signType) {
            LianLuSmsConfig.SignType = signType;
        }
        public void setType(Integer type) {
            LianLuSmsConfig.Type = type;
        }
    }


}