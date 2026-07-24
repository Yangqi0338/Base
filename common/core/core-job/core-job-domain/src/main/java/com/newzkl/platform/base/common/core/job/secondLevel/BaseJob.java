package com.newzkl.platform.base.common.core.job.secondLevel;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.common.core.job.dto.ExecuteLogDTO;
import com.newzkl.platform.base.common.core.job.param.AssetJobParam;
import com.newzkl.platform.base.common.core.job.param.SecondLevelJobParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

/**
 * Job 抽象基类 — 统一处理 ExecuteLog 状态机
 * <p>子类继承后:
 * <ul>
 * <li>实现 {@link #fill} 按 jobName 路由到对应 collectActive*ExecuteLogs</li>
 * <li>{@code @XxlJob} 方法签名固定: {@code  → List<ExecuteLogDTO>}</li>
 * <li>方法体仅写纯业务 </li>
 * </ul>
 * <p>MQ clear 由 JobAspect 负责。
 */
@Slf4j
public abstract class BaseJob {

    @Autowired
    protected ExecuteLogDomain executeLogDomain;

    /**
     * 按 jobName 路由到对应业务 fill 方法, 返回期望存在的 ExecuteLog 列表
     *
     * @param jobName 任务 handler 名
     * @return 期望存在的 ExecuteLog 列表
     */
    public abstract List<ExecuteLogDTO> fill(String jobName);

    /**
     * 路径分发 — 由 JobAspect 调用
     * <p>路径 B : fillExecuteLog=true → 全量 diff, 返回空列表<br>
     * 路径 C : handleExecuteLog=true 且 ids 空 → 仅填 map<br>
     * 路径 A/D : lock + jobBody + updateStatus
     *
     * @param meta    job 元数据
     * @param param   SecondLevelJobParam 入参(含路径标志位)
     * @param jobBody 业务执行闭包 (locked → processed)
     * @return processed 列表 (A/D 路径) / 空列表 (B/C 路径)
     */
    public List<ExecuteLogDTO> secondHandleProcess(JobContext.JobMeta meta,
                                                   SecondLevelJobParam param,
                                                   Function<List<ExecuteLogDTO>, List<ExecuteLogDTO>> jobBody) {
        String jobName = meta.getJobName();

        // 路径 B: fill(MQ clear 由 JobAspect 发送)
        if (param instanceof AssetJobParam a && a.isFillExecuteLog()) {
            List<ExecuteLogDTO> expected = fill(jobName);
            executeLogDomain.fillByDiff(jobName, expected);
            log.info("[BaseJob.fill] jobName={} expected={}", jobName, expected.size());
            return List.of();
        }

        /*
         * ids 路径不依赖 cron — 手动 trigger 场景 cron 可能未配 / admin 不通,
         * 早退会让 ids 永远跑不起来。仅 fill/reload 路径用 endTime 过滤范围。
         */
        boolean idsPresent = !CollUtil.isEmpty(param.getExecuteLogIds());
        LocalDateTime endTime = null;
        if (!idsPresent) {
            endTime = JobContext.nextTriggerTimes(jobName, 1).stream().findFirst().orElse(null);
            if (endTime == null) {
                log.warn("[BaseJob] cron 无下次触发时间 jobName={}", jobName);
                return List.of();
            }
        }

        List<ExecuteLogDTO> pending = executeLogDomain.listPendingBefore(jobName, endTime, param.getExecuteLogIds());

        // 路径 C: reload(handleExecuteLog=true 且 ids 空 → 仅填 map)
        boolean reloadPath = param.isHandleExecuteLog() && CollUtil.isEmpty(param.getExecuteLogIds());
        if (reloadPath) {
            /*
             * pending 空 → 当前到下次 cron 触发间无待执行记录, 仍需 touch 占位
             * 否则 ScanJob.containsKey 返 false 视为宕机, 重发 reload
             */
            JobContext.touch(jobName);
            for (ExecuteLogDTO e : pending) {
                JobContext.register(jobName, e.getId(), e.dueDateTime());
            }
            log.info("[BaseJob.reload] jobName={} registered={}", jobName, pending.size());
            return List.of();
        }

        if (pending.isEmpty()) {
            return List.of();
        }

        // 路径 A/D: lock + body + updateStatus
        List<ExecuteLogDTO> locked = pending.stream()
                .filter(e -> executeLogDomain.tryLockToRunning(e.getId()))
                .toList();
        if (locked.isEmpty()) {
            return List.of();
        }

        List<ExecuteLogDTO> processed = jobBody.apply(locked);
        if (processed != null && !processed.isEmpty()) {
            executeLogDomain.updateStatus(processed);
        }
        return processed == null ? List.of() : processed;
    }
}
