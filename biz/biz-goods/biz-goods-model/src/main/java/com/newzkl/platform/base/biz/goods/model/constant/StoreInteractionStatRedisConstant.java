package com.newzkl.platform.base.biz.goods.model.constant;

/**
 * 互动统计模块 Redis 常量
 *
 * <p>统一管理互动统计缓存 Key、分布式锁 Key 及锁配置参数, 供互动统计领域服务与
 * MQ 消费者、定时同步任务共用。</p>
 *
 * @author sijiwang
 */
public final class StoreInteractionStatRedisConstant {

    /** 统计缓存 Key 前缀 (格式: store:stat:{targetType}:{targetId})。 */
    public static final String REDIS_STAT_KEY_PREFIX = "store:stat:";

    /** 缓存同步数据库的 Key 集合 (Set 类型, 存储需同步的缓存 Key)。 */
    public static final String SYNC_CACHE_KEY_SET = "store:stat:sync:keys";

    /** 普通统计锁 Key 前缀 (格式: store:stat:lock:{targetType}:{targetId})。 */
    public static final String REDIS_LOCK_KEY_PREFIX = "store:stat:lock:";

    /** 定时同步任务专属锁 Key (防止多实例重复执行同步任务)。 */
    public static final String SYNC_TASK_LOCK_KEY = "store:stat:sync:task:lock";

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
