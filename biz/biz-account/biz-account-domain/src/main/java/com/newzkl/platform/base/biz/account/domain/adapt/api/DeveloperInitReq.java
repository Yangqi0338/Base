package com.newzkl.platform.base.biz.account.domain.adapt.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 开放平台开发者初始化入参。
 *
 * <p>迁移: 跨域 openapi 结构
 * {@code com.zkl.scm.openapi.rpc.model.req.DeveloperInitReq}
 * 降级为 account 本地端口 DTO。</p>
 *
 * @author KC
 */
@Data
public class DeveloperInitReq implements Serializable {

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用密钥
     */
    private String secret;
}
