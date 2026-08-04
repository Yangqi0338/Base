package com.newzkl.platform.base.biz.sys.model.config.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 华为云 OCR 配置属性
 *
 * <p>迁移说明: 源 {@code HuaweiApiConfig} 把 AK/SK 硬编码在 bean 装配代码内
 * ({@code withAk("JIS6MBVGEUFLELWX6G0L")} / {@code withSk("hn0N...")}), 旧仓无配置类。
 * 此处外提为配置项, {@code region} 默认值 {@code cn-north-4} 与旧硬编码一致。</p>
 *
 * <p>安全提示: AK/SK 属云账号凭证, <b>不内置默认值</b>, 必须经 nacos / 密钥管理下发。
 * 旧代码硬编码的凭证已泄漏在 git 历史, 上线前应轮换。</p>
 *
 * @author KC
 */
@Data
@Component
@ConfigurationProperties(prefix = "huawei.ocr")
public class HuaweiOcrProperties {

    /**
     * 华为云 access key (不设默认值, 经配置下发)
     */
    private String ak;

    /**
     * 华为云 secret key (不设默认值, 经配置下发)
     */
    private String sk;

    /**
     * OCR 服务区域, 与旧硬编码一致
     */
    private String region = "cn-north-4";
}
