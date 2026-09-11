package com.newzkl.platform.base.biz.auth.model.oauth.dto;

import lombok.Data;

/**
 * 微信小程序 code2Session 会话信息
 *
 * <p>{@code openId} 为同一用户在同一小程序下的稳定唯一标识, {@code unionId}
 * 为微信开放平台账号下的稳定唯一标识(小程序未绑定开放平台时不返回, 可为空)。</p>
 *
 * @author KC
 */
@Data
public class WxSessionDTO {

    /**
     * 小程序用户 openId
     */
    private String openId;

    /**
     * 微信开放平台 unionId, 未绑定开放平台时为空
     */
    private String unionId;
}
