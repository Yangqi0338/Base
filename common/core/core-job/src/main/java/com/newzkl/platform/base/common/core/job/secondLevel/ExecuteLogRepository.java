package com.newzkl.platform.base.common.core.job.secondLevel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.ddd.model.dto.ExecuteLogDTO;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 执行日志仓储接口
 */
public interface ExecuteLogRepository {

    /**
     * 插入单条
     */
    Long insert(ExecuteLogDTO dto);

    /**
     * 批量保存
     * fillByDiff 主路径用：一次同时落 insert + update
     * @ext INSERT 或 UPDATE 自动判定，按 id 是否存在
     */
    int batchSave(List<ExecuteLogDTO> dtos);

    /**
     * 分页查询
     */
    Page<ExecuteLogDTO> page(ExecuteLogQuery query);

    /**
     * 按 id 查
     */
    ExecuteLogDTO getById(Long id);

    /**
     * 拉到点的 PENDING 列表
     * @ext ScanJob 主索引
     */
    List<ExecuteLogDTO> listPendingDue(LocalDate today, LocalTime nowTime, int limit);

    /**
     * 抢锁：PENDING → RUNNING
     * @ext 条件 UPDATE
     */
    boolean tryLockToRunning(Long id);

    /**
     * 标记 DONE
     */
    boolean markDone(Long id, Long bizId);

    /**
     * 标记 FAILED
     */
    boolean markFailed(Long id, String msg);

    /**
     * 标记 SKIP
     */
    boolean markSkip(Long id, String msg);

    /**
     * 改回 PENDING
     * @ext 运营手动重试
     */
    boolean retryToPending(Long id);

    /**
     * 检查  是否已存在
     * @ext bizSourceCode, jobHandler, scheduledDate
     */
    boolean existsByBizSourceJobDate(String bizSourceCode, String jobHandler, LocalDate scheduledDate);

    /**
     * 找指定 code+handler 的最早 PENDING
     * @ext 赠送插 SKIP 节点 / 退款留最早一条用
     */
    ExecuteLogDTO findEarliestPendingByCodeAndHandler(String bizSourceCode, String jobHandler);

    /**
     * 按 jobName 拉所有 PENDING
     * @ext 业务 Job reload 用，按 due 升序限量
     */
    List<ExecuteLogDTO> listPendingByJobName(String jobName, int limit);

    /**
     * 作废  下所有 PENDING:
     * 1. 留最早一条改为 SKIP
     * 2. 其余删除
     * 若无 PENDING 则 noop
     * @ext code, jobHandler
     * @ext msg=cancelMsg
     */
    void cancelPendingByCodeAndJobHandler(String code, String jobHandler, String cancelMsg);

    /**
     * 在  最早 PENDING 之前插一条 SKIP audit 节点
     * scheduledDate/scheduledTime = 最早 PENDING 时刻 - 1 秒
     * 若无 PENDING 则 noop
     * @ext code, jobHandler
     * @ext 但记 warn
     */
    void insertSkipNodeBeforeEarliest(String code, String jobHandler, String auditMsg);

    /**
     * 统计  当月 DONE 不同日期数
     * @ext code, jobHandler
     */
    long countDoneDaysInMonth(String code, String jobHandler, java.time.YearMonth ym);

    /**
     * 列出  指定日期范围 DONE 行
     * @ext code, jobHandler
     */
    List<ExecuteLogDTO> listDoneInRange(String code, String jobHandler,
                                        LocalDate from, LocalDate to);

    /**
     * 拉 jobHandler 在 endTime 之前的全部 PENDING
     * <p>ids 非空 → 按 ids + PENDING 拉；ids 空 → 按 endTime + PENDING 拉
     * @ext reload 用，全量不分页
     * @ext 忽略 endTime
     */
    List<ExecuteLogDTO> listPendingBefore(String jobHandler, LocalDateTime endTime, List<Long> ids);

    /**
     * 拉 jobHandler 全部活跃 PENDING
     * @ext fill diff 用，无时间限制
     */
    List<ExecuteLogDTO> listAllPendingByHandler(String jobHandler);

    /**
     * 按 id 更新 scheduledDate / scheduledTime
     * @ext fill diff 改时间用
     */
    boolean updateScheduledTime(Long id, LocalDate scheduledDate, LocalTime scheduledTime);

    /**
     * 按 id 删除 PENDING
     * @ext fill diff 删多余用
     */
    boolean deletePendingById(Long id);

    /**
     * 批量删除 PENDING
     * @ext 按 ids，仅 status=PENDING
     */
    int batchDeletePendingByIds(List<Long> ids);

    /**
     * 按 bizSourceCode 范围 + jobHandler 拉所有 PENDING
     * @ext fillByDiffByCodes 用
     */
    List<ExecuteLogDTO> listPendingByCodesAndHandler(String jobHandler, List<String> sourceCodes);

    /**
     * 检查  是否已存在任意 PENDING
     * @ext code, jobHandler
     */
    boolean existsAnyPendingByCode(String code, String jobHandler);
}
