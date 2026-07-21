package com.newzkl.platform.base.common.core.model.constants;

/**
 * 令牌与请求头透传字段常量。
 *
 * @author muc_fang
 */
public interface TokenConstants {
    /**
     * 请求头标识
     */
    String AUTHENTICATION = "token";
    /**
     * 请求头标识
     */
    String ROLE = "role";
    /**
     * 令牌前缀
     */
    String PREFIX = "Bearer ";
    /**
     * 令牌秘钥
     */
    String SECRET = "abcdefghijklmnopqrstuvwxyz";
    /**
     * TOKEN: 客户端字段
     */
    String DETAILS_CLIENT = "client";
    /**
     * TOKEN: 用户ID字段
     */
    String DETAILS_ACCOUNT_ID = "account_id";
    /**
     * TOKEN: 用户名字段
     */
    String DETAILS_USERNAME = "username";
    /**
     * TOKEN: 用户昵称
     */
    String DETAILS_NICKNAME = "nickname";
    /**
     * TOKEN: 上级ID
     */
    String DETAILS_UP_ID = "up_id";
    /**
     * 访问信息 (IP) RequestInfo.class
     */
    String REQUEST_INFO = "request_info";
    /**
     * 越过检查模式
     */
    String SKIP_MODE = "skip_mode";
}
