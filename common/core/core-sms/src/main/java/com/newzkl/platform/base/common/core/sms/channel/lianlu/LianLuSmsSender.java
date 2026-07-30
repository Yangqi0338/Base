package com.newzkl.platform.base.common.core.sms.channel.lianlu;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.common.core.sms.SmsSendReq;
import com.newzkl.platform.base.common.core.sms.SmsSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 连连短信通道 ({@code SmsSender} 实现)
 *
 * <p>迁移自 new-scm {@code CodeServiceImpl#sendCodeLianLu} / {@code #sign}:
 * 报文字段、签名参数过滤集、拼接顺序、MD5 大写结果、{@code status == "00"} 判定
 * 全部逐字保留 (三方签名契约, 改一处即验签失败)。</p>
 *
 * <p>迁移调整:</p>
 * <ul>
 *   <li>商户凭证外置到 {@code LianLuSmsProperties};</li>
 *   <li>{@code MessageDigest} 手写 MD5 换 hutool {@code DigestUtil.md5Hex} (同算法, 免受检异常);</li>
 *   <li>HTTP 走 hutool {@code HttpUtil} (旧 {@code HttpClientUtils} 未迁 Base)</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LianLuSmsSender implements SmsSender {

    /**
     * 三方受理成功状态码
     */
    private static final String STATUS_SUCCESS = "00";

    @Override
    public boolean send(SmsSendReq req) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("MchId", LianLuSmsProperties.MchId);
        requestData.put("AppId", LianLuSmsProperties.AppId);
        requestData.put("Version", LianLuSmsProperties.Version);
        requestData.put("SignType", LianLuSmsProperties.SignType);
        requestData.put("TimeStamp", System.currentTimeMillis());
        requestData.put("TemplateId", req.getTemplateId());
        requestData.put("PhoneNumberSet", Collections.singletonList(req.getPhone()));
        if (req.getParams() != null && !req.getParams().isEmpty()) {
            requestData.put("TemplateParamSet", req.getParams());
        }
        requestData.put("Type", LianLuSmsProperties.Type);
        requestData.put("Signature", sign(requestData, LianLuSmsProperties.AppKey));

        String result = HttpUtil.post(LianLuSmsProperties.Api, JSONUtil.toJsonStr(requestData));
        if (result == null || result.isEmpty()) {
            return false;
        }
        Map<?, ?> map = JSONUtil.toBean(result, Map.class);
        if (STATUS_SUCCESS.equals(map.get("status"))) {
            return true;
        }
        // 迁移调整: 旧实现把签名与整包报文打进 error 日志, 属凭证外泄, 此处只留三方错误描述
        log.error("短信发送失败, 手机号={}, 模板={}, 三方返回={}",
                req.getPhone(), req.getTemplateId(), map.get("message"));
        return false;
    }

    /**
     * 计算连连请求签名
     *
     * <p>参数按名排序后以 {@code key=value} 用 {@code &} 拼接, 结尾追加 {@code &key=appKey},
     * 整串 MD5 后转大写。</p>
     *
     * @param params 请求报文
     * @param appKey 应用密钥
     * @return 大写 MD5 签名
     */
    private String sign(Map<String, Object> params, String appKey) {
        String str = params.entrySet().stream()
                .filter(e -> !LianLuSmsProperties.SignExcludeFields.contains(e.getKey())
                        && e.getValue() != null
                        && !e.getValue().toString().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&")) + "&key=" + appKey;
        return DigestUtil.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toUpperCase();
    }
}
