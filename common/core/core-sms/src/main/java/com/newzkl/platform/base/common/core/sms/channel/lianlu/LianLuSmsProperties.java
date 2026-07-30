package com.newzkl.platform.base.common.core.sms.channel.lianlu;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 连连短信通道配置
 *
 * <p>迁移调整: new-scm 把商户号/AppId/AppKey 硬编码在 {@code CodeServiceImpl} 常量里,
 * 属凭证入库。此处外置为 {@code sms.lianlu.*} 配置, 由入口 starter 注入一次,
 * 各 biz 不再各自配置; 请求报文字段与签名算法保持与旧实现逐字一致。</p>
 *
 * @author KC
 */
@Configuration
@ConfigurationProperties(prefix = "sms.lianlu")
public class LianLuSmsProperties {

    /**
     * 模板短信下发地址
     */
    public static String Api = "https://apis.shlianlu.com/sms/trade/template/send";

    /**
     * 商户号
     */
    public static String MchId = "1067933";

    /**
     * 应用 id
     */
    public static String AppId = "10011756086551601";

    /**
     * 应用密钥 (参与签名, 不入报文)
     */
    public static String AppKey = "906b07cbf47442fc9197286c20daf041";

    /**
     * 接口版本
     */
    public static String Version = "1.1.0";

    /**
     * 签名方式
     */
    public static String SignType = "MD5";

    /**
     * 短信类型
     */
    public static Integer Type = 3;

    /**
     * 签名时需排除的字段名列表
     */
    public static List<String> SignExcludeFields = CollUtil.newArrayList(
            "PhoneNumberSet", "TemplateParamSet"
    );

    public void setMchId(String mchId) {
        LianLuSmsProperties.MchId = mchId;
    }

    public void setAppId(String appId) {
        LianLuSmsProperties.AppId = appId;
    }

    public void setAppKey(String appKey) {
        LianLuSmsProperties.AppKey = appKey;
    }

    public void setVersion(String version) {
        LianLuSmsProperties.Version = version;
    }

    public void setSignType(String signType) {
        LianLuSmsProperties.SignType = signType;
    }

    public void setType(Integer type) {
        LianLuSmsProperties.Type = type;
    }

    public static void setSignExcludeFields(List<String> signExcludeFields) {
        LianLuSmsProperties.SignExcludeFields = signExcludeFields;
    }
}
