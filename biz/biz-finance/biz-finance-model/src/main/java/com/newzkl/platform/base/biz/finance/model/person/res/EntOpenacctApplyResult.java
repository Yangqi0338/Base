package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连企业用户开户申请响应。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EntOpenacctApplyResult extends LianLianBaseRes {

    /**
     * 用户在商户系统中的唯一编号。
     */
    private String user_id;

    /**
     * 企业名称。
     */
    private String user_name;

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
     * 授权令牌。
     */
    private String token;

    /**
     * 用户状态: ACTIVATE_PENDING (已登记或开户失败) / CHECK_PENDING (审核中)
     * / REMITTANCE_VALID_PENDING (待打款验证) / NORMAL (正常) / CANCEL (销户)
     * / PAUSE (暂停) / ACTIVATE_PENDING_NEW (待激活)。
     */
    private String user_status;
}
