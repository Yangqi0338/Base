package com.newzkl.platform.base.biz.account.model.merchant.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 商户自助注册入参
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.role.model.req.MerchantCustomSaveReq}。
 * 商户主键取 {@code accountId} (与账号同 ID), 故不复用 {@code BaseReq#id}。</p>
 *
 * @author KC
 */
@Data
public class MerchantCustomSaveReq implements Serializable {

    /**
     * 账号 ID (即商户主键)
     */
    private Long accountId;

    /**
     * 上级渠道商 ID
     */
    private Long upChannelId;

    /**
     * 登录名称 (手机号)
     */
    private String username;

    /**
     * 名称 (为空时取 username)
     */
    private String name;

    /**
     * 头像
     */
    private String headImg;
}
