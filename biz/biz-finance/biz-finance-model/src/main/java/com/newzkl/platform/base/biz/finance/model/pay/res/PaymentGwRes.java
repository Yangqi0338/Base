package com.newzkl.platform.base.biz.finance.model.pay.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网关类支付 响应参数
 */
@Data
@EqualsAndHashCode
public class PaymentGwRes implements PayBaseRes {
    /** 返回码 */
    private String ret_code;
    /** 返回信息 */
    private String ret_msg;
    /** 商户号 */
    private String oid_partner;
    /** 用户ID */
    private String user_id;
    /** 交易金额 */
    private Double total_amount;
    /** 商户交易流水号 */
    private String txn_seqno;
    /** 平台交易流水号 */
    private String accp_txno;
    /** 支付授权令牌，有效期30分钟。当交易需要二次验证的时候，需要通过token调用调用交易二次短信验证接口 */
    private String token;
    /**
     * 网关地址。网银支付方式适用，返回跳转网关地址，用户跳转到网关完成后续支付操作。跳转方式：商户前端form表单POST提交。
     * 扫码支付返回此地址后，按链接生成二维码，让用户扫码完成支付。
     */
    private String gateway_url;
    /** 支付参数集合。返回外部渠道的标准支付提交参数，微信/支付宝可参考官方文档 */
    private String payload;
}
