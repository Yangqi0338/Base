package com.newzkl.platform.base.common.core.job.domain.secondLevel;

import com.newzkl.platform.base.common.core.job.model.param.SecondLevelJobParam;

/**
 * ScanJob.dispatchById → SecondLevelJobAspect 的参数传递通道
 *
 * <p>ScanJob 通过反射调 job 方法时, 将 SecondLevelJobParam 设入此 ThreadLocal,
 * 避免 AspectJ aroundAdvice 从 XxlJobHelper.getJobParam() 拿到空 raw 导致参数丢失。
 */
public final class SecondLevelJobThreadLocal {

    private static final ThreadLocal<SecondLevelJobParam> PARAM = new ThreadLocal<>();

    private SecondLevelJobThreadLocal() {
    }

    /**
     * 设入参数
     * @ext 反射调用前
     *
     * @param param 秒级任务参数
     */
    public static void set(SecondLevelJobParam param) {
        PARAM.set(param);
    }

    /**
     * 取参数
     *
     * @return 当前线程绑定的秒级任务参数
     */
    public static SecondLevelJobParam get() {
        return PARAM.get();
    }

    /**
     * 清除
     * @ext 反射调用 finally 块
     */
    public static void clear() {
        PARAM.remove();
    }
}
