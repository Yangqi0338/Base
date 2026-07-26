package com.newzkl.platform.base.biz.account.model.merchant.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信公众号配置值对象 (商户表 {@code wx_mp_config} JSON 列)。
 *
 * <p>迁移自旧 {@code com.zkl.scm.rpc.model.WxMpConfigVO} (旧位于 scm-common/common-rpc,
 * 因仅商户切片使用, 本仓收敛到账户域 model)。</p>
 *
 * @author muc_fang
 */
@Data
public class WxMpConfigVO implements Serializable {

    /**
     * 公众号 appId
     */
    private String appId;

    /**
     * 公众号密钥
     */
    private String secret;

    /**
     * 公众号消息校验 token
     */
    private String token;

    /**
     * 公众号消息加解密密钥
     */
    private String aesKey;
}
