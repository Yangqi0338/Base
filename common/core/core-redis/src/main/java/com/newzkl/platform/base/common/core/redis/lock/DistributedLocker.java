package com.newzkl.platform.base.common.core.redis.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口
 *
 * <p>提供基于 Redisson 的分布式锁的加锁、解锁和尝试获取锁等操作。</p>
 */
public interface DistributedLocker {

    /**
     * 加锁（阻塞，无超时时间）
     *
     * @param lockKey 锁Key
     * @return RLock 锁对象
     */
    RLock lock(String lockKey);

    /**
     * 加锁并设置自动过期时间（单位：秒）
     *
     * @param lockKey 锁Key
     * @param timeout 锁自动释放时间（秒）
     * @return RLock 锁对象
     */
    RLock lock(String lockKey, int timeout);

    /**
     * 加锁并设置自动过期时间（指定时间单位）
     *
     * @param lockKey 锁Key
     * @param unit    时间单位
     * @param timeout 锁自动释放时间
     * @return RLock 锁对象
     */
    RLock lock(String lockKey, TimeUnit unit, int timeout);

    /**
     * 尝试获取锁（非阻塞）
     *
     * @param lockKey   锁Key
     * @param unit      时间单位
     * @param waitTime  最大等待时间
     * @param leaseTime 锁自动释放时间
     * @return true-获取成功，false-超时未获取到
     */
    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    /**
     * 根据锁Key释放锁
     *
     * @param lockKey 锁Key
     */
    void unlock(String lockKey);

    /**
     * 直接释放 RLock 对象
     *
     * @param lock RLock 锁对象
     */
    void unlock(RLock lock);

    /**
     * 注入 RedissonClient 实例
     *
     * @param redissonClient Redisson客户端
     */
    void setRedissonClient(RedissonClient redissonClient);
}
