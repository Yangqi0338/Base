package com.newzkl.platform.base.common.core.job.secondLevel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.job.dto.ExecuteLogDTO;
import com.newzkl.platform.base.common.core.job.req.ExecuteLogManualReq;
import com.newzkl.platform.base.common.core.job.req.ExecuteLogQuery;
import com.newzkl.platform.base.common.core.job.vo.ExecuteLogVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 执行日志 Domain
 * @ext 业务编排, 下沉到 common, 所有子系统可用
 */
public interface ExecuteLogDomain {

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页 VO
     */
    Page<ExecuteLogVO> page(ExecuteLogQuery query);

    /**
     * 根据 id 查 DTO
     *
     * @param id 主键
     * @return 执行日志 DTO
     */
    ExecuteLogDTO getById(Long id);

    /**
     * 拉到点的 PENDING 列表
     * @ext ScanJob 主索引
     *
     * @param today   当前日期
     * @param nowTime 当前时刻
     * @param limit   限量
     * @return PENDING 列表
     */
    List<ExecuteLogDTO> listPendingDue(LocalDate today, LocalTime nowTime, int limit);

    /**
     * 抢锁: PENDING → RUNNING
     *
     * @param id 主键
     * @return 是否获锁成功
     */
    boolean tryLockToRunning(Long id);

    /**
     * 标记 DONE
     *
     * @param id    主键
     * @param bizId 业务对象 id
     * @return 是否成功
     */
    boolean markDone(Long id, Long bizId);

    /**
     * 标记 FAILED
     *
     * @param id  主键
     * @param msg 失败原因
     * @return 是否成功
     */
    boolean markFailed(Long id, String msg);

    /**
     * 运营手动 SKIP
     * @ext 仅 PENDING 可 SKIP
     *
     * @param id         主键
     * @param msg        备注
     * @param operatorId 操作人 id
     * @return 是否成功
     */
    boolean skip(Long id, String msg, Long operatorId);

    /**
     * 运营手动重试
     * @ext FAILED/SKIP → PENDING
     *
     * @param id         主键
     * @param operatorId 操作人 id
     * @return 是否成功
     */
    boolean retry(Long id, Long operatorId);

    /**
     * 运营手动新增 PENDING
     *
     * @param req        新增入参
     * @param operatorId 操作人 id
     * @return 新增记录 id
     */
    Long insertManual(ExecuteLogManualReq req, Long operatorId);

    /**
     * 插入 PENDING 调度计划
     * @ext 业务侧用
     *
     * @param dto 执行日志 DTO
     * @return 新增记录 id
     */
    Long insertPending(ExecuteLogDTO dto);

    /**
     * 批量插入 PENDING
     * @ext 业务侧用
     *
     * @param dtos 执行日志 DTO 列表
     * @return 影响行数
     */
    int batchInsertPending(List<ExecuteLogDTO> dtos);

    /**
     * 是否存在 PENDING/RUNNING/DONE
     * @ext bizSourceCode, jobHandler, scheduledDate
     *
     * @param bizSourceCode 业务来源 code
     * @param jobHandler    handler 名
     * @param scheduledDate 计划日期
     * @return 是否存在
     */
    boolean existsByBizSourceJobDate(String bizSourceCode, String jobHandler, LocalDate scheduledDate);

    /**
     * 找指定 code+handler 的最早 PENDING
     *
     * @param bizSourceCode 业务来源 code
     * @param jobHandler    handler 名
     * @return 最早 PENDING, 无则 null
     */
    ExecuteLogDTO findEarliestPendingByCodeAndHandler(String bizSourceCode, String jobHandler);

    /**
     * 按 jobName 拉所有 PENDING
     * @ext reload 用
     *
     * @param jobName handler 名
     * @param limit   限量
     * @return PENDING 列表
     */
    List<ExecuteLogDTO> listPendingByJobName(String jobName, int limit);

    /**
     * 作废下所有 PENDING
     * @ext code, jobHandler
     *
     * @param code       业务来源 code
     * @param jobHandler handler 名
     * @param cancelMsg  作废原因
     */
    void cancelPendingByCodeAndJobHandler(String code, String jobHandler, String cancelMsg);

    /**
     * 在最早 PENDING 之前插一条 SKIP audit 节点
     * @ext code, jobHandler
     *
     * @param code       业务来源 code
     * @param jobHandler handler 名
     * @param auditMsg   审计备注
     */
    void insertSkipNodeBeforeEarliest(String code, String jobHandler, String auditMsg);

    /**
     * 统计当月 DONE 不同日期数
     * @ext code, jobHandler
     *
     * @param code       业务来源 code
     * @param jobHandler handler 名
     * @param ym         年月
     * @return 不同日期数
     */
    long countDoneDaysInMonth(String code, String jobHandler, java.time.YearMonth ym);

    /**
     * 列出指定日期范围 DONE 行
     * @ext code, jobHandler
     *
     * @param code       业务来源 code
     * @param jobHandler handler 名
     * @param from       起始日期
     * @param to         结束日期
     * @return DONE 列表
     */
    List<ExecuteLogDTO> listDoneInRange(String code, String jobHandler, LocalDate from, LocalDate to);

    /**
     * 拉 jobHandler 在 endTime 之前的全部 PENDING
     * <p>ids 非空 → 按 ids + PENDING 拉; ids 空 → 按 endTime + PENDING 拉
     * @ext reload 用, 全量不分页
     *
     * @param jobHandler handler 名
     * @param endTime    截止时刻
     * @param ids        指定 id 列表
     * @return PENDING 列表
     */
    List<ExecuteLogDTO> listPendingBefore(String jobHandler, LocalDateTime endTime, List<Long> ids);

    /**
     * 批量更新状态: msg 非空 → FAILED; msg 空 → DONE
     * @ext 用 bizId
     *
     * @param processed 已处理列表
     */
    void updateStatus(List<ExecuteLogDTO> processed);

    /**
     * fill 全量 diff: 业务期望列表 vs DB PENDING → 补缺 / 删多 / 改时间
     *
     * @param jobHandler 任务 handler
     * @param expected   业务侧期望存在的 ExecuteLog 列表(不含 id; 以 bizSourceCode 作 key 比对)
     */
    void fillByDiff(String jobHandler, List<ExecuteLogDTO> expected);

    /**
     * 通用 diff: 直接传 existing 列表, 不再查库
     * @ext 核心
     *
     * @param jobHandler 任务 handler
     * @param expected   业务期望列表
     * @param existing   当前库中 PENDING 列表(调用方自行查询)
     */
    void fillByDiff(String jobHandler, List<ExecuteLogDTO> expected, List<ExecuteLogDTO> existing);

    /**
     * 按 sourceCodes 范围拉 existing 后执行 diff
     *
     * @param jobHandler  任务 handler
     * @param expected    业务期望列表(仅含 sourceCodes 范围内的条目)
     * @param sourceCodes 业务 code 范围(IN 过滤 existing)
     */
    void fillByDiffByCodes(String jobHandler, List<ExecuteLogDTO> expected, List<String> sourceCodes);
}
