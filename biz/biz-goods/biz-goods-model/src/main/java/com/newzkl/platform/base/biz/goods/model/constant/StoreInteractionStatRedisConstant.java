package com.newzkl.platform.base.biz.goods.model.constant;

/**
 * 互动统计模块 Redis 常量
 *
 * <p>管理互动统计的锁配置参数与 key 结构前缀(仅供 startsWith 校验), 供互动统计领域服务与
 * 定时同步任务共用。完整缓存 Key、锁 Key 已收拢至 {@code RedisEnum}。</p>
 *
 * @author sijiwang
 */
public final class StoreInteractionStatRedisConstant {

    /** 统计缓存 Key 前缀 (格式: store:stat:{targetType}:{targetId}), 仅用于 key 结构校验的 startsWith 判断; 完整 key 由 RedisEnum 管理 */
    public static final String REDIS_STAT_KEY_PREFIX = "store:stat:";

    /** 定时同步任务锁等待时间 (秒): 获取不到直接放弃, 避免阻塞。 */
    public static final int SYNC_TASK_LOCK_WAIT = 0;

    /** 定时同步任务锁过期时间 (秒): 足够单次批量同步执行。 */
    public static final int SYNC_TASK_LOCK_EXPIRE = 60;

    /** 普通统计锁等待时间 (秒): 消息消费时获取锁的最大等待时间。 */
    public static final int COMMON_LOCK_WAIT = 5;

    /** 普通统计锁过期时间 (秒): 消息消费时锁的自动释放时间。 */
    public static final int COMMON_LOCK_EXPIRE = 30;

    private StoreInteractionStatRedisConstant() {
    }
}
