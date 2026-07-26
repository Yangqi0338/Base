package com.newzkl.platform.base.biz.finance.model.person.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连随机密码因子获取请求。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RandomPasswordReq extends TripartiteBaseParam {

    /**
     * 用户在商户系统中的唯一编号。
     */
    private String user_id;

    /**
     * 交易发起渠道: ANDROID / IOS / H5 / PCH5 / PC。
     */
    private String flag_chnl;

    /**
     * APP 包名。flag_chnl 为 H5 或 PCH5 时送商户一级域名。
     */
    private String pkg_name;

    /**
     * APP 应用名。flag_chnl 为 H5 或 PCH5 时送商户一级域名。
     */
    private String app_name;

    /**
     * 加密算法: RSA (默认) / SM2 (flag_chnl = PCH5 时必须)。
     */
    private String encrypt_algorithm;
}
