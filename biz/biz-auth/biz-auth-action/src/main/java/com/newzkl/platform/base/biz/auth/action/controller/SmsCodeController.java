package com.newzkl.platform.base.biz.auth.action.controller;

import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.core.redis.utils.SmsMethod;
import com.newzkl.platform.base.common.core.sms.CodeReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信验证码控制器
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/code")
public class SmsCodeController {

    /**
     * 获取短信验证码
     *
     * @param codeReq 短信入参
     * @return 发送结果
     */
    @PostMapping("/getVerificationCode")
    public PlatformResult<Object> getVerificationCode(@RequestBody CodeReq codeReq) {
        return SmsMethod.sendNotifyCode(codeReq);
    }

//    /**
//     * 供应商退货地址通知 TODO 理应在退货单通过后通知供应商
//     *
//     * @param sendVerificationCode 手机号集合
//     * @return 操作结果
//     */
//    @PostMapping("/sendVerificationCode")
//    public PlatformResult<Void> sendVerificationCode(@RequestBody SendVerificationCode sendVerificationCode) {
//        for (String s : sendVerificationCode.getPhone()) {
//            CodeReq codeReq = new CodeReq();
//            codeReq.setPhone(s);
//            codeReq.setType(SmsEnum.Type.SUPPLIER_REFUND_ADDRESS);
//            SmsMethod.sendCode(codeReq);
//        }
//        return PlatformResult.success();
//    }
}
