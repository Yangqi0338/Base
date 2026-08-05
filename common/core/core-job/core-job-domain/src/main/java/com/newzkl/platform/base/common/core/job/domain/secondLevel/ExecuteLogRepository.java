package com.newzkl.platform.base.common.core.job.domain.secondLevel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.job.model.dto.ExecuteLogDTO;
import com.newzkl.platform.base.common.core.job.model.req.ExecuteLogQuery;

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
     *
     * @param dto 执行日志 DTO
     * @return 新增记录 id
     */
    Long insert(ExecuteLogDTO dto);

    /**
     * 批量保存
     * fillByDiff 主路径用: 一次同时落 insert + update
     * @ext INSERT 或 UPDATE 自动判定, 按 id 是否存在
     *
     * @param dtos 执行日志 DTO 列表
     * @return 影响行数
     */
    int batchSave(List<ExecuteLogDTO> dtos);

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页 DTO
     */
    Page<ExecuteLogDTO> page(ExecuteLogQuery query);

    /**
     * 按 id 查
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
     * @ext 条件 UPDATE
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
     * 标记 SKIP
     *
     * @param id  主键
     * @param msg SKIP 原因
     * @return 是否成功
     */
    boolean markSkip(Long id, String msg);

    /**
     * 改回 PENDING
     * @ext 运营手动重试
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean retryToPending(Long id);

    /**
     * 检查是否已存在
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
     * @ext 赠送插 SKIP 节点 / 退款留最早一条用
     *
     * @param bizSourceCode 业务来源 code
     * @param jobHandler    handler 名
     * @return 最早 PENDING, 无则 null
     */
    ExecuteLogDTO findEarliestPendingByCodeAndHandler(String bizSourceCode, String jobHandler);

    /**
     * 按 jobName 拉所有 PENDING
     * @ext 业务 Job reload 用, 按 due 升序限量
     *
     * @param jobName handler 名
     * @param limit   限量
     * @return PENDING 列表
     */
    List<ExecuteLogDTO> listPendingByJobName(String jobName, int limit);

    /**
     * 作废下所有 PENDING:
     * 1. 留最早一条改为 SKIP
     * 2. 其余删除
     * 若无 PENDING 则 noop
     * @ext code, jobHandler
     *
     * @param code       业务来源 code
     * @param jobHandler handler 名
     * @param cancelMsg  作废原因
     */
    void cancelPendingByCodeAndJobHandler(String code, String jobHandler, String cancelMsg);

    /**
     * 在最早 PENDING 之前插一条 SKIP audit 节点
     * scheduledDate/scheduledTime = 最早 PENDING 时刻 - 1 秒
     * 若无 PENDING 则 noop
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
    List<ExecuteLogDTO> listDoneInRange(String code, String jobHandler,
                                        LocalDate from, LocalDate to);

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
     * 拉 jobHandler 全部活跃 PENDING
     * @ext fill diff 用, 无时间限制
     *
     * @param jobHandler handler 名
     * @return PENDING 列表
     */
    List<ExecuteLogDTO> listAllPendingByHandler(String jobHandler);

    /**
     * 按 id 更新 scheduledDate / scheduledTime
     * @ext fill diff 改时间用
     *
     * @param id            主键
     * @param scheduledDate 计划日期
     * @param scheduledTime 计划时刻
     * @return 是否成功
     */
    boolean updateScheduledTime(Long id, LocalDate scheduledDate, LocalTime scheduledTime);

    /**
     * 按 id 删除 PENDING
     * @ext fill diff 删多余用
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deletePendingById(Long id);

    /**
     * 批量删除 PENDING
     * @ext 按 ids, 仅 status=PENDING
     *
     * @param ids 主键列表
     * @return 影响行数
     */
    int batchDeletePendingByIds(List<Long> ids);

    /**
     * 按 bizSourceCode 范围 + jobHandler 拉所有 PENDING
     * @ext fillByDiffByCodes 用
     *
     * @param jobHandler  handler 名
     * @param sourceCodes 业务来源 code 列表
     * @return PENDING 列表
     */
    List<ExecuteLogDTO> listPendingByCodesAndHandler(String jobHandler, List<String> sourceCodes);

    /**
     * 检查是否已存在任意 PENDING
     * @ext code, jobHandler
     *
     * @param code       业务来源 code
     * @param jobHandler handler 名
     * @return 是否存在
     */
    boolean existsAnyPendingByCode(String code, String jobHandler);
}
