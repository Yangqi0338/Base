package com.newzkl.platform.base.common.core.job.secondLevel;

import com.newzkl.platform.base.common.core.job.XxlJobAdminClient;
import com.newzkl.platform.base.common.core.job.dto.ExecuteLogDTO;
import com.newzkl.platform.base.common.core.job.param.SecondLevelJobParam;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ExecuteLog 秒级扫描器 — 内存 map 主路径
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ScanJob {

    private final XxlJobAdminClient adminClient;
    private final ApplicationContext applicationContext;

    /*
     * 固定线程池 16: 限制单实例同时执行的 ExecuteLog dispatch 总并发
     * 避免无界虚拟线程在 due 项积压时打爆 DB 连接 + 触发跨 tx 行锁堆积
     */
    private final ExecutorService executor = Executors.newFixedThreadPool(16, r -> {
        Thread t = new Thread(r);
        t.setName("execute-log-scan-" + t.getId());
        t.setDaemon(true);
        return t;
    });

    /**
     * 秒级扫描入口: 遍历秒级 job, 到点项提交线程池 dispatch
     */
    @XxlJob("executeLogSecondScan")
    public void scan() {
        LocalDateTime now = LocalDateTime.now();
        for (String jobName : JobContext.secondLevelJobNames()) {
            if (!JobContext.containsKey(jobName)) {
                triggerReload(jobName);
                continue;
            }
            Map<Long, LocalDateTime> snap = JobContext.snapshot(jobName);
            for (Map.Entry<Long, LocalDateTime> e : snap.entrySet()) {
                LocalDateTime due = e.getValue();
                if (due == null) {
                    continue;
                }
                if (!due.isAfter(now)) {
                    Long id = e.getKey();
                    executor.submit(() -> dispatchById(id, jobName));
                }
            }
        }
    }

    /**
     * map 无 key — 走 XXL-Job admin 触发本 job 自身的 reload 路径
     */
    private void triggerReload(String jobName) {
        try {
            Integer xxlJobId = adminClient.findXxlJobIdByHandler(jobName);
            if (xxlJobId == null) {
                log.warn("[SecondScan] jobName={} xxlJobId not found, skip reload", jobName);
                return;
            }
            adminClient.trigger(xxlJobId, "");
            log.info("[SecondScan] reload triggered jobName={} xxlJobId={}", jobName, xxlJobId);
        } catch (Exception ex) {
            log.error("[SecondScan] reload failed jobName={}", jobName, ex);
        }
    }

    /**
     * 处理单条 ExecuteLog: BaseJob 子类直调 secondHandleProcess; 旧 job 走反射 + ThreadLocal
     */
    private void dispatchById(Long id, String jobName) {
        try {
            JobContext.JobMeta meta = JobContext.getMeta(jobName);
            if (meta == null || meta.getDeclaringClass() == null) {
                log.warn("[SecondScan] meta missing jobName={}", jobName);
                JobContext.unregister(jobName, id);
                return;
            }

            Object bean = applicationContext.getBean(meta.getDeclaringClass());

            if (bean instanceof BaseJob baseJob) {
                // BaseJob 子类: 直调 secondHandleProcess, jobBody 反射 raw target 进 job body 方法 (避开 JobAspect 重入)
                SecondLevelJobParam param = buildParam(meta, id);
                if (param == null) {
                    JobContext.unregister(jobName, id);
                    return;
                }
                Object rawTarget = AopProxyUtils.getSingletonTarget(bean);
                if (rawTarget == null) {
                    rawTarget = bean;
                }
                Object invokeTarget = rawTarget;
                baseJob.secondHandleProcess(meta, param, locked -> {
                    try {
                        Method m = meta.getMethod();
                        @SuppressWarnings("unchecked")
                        List<ExecuteLogDTO> result = (List<ExecuteLogDTO>) m.invoke(invokeTarget, meta, locked);
                        return result;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                // 旧签名 job: ThreadLocal 传 param, 反射经 AOP 路径
                if (meta.getParamClass() == null
                        || !SecondLevelJobParam.class.isAssignableFrom(meta.getParamClass())) {
                    log.warn("[SecondScan] paramClass not SecondLevelJobParam jobName={}", jobName);
                    JobContext.unregister(jobName, id);
                    return;
                }
                SecondLevelJobParam param = buildParam(meta, id);
                if (param == null) {
                    JobContext.unregister(jobName, id);
                    return;
                }
                SecondLevelJobThreadLocal.set(param);
                try {
                    Method method = meta.getMethod();
                    Class<?>[] paramTypes = method.getParameterTypes();
                    Object[] args = new Object[paramTypes.length];
                    for (int i = 0; i < paramTypes.length; i++) {
                        args[i] = JobContext.JobMeta.class.isAssignableFrom(paramTypes[i]) ? meta : param;
                    }
                    method.invoke(bean, args);
                } finally {
                    SecondLevelJobThreadLocal.clear();
                }
            }
        } catch (Exception ex) {
            log.error("[SecondScan] dispatchById failed id={} jobName={}", id, jobName, ex);
        } finally {
            JobContext.unregister(jobName, id);
        }
    }

    private SecondLevelJobParam buildParam(JobContext.JobMeta meta, Long id) {
        try {
            Class<?> paramClass = meta.getParamClass();
            // BaseJob 子类无 paramClass(resolveParamClass 跳过 List<ExecuteLogDTO>), 用 SecondLevelJobParam 兜底
            if (paramClass == null || !SecondLevelJobParam.class.isAssignableFrom(paramClass)) {
                SecondLevelJobParam p = new SecondLevelJobParam();
                p.setExecuteLogIds(List.of(id));
                return p;
            }
            SecondLevelJobParam param = (SecondLevelJobParam) paramClass.getDeclaredConstructor().newInstance();
            param.setExecuteLogIds(List.of(id));
            return param;
        } catch (Exception e) {
            log.error("[SecondScan] buildParam failed jobName={}", meta.getJobName(), e);
            return null;
        }
    }
}
