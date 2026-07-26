package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连个人用户开户申请响应。
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OpenacctApplyResult extends LianLianBaseRes {

    /**
     * 用户在商户系统中的唯一编号。
     */
    private String user_id;

    /**
     * 用户姓名。
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
     * 授权令牌。
     */
    private String token;
}
