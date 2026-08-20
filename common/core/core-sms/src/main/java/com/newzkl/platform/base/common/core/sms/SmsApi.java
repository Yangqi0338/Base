package com.newzkl.platform.base.common.core.sms;

import com.dtflys.forest.annotation.ForestClient;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.LogEnabled;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Retry;


/**
 * 联麓短信API客户端
 */
@ForestClient
@Retry(maxRetryCount = "0", maxRetryInterval = "10")
@LogEnabled(false)
public interface SmsApi {

    /**
     * 发送短信模板消息
     * @ext 联麓
     * @param req 发送请求参数
     * @return 短信发送结果
     */
    @Post(url = "https://apis.shlianlu.com/sms/trade/template/send", interceptor = LianLuSmsInterceptor.class)
    SmsRes.LianLuSendMsgRes lianLuSendMsg(@JSONBody SmsReq.LianLuSendMsgReq req);
}