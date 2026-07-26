package com.newzkl.platform.base.common.core.job.secondLevel;

import cn.hutool.json.JSONUtil;
import com.newzkl.platform.base.common.core.job.dto.ExecuteLogDTO;
import com.newzkl.platform.base.common.core.job.param.AssetJobParam;
import com.newzkl.platform.base.common.core.job.param.SecondLevelJobParam;
import com.newzkl.platform.base.common.core.mq.constant.MQ;
import com.newzkl.platform.base.common.core.mq.message.JobContextClearMessage;
import com.newzkl.platform.base.common.core.mq.utils.MQUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * XXL-Job 切面 — 单层处理 参数解析 + BaseJob 路径分发 + ReturnT 包装
 * <p>所有 @XxlJob 方法约定继承 BaseJob。两类签名:
 * <ul>
 * <li>{@code  → List<ExecuteLogDTO>} — 走 secondHandleProcess 分发</li>
 * <li>其他签名 — 直接 proceed </li>
 * </ul>
 * <p>切点用 {@code execution} + 反射取注解, 不用 binding, 避免 pjp.proceed 嵌套时
 * JoinPointMatch 丢失。
 */
@Slf4j
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecondLevelJobAspect {

    /**
     * @XxlJob 环绕: 解析参数 → BaseJob 分发 / 旧签名兼容 → ReturnT 包装
     *
     * @param pjp 连接点
     * @return 执行结果
     */
    @Around("execution(@com.xxl.job.core.handler.annotation.XxlJob * *(..))")
    public Object around(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        XxlJob xxlJob = method.getAnnotation(XxlJob.class);

        JobContext.JobMeta meta = JobContext.getMeta(xxlJob.value());
        if (meta == null) {
            log.warn("[JobAspect] meta 缺失 jobName={}", xxlJob.value());
            return safeProceed(pjp, pjp.getArgs(), method);
        }

        Object target = pjp.getTarget();
        if (target instanceof BaseJob baseJob && isSecondLevelSignature(meta)) {
            return dispatchSecondLevel(pjp, baseJob, meta, method);
        }

        // 旧签名兼容路径
        Object[] newArgs = buildLegacyArgs(meta, pjp.getArgs());
        return safeProceed(pjp, newArgs, method);
    }

    /**
     * BaseJob + 签名分发
     */
    private Object dispatchSecondLevel(ProceedingJoinPoint pjp, BaseJob baseJob,
                                       JobContext.JobMeta meta, Method method) {
        SecondLevelJobParam param = resolveSecondLevelParam(meta, pjp.getArgs());
        if (param == null) {
            log.warn("[JobAspect] secondLevel param 解析失败 jobName={}", meta.getJobName());
            return defaultFailReturn(method);
        }
        try {
            List<ExecuteLogDTO> result = baseJob.secondHandleProcess(meta, param, locked -> {
                try {
                    Object body = pjp.proceed(new Object[]{meta, locked});
                    if (body instanceof List<?> list) {
                        @SuppressWarnings("unchecked")
                        List<ExecuteLogDTO> typed = (List<ExecuteLogDTO>) list;
                        return typed;
                    }
                    return locked;
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            });
            if (param instanceof AssetJobParam a && a.isFillExecuteLog()) {
                // fill 路径末: 发 JOB_CONTEXT_CLEAR 通知各实例 ScanJob reload
                JobContextClearMessage clearMsg = new JobContextClearMessage();
                clearMsg.setJobName(meta.getJobName());
                MQUtil.send(MQ.Tag.JOB_CONTEXT_CLEAR, clearMsg);
                log.info("[JobAspect] fill 路径完成 + JOB_CONTEXT_CLEAR jobName={}", meta.getJobName());
            }
            // method 实际声明返回 List<ExecuteLogDTO>; 若 admin 调度需 ReturnT 由 XXL-Job 框架兜底为 SUCCESS
            return ReturnT.class.isAssignableFrom(method.getReturnType()) ? ReturnT.SUCCESS : result;
        } catch (Throwable e) {
            log.error("Job [{}] 异常", meta.getJobName(), e);
            return defaultFailReturn(method);
        }
    }

    private boolean isSecondLevelSignature(JobContext.JobMeta meta) {
        Class<?>[] pts = meta.getMethod().getParameterTypes();
        return pts.length == 2
                && JobContext.JobMeta.class.isAssignableFrom(pts[0])
                && List.class.isAssignableFrom(pts[1]);
    }

    /**
     * 解析 SecondLevelJobParam: ThreadLocal → origArgs 已含实例 → XxlJob raw → 默认实例
     */
    private SecondLevelJobParam resolveSecondLevelParam(JobContext.JobMeta meta, Object[] origArgs) {
        SecondLevelJobParam tlParam = SecondLevelJobThreadLocal.get();
        if (tlParam != null) {
            return tlParam;
        }
        if (origArgs != null) {
            for (Object a : origArgs) {
                if (a instanceof SecondLevelJobParam s) {
                    return s;
                }
            }
        }
        Class<?> paramClass = resolveDefaultParamClass(meta);
        String raw = XxlJobHelper.getJobParam();
        Object obj;
        if (raw == null || raw.isBlank()) {
            try {
                obj = paramClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                log.error("[JobAspect] 无法创建默认 param paramClass={}", paramClass.getName(), e);
                return null;
            }
        } else {
            obj = JSONUtil.toBean(raw, paramClass);
        }
        if (obj instanceof SecondLevelJobParam s) {
            s.setHandleExecuteLog(meta.isSecondLevel());
            return s;
        }
        return null;
    }

    private Class<?> resolveDefaultParamClass(JobContext.JobMeta meta) {
        if (meta.getBizType() != null && meta.getBizType().getSecondHandle()) {
            return AssetJobParam.class;
        }
        return SecondLevelJobParam.class;
    }

    /**
     * 旧签名 / 非 BaseJob 路径的参数解析: JobMeta 位 → meta; 其他 → origArgs 或 raw 解析
     */
    private Object[] buildLegacyArgs(JobContext.JobMeta meta, Object[] origArgs) {
        Method m = meta.getMethod();
        Class<?>[] paramTypes = m.getParameterTypes();
        Object[] newArgs = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> pc = paramTypes[i];
            if (JobContext.JobMeta.class.isAssignableFrom(pc)) {
                newArgs[i] = meta;
            } else if (List.class.isAssignableFrom(pc)) {
                newArgs[i] = (origArgs != null && origArgs.length > i) ? origArgs[i] : null;
            } else {
                Object obj;
                if (origArgs != null && i < origArgs.length && origArgs[i] != null && pc.isInstance(origArgs[i])) {
                    obj = origArgs[i];
                } else if (meta.getParamClass() != null && pc.isAssignableFrom(meta.getParamClass())) {
                    String raw = XxlJobHelper.getJobParam();
                    if (raw == null || raw.isBlank()) {
                        obj = meta.defaultSupplier().get();
                    } else {
                        obj = JSONUtil.toBean(raw, pc);
                    }
                } else {
                    obj = null;
                }
                if (obj instanceof SecondLevelJobParam s) {
                    s.setHandleExecuteLog(meta.isSecondLevel());
                }
                newArgs[i] = obj;
            }
        }
        return newArgs;
    }

    private Object safeProceed(ProceedingJoinPoint pjp, Object[] args, Method method) {
        boolean returnsReturnT = ReturnT.class.isAssignableFrom(method.getReturnType());
        try {
            Object result = pjp.proceed(args);
            if (returnsReturnT) {
                return result instanceof ReturnT ? result : ReturnT.SUCCESS;
            }
            return result;
        } catch (Throwable e) {
            log.error("Job 异常", e);
            return returnsReturnT ? ReturnT.FAIL : null;
        }
    }

    private Object defaultFailReturn(Method method) {
        return ReturnT.class.isAssignableFrom(method.getReturnType()) ? ReturnT.FAIL : null;
    }
}
