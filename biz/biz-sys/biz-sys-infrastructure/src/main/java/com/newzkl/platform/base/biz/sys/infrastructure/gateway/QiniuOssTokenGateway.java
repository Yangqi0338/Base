package com.newzkl.platform.base.biz.sys.infrastructure.gateway;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.sys.domain.adapt.api.OssTokenApi;
import com.newzkl.platform.base.common.ddd.model.properties.QiniuProperties;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 七牛云上传凭证网关
 *
 * <p>迁移说明: 源 {@code CommonController#pictureUpToken} 直接调 {@code com.qiniu.util.Auth}
 * (qiniu-java-sdk)。该 SDK 在 Base 根 pom 仅有 {@code dependencyManagement} 版本声明,
 * 未被任何模块实际引入; 本次迁移不得改动 pom, 故此处按七牛官方上传凭证算法自行签名,
 * 不引 SDK。算法与 qiniu-java-sdk 7.5.0 {@code Auth#uploadToken(bucket)} 逐步等价:</p>
 * <ol>
 *   <li>{@code deadline = 当前秒 + expires}, 上传策略 {@code {"scope":bucket,"deadline":deadline}}</li>
 *   <li>{@code encodedPolicy = urlsafe-base64(policy)} (URL 安全字母表, 保留 {@code =} 补位, 不换行)</li>
 *   <li>{@code sign = urlsafe-base64(HmacSHA1(secretKey, encodedPolicy))}</li>
 *   <li>{@code token = accessKey + ":" + sign + ":" + encodedPolicy}</li>
 * </ol>
 *
 * <p>与源的唯一差异: 源固定 1 小时有效期 ({@code Auth#uploadToken(bucket)} 内置 3600),
 * 此处优先取配置 {@code qiniuyun.config.expires}, 未配置时回落 3600 秒。既有 nacos 配置
 * 该项为 300, 故上线后凭证有效期由 1 小时收敛为 5 分钟 —— 上传凭证仅用于换取直传许可,
 * 前端每次上传前均重新拉取, 不影响功能</p>
 *
 * @author KC
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QiniuOssTokenGateway implements OssTokenApi {

    /**
     * 签名算法, 七牛上传凭证固定 HmacSHA1
     */
    private static final String HMAC_SHA1 = "HmacSHA1";

    @Override
    public String uploadToken() {
        String accessKey = QiniuProperties.accessKey;
        String secretKey = QiniuProperties.secretKey;
        String bucket = QiniuProperties.bucket;
        if (StrUtil.hasBlank(accessKey, secretKey, bucket)) {
            log.error("[QiniuOssTokenGateway] 七牛配置缺失, 请检查 qiniuyun.config 下 access-key/secret-key/bucket");
            throw new PlatformException(BaseErrorCode.SERVER);
        }

        Long expires = QiniuProperties.expires;
        long deadline = System.currentTimeMillis() / 1000 + expires;

        Map<String, Object> policy = new LinkedHashMap<>(2);
        policy.put("scope", bucket);
        policy.put("deadline", deadline);
        String encodedPolicy = urlSafeBase64(JSONUtil.toJsonStr(policy).getBytes(StandardCharsets.UTF_8));

        String sign = urlSafeBase64(hmacSha1(secretKey, encodedPolicy));
        return accessKey + ":" + sign + ":" + encodedPolicy;
    }

    /**
     * 计算 HmacSHA1 摘要
     *
     * @param secretKey 七牛 SecretKey
     * @param data      待签名字符串
     * @return 摘要字节
     */
    private byte[] hmacSha1(String secretKey, String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA1);
            mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA1));
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("[QiniuOssTokenGateway] 上传凭证签名失败", e);
            throw new PlatformException(BaseErrorCode.SERVER);
        }
    }

    /**
     * URL 安全的 base64 编码
     *
     * <p>保留 {@code =} 补位且不换行, 与七牛 {@code UrlSafeBase64} 的
     * {@code URL_SAFE | NO_WRAP} 组合等价</p>
     *
     * @param data 待编码字节
     * @return 编码结果
     */
    private String urlSafeBase64(byte[] data) {
        return Base64.getUrlEncoder().encodeToString(data);
    }
}
