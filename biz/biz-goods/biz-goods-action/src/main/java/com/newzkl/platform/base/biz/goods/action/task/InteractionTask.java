package com.newzkl.platform.base.biz.goods.action.task;

import com.newzkl.platform.base.biz.goods.domain.interaction.service.StoreTargetInteractionStatService;
import com.newzkl.platform.base.common.core.redis.aspect.DistributedLock;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 互动统计缓存同步任务
 *
 * <p>迁移自 {@code com.zkl.scm.goods.application.task.InteractionTask}。
 * 定期将 Redis 缓存中的互动统计值同步到数据库，解决查询延迟问题。</p>
 * <p>分布式锁经 {@link DistributedLock} 注解由切面统一控制, 避免多实例重复执行。</p>
 *
 * @author sijiwang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InteractionTask {

    private final StoreTargetInteractionStatService statService;

    @XxlJob("interactionTask")
    @DistributedLock(key = "'interactionStatSync'")
    public void syncCacheToDb() {
        log.info("互动统计缓存同步任务开始执行，时间：{}", new Date());
        statService.syncCacheToDb();
        log.info("互动统计缓存同步任务执行完成");
    }
}