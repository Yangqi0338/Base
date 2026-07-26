package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;

/**
 * 连连个人/企业开户异步回调报文。
 *
 * <p>迁移自 new-scm {@code application.utils.model.res.ParsonPurseNotifyResult}
 * (旧名拼写错误 Parson, 迁移时更正为 {@code PersonPurseNotifyRes})。</p>
 *
 * <p>字段沿用连连下划线命名, 便于 JSON 直接反序列化, 不做驼峰改写。</p>
 *
 * @author KC
 */
@Data
public class PersonPurseNotifyRes {

    /**
     * 商户号。
     */
    private String oid_partner;

    /**
     * 商户端用户号 (即平台 accountId)。
     */
    private String user_id;

    /**
     * 商户交易流水号。
     */
    private String txn_seqno;

    /**
     * ACCP 系统交易单号。
     */
    private String accp_txno;

    /**
     * 连连侧用户号。
     */
    private String oid_userno;

    /**
     * 账户状态, 取值见 {@code PurseEnum.TripartitePurchaseStatus}。
     */
    private String user_status;

    /**
     * 备注 (开户失败原因等)。
     */
    private String remark;

    /**
     * 账户等级信息。
     */
    private AccountLevelInfo accountInfo;
}
