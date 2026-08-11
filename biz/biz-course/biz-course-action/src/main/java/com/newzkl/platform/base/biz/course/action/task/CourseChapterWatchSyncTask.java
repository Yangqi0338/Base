package com.newzkl.platform.base.biz.course.action.task;

import com.newzkl.platform.base.biz.course.domain.service.CourseChapterWatchRecordDomain;
import com.newzkl.platform.base.common.core.redis.aspect.DistributedLock;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 课程章节观看统计同步任务
 *
 * <p>定期将 Redis 缓冲队列中的章节观看事件批量回写数据库(替代旧 MQ 异步刷新), 解决查询高频写库压力。
 * 多实例部署时以分布式锁保证单实例执行</p>
 *
 * @author KC
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CourseChapterWatchSyncTask {

    private final CourseChapterWatchRecordDomain courseChapterWatchRecordDomain;

    @XxlJob("courseChapterWatchSync")
    @DistributedLock(key = "'courseChapterWatchSync'")
    public void syncWatchBucket() {
        log.info("章节观看统计同步任务开始执行, 时间: {}", new Date());
        courseChapterWatchRecordDomain.syncBucketToDb();
        log.info("章节观看统计同步任务执行完成");
    }
}