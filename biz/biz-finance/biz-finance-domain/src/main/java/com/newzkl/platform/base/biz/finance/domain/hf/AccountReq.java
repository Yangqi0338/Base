package com.newzkl.platform.base.biz.finance.domain.hf;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 账号相关的内部请求类
 *
 * @author kc
 */
abstract class AccountReq {

    /**
     * 卡信息
     */
    @Data
    static class CardInfo {

        /**
         * 卡类型
         */
        @NotBlank(message = "卡类型不能为空")
        @Pattern(regexp = "^[0124]$", message = "卡类型只能是0（对公）、1（对私法人）、2（对私非法人）、4（对公非同名）")
        private String card_type;

        /**
         * 卡户名
         */
        @Size(max = 128, message = "卡户名长度不能超过128位")
        private String card_name;

        /**
         * 卡号
         */
        @NotBlank(message = "卡号不能为空")
        @Size(max = 32, message = "卡号长度不能超过32位")
        private String card_no;

        /**
         * 银行所在省
         */
        @NotBlank(message = "银行所在省不能为空")
        @Size(max = 6, message = "银行所在省编码长度不能超过6位")
        private String prov_id;

        /**
         * 银行所在市
         */
        @NotBlank(message = "银行所在市不能为空")
        @Size(max = 6, message = "银行所在市编码长度不能超过6位")
        private String area_id;

        /**
         * 支行联行号
         */
        @Size(max = 12, message = "支行联行号长度不能超过12位")
        private String branch_code;

        /**
         * 持卡人证件类型
         */
        @Size(max = 2, message = "持卡人证件类型长度不能超过2位")
        private String cert_type;

        /**
         * 持卡人证件号码
         */
        @Size(max = 32, message = "持卡人证件号码长度不能超过32位")
        private String cert_no;

        /**
         * 持卡人证件有效期类型
         */
        @Pattern(regexp = "^[01]$", message = "持卡人证件有效期类型只能是非长期有效或长期有效")
        private String cert_validity_type;

        /**
         * 持卡人证件有效期（起始）
         */
        @Pattern(regexp = "^\\d{8}$", message = "持卡人证件有效期（起始）格式必须为yyyyMMdd")
        private String cert_begin_date;

        /**
         * 持卡人证件有效期（截止）
         */
        @Pattern(regexp = "^\\d{8}$", message = "持卡人证件有效期（截止）格式必须为yyyyMMdd")
        private String cert_end_date;

    }

    /**
     * 取现配置
     */
    @Data
    static class CashConfig {

        /**
         * 业务类型
         */
        @Pattern(regexp = "^(T1|D1|D0|DM)?$", message = "业务类型只能是T1、D1、D0或DM")
        private String cash_type;

        /**
         * 提现手续费（固定/元）
         */
        @Pattern(regexp = "^\\d{1,4}\\.\\d{2}$", message = "提现手续费需保留小数点后两位，总长度不超过6位")
        private String fix_amt;

        /**
         * 提现手续费率（%）
         */
        @Pattern(regexp = "^(0\\.00|100\\.00|([1-9]\\d?|0)\\.\\d{2})$", message = "提现手续费率需保留小数点后两位，取值范围[0.00,100.00]，总长度不超过6位")
        private String fee_rate;

        /**
         * D1工作日取现手续费固定金额
         */
        @Pattern(regexp = "^\\d{1,4}\\.\\d{2}$", message = "D1工作日取现手续费固定金额需保留小数点后两位，总长度不超过6位")
        private String weekday_fix_amt;

        /**
         * D1工作日取现手续费率
         */
        @Pattern(regexp = "^(0\\.00|100\\.00|([1-9]\\d?|0)\\.\\d{2})$", message = "D1工作日取现手续费率需保留小数点后两位，取值范围[0.00,100.00]，总长度不超过6位")
        private String weekday_fee_rate;

        /**
         * 是否交易手续费外扣
         */
        @Pattern(regexp = "^[12]?$", message = "是否交易手续费外扣只能是外扣或内扣")
        private String out_fee_flag;

        /**
         * 手续费承担方
         */
        @Size(max = 18, message = "手续费承担方长度不能超过18位")
        private String out_fee_huifu_id;

        /**
         * 交易手续费外扣的账户类型
         */
        @Pattern(regexp = "^(01|05)?$", message = "交易手续费外扣的账户类型只能是基本户或充值户")
        private String out_fee_acct_type;

        /**
         * 是否优先到账
         */
        @Pattern(regexp = "^[YN]?$", message = "是否优先到账只能是是或否")
        private String is_priority_receipt;

    }

    /**
     * 汇付绑卡扩展
     *
     * @author niu
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class EnterCardReq extends Base.Req {
        /**
         * 渠道商/商户汇付Id
         */
        @NotBlank(message = "渠道商/商户汇付Id不能为空")
        private String upper_huifu_id;
        /**
         * 结算配置
         */
        @Valid
        private SettleConfig settle_config;
        /**
         * 卡信息
         */
        @Valid
        private CardInfo card_info;
        /**
         * 提现配置
         */
        @Valid
        private List<CashConfig> cash_config;
        /**
         * 异步通知地址
         */
        private String async_return_url;
        /**
         * 文件列表
         */
        private List<Base.FileListItem> file_list;
    }

    /**
     * 汇付企业用户入驻请求
     *
     * @author niu
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class EntUserEnterReq extends Base.Req {

        /**
         * 企业用户名称
         */
        @NotBlank(message = "企业用户名称不能为空")
        @Size(max = 128, message = "企业用户名称长度不能超过128位")
        private String reg_name;

        /**
         * 营业执照编号
         */
        @NotBlank(message = "营业执照编号不能为空")
        @Size(max = 18, message = "营业执照编号长度不能超过18位")
        private String license_code;

        /**
         * 证照有效期类型
         */
        @NotBlank(message = "证照有效期类型不能为空")
        @Pattern(regexp = "^[01]$", message = "证照有效期类型只能是非长期有效或长期有效")
        private String license_validity_type;

        /**
         * 证照有效期起始日期
         */
        @NotBlank(message = "证照有效期起始日期不能为空")
        @Pattern(regexp = "^\\d{8}$", message = "证照有效期起始日期格式必须为yyyyMMdd")
        private String license_begin_date;

        /**
         * 证照有效期结束日期
         */
        @Pattern(regexp = "^\\d{8}$", message = "证照有效期结束日期格式必须为yyyyMMdd")
        private String license_end_date;

        /**
         * 注册地址(省)
         */
        @NotBlank(message = "注册地址(省)不能为空")
        @Size(max = 6, message = "注册地址(省)编码长度不能超过6位")
        private String reg_prov_id;

        /**
         * 注册地址(市)
         */
        @NotBlank(message = "注册地址(市)不能为空")
        @Size(max = 8, message = "注册地址(市)编码长度不能超过8位")
        private String reg_area_id;

        /**
         * 注册地址(区)
         */
        @NotBlank(message = "注册地址(区)不能为空")
        @Size(max = 12, message = "注册地址(区)编码长度不能超过12位")
        private String reg_district_id;

        /**
         * 注册地址(详细信息)
         */
        @NotBlank(message = "注册地址(详细信息)不能为空")
        @Size(max = 256, message = "注册地址(详细信息)长度不能超过256位")
        private String reg_detail;

        /**
         * 法人姓名
         */
        @NotBlank(message = "法人姓名不能为空")
        @Size(max = 32, message = "法人姓名长度不能超过32位（32位英文字符或16个汉字）")
        private String legal_name;

        /**
         * 法人证件类型
         */
        @NotBlank(message = "法人证件类型不能为空")
        @Size(max = 2, message = "法人证件类型长度不能超过2位")
        private String legal_cert_type;

        /**
         * 法人证件号码
         */
        @NotBlank(message = "法人证件号码不能为空")
        @Size(max = 20, message = "法人证件号码长度不能超过20位")
        private String legal_cert_no;

        /**
         * 法人证件有效期类型
         */
        @NotBlank(message = "法人证件有效期类型不能为空")
        @Pattern(regexp = "^[01]$", message = "法人证件有效期类型只能是非长期有效或长期有效")
        private String legal_cert_validity_type;

        /**
         * 法人证件有效期开始日期
         */
        @NotBlank(message = "法人证件有效期开始日期不能为空")
        @Pattern(regexp = "^\\d{8}$", message = "法人证件有效期开始日期格式必须为yyyyMMdd")
        private String legal_cert_begin_date;

        /**
         * 法人证件有效期截止日期
         */
        @Pattern(regexp = "^\\d{8}$", message = "法人证件有效期截止日期格式必须为yyyyMMdd")
        private String legal_cert_end_date;

        /**
         * 联系人姓名
         */
        @NotBlank(message = "联系人姓名不能为空")
        @Size(max = 32, message = "联系人姓名长度不能超过32位（32位英文字符或16个汉字）")
        private String contact_name;

        /**
         * 联系人手机号
         */
        @NotBlank(message = "联系人手机号不能为空")
        @Pattern(regexp = "^1\\d{10}$", message = "联系人手机号格式不正确")
        private String contact_mobile;

        @AssertFalse(message = "法人证件有效期截止日期不能为空")
        public boolean checkLegalCertEndDate() {
            return "0".equals(legal_cert_begin_date) && StrUtil.isBlank(legal_cert_end_date);
        }

        @AssertFalse(message = "证照有效期结束日期不能为空")
        public boolean checkLicenseCertEndDate() {
            return "0".equals(license_validity_type) && StrUtil.isBlank(license_end_date);
        }
    }

    /**
     * 汇付结算配置
     *
     * @author kc
     */
    @Data
    static class SettleConfig {

        /**
         * 结算周期
         */
        @NotBlank(message = "结算周期不能为空")
        @Pattern(regexp = "^(T1|D1|TS)$", message = "结算周期只能是下个工作日到账或下个自然日到账")
        private String settle_cycle;

        /**
         * 起结金额
         */
        @Pattern(regexp = "^(0\\.0[1-9]|0\\.[1-9]\\d|[1-9]\\d{0,9}\\.\\d{2}|99999999999\\.99)$", message = "起结金额需精确到小数点后两位，取值范围[0.01,99999999999.99]")
        private String min_amt;

        /**
         * 留存金额
         */
        @Pattern(regexp = "^(0\\.0[1-9]|0\\.[1-9]\\d|[1-9]\\d{0,9}\\.\\d{2}|99999999999\\.99)$", message = "留存金额需精确到小数点后两位，取值范围[0.01,99999999999.99]")
        private String remained_amt;

        /**
         * 结算摘要
         */
        @Size(max = 128, message = "结算摘要长度不能超过128位")
        private String settle_abstract;

        /**
         * 手续费外扣标记
         */
        @Pattern(regexp = "^[12]?$", message = "手续费外扣标记只能是外扣或内扣")
        private String out_settle_flag;

        /**
         * 结算手续费外扣时的汇付ID
         */
        @Size(max = 18, message = "结算手续费外扣时的汇付ID长度不能超过18位")
        private String out_settle_huifuid;

        /**
         * 结算手续费外扣时的账户类型
         */
        @Pattern(regexp = "^(01|05)?$", message = "结算手续费外扣时的账户类型只能是基本户或充值户")
        private String out_settle_acct_type;

        /**
         * 结算方式
         */
        @Pattern(regexp = "^(P0|P1|P2)?$", message = "结算方式只能是批次结算、定时结算或批次定时结算")
        private String settle_pattern;

        /**
         * 结算批次号
         */
        @Size(max = 32, message = "结算批次号长度不能超过32位")
        private String settle_batch_no;

        /**
         * 是否优先到账
         */
        @Pattern(regexp = "^[YN]?$", message = "是否优先到账只能是是或否")
        private String is_priority_receipt;

        /**
         * 自定义结算处理时间
         */
        @Pattern(regexp = "^((0[1-9]|1\\d|2[0-3])[0-5]\\d(0[1-9]|[1-5]\\d)|00[3-5]\\d\\d{2})$", message = "自定义结算处理时间格式为HHmmss，且00:00到00:30不能指定")
        private String settle_time;

        /**
         * 节假日结算手续费率
         */
        @Pattern(regexp = "^(0\\.00|100\\.00|([1-9]\\d?|0)\\.\\d{2})$", message = "节假日结算手续费率需保留小数点后两位，取值范围[0.00,100.00]")
        private String fixed_ratio;

        /**
         * 工作日结算手续费率
         */
        @Pattern(regexp = "^(0\\.00|100\\.00|([1-9]\\d?|0)\\.\\d{2})$", message = "工作日结算手续费率需保留小数点后两位，取值范围[0.00,100.00]")
        private String workday_fixed_ratio;

        /**
         * 工作日结算手续费固定金额
         */
        @Pattern(regexp = "^\\d{0,13}\\.\\d{2}$", message = "工作日结算手续费固定金额需精确到小数点后两位，总长度不超过15位")
        private String workday_constant_amt;

        /**
         * 节假日结算手续费固定金额
         */
        @Pattern(regexp = "^\\d{0,13}\\.\\d{2}$", message = "节假日结算手续费固定金额需精确到小数点后两位，总长度不超过15位")
        private String constant_amt;

    }

    /**
     * 汇付用户入驻请求
     *
     * @author niu
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class UserEnterReq extends Base.Req {
        /**
         * 个人姓名
         */
        @NotBlank(message = "姓名不能为空")
        @Size(max = 32, message = "姓名长度不能超过32位")
        private String name;
        /**
         * 个人证件类型
         */
        @NotBlank(message = "证件类型不能为空")
        @Size(max = 2, message = "证件类型长度不能超过2位")
        private String cert_type;
        /**
         * 个人证件号码
         */
        @NotBlank(message = "证件号码不能为空")
        @Size(max = 32, message = "证件号码长度不能超过20位")
        private String cert_no;
        /**
         * 个人证件有效期类型
         */
        @NotBlank(message = "证件有效期类型不能为空")
        @Pattern(regexp = "^[01]$", message = "证件有效期类型只能是非长期有效或长期有效")
        private String cert_validity_type;
        /**
         * 个人证件有效期开始日期
         */
        @NotBlank(message = "证件有效期开始日期不能为空")
        @Pattern(regexp = "^\\d{8}$", message = "证件有效期开始日期格式必须为yyyyMMdd")
        private String cert_begin_date;
        /**
         * 个人证件有效期截止日期
         */
        @Pattern(regexp = "^\\d{8}$", message = "证件有效期截止日期格式必须为yyyyMMdd")
        private String cert_end_date;
        /**
         * 手机号
         */
        @NotBlank(message = "手机号不能为空")
        @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
        private String mobile_no;
    }

    /**
     * 斗拱e账户功能配置
     *
     * @author 斗拱e账户功能配置
     * @description: kc
     * @date 2025-08-25 17:05:40
     */
    @Data
    static class ElecAcctConfig {

        /**
         * 电子账户开关
         */
        @NotBlank(message = "电子账户开关不能为空")
        @Pattern(regexp = "^[01]$", message = "电子账户开关只能是关闭或开通")
        private String switch_state;

        /**
         * 账户类型
         */
        @NotBlank(message = "账户类型不能为空")
        @Pattern(regexp = "^01$", message = "账户类型仅支持中信e管家")
        private String acct_type;

        /**
         * 电子账户提现手续费承担方
         */
        @NotBlank(message = "电子账户提现手续费承担方不能为空")
        @Pattern(regexp = "^[12]$", message = "电子账户提现手续费承担方只能是总部或其他")
        private String cash_fee_party;

        /**
         * 场景
         */
        @NotBlank(message = "场景不能为空")
        @Size(max = 3, message = "场景长度必须为3位")
        private String scene;

        /**
         * 角色类型(角色编号)
         */
        @NotBlank(message = "角色类型(角色编号)不能为空")
        @Size(max = 6, message = "角色类型(角色编号)长度不能超过6位")
        private String role_type;

        /**
         * 签约成功标志
         */
        @NotBlank(message = "签约成功标志不能为空")
        @Pattern(regexp = "^Y$", message = "签约成功标志仅支持成功")
        private String sign_success_flag;

    }

    static class Ext {
        /**
         * 卡信息扩展
         */
        @EqualsAndHashCode(callSuper = true)
        @Data
        static class CardInfoExt extends CardInfo {

            /**
             * 银行卡绑定手机号
             */
            @Pattern(regexp = "^1\\d{10}$", message = "银行卡绑定手机号格式不正确")
            private String mp;

            /**
             * 默认结算卡标志
             */
            @Pattern(regexp = "^[YN]?$", message = "默认结算卡标志只能是是或否")
            private String is_settle_default;

        }

        /**
         * 汇付绑卡请求
         *
         * @author niu
         */
        @EqualsAndHashCode(callSuper = true)
        @Data
        static class EnterCardReqExt extends EnterCardReq {
            /**
             * 渠道商/商户汇付Id
             */
            @NotBlank(message = "渠道商/商户汇付Id不能为空")
            private String upper_huifu_id;
            /**
             * 结算配置
             */
            @Valid
            private SettleConfig settle_config;
            /**
             * 卡信息
             */
            @Valid
            private CardInfo card_info;
            /**
             * 提现配置
             */
            @Valid
            private List<CashConfig> cash_config;
            /**
             * 斗拱e账户功能配置
             *
             */
            @Valid
            private ElecAcctConfig elec_acct_config;
            /**
             * 异步通知地址
             */
            private String async_return_url;
            /**
             * 文件列表
             */
            private List<Base.FileListItem> file_list;
        }

        /**
         * 汇付企业用户入驻扩展
         *
         * @author niu
         */
        @EqualsAndHashCode(callSuper = true)
        @Data
        static class EntUserEnterReqExt extends EntUserEnterReq {

            @Size(max = 20, message = "经营简称长度不能超过20位（20位英文字符或10个汉字）")
            private String short_name;

            @Size(max = 50, message = "法人国籍长度不能超过50位")
            private String legal_cert_nationality;

            @Size(max = 64, message = "联系人电子邮箱长度不能超过64位")
            @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "联系人电子邮箱格式不正确")
            private String contact_email;


            @Size(max = 32, message = "管理员账号长度不能超过32位")
            private String login_name;

            @Size(max = 32, message = "操作员长度不能超过32位")
            private String operator_id;

            @Pattern(regexp = "^[YN]$", message = "是否发送短信标识只能是发送或不发送")
            private String sms_send_flag;

            @Size(max = 18, message = "扩展方字段长度不能超过18位")
            private String expand_id;

            private List<Base.FileListItem> file_list;

            @NotBlank(message = "公司类型不能为空")
            @Pattern(regexp = "^[1-8]$", message = "公司类型只能是 1:政府机构 2:国营企业 3:私营企业 4:外资企业 5:个体工商户 6:其它组织 7:事业单位 8:集体经济")
            private String ent_type;

            @Size(max = 7, message = "所属行业编码长度不能超过7位")
            private String mcc;
        }

        /**
         * 汇付用户入驻扩展
         *
         * @author niu
         */
        @EqualsAndHashCode(callSuper = true)
        @Data
        static class UserEnterReqExt extends UserEnterReq {
            /**
             * 国籍
             */
            @Size(max = 50, message = "国籍长度不能超过50位")
            private String cert_nationality;

            /**
             * 电子邮箱
             */
            @Size(max = 64, message = "电子邮箱长度不能超过64位")
            @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "电子邮箱格式不正确")
            private String email;

            /**
             * 管理员账号
             */
            @Size(max = 32, message = "管理员账号长度不能超过32位")
            private String login_name;

            /**
             * 是否发送短信标识
             */
            @Size(max = 1, message = "是否发送短信标识长度必须为1位")
            @Pattern(regexp = "^[YN]$", message = "是否发送短信标识只能是发送或不发送")
            private String sms_send_flag;

            /**
             * 扩展方字段
             */
            @Size(max = 18, message = "扩展方字段长度不能超过18位")
            private String expand_id;

            /**
             * 文件列表
             */
            private List<Base.FileListItem> file_list;

            /**
             * 公司类型
             */
            @NotBlank(message = "公司类型不能为空")
            @Size(max = 1, message = "公司类型长度必须为1位")
            @Pattern(regexp = "^[1-8]$", message = "公司类型只能是 1:政府机构 2:国营企业 3:私营企业 4:外资企业 5:个体工商户 6:其它组织 7:事业单位 8:集体经济")
            private String ent_type;

            /**
             * 所属行业
             */
            @Size(max = 7, message = "所属行业编码长度不能超过7位")
            private String mcc;

            /**
             * 地址(省)
             */
            @NotBlank(message = "地址(省)不能为空")
            @Size(max = 6, message = "地址(省)编码长度不能超过6位")
            private String prov_id;

            /**
             * 地址(市)
             */
            @NotBlank(message = "地址(市)不能为空")
            @Size(max = 6, message = "地址(市)编码长度不能超过8位")
            private String area_id;

            /**
             * 地址(区)
             */
            @NotBlank(message = "地址(区)不能为空")
            @Size(max = 6, message = "地址(区)编码长度不能超过12位")
            private String district_id;

            /**
             * 地址
             */
            @NotBlank(message = "地址不能为空")
            @Size(max = 256, message = "地址长度不能超过256位")
            private String address;

        }

        /**
         * 斗拱e账户功能扩展
         *
         * @author 斗拱e账户功能配置
         * @description: kc
         * @date 2025-08-25 17:05:40
         */
        @EqualsAndHashCode(callSuper = true)
        @Data
        static class ElecAcctConfigExt extends ElecAcctConfig {

            /**
             * 银行卡信息
             */
            @Size(message = "银行卡信息需为jsonArray字符串格式")
            private String elec_card_list;

            /**
             * 用户类型
             */
            @Pattern(regexp = "^(SPLIT|SETTLE)?$", message = "用户类型只能是分账用户或结算用户")
            private String user_type;

            /**
             * 中信签约短信流水号
             */
            @Size(max = 64, message = "中信签约短信流水号长度不能超过64位")
            private String elec_acct_sign_seq_id;

        }
    }

}
