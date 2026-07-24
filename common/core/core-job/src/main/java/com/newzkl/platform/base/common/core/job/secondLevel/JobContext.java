package com.newzkl.platform.base.common.core.job.secondLevel;

import cn.hutool.cron.pattern.CronPattern;
import cn.hutool.cron.pattern.CronPatternUtil;
import com.newzkl.platform.base.common.core.job.XxlJobAdminClient;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zhongze.chicken.common.biz.dto.ExecuteLogDTO;
import com.zhongze.chicken.common.enums.admin.ExecuteLogEnum;
import com.zhongze.chicken.common.schedule.SecondLevelJob;
import com.zhongze.chicken.common.schedule.model.XxlJobInfoResp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * 项目维度的任务上下文
 */
@Slf4j
@Component
public class JobContext {

    /**
     * 秒级触发映射
     */
    private static final ConcurrentMap<String, ConcurrentMap<Long, LocalDateTime>> registry = new ConcurrentHashMap<>();

    /**
     * jobName 到元数据的映射
     */
    private static final ConcurrentMap<String, JobMeta> metaMap = new ConcurrentHashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private XxlJobAdminClient adminClient;

    @EventListener(ApplicationReadyEvent.class)
    public void scan() {
        /*
         * 启动扫描所有 @XxlJob 方法，结合 XXL-Job admin pageJobs 合并 cron / xxlJobId / description 等元数据
         */
        scanLocalHandlers();
        try {
            mergeAdminInfo();
        } catch (Exception e) {
            log.warn("JobContext: 拉取 XXL-Job admin pageJobs 失败，元数据 cron/xxlJobId 缺失", e);
        }
    }

    private void scanLocalHandlers() {
        Map<String, Object> beans = applicationContext.getBeansOfType(Object.class);
        for (Object bean : beans.values()) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            for (Method m : targetClass.getDeclaredMethods()) {
                XxlJob job = AnnotationUtils.findAnnotation(m, XxlJob.class);
                if (job == null) {
                    continue;
                }
                String jobName = job.value();
                Class<?> paramClass = resolveParamClass(m);
                SecondLevelJob secondLevel = AnnotationUtils.findAnnotation(m, SecondLevelJob.class);
                if (secondLevel != null) {
                    assertSecondLevelReturnType(m);
                }
                ExecuteLogEnum.BizType bizType = secondLevel != null ? secondLevel.bizType() : null;
                boolean secondHandle = secondLevel != null && secondLevel.bizType().getSecondHandle();
                metaMap.put(jobName, new JobMeta(jobName, paramClass, bizType, secondHandle, m, targetClass, null, null, null));
                log.info("[JobContext] scan jobName={} paramClass={} bizType={} secondLevel={}",
                        jobName, paramClass == null ? null : paramClass.getSimpleName(), bizType, secondHandle);
            }
        }
    }

    private static Class<?> resolveParamClass(Method m) {
        /*
         * 支持签名：(T)、(JobMeta, T)、(JobMeta)、(JobMeta, List<ExecuteLogDTO>) — 返回第一个非 JobMeta / 非 List 的参数类型
         * BaseJob 子类签名 (JobMeta, List<ExecuteLogDTO>) → null（List 占位，非真实 param）
         */
        Class<?>[] pts = m.getParameterTypes();
        for (Class<?> pt : pts) {
            if (!JobMeta.class.isAssignableFrom(pt) && pt != String.class && !List.class.isAssignableFrom(pt)) {
                return pt;
            }
        }
        return null;
    }

    private static void assertSecondLevelReturnType(Method m) {
        /*
         * @SecondLevelJob 方法必须返回 List<ExecuteLogDTO>
         * SecondLevelJobAspect 拿返回值走 updateStatus；类型不符直接 boot 失败
         */
        Type generic = m.getGenericReturnType();
        boolean ok = false;
        if (generic instanceof ParameterizedType pt
                && pt.getRawType() instanceof Class<?> raw
                && List.class.isAssignableFrom(raw)) {
            Type[] args = pt.getActualTypeArguments();
            if (args.length == 1 && args[0] instanceof Class<?> elem
                    && ExecuteLogDTO.class.isAssignableFrom(elem)) {
                ok = true;
            }
        }
        if (!ok) {
            throw new IllegalStateException("@SecondLevelJob 方法返回类型必须 List<ExecuteLogDTO>，当前 "
                    + m.getDeclaringClass().getSimpleName() + "#" + m.getName()
                    + " 返回 " + generic.getTypeName());
        }
    }

    private void mergeAdminInfo() {
        for (Integer gid : adminClient.getGroupIds()) {
            XxlJobInfoResp resp = adminClient.pageJobs(gid, -1, 0, 1000);
            if (resp == null || resp.getData() == null) continue;
            for (XxlJobInfoResp.Item item : resp.getData()) {
                String handler = item.getExecutorHandler();
                if (handler == null) continue;
                JobMeta meta = metaMap.get(handler);
                if (meta == null) continue;
                meta.xxlJobId = item.getId();
                meta.cron = item.getScheduleConf();
                meta.description = item.getJobDesc();
            }
        }
    }

    /**
     * 取 jobName 元数据
     */
    public static JobMeta getMeta(String jobName) {
        return metaMap.get(jobName);
    }

    /**
     * 全部已注册 jobName 集合
     */
    public static Set<String> registeredJobNames() {
        return Collections.unmodifiableSet(metaMap.keySet());
    }

    /**
     * 秒级 Job 已注册 jobName 集合
     */
    public static Set<String> secondLevelJobNames() {
        Set<String> set = new HashSet<>();
        for (Map.Entry<String, JobMeta> e : metaMap.entrySet()) {
            if (e.getValue().secondLevel) set.add(e.getKey());
        }
        return set;
    }

    /**
     * 计算 jobName 接下来 N 次触发时间
     */
    public static List<LocalDateTime> nextTriggerTimes(String jobName, int count) {
        JobMeta meta = metaMap.get(jobName);
        if (meta == null || meta.cron == null || meta.cron.isBlank()) {
            return List.of();
        }
        return nextTriggerTimesByCron(meta.cron, count);
    }

    /**
     * 基于 cron 表达式计算 N 次触发时间
     */
    public static List<LocalDateTime> nextTriggerTimesByCron(String cron, int count) {
        if (cron == null || cron.isBlank() || count <= 0) return List.of();
        CronPattern pattern = CronPattern.of(cron);
        List<LocalDateTime> list = new ArrayList<>(count);
        Date cursor = new Date();
        for (int i = 0; i < count; i++) {
            Date next = CronPatternUtil.nextDateAfter(pattern, cursor, true);
            if (next == null) break;
            list.add(LocalDateTime.ofInstant(next.toInstant(), ZoneId.systemDefault()));
            cursor = new Date(next.getTime() + 1000);
        }
        return list;
    }

    /**
     * 注册一条待触发任务
     */
    public static void register(String jobName, Long id, LocalDateTime dueTime) {
        registry.computeIfAbsent(jobName, k -> new ConcurrentHashMap<>()).put(id, dueTime);
    }

    /**
     * 占位空 bucket — 当前到下次 cron 触发间无待执行记录
     *
     * <p>避免 reload 时 pending 为空导致 registry 缺 key, 被 ScanJob 误判为宕机重新触发
     */
    public static void touch(String jobName) {
        registry.computeIfAbsent(jobName, k -> new ConcurrentHashMap<>());
    }

    /**
     * 移除一条已执行任务
     */
    public static void unregister(String jobName, Long id) {
        ConcurrentMap<Long, LocalDateTime> bucket = registry.get(jobName);
        if (bucket != null) {
            bucket.remove(id);
        }
    }

    /**
     * 清空指定 jobName 的全部任务
     */
    public static void clear(String jobName) {
        /*
         * MQ 清 key 用，触发 reload
         */
        registry.remove(jobName);
    }

    /**
     * 判断 jobName 是否已注册
     */
    public static boolean containsKey(String jobName) {
        /*
         * 返回 false 时 ScanJob 触发 reload
         */
        return registry.containsKey(jobName);
    }

    /**
     * 拍取 jobName 当前任务快照
     */
    public static Map<Long, LocalDateTime> snapshot(String jobName) {
        /*
         * 返回不可变副本，避免迭代时被并发修改
         */
        ConcurrentMap<Long, LocalDateTime> bucket = registry.get(jobName);
        return bucket == null ? Collections.emptyMap() : Map.copyOf(bucket);
    }

    /**
     * Job 元数据
     */
    @AllArgsConstructor
    @Getter
    public static class JobMeta {
        private final String jobName;
        private final Class<?> paramClass;
        private final ExecuteLogEnum.BizType bizType;
        private final boolean secondLevel;
        private final Method method;
        private final Class<?> declaringClass;
        private volatile Integer xxlJobId;
        private volatile String cron;
        private volatile String description;

        /**
         * 取入参默认实例供应器
         */
        public Supplier<Object> defaultSupplier() {
            return () -> {
                if (paramClass == null) return null;
                try {
                    return paramClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException("JobMeta: " + paramClass.getName() + " 缺少无参构造", e);
                }
            };
        }
    }
}
