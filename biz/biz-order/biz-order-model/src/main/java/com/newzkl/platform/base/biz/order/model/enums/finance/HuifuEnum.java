package com.newzkl.platform.base.biz.order.model.enums.finance;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 汇付枚举
 *
 * @author niu
 * @description: 财务枚举
 * @date 2023/12/18 10:04
 */
public class HuifuEnum implements Serializable {

    public static String notifySuccess = "Success";

    public static boolean isSuccess(String code) {
        return AccountBizResCodeEnum.isSuccess(code) || BankBizResCode.isSuccess(code) || BizResCode.isSuccess(code) || BizCodeEnum.isSuccess(code);
    }

    /**
     * 扫码交易业务返回码枚举
     */
    @Getter
    @AllArgsConstructor
    public enum BizResCode {
        /**
         * 处理成功，实际交易结果以 trans_stat 为准
         */
        SUCCESS("00000000", "处理成功"),
        /**
         * 交易正在处理中
         */
        PROCESSING("00000100", "交易正在处理中"),
        /**
         * 无效参数
         */
        INVALID_PARAM("10000000", "无效参数"),
        /**
         * 原交易没有处理完成
         */
        ORIGINAL_TRADE_NOT_FINISHED("10000001", "原交易没有处理完成"),
        /**
         * 退款金额大于可退金额
         */
        REFUND_AMOUNT_EXCEED_REFUNDABLE("10000002", "退款金额大于可退金额"),
        /**
         * 退款金额大于待确认金额
         */
        REFUND_AMOUNT_EXCEED_PENDING("10000003", "退款金额大于待确认金额"),
        /**
         * 手续费退款金额大于可退金额
         */
        FEE_REFUND_EXCEED_REFUNDABLE("10000004", "手续费退款金额大于可退金额"),
        /**
         * 银联聚合补账不允许退款
         */
        UNIONPAY_SUPPLEMENT_REFUND_NOT_ALLOWED("10000005", "银联聚合补账不允许退款"),
        /**
         * 必须传入分账串
         */
        DIVISION_DATA_REQUIRED("10000006", "必须传入分账串"),
        /**
         * 该退款不支持传入分账
         */
        REFUND_DIVISION_NOT_SUPPORTED("10000007", "该退款不支持传入分账"),
        /**
         * 退款分账金额不能大于可退分账金额
         */
        REFUND_DIVISION_AMOUNT_EXCEED_REFUNDABLE("10000008", "退款分账金额不能大于可退分账金额"),
        /**
         * 该交易不支持部分退款
         */
        PARTIAL_REFUND_NOT_SUPPORTED("10000009", "该交易不支持部分退款"),
        /**
         * 含有未知的子客户号
         */
        UNKNOWN_SUB_CUST_ID("10000010", "含有未知的子客户号"),
        /**
         * 手续费子客户和原交易的不一致
         */
        FEE_SUB_CUST_MISMATCH("10000011", "手续费子客户和原交易的不一致"),
        /**
         * 优惠交易传入的分账金额总和不等于结算金额
         */
        DIVISION_SUM_NOT_EQUAL_SETTLE_AMOUNT("10000012", "优惠交易传入的分账金额总和不等于结算金额"),
        /**
         * 分账金额总和不等于退款订单金额
         */
        DIVISION_SUM_NOT_EQUAL_REFUND_AMOUNT("10000013", "分账金额总和不等于退款订单金额"),
        /**
         * 数据库更新失败
         */
        DB_UPDATE_FAILED("10000014", "数据库更新失败"),
        /**
         * 不允许关闭一分钟以内的订单
         */
        CLOSE_ORDER_TOO_EARLY("10000015", "不允许关闭一分钟以内的订单"),
        /**
         * 原订单已为终态,请发起查询交易获取
         */
        ORDER_FINAL_STATE("10000016", "原订单已为终态"),
        /**
         * 该交易暂不支持银联二维码
         */
        UNIONPAY_QR_NOT_SUPPORTED("10000017", "该交易暂不支持银联二维码"),
        /**
         * 关单失败
         */
        CLOSE_ORDER_FAILED("10000018", "关单失败"),
        /**
         * 手续费子客户和原交易的不一致
         */
        FEE_SUB_CUST_MISMATCH_AGAIN("10000019", "手续费子客户和原交易的不一致"),
        /**
         * 没有退款权限
         */
        NO_REFUND_PERMISSION("10000020", "没有退款权限"),
        /**
         * 没有分账退款权限
         */
        NO_DIVISION_REFUND_PERMISSION("10000021", "没有分账退款权限"),
        /**
         * 退款分账不能为空
         */
        REFUND_DIVISION_CANNOT_BE_EMPTY("10000022", "退款分账不能为空"),
        /**
         * 访问限制权限
         */
        ACCESS_RESTRICTED("11000000", "访问限制权限"),
        /**
         * 重复交易
         */
        DUPLICATE_TRADE("20000000", "重复交易"),
        /**
         * 操作过于频繁
         */
        TOO_FREQUENT("20000001", "操作过于频繁"),
        /**
         * 权限不足
         */
        INSUFFICIENT_PERMISSION("20000002", "权限不足"),
        /**
         * 账户已被冻结
         */
        ACCOUNT_FROZEN("20000003", "账户已被冻结"),
        /**
         * 交易不存在
         */
        TRADE_NOT_FOUND("20000004", "交易不存在"),
        /**
         * 商户不存在
         */
        MERCHANT_NOT_FOUND("20000005", "商户不存在"),
        /**
         * 商户状态异常
         */
        MERCHANT_STATUS_ABNORMAL("20000006", "商户状态异常"),
        /**
         * 结算卡不存在
         */
        SETTLEMENT_CARD_NOT_FOUND("20000007", "结算卡不存在"),
        /**
         * 账户信息不存在
         */
        ACCOUNT_INFO_NOT_FOUND("20000008", "账户信息不存在"),
        /**
         * 手续费账户信息不存在
         */
        FEE_ACCOUNT_INFO_NOT_FOUND("20000009", "手续费账户信息不存在"),
        /**
         * SSP子客户号和账户号无效
         */
        SSP_SUB_CUST_ACCOUNT_INVALID("20000010", "SSP子客户号和账户号无效"),
        /**
         * 手续费未配置
         */
        FEE_NOT_CONFIGURED("20000011", "手续费未配置"),
        /**
         * 分账金额不等于订单总金额
         */
        DIVISION_AMOUNT_NOT_EQUAL_TOTAL("20000012", "分账金额不等于订单总金额"),
        /**
         * 交易未完全成功
         */
        TRADE_NOT_FULLY_SUCCESS("20000013", "交易未完全成功"),
        /**
         * 已无可确认金额
         */
        NO_CONFIRMABLE_AMOUNT("20000014", "已无可确认金额"),
        /**
         * 确认参数或确认手续费承担方式与原交易冲突
         */
        CONFIRM_PARAM_CONFLICT("20000015", "确认参数或确认手续费承担方式与原交易冲突"),
        /**
         * 原交易发起方与此交易不一致
         */
        ORIGINAL_INITIATOR_MISMATCH("20000016", "原交易发起方与此交易不一致"),
        /**
         * 交易非延迟入账
         */
        TRADE_NOT_DELAYED("20000017", "交易非延迟入账"),
        /**
         * 参数逻辑校验不合法
         */
        PARAM_LOGIC_INVALID("21000000", "参数逻辑校验不合法"),
        /**
         * 产品配置信息异常
         */
        PRODUCT_CONFIG_ERROR("22000000", "产品配置信息异常"),
        /**
         * 商户所属关系异常
         */
        MERCHANT_RELATION_ERROR("22000001", "商户所属关系异常"),
        /**
         * 商户配置信息异常
         */
        MERCHANT_CONFIG_ERROR("22000002", "商户配置信息异常"),
        /**
         * 账户配置异常
         */
        ACCOUNT_CONFIG_ERROR("22000003", "账户配置异常"),
        /**
         * 权限配置异常
         */
        PERMISSION_CONFIG_ERROR("22000004", "权限配置异常"),
        /**
         * 其他配置异常
         */
        OTHER_CONFIG_ERROR("22000005", "其他配置异常"),
        /**
         * 数据权限不足
         */
        DATA_PERMISSION_DENIED("23000002", "数据权限不足"),
        /**
         * 金额校验异常
         */
        AMOUNT_VALIDATION_ERROR("23000003", "金额校验异常"),
        /**
         * 不支持的交易
         */
        UNSUPPORTED_TRADE("23000004", "不支持的交易"),
        /**
         * 调用收银台退款接口失败
         */
        CASHIER_REFUND_FAILED("30000000", "调用收银台退款接口失败"),
        /**
         * 收银台校验失败
         */
        CASHIER_VALIDATION_FAILED("30000001", "收银台校验失败"),
        /**
         * 交易受限，请联系客服。可能原因：单笔/单日金额超限、单日次数超限、换卡重试、交易风险、分账金额或方数超限
         */
        TRADE_RESTRICTED("90000000", "交易受限，请联系客服。\n 可能原因：单笔/单日金额超限、单日次数超限、换卡重试、交易风险、分账金额或方数超限"),
        /**
         * 交易存在风险，请稍后再试
         */
        TRADE_RISK_SUSPECTED("90000001", "交易存在风险，请稍后再试"),
        /**
         * 风控拦截
         */
        RISK_CONTROL_INTERCEPTED("90000002", "风控拦截"),
        /**
         * 未知系统错误
         */
        UNKNOWN_SYSTEM_ERROR("98888888", "未知系统错误"),
        /**
         * 系统异常，请稍后重试
         */
        SYSTEM_ERROR("99999999", "系统异常，请稍后重试"),
        ;

        private final String code;
        private final String message;

        /**
         * 判断是否为成功码
         *
         * @param code 返回码
         * @return true/false
         */
        public static boolean isSuccess(String code) {
            return SUCCESS.getCode().equals(code);
        }
    }

    /**
     * 线上交易类业务返回码枚举
     */
    @Getter
    @AllArgsConstructor
    public enum BankBizResCode {
        /**
         * 处理成功，实际交易结果以 trans_stat 为准
         */
        SUCCESS("00000000", "处理成功"),
        /**
         * 交易正在处理中
         */
        PROCESSING("00000100", "交易正在处理中"),
        /**
         * 无效参数
         */
        INVALID_PARAM("10000000", "无效参数"),
        /**
         * 原交易没有处理完成
         */
        ORIGINAL_TRADE_NOT_FINISHED("10000001", "原交易没有处理完成"),
        /**
         * 退款金额大于可退金额
         */
        REFUND_AMOUNT_EXCEED_REFUNDABLE("10000002", "退款金额大于可退金额"),
        /**
         * 退款金额大于待确认金额
         */
        REFUND_AMOUNT_EXCEED_PENDING("10000003", "退款金额大于待确认金额"),
        /**
         * 手续费退款金额大于可退金额
         */
        FEE_REFUND_EXCEED_REFUNDABLE("10000004", "手续费退款金额大于可退金额"),
        /**
         * 银联聚合补账不允许退款
         */
        UNIONPAY_SUPPLEMENT_REFUND_NOT_ALLOWED("10000005", "银联聚合补账不允许退款"),
        /**
         * 必须传入分账串
         */
        DIVISION_DATA_REQUIRED("10000006", "必须传入分账串"),
        /**
         * 该退款不支持传入分账
         */
        REFUND_DIVISION_NOT_SUPPORTED("10000007", "该退款不支持传入分账"),
        /**
         * 退款分账金额不能大于可退分账金额
         */
        REFUND_DIVISION_AMOUNT_EXCEED_REFUNDABLE("10000008", "退款分账金额不能大于可退分账金额"),
        /**
         * 该交易不支持部分退款
         */
        PARTIAL_REFUND_NOT_SUPPORTED("10000009", "该交易不支持部分退款"),
        /**
         * 含有未知的子客户号
         */
        UNKNOWN_SUB_CUST_ID("10000010", "含有未知的子客户号"),
        /**
         * 手续费子客户和原交易的不一致
         */
        FEE_SUB_CUST_MISMATCH("10000011", "手续费子客户和原交易的不一致"),
        /**
         * 优惠交易传入的分账金额总和不等于结算金额
         */
        DIVISION_SUM_NOT_EQUAL_SETTLE_AMOUNT("10000012", "优惠交易传入的分账金额总和不等于结算金额"),
        /**
         * 分账金额总和不等于退款订单金额
         */
        DIVISION_SUM_NOT_EQUAL_REFUND_AMOUNT("10000013", "分账金额总和不等于退款订单金额"),
        /**
         * 数据库更新失败
         */
        DB_UPDATE_FAILED("10000014", "数据库更新失败"),
        /**
         * 不允许关闭一分钟以内的订单
         */
        CLOSE_ORDER_TOO_EARLY("10000015", "不允许关闭一分钟以内的订单"),
        /**
         * 原订单已为终态,请发起查询交易获取
         */
        ORDER_FINAL_STATE("10000016", "原订单已为终态,请发起查询交易获取"),
        /**
         * 该交易暂不支持银联二维码
         */
        UNIONPAY_QR_NOT_SUPPORTED("10000017", "该交易暂不支持银联二维码"),
        /**
         * 关单失败
         */
        CLOSE_ORDER_FAILED("10000018", "关单失败"),
        /**
         * 手续费子客户和原交易的不一致
         */
        FEE_SUB_CUST_MISMATCH_AGAIN("10000019", "手续费子客户和原交易的不一致"),
        /**
         * 没有退款权限
         */
        NO_REFUND_PERMISSION("10000020", "没有退款权限"),
        /**
         * 没有分账退款权限
         */
        NO_DIVISION_REFUND_PERMISSION("10000021", "没有分账退款权限"),
        /**
         * 退款分账不能为空
         */
        REFUND_DIVISION_CANNOT_BE_EMPTY("10000022", "退款分账不能为空"),
        /**
         * 风控异常
         */
        RISK_CONTROL_EXCEPTION("10000023", "风控异常"),
        /**
         * 重复交易
         */
        DUPLICATE_TRADE("20000000", "重复交易"),
        /**
         * 权限不足
         */
        INSUFFICIENT_PERMISSION("20000002", "权限不足"),
        /**
         * 账户已被冻结
         */
        ACCOUNT_FROZEN("20000003", "账户已被冻结"),
        /**
         * 交易不存在
         */
        TRADE_NOT_FOUND("20000004", "交易不存在"),
        /**
         * 数据库更新失败
         */
        DB_UPDATE_FAILED_2("20000005", "数据库更新失败"),
        /**
         * 商户状态异常
         */
        MERCHANT_STATUS_ABNORMAL("20000006", "商户状态异常"),
        /**
         * 结算卡不存在
         */
        SETTLEMENT_CARD_NOT_FOUND("20000007", "结算卡不存在"),
        /**
         * 账户信息不存在
         */
        ACCOUNT_INFO_NOT_FOUND("20000008", "账户信息不存在"),
        /**
         * 手续费账户信息不存在
         */
        FEE_ACCOUNT_INFO_NOT_FOUND("20000009", "手续费账户信息不存在"),
        /**
         * 子客户号和账户号无效
         */
        SUB_CUST_ACCOUNT_INVALID("20000010", "子客户号和账户号无效"),
        /**
         * 手续费未配置
         */
        FEE_NOT_CONFIGURED("20000011", "手续费未配置"),
        /**
         * 充值仅支持借记卡
         */
        RECHARGE_ONLY_DEBIT_CARD("20000012", "充值仅支持借记卡"),
        /**
         * 页面数据被篡改
         */
        PAGE_DATA_TAMPERED("20000013", "页面数据被篡改"),
        /**
         * 订单已过期
         */
        ORDER_EXPIRED("20000014", "订单已过期"),
        /**
         * 短信发送频繁
         */
        SMS_SEND_TOO_FREQUENT("20000015", "短信发送频繁"),
        /**
         * 请重新发送短信
         */
        PLEASE_RESEND_SMS("20000016", "请重新发送短信"),
        /**
         * 短信发送中
         */
        SMS_SENDING("20000017", "短信发送中"),
        /**
         * 用户不存在
         */
        USER_NOT_FOUND("20000018", "用户不存在"),
        /**
         * 绑卡信息不存在
         */
        CARD_BINDING_NOT_FOUND("20000019", "绑卡信息不存在"),
        /**
         * 贷记卡不支持充值
         */
        CREDIT_CARD_NOT_SUPPORT_RECHARGE("20000020", "贷记卡不支持充值"),
        /**
         * 银行卡号不正确
         */
        BANK_CARD_NUMBER_INVALID("20000021", "银行卡号不正确"),
        /**
         * 订单信息不匹配
         */
        ORDER_INFO_MISMATCH("20000022", "订单信息不匹配"),
        /**
         * 不支持该银行卡号
         */
        BANK_CARD_NOT_SUPPORTED("20000023", "不支持该银行卡号"),
        /**
         * 数据加解密异常
         */
        ENCRYPT_DECRYPT_ERROR("20000024", "数据加解密异常"),
        /**
         * 随机因子异常
         */
        RANDOM_FACTOR_ERROR("20000025", "随机因子异常"),
        /**
         * 银行卡相关信息不完整或格式不正确
         */
        BANK_CARD_INFO_INCOMPLETE("20000026", "银行卡相关信息不完整或格式不正确"),
        /**
         * 商户证件信息不存在
         */
        MERCHANT_CERT_INFO_NOT_FOUND("20000027", "商户证件信息不存在"),
        /**
         * 商户不支持该银行
         */
        MERCHANT_NOT_SUPPORT_BANK("20000028", "商户不支持该银行"),
        /**
         * 用户与商户关系不存在
         */
        USER_MERCHANT_RELATION_NOT_FOUND("20000029", "用户与商户关系不存在"),
        /**
         * 无任何银行信息
         */
        NO_BANK_INFO("20000030", "无任何银行信息"),
        /**
         * 充值不支持延时分账
         */
        RECHARGE_NOT_SUPPORT_DELAY_DIVISION("20000031", "充值不支持延时分账"),
        /**
         * 代扣支付不支持贷记卡
         */
        WITHHOLDING_NOT_SUPPORT_CREDIT("20000032", "代扣支付不支持贷记卡"),
        /**
         * 未开通分期支付功能
         */
        INSTALLMENT_NOT_OPENED("20000033", "未开通分期支付功能"),
        /**
         * 未开通银行卡分期
         */
        BANK_INSTALLMENT_NOT_OPENED("20000034", "未开通银行卡分期"),
        /**
         * 无对应的分期费率
         */
        INSTALLMENT_RATE_NOT_FOUND("20000035", "无对应的分期费率"),
        /**
         * 分期期数不支持
         */
        INSTALLMENT_TERM_NOT_SUPPORTED("20000036", "分期期数不支持"),
        /**
         * 3期费率开关未开启
         */
        INSTALLMENT_3_RATE_DISABLED("20000037", "3期费率开关未开启"),
        /**
         * 6期费率开关未开启
         */
        INSTALLMENT_6_RATE_DISABLED("20000038", "6期费率开关未开启"),
        /**
         * 12期费率开关未开启
         */
        INSTALLMENT_12_RATE_DISABLED("20000039", "12期费率开关未开启"),
        /**
         * 24期费率开关未开启
         */
        INSTALLMENT_24_RATE_DISABLED("20000040", "24期费率开关未开启"),
        /**
         * 商户不存在
         */
        MERCHANT_NOT_FOUND("20000041", "商户不存在"),
        /**
         * 商户状态异常
         */
        MERCHANT_STATUS_ABNORMAL_2("20000042", "商户状态异常"),
        /**
         * 企业商户不存在
         */
        ENTERPRISE_MERCHANT_NOT_FOUND("20000043", "企业商户不存在"),
        /**
         * 个人商户不存在
         */
        PERSONAL_MERCHANT_NOT_FOUND("20000044", "个人商户不存在"),
        /**
         * 银行返回异常
         */
        BANK_RESPONSE_ERROR("20000045", "银行返回异常"),
        /**
         * 个人用户无法使用B2B网关
         */
        PERSONAL_CANNOT_USE_B2B("20000046", "个人用户无法使用B2B网关"),
        /**
         * 当前商户未配置支付银行，暂时无法使用网银支付功能
         */
        NO_BANK_CONFIG_FOR_ONLINE("20000047", "当前商户未配置支付银行"),
        /**
         * 退款请求FPC收银台失败
         */
        REFUND_FPC_CASHIER_FAILED("20000048", "退款请求FPC收银台失败"),
        /**
         * 退款请求OBC收银台失败
         */
        REFUND_OBC_CASHIER_FAILED("20000049", "退款请求OBC收银台失败"),
        /**
         * 充值不支持分账
         */
        RECHARGE_NOT_SUPPORT_DIVISION("20000050", "充值不支持分账"),
        /**
         * 充值时入账信息中客户号的必须为当前主交易的客户号
         */
        RECHARGE_ACCOUNTING_CUST_ID_MUST_MATCH("20000051", "充值时入账信息中客户号的必须为当前主交易的客户号"),
        /**
         * 分账比例配置异常
         */
        DIVISION_RATIO_CONFIG_ERROR("20000052", "分账比例配置异常"),
        /**
         * 没有延时入账权限
         */
        NO_DELAY_POSTING_PERMISSION("20000053", "没有延时入账权限"),
        /**
         * 手续费账户号未配置
         */
        FEE_ACCOUNT_NOT_CONFIGURED("20000054", "手续费账户号未配置"),
        /**
         * 交易金额必须大于手续费金额
         */
        AMOUNT_MUST_GREATER_THAN_FEE("20000055", "交易金额必须大于手续费金额"),
        /**
         * 获取支付手续费费率配置异常手续费类型错误
         */
        FEE_RATE_CONFIG_TYPE_ERROR("20000056", "获取支付手续费费率配置异常手续费类型错误"),
        /**
         * 获取支付手续费配置异常（网银）
         */
        FEE_CONFIG_ONLINE_ERROR("20000057", "获取支付手续费配置异常（网银）"),
        /**
         * 获取支付手续费配置异常（快捷、代扣）
         */
        FEE_CONFIG_QUICK_WITHHOLD_ERROR("20000058", "获取支付手续费配置异常（快捷、代扣）"),
        /**
         * 获取支付手续费配置异常不支持当前手续费类型
         */
        FEE_CONFIG_TYPE_NOT_SUPPORTED("20000059", "获取支付手续费配置异常不支持当前手续费类型"),
        /**
         * 获取支付手续费配置异常手续费交易类型错误
         */
        FEE_CONFIG_TRADE_TYPE_ERROR("20000060", "获取支付手续费配置异常手续费交易类型错误"),
        /**
         * 支付手续费配置不存在
         */
        FEE_CONFIG_NOT_FOUND("20000061", "支付手续费配置不存在"),
        /**
         * 手续费客户号与商户关系不存在
         */
        FEE_CUST_MERCHANT_RELATION_NOT_FOUND("20000062", "手续费客户号与商户关系不存在"),
        /**
         * 风控信息格式不正确
         */
        RISK_INFO_FORMAT_ERROR("20000063", "风控信息格式不正确"),
        /**
         * 获取支付手续费配置异常（手机WAP支付）
         */
        FEE_CONFIG_WAP_ERROR("20000064", "获取支付手续费配置异常（手机WAP支付）"),
        /**
         * 原交易订单已失败不允许关单
         */
        ORIGINAL_ORDER_FAILED_CANNOT_CLOSE("23000000", "原交易订单已失败不允许关单"),
        /**
         * 交易受限，请联系客服。可能原因：单笔/单日金额超限、单日次数超限、换卡重试、交易风险、分账金额或方数超限
         */
        TRADE_RESTRICTED("90000000", "交易受限，请联系客服。可能原因：单笔/单日金额超限、单日次数超限、换卡重试、交易风险、分账金额或方数超限"),
        /**
         * 交易存在风险，请稍后再试
         */
        TRADE_RISK_SUSPECTED("90000001", "交易存在风险，请稍后再试"),
        /**
         * 未知系统错误
         */
        UNKNOWN_SYSTEM_ERROR("98888888", "未知系统错误"),
        /**
         * 系统异常，请稍后重试
         */
        SYSTEM_ERROR("99999999", "系统异常，请稍后重试"),
        ;

        private final String code;
        private final String message;

        public static boolean isSuccess(String code) {
            return SUCCESS.getCode().equals(code);
        }
    }

    /**
     * 商户进件类业务返回码枚举
     */
    @Getter
    @AllArgsConstructor
    public enum AccountBizResCodeEnum {
        /**
         * 处理成功
         */
        SUCCESS("00000000", "处理成功"),
        /**
         * 基本参数校验失败；请检查请求参数
         */
        PARAM_VALIDATION_FAILED("00000001", "基本参数校验失败"),
        /**
         * 系统异常，请联系客服
         */
        SYSTEM_ERROR_00000002("00000002", "系统异常，请联系客服"),
        /**
         * 系统异常，请联系客服
         */
        SYSTEM_ERROR_00000003("00000003", "系统异常，请联系客服"),
        /**
         * 系统异常，请联系客服
         */
        SYSTEM_ERROR_00000004("00000004", "系统异常，请联系客服"),
        /**
         * 系统异常，请联系客服
         */
        SYSTEM_ERROR_00000005("00000005", "系统异常，请联系客服"),
        /**
         * 交易正在处理中
         */
        PROCESSING("00000100", "交易正在处理中"),
        /**
         * 交易失败，请重试
         */
        TRADE_FAILED_RETRY("00000101", "交易失败，请重试"),
        /**
         * 交易结果未知，请稍后查询
         */
        TRADE_RESULT_UNKNOWN("00000102", "交易结果未知，请稍后查询"),
        /**
         * 请求系统号无接口访问权限
         */
        NO_API_ACCESS_PERMISSION("00000103", "请求系统号无接口访问权限"),
        /**
         * 交易存在风险，请联系客服
         */
        TRADE_RISK_CONTACT_CUSTOMER("00000104", "交易存在风险，请联系客服"),
        /**
         * 账户余额不足
         */
        INSUFFICIENT_BALANCE("00000105", "账户余额不足"),
        /**
         * 商户状态异常
         */
        MERCHANT_STATUS_ABNORMAL("00000106", "商户状态异常"),
        /**
         * 钱包户不存在
         */
        WALLET_ACCOUNT_NOT_FOUND("00000107", "钱包户不存在"),
        /**
         * 钱包户状态异常
         */
        WALLET_ACCOUNT_STATUS_ABNORMAL("00000108", "钱包户状态异常"),
        /**
         * 解冻明细列表不能为空
         */
        UNFREEZE_DETAIL_LIST_EMPTY("00000109", "解冻明细列表不能为空"),
        /**
         * 开户成功绑卡失败
         */
        OPEN_ACCOUNT_SUCCESS_BIND_CARD_FAILED("00000113", "开户成功绑卡失败"),
        /**
         * 审核中
         */
        UNDER_REVIEW("90000000", "审核中"),
        /**
         * 系统异常，请联系客服
         */
        SYSTEM_ERROR("99999999", "系统异常，请联系客服"),

        ;

        private final String code;
        private final String message;

        public static boolean isSuccess(String code) {
            return SUCCESS.getCode().equals(code);
        }
    }

    /**
     * 商户进件类业务返回码枚举
     */
    @Getter
    @AllArgsConstructor
    public enum BizCodeEnum {
        /**
         * 成功
         */
        SUCCESS("S", "成功"),
        /**
         * 失败
         */
        FAIL("F", "失败"),
        ;

        private final String code;
        private final String message;

        public static boolean isSuccess(String code) {
            return SUCCESS.getCode().equals(code);
        }
    }

    /**
     * 商户进件类业务返回码枚举
     */
    @Getter
    @AllArgsConstructor
    public enum AuditCodeEnum {
        /**
         * 审核通过
         */
        SUCCESS("Y", "审核通过"),
        /**
         * 审核中
         */
        PROCESS("P", "审核中"),
        /**
         * 审核拒绝
         */
        NOT("N", "审核拒绝"),
        ;

        private final String code;
        private final String message;

        public static boolean isSuccess(String code) {
            return SUCCESS.getCode().equals(code);
        }
    }

    /**
     * 三方账号审核状态
     */
    @Getter
    @AllArgsConstructor
    public enum AuditStatus {

        /**
         * 汇付审核通过
         */
        HuiFu_SUCCESS("Y", "审核通过"),
        HuiFu_PROCESS("P", "审核中"),
        HuiFu_FAIL("N", "审核失败"),
        /* --- 汇付 --- */;

        private final String value;
        private final String desc;

        public static AuditStatus findByCode(String code) {
            return Arrays.stream(AuditStatus.values()).filter(it -> it.getValue().equals(code)).findFirst().orElse(null);
        }
    }
}
