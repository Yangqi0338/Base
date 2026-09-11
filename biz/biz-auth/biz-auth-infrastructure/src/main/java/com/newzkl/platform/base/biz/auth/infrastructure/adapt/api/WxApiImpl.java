package com.newzkl.platform.base.biz.auth.infrastructure.adapt.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.biz.auth.domain.adapt.api.WxApi;
import com.newzkl.platform.base.biz.auth.model.oauth.dto.WxSessionDTO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.properties.FinanceProperties.HuiFuProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 微信小程序 code2Session 实现
 *
 * <p>微信 {@code GET /sns/jscode2session} 无签名, 直接用 hutool HTTP 调用。
 * 参考 adopt-chicken {@code WxMethod.code2Session} 的换码语义。</p>
 *
 * @author KC
 */
@Slf4j
@Component
public class WxApiImpl implements WxApi {

    private static final String CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Override
    public WxSessionDTO code2Session(String wxCode) {
        ThrowsException.isBlank(wxCode, "微信登录凭证");
        try (HttpResponse response = HttpRequest.get(CODE2SESSION_URL)
                .form("appid", HuiFuProperties.wxAppId)
                .form("secret", HuiFuProperties.wxAppSecret)
                .form("js_code", wxCode)
                .form("grant_type", "authorization_code")
                .timeout(10_000)
                .execute()) {
            String body = response.body();
            JSONObject json = JSONUtil.parseObj(body);
            Integer errcode = json.getInt("errcode");
            if (errcode != null && errcode != 0) {
                log.error("code2Session 失败 errcode={}, errmsg={}", errcode, json.getStr("errmsg"));
                ThrowsException.exception(BaseErrorCode.REMOTE, "微信授权失败: " + json.getStr("errmsg"));
            }
            String openId = json.getStr("openid");
            if (StrUtil.isBlank(openId)) {
                ThrowsException.exception(BaseErrorCode.REMOTE, "微信授权失败: openId 为空");
            }
            WxSessionDTO dto = new WxSessionDTO();
            dto.setOpenId(openId);
            dto.setUnionId(json.getStr("unionid"));
            return dto;
        }
    }
}
