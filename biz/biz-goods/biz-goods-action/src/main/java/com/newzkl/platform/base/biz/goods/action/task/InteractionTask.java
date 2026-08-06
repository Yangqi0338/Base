package com.newzkl.platform.base.biz.goods.action.task;

import com.newzkl.platform.base.biz.goods.domain.interaction.service.StoreTargetInteractionStatService;
import com.newzkl.platform.base.biz.goods.model.constant.StoreInteractionStatRedisConstant;
import com.newzkl.platform.base.common.core.redis.lock.impl.RedissonLockUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 互动统计缓存同步任务
 *
 * <p>迁移自 {@code com.zkl.scm.goods.application.task.InteractionTask}。
 * 定期将 Redis 缓存中的互动统计值同步到数据库，解决查询延迟问题。</p>
 *
 * @author sijiwang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InteractionTask {

    private final StoreTargetInteractionStatService statService;

    @XxlJob("interactionTask")
    public void syncCacheToDb() {
        log.info("互动统计缓存同步任务开始执行，时间：{}", new Date());
        boolean locked = false;
        RLock taskLock = null;
        try {
            locked = RedissonLockUtil.tryLock(
                    StoreInteractionStatRedisConstant.SYNC_TASK_LOCK_KEY,
                    TimeUnit.SECONDS,
                    StoreInteractionStatRedisConstant.SYNC_TASK_LOCK_WAIT,
                    StoreInteractionStatRedisConstant.SYNC_TASK_LOCK_EXPIRE);
            if (!locked) {
                log.info("未获取到分布式锁，任务提前结束");
                return;
            }
            statService.syncCacheToDb();

            taskLock = RedissonLockUtil.lock(StoreInteractionStatRedisConstant.SYNC_TASK_LOCK_KEY);

            log.info("互动统计缓存同步任务执行完成");
        } catch (Exception e) {
            log.error("互动统计缓存同步任务执行失败", e);
        } finally {
            if (locked && taskLock != null && taskLock.isHeldByCurrentThread()) {
                try {
                    RedissonLockUtil.unlock(taskLock);
                    log.info("分布式锁已释放");
                } catch (Exception e) {
                    log.error("释放分布式锁时发生异常", e);
                }
            }
        }
    }
}