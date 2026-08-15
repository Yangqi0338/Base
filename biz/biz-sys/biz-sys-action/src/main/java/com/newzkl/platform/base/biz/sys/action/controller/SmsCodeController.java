package com.newzkl.platform.base.biz.sys.action.controller;

import cn.hutool.core.util.RandomUtil;
import com.newzkl.platform.base.biz.sys.application.service.CodeService;
import com.newzkl.platform.base.biz.sys.model.code.req.CodeReq;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.core.model.enums.SmsEnum;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 短信验证码控制器
 *
 * <p>迁移自 new-scm {@code message.interfaces.controller.SmsCodeController},
 * 类级与方法级路径、HTTP verb、参数注解形式逐字保留。</p>
 *
 * <p>迁移调整:</p>
 * <ul>
 *   <li>{@code ScmResult} → {@code PlatformResult}; {@code jakarta.validation} → {@code jakarta.validation};</li>
 *   <li>{@code RedisClient} 实例注入 → Base 静态门面 {@code RedisUtil} (hSet/hGet/expire 语义等价);</li>
 *   <li>{@code ICodeService} → {@code CodeService}; 旧签名上的
 *       {@code UnsupportedEncodingException}/{@code NoSuchAlgorithmException} 随 MD5 实现下沉一并去掉
 *       (对外响应体不变);</li>
 *   <li>旧代码注释掉的 {@code codeService.sendCode} 分支不迁;</li>
 *   <li>短信模板枚举来源 → {@code core-sms} 的 {@code SmsEnum}</li>
 * </ul>
 *
 * <p>本控制器为免鉴权入口 (注册/登录前置), 与旧实现一致, 防刷靠下方分钟/小时限频。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/code")
@RequiredArgsConstructor
public class SmsCodeController {

    /**
     * 验证码长度
     */
    private static final int CODE_LENGTH = 6;

    private final CodeService codeService;

    /**
     * 环境标识, {@code dev} 跳过限频
     *
     * <p>默认 {@code prod} 而非 {@code dev}: 缺配置时必须**执行**限频而不是跳过,
     * 否则该端点在未配置环境下会成为无限频的短信发送入口。</p>
     */
    @Value("${sms.code.env:prod}")
    private String prefix;

    /**
     * 每小时最大发送条数。默认取保守值, 由入口 starter 按环境覆盖。
     */
    @Value("${sms.code.maxHourCount:5}")
    private Integer maxHourCount;

    /**
     * 每分钟最大发送条数。默认取保守值, 由入口 starter 按环境覆盖。
     */
    @Value("${sms.code.maxMinuteCount:1}")
    private Integer maxMinuteCount;

    /**
     * 获取短信验证码
     *
     * @param codeReq 短信入参
     * @return 发送结果
     */
    @PostMapping("/getVerificationCode")
    public PlatformResult<Object> getVerificationCode(@RequestBody CodeReq codeReq) {
        PlatformResult<Object> checked = checkSendCount(codeReq.getPhone());
        if (!checked.isSuccess()) {
            return checked;
        }
        SmsEnum.Type smsType = SmsEnum.Type.getByCode(codeReq.getType());
        String code = RandomUtil.randomNumbers(CODE_LENGTH);
        RedisUtil.hSet(RedisEnum.Key.SMS_CODE_HASH.getCode(), codeReq.getPhone(), code);
        codeReq.setParams(Collections.singletonList(code));
        codeReq.setTemplateId(smsType.getTemplateId());
        boolean result = codeService.sendCodeLianLu(codeReq);
        alterSendCount(codeReq.getPhone(), (int[]) checked.getData());
        return result ? PlatformResult.success() : PlatformResult.fail("短信发送失败");
    }

    /**
     * 批量发送短信 (供应商退货地址通知)
     *
     * @param sendVerificationCode 手机号集合
     * @return 操作结果
     */
    @PostMapping("/sendVerificationCode")
    public PlatformResult<Void> sendVerificationCode(@RequestBody SendVerificationCode sendVerificationCode) {
        for (String s : sendVerificationCode.getPhone()) {
            CodeReq codeReq = new CodeReq();
            codeReq.setPhone(s);
//            codeReq.setType(SmsEnum.Type.SUPPLIER_REFUND_ADDRESS.getCode());
            codeService.sendNotifyCode(codeReq);
        }
        return PlatformResult.success();
    }

    /**
     * 批量发送短信入参
     */
    @Data
    public static class SendVerificationCode {

        /**
         * 手机号集合
         */
        @NotEmpty
        private List<String> phone;
    }

    /**
     * 供应商注册获取短信验证码
     *
     * @param mobile 手机号
     * @return 发送结果
     */
    @PostMapping("/supplierRegisterCode/{mobile}")
    public PlatformResult<Object> supplierRegisterCode(@PathVariable String mobile) {
        PlatformResult<Object> checked = checkSendCount(mobile);
        if (!checked.isSuccess()) {
            return checked;
        }
        String code = RandomUtil.randomNumbers(CODE_LENGTH);
        RedisUtil.hSet(RedisEnum.Key.SMS_CODE_HASH.getCode(), mobile, code);
        CodeReq codeReq = new CodeReq();
        codeReq.setPhone(mobile);
        codeReq.setParams(Collections.singletonList(code));
        codeReq.setTemplateId(SmsEnum.Type.SUPPLIER_REGISTER_CODE.getTemplateId());
        boolean result = codeService.sendCodeLianLu(codeReq);
        alterSendCount(codeReq.getPhone(), (int[]) checked.getData());
        return result ? PlatformResult.success() : PlatformResult.fail("短信发送失败");
    }

    /**
     * 校验发送频次
     *
     * <p>{@code dev} 环境跳过限制, 但仍返回统一格式的计数, 保证调用方拿得到累加基数。</p>
     *
     * @param mobile 手机号
     * @return 成功时 data 为 {@code [分钟计数, 小时计数]}; 超限返回失败
     */
    private PlatformResult<Object> checkSendCount(String mobile) {
        int minuteCount = 0;
        int hourCount = 0;

        if (!"dev".equals(prefix)) {
            String minuteCountStr = RedisUtil.hGet(RedisEnum.Key.SMS_SEND_COUNT_MINUTE.getCode(), mobile);
            minuteCount = minuteCountStr != null ? Integer.parseInt(minuteCountStr) : 0;
            if (minuteCount >= maxMinuteCount) {
                return PlatformResult.fail("发送过于频繁，请稍后再试");
            }

            String hourCountStr = RedisUtil.hGet(RedisEnum.Key.SMS_SEND_COUNT_HOUR.getCode(), mobile);
            hourCount = hourCountStr != null ? Integer.parseInt(hourCountStr) : 0;
            if (hourCount >= maxHourCount) {
                return PlatformResult.fail("发送次数已达上限，请稍后再试");
            }
        }

        int[] counts = {minuteCount, hourCount};
        return PlatformResult.success(counts);
    }

    /**
     * 累加发送计数并刷新过期时间
     *
     * @param mobile 手机号
     * @param counts 发送前的 {@code [分钟计数, 小时计数]}
     */
    private void alterSendCount(String mobile, int[] counts) {
        RedisUtil.hSet(RedisEnum.Key.SMS_SEND_COUNT_MINUTE.getCode(), mobile, String.valueOf(counts[0] + 1));
        RedisUtil.hSet(RedisEnum.Key.SMS_SEND_COUNT_HOUR.getCode(), mobile, String.valueOf(counts[1] + 1));

        RedisUtil.expire(RedisEnum.Key.SMS_SEND_COUNT_MINUTE.getCode(), 60);
        RedisUtil.expire(RedisEnum.Key.SMS_SEND_COUNT_HOUR.getCode(), 3600);
    }
}
