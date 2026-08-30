package com.newzkl.platform.base.biz.finance.domain.hf;

import cn.hutool.core.util.NumberUtil;
import com.newzkl.platform.base.biz.finance.model.pay.res.PayBaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 金额相关的内部返回类
 */
abstract class AmountRes {

    @EqualsAndHashCode(callSuper = true)
    @Data
    static class AmountNotifyRes extends TradeRes {

        private String trans_stat;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class PayRes extends TradeRes implements PayBaseRes {

        private String bank_message;
        private Map<String, Object> pay_info;
        private String trans_stat;
        private String remark;
        private String trans_amt;
        private String trade_type;
        private String qr_code;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    static class PayStateRes extends Base.Res implements PayBaseRes {

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    static class RefundRes extends AmountNotifyRes {

        /**
         * 原交易请求日期 String(8) N 格式为yyyyMMdd，示例值：20220925
         */
        private String org_req_date;

        /**
         * 原交易请求流水号 String(128) N 示例值：rQ202112131149875651
         */
        private String org_req_seq_id;

        /**
         * 退款交易发生日期 String(8) N 格式为yyyyMMdd，示例值：20220925
         */
        private String trans_date;

        /**
         * 退款交易发生时间 String(6) N 格式：HHMMSS，示例值：091010 代表9点10分10秒
         */
        private String trans_time;

        /**
         * 退款完成时间 String(14) N 格式yyyyMMddHHmmss；示例值：20091225091010
         */
        private String trans_finish_time;

        /**
         * 退款金额（元） String(14) Y 需保留小数点后两位；示例值：1.00，最低传入0.01
         */
        private String ord_amt;

        /**
         * 实际退款金额（元） String(14) N 需保留小数点后两位，示例值：1.00，最低传入0.01
         */
        private String actual_ref_amt;

        /**
         * 分账信息 String(2048) N 分账信息，jsonObject字符串
         */
        private String acct_split_bunch;

        /**
         * 微信返回的响应报文 String(6000) N
         */
        private String wx_response;

        /**
         * 支付宝返回的响应报文 String(6000) N 直连返回字段
         */
        private String alipay_response;

        /**
         * 补贴支付信息 String N jsonArray字符串；参见《补贴支付信息》
         */
        private List<String> combinedpay_data;

        /**
         * 补贴支付手续费承担方信息 String N jsonObject字符串
         */
        private String combinedpay_data_fee_info;

        /**
         * 备注 String(84) N 原样返回；示例值：备注
         */
        private String remark;

        /**
         * 是否垫资退款 String(2) N Y 是垫资出款， N 是普通出款， 为空默认N；示例值：N
         */
        private String loan_flag;

        /**
         * 垫资承担者 String(32) N 为空: 各自承担, 不为空走第三方垫资；示例值：6666000108854952
         */
        private String loan_undertaker;

        /**
         * 垫资账户类型 String(2) N 01:基本户, 05: 充值户, 默认充值户；示例值：05
         */
        private String loan_acct_type;

        /**
         * 通道返回描述 String(256) N 示例值：SUCCESS
         */
        private String bank_message;

        /**
         * 银联返回的响应报文 String(6000) N Json格式
         */
        private String unionpay_response;

        /**
         * 数字货币返回报文 String N 数字货币返回报文
         */
        private String dc_response;

        /**
         * 待确认金额 String(14) N 待确认金额；单位元。示例值：1.00
         */
        private String unconfirm_amt;

        /**
         * 资金冻结状态 String(16) N FREEZE：冻结；UNFREEZE：解冻；示例值：UNFREEZE（退款发生时，对应原交易的资金冻结状态）
         */
        private String fund_freeze_stat;

        /**
         * 手续费补贴返还信息 String N 手续费补贴返还信息对象，jsonObject字符串
         */
        private String trans_fee_ref_allowance_info;

        /**
         * 交易通道 String(1) N 枚举值：A-支付宝、T-微信、U-银联二维码、D-数字货币
         */
        private String pay_channel;

        /**
         * 是否退还手续费 String(1) N 是否退还手续费，支付宝直连场景下返回,Y或者空: 退费，N-不退费
         */
        private String is_refund_fee_flag;
    }

    /**
     * @author niu
     * @description:
     * @date 2025-08-25 15:25:03
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class RollOutRes extends AmountNotifyRes {

        /**
         * 出款商户号 String(32) N 出款方商户号；示例值：6666000108854953
         */
        private String out_huifu_id;

        /**
         * 支付金额 String(14) Y 示例值：200.00
         */
        private String ord_amt;

        /**
         * 分账对象 Y json格式。余额支付支持付款给多账户
         */
        private AcctSplitBunch acct_split_bunch;

        /**
         * 商品描述 String(256) N 示例值：商品描述
         */
        private String good_desc;

        /**
         * 交易完成时间 String(14) N 余额支付完成时间，格式：yyyyMMddHHmmss；示例值：20211021163242
         */
        private String trans_finish_time;

        /**
         * 备注 String(256) N 示例值：备注
         */
        private String remark;

        /**
         * 出款方账户号 String(32) N 示例值：F00598600
         */
        private String out_acct_id;

        /**
         * 资金类型 String(16) C 资金类型。支付渠道为中信E管家时，资金类型必填（详见说明）
         */
        private String fund_type;

        /**
         * 支付渠道 String(8) N 支付渠道。HUIFU=汇付账务(默认)；ZXE=中信E管家
         */
        private String acct_channel;

        /**
         * 灵活用工标志 String(1) N Y：灵活用工，N：非灵活用工(默认)；示例值：Y
         */
        private String hyc_flag;

        /**
         * 灵活用工平台 String(3) N 灵活用工平台 LJH：乐接活，HYC：汇优财(默认)。仅在hyc_flag=Y时起作用
         */
        private String lg_platform_type;

        /**
         * 代发模式 String(1) N 1：普票，2：专票。灵活用工平台为汇优财时必填！示例值：1
         */
        private String salary_modle_type;

        /**
         * 落地公司商户号 String(18) N 灵活用工平台为汇优财时必填！示例值：6666000109812124
         */
        private String bmember_id;

        /**
         * 灵活用工代发批次号 String(12) N 灵活用工平台为汇优财、汇薪云时返回！示例值：
         */
        private String hyc_attach_id;

        /**
         * 乐接活返回参数集合 N jsonObject字符串
         */
        private RollOutRes.LjhResponse ljh_response;

        /**
         * 手续费承担方标识 String(4) C 余额支付手续费承担方标识；商户余额支付扣收规则为接口指定承担方时必填！枚举值：OUT：出款方；IN：分账接受方。示例值：IN
         */
        private String trans_fee_take_flag;

        /**
         * 待确认总金额 String(14) N 单位元，需保留小数点后两位，示例值：1.00
         */
        private String unconfirm_amt;

        /**
         * 已确认总金额 String(14) N 单位元，需保留小数点后两位，示例值：1.00
         */
        private String confirmed_amt;

        // -------------------------- 嵌套对象：分账对象 --------------------------
        @Data
        public static class AcctSplitBunch {
            /**
             * 分账明细 Array N
             */
            private List<RollOutRes.AcctInfo> acct_infos;

            /**
             * 是否净值分账 String(1) N Y:使用净值分账，仅在交易手续费由出款方承担且使用百分比分账时起作用；示例值：Y
             */
            private String is_clean_split;

            /**
             * 手续费金额 String(14) N 单位:元，保留两位小数。示例值：1.23
             */
            private String fee_amt;
        }

        @Data
        public static class AcctInfo {
            /**
             * 分账金额 String(14) Y 单位元，需保留小数点后两位，灵活用工以异步返回的金额为准示例值：1.00 ,最低传入0.01
             */
            private String div_amt;

            /**
             * 分账接收方ID String(32) Y 开户自动生成编号；示例值：6666000108854952
             */
            private String huifu_id;

            /**
             * 账户号 String(32) N 可指定账户号，仅支持基本户、现金户，不填默认为基本户，示例值：F00598600
             */
            private String acct_id;

            /**
             * 交易状态 String(1) N P：处理中、S：成功、F：失败；灵工用工场景，以异步返回的交易状态为准示例值：P
             */
            private String trans_stat;

            /**
             * 手续费金额 String(14) N 手续费金额，单位:元。仅在分账接受方承担手续费时返回。示例值：0.02
             */
            private String fee_amt;

            /**
             * 手续费承担方商户号 String(32) Y 示例值：6666000108854952
             */
            private String fee_huifu_id;

            /**
             * 手续费承担方账户号 String(32) N 示例值：F00598600
             */
            private String fee_acct_id;
        }

        // -------------------------- 嵌套对象：乐接活返回参数 --------------------------
        @Data
        public static class LjhResponse {
            /**
             * 税源地ID String(64) N 税源地ID。灵活用工平台为乐接活时必填！
             */
            private String tax_area_id;

            /**
             * 任务模板ID String(64) N 任务模板ID。灵活用工平台为乐接活时必填！
             */
            private String template_id;
        }

    }

    /**
     * 交易类响应结果
     * @author niu
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class TradeRes extends Base.Res implements PayBaseRes {

        /**
         * 请求日期 String(8) Y 格式为yyyyMMdd，示例值：20220925
         */
        private String req_date;

        /**
         * 请求流水号 String(128) Y 交易时传入，原样返回；示例值：rQ2021121311173944134649875651
         */
        private String req_seq_id;

        /**
         * 全局流水号 String(128) N 示例值：00470topo1A221019132207P068ac1362af00000
         */
        private String hf_seq_id;

        @Override
        public Long getTradeNo() {
            return NumberUtil.parseLong(this.getReq_seq_id());
        }

        @Override
        public String getThirdTradeNo() {
            return this.getHf_seq_id();
        }
    }

    /**
     * @author niu
     * @description:
     * @date 2025-08-25 15:25:03
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    static class WithdrawRes extends AmountNotifyRes {

        /**
         * 账户号 String(32) N 可指定账户号，仅支持基本户、现金户，不填默认为基本户；示例值：F00598600
         */
        private String acct_id;

    }

}
