package com.newzkl.platform.base.common.core.redis.aspect;

import cn.hutool.core.text.StrJoiner;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.common.core.utils.spring.SpElParseUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 重复提交校验切面。
 *
 * @author 孔祥基
 */
@Aspect
@Component
@RequiredArgsConstructor
public class DuplicationCheckAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(duplicationCheck)")
    public Object logMethod(ProceedingJoinPoint joinPoint, DuplicationCheck duplicationCheck) throws Throwable {
        if (!duplicationCheck.enabled()) {
            return joinPoint.proceed();
        }
        try {
            String key = generateKey(joinPoint, duplicationCheck);
            RBucket<String> bucket = redissonClient.getBucket(key);
            if (bucket.isExists()) {
                throw new ScmException(BaseErrorCode.REPEAT.getCode(), duplicationCheck.message());
            }
            bucket.set(key, duplicationCheck.time(), TimeUnit.SECONDS);
            return joinPoint.proceed();
        } finally {
            if (duplicationCheck.time() > 3) {
                redissonClient.getBucket(generateKey(joinPoint, duplicationCheck));
            }
        }
    }

    private String generateKey(ProceedingJoinPoint joinPoint, DuplicationCheck duplicationCheck) {
        StrJoiner sb = new StrJoiner(":");
        sb.append(joinPoint.getTarget().getClass().getName());
        sb.append(joinPoint.getSignature().getName());

        if (duplicationCheck.type() > 1) {
            sb.append(SpElParseUtil.generateKeyBySpEL(duplicationCheck.value(), joinPoint));
        }
        return sb.toString();
    }

}
