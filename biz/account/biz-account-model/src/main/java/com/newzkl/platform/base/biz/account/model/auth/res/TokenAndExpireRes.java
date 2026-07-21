package com.newzkl.platform.base.biz.account.model.auth.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token及有效期返回对象
 *
 * @author yourname
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenAndExpireRes {
    /**
     * 登录Token值
     */
    private String token;

    /**
     * Token剩余有效期（单位：秒）
     * -1 = 永久有效
     * -2 = 无此Token/Token已失效
     */
    private long expireSeconds;

    /**
     * 登录设备类型（如PC/APP/小程序，无则为null）
     */
    private String device;

    /**
     * 账号ID
     */
    private Long accountId;
}