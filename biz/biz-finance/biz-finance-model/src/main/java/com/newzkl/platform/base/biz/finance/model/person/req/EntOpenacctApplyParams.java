package com.newzkl.platform.base.biz.finance.model.person.req;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连企业用户开户申请请求。
 *
 * <p>迁移偏离: 旧 8 个子信息类平铺在 {@code utils.model.openacct} 包下,
 * 本次收敛为本类的静态内部类 (仅本请求使用, 无跨类复用)。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EntOpenacctApplyParams extends TripartiteBaseParam {

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
     * 企业法定代表人信息。
     */
    private LegalreptInfo legalreptInfo;

    /**
     * 企业联系人信息。
     */
    private ContactsInfo contactsInfo;

    /**
     * 企业经营信息。
     */
    private BusinessInfo businessInfo;

    /**
     * 企业基本户信息。
     */
    private BasicAcctInfo basicAcctInfo;

    /**
     * 受益所有人信息。
     */
    private UboInfos uboInfos;

    /**
     * 开户账户申请信息。
     */
    private AccountInfo accountInfo;

    /**
     * 企业开户基本信息。
     *
     * @author KC
     */
    @Data
    public static class BasicInfo {
        /** 注册手机号。 */
        private String reg_phone;
        /** 手机号权属证明。 */
        private String reg_phone_evidence;
        /** 企业名称。 */
        private String user_name;
        /** 证件类型。 */
        private String id_type;
        /** 证件号。 */
        private String id_no;
        /** 证件有效期。 */
        private String id_exp;
        /** 证件起始日期。 */
        private String id_std;
        /** 统一社会信用代码。 */
        private String unified_code;
        /** 注册地址。 */
        private String address;
        /** 地址证明图片。 */
        private String address_pic;
        /** 注册邮箱。 */
        private String reg_email;
        /** 支付密码。 */
        private String password;
        /** 密码随机因子 key。 */
        private String random_key;
    }

    /**
     * 企业开户绑卡信息。
     *
     * @author KC
     */
    @Data
    public static class LinkedAcctInfo {
        /** 绑定账户类型。 */
        private String linked_accttype;
        /** 绑定账号。 */
        private String linked_acctno;
        /** 银行编码。 */
        private String linked_bankcode;
        /** 支行名称。 */
        private String linked_brbankname;
        /** 大额行号。 */
        private String linked_brbankno;
        /** 账户名。 */
        private String linked_acctname;
        /** 银行预留手机号。 */
        private String linked_phone;
    }

    /**
     * 企业法定代表人信息。
     *
     * @author KC
     */
    @Data
    public static class LegalreptInfo {
        /** 法人姓名。 */
        private String legalrept_name;
        /** 法人手机号。 */
        private String legalrept_phone;
        /** 法人证件类型。 */
        private String legalrept_id_type;
        /** 法人证件号。 */
        private String legalrept_idno;
        /** 证件国徽面。 */
        private String id_emblem;
        /** 证件人像面。 */
        private String id_portrait;
        /** 法人证件有效期。 */
        private String legalrept_idexp;
        /** 法人证件起始日期。 */
        private String legalrept_std;
    }

    /**
     * 企业联系人信息。
     *
     * @author KC
     */
    @Data
    public static class ContactsInfo {
        /** 联系人姓名。 */
        private String contacts_name;
        /** 联系人手机号。 */
        private String contacts_phone;
        /** 联系人证件类型。 */
        private String contacts_id_type;
        /** 联系人证件号。 */
        private String contacts_idno;
    }

    /**
     * 企业经营信息。
     *
     * @author KC
     */
    @Data
    public static class BusinessInfo {
        /** 企业规模。 */
        private String scale;
        /** 行业编码。 */
        private String industry_code;
        /** 注册资本。 */
        private String registered_capital;
        /** 经营范围。 */
        private String business_scope;
        /** 开户许可证。 */
        private String open_permit;
    }

    /**
     * 企业基本户信息。
     *
     * @author KC
     */
    @Data
    public static class BasicAcctInfo {
        /** 基本户银行编码。 */
        private String basicacct_bankcode;
        /** 基本户账号。 */
        private String basicacct_no;
    }

    /**
     * 受益所有人信息。
     *
     * @author KC
     */
    @Data
    public static class UboInfos {
        /** 受益人姓名。 */
        private String ubo_name;
        /** 受益人英文名。 */
        private String ubo_name_en;
        /** 受益人手机号。 */
        private String ubo_phone;
        /** 证件类型。 */
        private String id_type;
        /** 证件号。 */
        private String id_no;
        /** 证件国徽面。 */
        private String id_emblem;
        /** 证件人像面。 */
        private String id_portrait;
        /** 统一社会信用代码。 */
        private String unified_code;
        /** 证件有效期。 */
        private String id_exp;
        /** 发证机关。 */
        private String id_issue;
        /** 地址。 */
        private String address;
        /** 受益人权属证明。 */
        private String ubo_evidence;
        /** 受益人类型。 */
        private String ubo_type;
    }

    /**
     * 企业开户账户申请信息。
     *
     * @author KC
     */
    @Data
    public static class AccountInfo {
        /** 账户类型。 */
        private String account_type;
    }
}
