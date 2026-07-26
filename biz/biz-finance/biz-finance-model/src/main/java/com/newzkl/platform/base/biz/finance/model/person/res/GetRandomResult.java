package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连随机密码因子获取响应。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetRandomResult extends LianLianBaseRes {

    /**
     * 用户在商户系统中的唯一编号。
     */
    private String user_id;

    /**
     * 随机因子 key。
     */
    private String random_key;

    /**
     * 随机因子值。
     */
    private String random_value;

    /**
     * 密码控件 license。
     */
    private String license;

    /**
     * 键盘映射数组。
     */
    private String map_arr;

    /**
     * RSA 公钥内容。
     */
    private String rsa_public_content;

    /**
     * SM2 公钥 (hex)。
     */
    private String sm2_key_hex;
}
