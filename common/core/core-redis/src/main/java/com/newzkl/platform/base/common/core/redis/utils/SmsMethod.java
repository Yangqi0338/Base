package com.newzkl.platform.base.common.core.redis.utils;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.RandomUtil;
import com.newzkl.platform.base.common.core.model.enums.SmsEnum;
import com.newzkl.platform.base.common.core.sms.CodeReq;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;

import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.common.core.redis.model.req.VerificationCodeReq;
import com.newzkl.platform.base.common.core.sms.SmsConfig;
import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import com.newzkl.platform.base.common.ddd.model.properties.UserProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder.isProd;


/**
 * 带持久化的短信工具类
 */
@Component("smsRedisMethod")
@Slf4j
public class SmsMethod extends com.newzkl.platform.base.common.core.sms.SmsMethod {

    /**
     * 发送验证短信
     * @ext 随机生成验证码，存入Redis，并更新发送计数
     * @param codeReq 短信发送请求（含手机号、短信类型等）
     * @return 发送结果
     */
    public static PlatformResult<Object> sendNotifyCode(CodeReq codeReq) {
        String phone = codeReq.getPhone();
        // 检查是否超过发送限制
        PlatformResult<Integer[]> checked = checkSendCount(phone);
        if (!checked.isSuccess()) {
            return PlatformResult.fail(checked.getMessage());
        }
        String code = RandomUtil.randomNumbers(6);
        codeReq.setParams(Collections.singletonList(code));
        // 发送验证码
        boolean sendState = sendCode(codeReq);
        if (sendState) {
            SmsEnum.Type type = codeReq.getType();
            if (checkCode(type, "")) {
                // 存入Redis
                RedisUtil.hSet(RedisEnum.Key.SMS.getCode(type), phone, code);
            }
        } else {
            return PlatformResult.fail("短信发送失败");
        }
        // 发送限制递增
        alterSendCount(phone, checked.getData());
        return PlatformResult.success();
    }

    public static boolean checkCode(SmsEnum.Type type, String code){
        // 消息类型为验证码且未通过全局通行码校验
        return type.getSignType() == 1 && !UserProperties.isPassSmsCode(code);
    }

    /**
     * 校验短信验证码
     * @ext 从Redis读取后与请求中的验证码比对
     * @param req 验证码校验请求（含手机号、验证码、类型）
     * @return 验证码匹配返回 true
     */
    public static boolean verificationCode(VerificationCodeReq req) {
        if (checkCode(req.getType(), req.getCode())) {
            if (!PatternUtil.isChinaPhone(req.getPhone())) {
                return false;
            }
            String sendCode = RedisUtil.hGet(RedisEnum.Key.SMS.getCode(req.getType()), req.getPhone());
            return req.getCode().equals(sendCode);
        }
        return true;
    }

    /**
     * 检查手机号在当前时间窗口内的短信发送次数是否超限
     *
     * @param mobile 手机号
     * @return 成功时 data 包含 [分钟计数, 小时计数]；超限时返回失败
     */
    public static PlatformResult<Integer[]> checkSendCount(String mobile) {
        // 初始化计数，dev环境默认0（跳过限制），非dev环境从Redis读取
        Integer minuteCount = 0;
        Integer hourCount = 0;

        // 非dev环境才执行次数限制检查
        if (isProd()) {
            // 检查分钟限制（1分钟1条）
            minuteCount = RedisUtil.hGet(RedisEnum.Key.SMS_SEND_COUNT_MINUTE.getCode(), mobile);
            minuteCount = Opt.ofNullable(minuteCount).orElse(0);
            if (minuteCount >= SmsConfig.codeMinuteLimit) {
                return PlatformResult.fail("发送过于频繁，请稍后再试");
            }

            // 检查小时限制（1小时5条）
            hourCount = RedisUtil.hGet(RedisEnum.Key.SMS_SEND_COUNT_HOUR.getCode(), mobile);
            hourCount = Opt.ofNullable(hourCount).orElse(0);
            if (hourCount >= SmsConfig.codeHourLimit) {
                return PlatformResult.fail("发送次数已达上限，请稍后再试");
            }
        }

        // 无论是否dev环境，都构造计数数组并返回，保证后续逻辑能拿到统一格式的结果
        Integer[] counts = {minuteCount, hourCount};
        return PlatformResult.success(counts);
    }

    /**
     * 发送成功后递增手机号的分钟和小时发送计数，并刷新过期时间
     *
     * @param mobile 手机号
     * @param counts 当前计数数组 [分钟计数, 小时计数]
     */
    public static void alterSendCount(String mobile, Integer[] counts) {
        // 使用统一的Hash key管理所有手机号的限制信息
        String minuteHashKey = RedisEnum.Key.SMS_SEND_COUNT_MINUTE.getCode();
        String hourHashKey = RedisEnum.Key.SMS_SEND_COUNT_HOUR.getCode();

        // 检查分钟限制（1分钟1条）
        RedisUtil.hSet(minuteHashKey, mobile, counts[0] + 1);
        RedisUtil.hSet(hourHashKey, mobile, counts[1] + 1);

        // 设置过期时间
        RedisUtil.expire(minuteHashKey, 120); // 2分钟过期
        RedisUtil.expire(hourHashKey, 7200);  // 2小时过期
    }
}
