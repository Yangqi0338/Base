package com.newzkl.platform.base.biz.sys.infrastructure.gateway;

import cn.hutool.core.util.BooleanUtil;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.ocr.v1.OcrClient;
import com.huaweicloud.sdk.ocr.v1.region.OcrRegion;
import com.newzkl.platform.base.biz.sys.model.config.support.HuaweiOcrProperties;
import com.newzkl.platform.base.common.core.utils.properties.HttpProxyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 华为云 OCR 客户端装配
 *
 * <p>迁移说明: 对应源 {@code HuaweiApiConfig}。AK/SK 由硬编码改为经
 * {@code HuaweiOcrProperties} 注入 (前缀 {@code huawei.ocr}); 代理开关复用既有
 * {@code HttpProxyProperties} (前缀 {@code scm.web.proxy}), 与源逐字一致。</p>
 *
 * @author KC
 */
@Configuration
@RequiredArgsConstructor
public class HuaweiOcrConfig {

    private final HuaweiOcrProperties huaweiOcrProperties;
    private final HttpProxyProperties httpProxyProperties;

    /**
     * 装配华为云 OCR 客户端
     *
     * @return {@code OcrClient} 实例
     */
    @Bean
    public OcrClient ocrClient() {
        HttpConfig config = HttpConfig.getDefaultHttpConfig();
        if (BooleanUtil.isTrue(httpProxyProperties.getEnabled())) {
            config.withProxyHost(httpProxyProperties.getIp()).withProxyPort(httpProxyProperties.getPort());
        }
        ICredential auth = new BasicCredentials()
                .withAk(huaweiOcrProperties.getAk())
                .withSk(huaweiOcrProperties.getSk());
        return OcrClient.newBuilder()
                .withHttpConfig(config)
                .withCredential(auth)
                .withRegion(OcrRegion.valueOf(huaweiOcrProperties.getRegion()))
                .build();
    }
}
