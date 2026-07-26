package com.newzkl.platform.base.biz.finance.model.person.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连个人用户开户申请请求。
 *
 * <p>迁移偏离: 旧类在 {@code utils.model.test} 包下且未继承公共参数基类,
 * 本次归入 {@code model.person.req} 并统一继承 {@link TripartiteBaseParam}。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OpenacctApplyParams extends TripartiteBaseParam {

    /**
     * 用户在商户系统中的唯一编号。
     */
    private String user_id;

    /**
     * 商户系统唯一交易流水号。
     */
    private String txn_seqno;

    /**
     * 交易时间。
     */
    private String txn_time;

    /**
     * 异步通知地址。
     */
    private String notify_url;

    /**
     * 是否开启短信验证。
     */
    private String open_sms_flag;

    /**
     * 风控参数。
     */
    private String risk_item;

    /**
     * 开户基本信息。
     */
    private BasicInfo basicInfo;

    /**
     * 开户绑卡信息。
     */
    private LinkedAcctInfo linkedAcctInfo;

    /**
     * 开户账户申请信息。
     */
    private AccountInfo accountInfo;

    /**
     * 个人开户基本信息。
     *
     * @author KC
     */
    @Data
    public static class BasicInfo {
        /** 注册手机号。 */
        private String reg_phone;
        /** 用户姓名。 */
        private String user_name;
        /** 证件类型。 */
        private String id_type;
        /** 证件号。 */
        private String id_no;
        /** 证件有效期。 */
        private String id_exp;
        /** 证件起始日期。 */
        private String id_std;
        /** 发证机关。 */
        private String id_authority;
        /** 地区编码。 */
        private String area_code;
        /** 地址。 */
        private String address;
        /** 职业。 */
        private String occupation;
        /** 证件国徽面。 */
        private String id_emblem;
        /** 证件人像面。 */
        private String id_portrait;
    }

    /**
     * 个人开户绑卡信息。
     *
     * @author KC
     */
    @Data
    public static class LinkedAcctInfo {
        /** 绑定银行卡号。 */
        private String linked_acctno;
        /** 银行预留手机号。 */
        private String linked_phone;
    }

    /**
     * 个人开户账户申请信息。
     *
     * @author KC
     */
    @Data
    public static class AccountInfo {
        /** 账户类型。 */
        private String account_type;
        /** 账户等级需求。 */
        private String account_need_level;
    }
}
