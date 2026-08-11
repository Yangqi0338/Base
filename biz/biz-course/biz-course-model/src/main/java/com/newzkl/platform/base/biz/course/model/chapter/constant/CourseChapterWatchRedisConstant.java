package com.newzkl.platform.base.biz.course.model.chapter.constant;

/**
 * 课程章节观看统计 Redis 常量
 *
 * <p>观看事件写入缓冲队列, 由定时同步任务批量回写数据库(替代旧 MQ 异步刷新)。管理同步任务的锁配置与
 * 批量上限, 供观看记录领域服务与同步任务共用。缓冲队列 Key 与任务锁 Key 已收拢至 {@code RedisEnum}</p>
 *
 * @author KC
 */
public final class CourseChapterWatchRedisConstant {

    /** 定时同步任务锁等待时间 (秒): 获取不到直接放弃, 避免阻塞 */
    public static final int SYNC_TASK_LOCK_WAIT = 0;

    /** 定时同步任务锁过期时间 (秒): 足够单次批量同步执行 */
    public static final int SYNC_TASK_LOCK_EXPIRE = 60;

    /** 单次同步 drain 的事件上限 (防止单次任务处理过多导致长事务) */
    public static final int SYNC_BATCH_SIZE = 1000;

    private CourseChapterWatchRedisConstant() {
    }
}
