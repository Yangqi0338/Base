package com.newzkl.platform.base.common.core.job.secondLevel;

import com.newzkl.platform.base.common.core.job.dto.ExecuteLogDTO;
import com.newzkl.platform.base.common.core.job.param.AssetJobParam;
import com.newzkl.platform.base.common.core.job.param.SecondLevelJobParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 反射本进程调用 BaseJob 子类, 走 secondHandleProcess 路径分发
 *
 * <p>callback 内反射 raw target 调用方法体, 避开 JobAspect 重入导致 ids 丢失
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JobInvoker {

    private final ApplicationContext applicationContext;

    /**
     * 反射本进程调用 BaseJob 子类, 走 secondHandleProcess 路径
     *
     * @param meta  job 元数据
     * @param param 执行参数(含路径标志位)
     * @return processed 列表(execute 路径), fill/reload 路径返回空列表
     */
    public List<ExecuteLogDTO> invoke(JobContext.JobMeta meta, SecondLevelJobParam param) {
        Object bean = applicationContext.getBean(meta.getDeclaringClass());
        if (!(bean instanceof BaseJob baseJob)) {
            throw new IllegalStateException("不是 BaseJob 子类: " + meta.getJobName());
        }
        // 取 raw target 避开 Spring AOP 代理 → callback m.invoke 不重入 JobAspect
        Object rawTarget = AopProxyUtils.getSingletonTarget(bean);
        if (rawTarget == null) {
            rawTarget = bean;
        }
        Object invokeTarget = rawTarget;
        return baseJob.secondHandleProcess(meta, param, locked -> {
            try {
                Method m = meta.getMethod();
                @SuppressWarnings("unchecked")
                List<ExecuteLogDTO> result = (List<ExecuteLogDTO>) m.invoke(invokeTarget, meta, locked);
                return result == null ? List.of() : result;
            } catch (Exception e) {
                throw new RuntimeException("JobInvoker 反射调用失败: " + meta.getJobName(), e);
            }
        });
    }

    /**
     * 构建 fill 参数并调用
     * @ext 内置 fillExecuteLog=true
     *
     * @param meta job 元数据
     */
    public void invokeFill(JobContext.JobMeta meta) {
        SecondLevelJobParam param = buildFillParam(meta);
        invoke(meta, param);
    }

    /**
     * 构建单条 ExecuteLog 执行参数并调用
     *
     * @param meta         job 元数据
     * @param executeLogId 要立即执行的 ExecuteLog id
     */
    public void invokeById(JobContext.JobMeta meta, Long executeLogId) {
        SecondLevelJobParam param = buildParamById(meta, executeLogId);
        invoke(meta, param);
    }

    private SecondLevelJobParam buildFillParam(JobContext.JobMeta meta) {
        AssetJobParam p = new AssetJobParam();
        p.setFillExecuteLog(true);
        return p;
    }

    private SecondLevelJobParam buildParamById(JobContext.JobMeta meta, Long id) {
        Class<?> paramClass = meta.getParamClass();
        try {
            if (paramClass == null || !SecondLevelJobParam.class.isAssignableFrom(paramClass)) {
                SecondLevelJobParam p = new SecondLevelJobParam();
                p.setExecuteLogIds(List.of(id));
                return p;
            }
            SecondLevelJobParam p = (SecondLevelJobParam) paramClass.getDeclaredConstructor().newInstance();
            p.setExecuteLogIds(List.of(id));
            return p;
        } catch (Exception e) {
            log.error("[JobInvoker] buildParamById 失败 jobName={}", meta.getJobName(), e);
            SecondLevelJobParam p = new SecondLevelJobParam();
            p.setExecuteLogIds(List.of(id));
            return p;
        }
    }
}
