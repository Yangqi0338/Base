package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连开户验证响应 (个人 / 企业共用)。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OpenacctVerifyResult extends LianLianBaseRes {

    /**
     * 用户在商户系统中的唯一编号。
     */
    private String user_id;

    /**
     * 商户系统唯一交易流水号。
     */
    private String txn_seqno;

    /**
     * 连连交易号。
     */
    private String accp_txno;

    /**
     * 连连用户号。
     */
    private String oid_userno;

    /**
     * 用户状态, 取值见 {@link EntOpenacctApplyResult#getUser_status()}。
     */
    private String user_status;
}
