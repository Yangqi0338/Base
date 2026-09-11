package com.newzkl.platform.base.biz.auth.domain.adapt.api;

import com.newzkl.platform.base.biz.auth.model.oauth.dto.WxSessionDTO;

/**
 * 微信小程序登录凭证换会话出站端口
 *
 * <p>由前端 {@code wx.login} 的临时 code 换取稳定的 openId/unionId。实现侧调微信
 * {@code jscode2session}。</p>
 *
 * @author KC
 */
public interface WxApi {

    /**
     * code2Session 换 openId/unionId
     *
     * @param wxCode 前端 {@code wx.login} 返回的临时凭证
     * @return 会话信息, openId 非空
     */
    WxSessionDTO code2Session(String wxCode);
}
