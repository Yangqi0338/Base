package com.newzkl.platform.base.common.core.redis.aspect;


import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.spring.SpElParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁切面。
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final String prefix = "DistributedLock:";
    private final RedissonClient redissonClient;

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String key = distributedLock.key();
        String lockKey = SpElParseUtil.generateKeyBySpEL(key, joinPoint);
        if (StrUtil.isEmpty(lockKey)) {
            key = joinPoint.getSignature().getName();
        } else {
            key = lockKey;
        }
        String rKey = prefix + key;
        log.info("加锁:{}", rKey);
        RLock lock = redissonClient.getLock(rKey);
        try {
            if (lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TimeUnit.SECONDS)) {
                return joinPoint.proceed();
            } else {
                log.info("加锁失败:{}", rKey);
                throw new PlatformException(BaseErrorCode.SERVER, distributedLock.errorMsg());
            }
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
            log.info("释放锁:{}", rKey);
        }
    }
}
