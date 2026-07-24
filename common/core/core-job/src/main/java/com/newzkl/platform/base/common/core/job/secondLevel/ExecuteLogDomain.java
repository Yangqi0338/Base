package com.newzkl.platform.base.common.core.job.secondLevel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongze.chicken.common.biz.dto.ExecuteLogDTO;
import com.zhongze.chicken.common.biz.req.ExecuteLogManualReq;
import com.zhongze.chicken.common.biz.req.ExecuteLogQuery;
import com.zhongze.chicken.common.biz.res.ExecuteLogVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 执行日志 Domain
 * @ext 业务编排，下沉到 common，所有子系统可用
 */
public interface ExecuteLogDomain {

    /**
     * 分页查询
     */
    Page<ExecuteLogVO> page(ExecuteLogQuery query);

    /**
     * 根据 id 查 DTO
     */
    ExecuteLogDTO getById(Long id);

    /**
     * 拉到点的 PENDING 列表
     * @ext ScanJob 主索引
     */
    List<ExecuteLogDTO> listPendingDue(LocalDate today, LocalTime nowTime, int limit);

    /**
     * 抢锁：PENDING → RUNNING
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
     * 运营手动 SKIP
     * @ext 仅 PENDING 可 SKIP
     */
    boolean skip(Long id, String msg, Long operatorId);

    /**
     * 运营手动重试
     * @ext FAILED/SKIP → PENDING
     */
    boolean retry(Long id, Long operatorId);

    /**
     * 运营手动新增 PENDING
     */
    Long insertManual(ExecuteLogManualReq req, Long operatorId);

    /**
     * 插入 PENDING 调度计划
     * @ext 业务侧用
     */
    Long insertPending(ExecuteLogDTO dto);

    /**
     * 批量插入 PENDING
     * @ext 业务侧用
     */
    int batchInsertPending(List<ExecuteLogDTO> dtos);

    /**
     * 是否存在  PENDING/RUNNING/DONE
     * @ext bizSourceCode, jobHandler, scheduledDate
     */
    boolean existsByBizSourceJobDate(String bizSourceCode, String jobHandler, LocalDate scheduledDate);

    /**
     * 找指定 code+handler 的最早 PENDING
     */
    ExecuteLogDTO findEarliestPendingByCodeAndHandler(String bizSourceCode, String jobHandler);

    /**
     * 按 jobName 拉所有 PENDING
     * @ext reload 用
     */
    List<ExecuteLogDTO> listPendingByJobName(String jobName, int limit);

    /**
     * 作废  下所有 PENDING
     * @ext code, jobHandler
     */
    void cancelPendingByCodeAndJobHandler(String code, String jobHandler, String cancelMsg);

    /**
     * 在  最早 PENDING 之前插一条 SKIP audit 节点
     * @ext code, jobHandler
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
    List<ExecuteLogDTO> listDoneInRange(String code, String jobHandler, LocalDate from, LocalDate to);

    /**
     * 拉 jobHandler 在 endTime 之前的全部 PENDING
     * <p>ids 非空 → 按 ids + PENDING 拉；ids 空 → 按 endTime + PENDING 拉
     * @ext reload 用，全量不分页
     * @ext 忽略 endTime
     */
    List<ExecuteLogDTO> listPendingBefore(String jobHandler, LocalDateTime endTime, List<Long> ids);

    /**
     * 批量更新状态: msg 非空 → FAILED；msg 空 → DONE
     * @ext 用 bizId
     */
    void updateStatus(List<ExecuteLogDTO> processed);

    /**
     * fill 全量 diff: 业务期望列表 vs DB PENDING → 补缺 / 删多 / 改时间
     *
     * @param jobHandler 任务 handler
     * @param expected   业务侧期望存在的 ExecuteLog 列表（不含 id；以 bizSourceCode 作 key 比对）
     */
    void fillByDiff(String jobHandler, List<ExecuteLogDTO> expected);

    /**
     * 通用 diff: 直接传 existing 列表，不再查库
     * @ext 核心
     * @param jobHandler 任务 handler
     * @param expected   业务期望列表
     * @param existing   当前库中 PENDING 列表（调用方自行查询）
     */
    void fillByDiff(String jobHandler, List<ExecuteLogDTO> expected, List<ExecuteLogDTO> existing);

    /**
     * 按 sourceCodes 范围拉 existing 后执行 diff
     *
     * @param jobHandler  任务 handler
     * @param expected    业务期望列表（仅含 sourceCodes 范围内的条目）
     * @param sourceCodes 业务 code 范围（IN 过滤 existing）
     */
    void fillByDiffByCodes(String jobHandler, List<ExecuteLogDTO> expected, List<String> sourceCodes);
}
