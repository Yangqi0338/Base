package com.newzkl.platform.base.biz.finance.domain.hf;


import com.dtflys.forest.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 汇付纯净API接口(FOREST)
 */
@ForestClient
@Component
@BaseRequest(baseURL = "#{platform.fi.huiFuPreUrl}", interceptor = HuiFuInterceptor.class)
@Retry(maxRetryCount = "0", maxRetryInterval = "10")
@LogEnabled(false)
public interface HuiFuApi {

    /**
     * 汇付聚合正扫
     * 场景1-台牌码：用户通过扫描聚合静态二维码，输入订单金额完成支付操作
     * 场景2-公众号/小程序商城：用户通过公众号/小程序下单，输入密码完成支付操作。
     * 场景3-商户根据用户选择支付方式，调用该接口生成支付宝/银联/数字人民币，用户使用对应app下“扫一扫”完成支付操作。
     */
    @Post(url = "/v3/trade/payment/jspay")
    AmountRes.PayRes pay(@JSONBody AmountReq.PayReq req);

    /**
     * 主动查询支付单支付状态
     */
    @Post(url = "/v3/trade/payment/scanpay/query")
    AmountRes.PayStateRes payState(@JSONBody AmountReq.PayStateReq req);

    /**
     * 汇付扫码交易原路退款
     * 交易发生之后一段时间内，由于用户或者商户的原因需要退款时，商户可以通过本接口将支付款退还给用户，退款成功资金将原路返回
     */
    @Post(url = "/v2/trade/payment/scanpay/refund")
    AmountRes.RefundRes refund(@JSONBody AmountReq.RefundReq req);

    /**
     * 汇付取现
     */
    @Post(url = "/v2/trade/settlement/encashment")
    AmountRes.WithdrawRes withdraw(@JSONBody AmountReq.WithdrawReq req);

    /**
     * 汇付代发 (走余额支付, 根据设置的用户结算周期, 来自动结算钱)
     */
    @Post(url = "/v2/trade/acctpayment/pay")
    AmountRes.RollOutRes rollout(@JSONBody AmountReq.RollOutReq req);

    /**
     * 汇付企业开户
     */
    @Post(url = "/v2/user/basicdata/ent")
    AccountRes.OpenAccountRes entOpenAccount(@JSONBody AccountReq.EntUserEnterReq req);

    /**
     * 汇付企业开户修改
     */
    @Post(url = "/v2/user/basicdata/ent/modify")
    AccountRes.OpenAccountRes entUpdateAccount(@JSONBody AccountReq.EntUserEnterReq req, @Header(value = "isModify") Boolean isModify);

    /**
     * 用户汇付开户
     */
    @Post(url = "/v2/user/basicdata/indv")
    AccountRes.OpenAccountRes userOpenAccount(@JSONBody AccountReq.UserEnterReq req);

    /**
     * 用户汇付开户修改
     */
    @Post(url = "/v2/user/basicdata/indv/modify")
    AccountRes.OpenAccountRes userUpdateAccount(@JSONBody AccountReq.UserEnterReq req, @Header(value = "isModify") Boolean isModify);

    /**
     * 用户汇付绑卡
     */
    @Post(url = "/v2/user/busi/open")
    AccountRes.AccountBindSyncRes bindCard(@JSONBody AccountReq.EnterCardReq req);

    /**
     * 用户汇付绑卡修改
     */
    @Post(url = "/v2/user/busi/open/modify")
    AccountRes.AccountBindSyncRes modifyCard(@JSONBody AccountReq.EnterCardReq req, @Header(value = "isModify") Boolean isModify);
}
