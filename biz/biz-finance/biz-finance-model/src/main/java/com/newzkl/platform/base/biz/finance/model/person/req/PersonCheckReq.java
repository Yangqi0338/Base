package com.newzkl.platform.base.biz.finance.model.person.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连开户验证请求 (个人 / 企业共用)。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PersonCheckReq extends TripartiteBaseParam {

    /**
     * 用户在商户系统中的唯一编号。
     */
    private String user_id;

    /**
     * 商户系统唯一交易流水号, 与开户申请一致。
     */
    private String txn_seqno;

    /**
     * 授权令牌, 有效期 30 分钟。
     */
    private String token;

    /**
     * 银行预留手机短信验证码。
     */
    private String verify_code;

    /**
     * 支付密码。
     */
    private String password;

    /**
     * 密码随机因子 key。
     */
    private String random_key;
}
