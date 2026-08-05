package com.newzkl.platform.base.common.core.job.infrastructure.secondLevel.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.common.core.job.model.model.ExecuteLogEnum;
import com.newzkl.platform.base.common.core.job.model.req.ExecuteLogQuery;
import com.newzkl.platform.base.common.core.job.infrastructure.secondLevel.entity.ExecuteLogDO;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 执行日志 DAO
 */
@Mapper
public interface JobExecuteLogDAO extends BaseMapper<ExecuteLogDO> {

    /**
     * 构建执行日志查询条件
     *
     * @param query 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<ExecuteLogDO> getLw(ExecuteLogQuery query) {
        BaseLambdaQueryWrapper<ExecuteLogDO> lw = new BaseLambdaQueryWrapper<ExecuteLogDO>()
                .notEmptyEq(ExecuteLogDO::getBizType, query.getBizType())
                .notEmptyEq(ExecuteLogDO::getBizSourceCode, query.getBizSourceCode())
                .notEmptyEq(ExecuteLogDO::getJobHandler, query.getJobHandler())
                .notEmptyEq(ExecuteLogDO::getStatus, query.getStatus())
                .notEmptyEq(ExecuteLogDO::getSource, query.getSource());
        lw.in(query.getUserIds() != null && !query.getUserIds().isEmpty(),
                ExecuteLogDO::getUserId, query.getUserIds());
        lw.ge(query.getScheduledDateStart() != null, ExecuteLogDO::getScheduledDate, query.getScheduledDateStart());
        lw.le(query.getScheduledDateEnd() != null, ExecuteLogDO::getScheduledDate, query.getScheduledDateEnd());
        lw.ge(query.getCreateTimeStart() != null, ExecuteLogDO::getCreateTime, query.getCreateTimeStart());
        lw.le(query.getCreateTimeEnd() != null, ExecuteLogDO::getCreateTime, query.getCreateTimeEnd());
        lw.orderByAsc(ExecuteLogDO::getScheduledDate)
          .orderByAsc(ExecuteLogDO::getScheduledTime);
        return lw;
    }

    /**
     * 抢锁: PENDING → RUNNING
     * @ext 条件 UPDATE
     *
     * @param id 主键
     * @return 受影响行数(= 1 表示获锁成功)
     */
    @Update("UPDATE execute_log SET status = 1, update_time = NOW() WHERE id = #{id} AND status = 0")
    int tryLockToRunning(@Param("id") Long id);

    /**
     * 标记 DONE, 写 execute_time 与 biz_id
     *
     * @param id    主键
     * @param bizId 业务对象 id
     * @return 受影响行数
     */
    @Update("UPDATE execute_log SET status = 2, biz_id = #{bizId}, execute_time = NOW(), update_time = NOW() WHERE id = #{id}")
    int markDone(@Param("id") Long id, @Param("bizId") Long bizId);

    /**
     * 标记 FAILED, 写 msg 与 execute_time
     *
     * @param id  主键
     * @param msg 失败原因
     * @return 受影响行数
     */
    @Update("UPDATE execute_log SET status = -1, msg = #{msg}, execute_time = NOW(), update_time = NOW() WHERE id = #{id}")
    int markFailed(@Param("id") Long id, @Param("msg") String msg);

    /**
     * 标记 SKIP, 写 msg
     *
     * @param id  主键
     * @param msg SKIP 原因
     * @return 受影响行数
     */
    @Update("UPDATE execute_log SET status = -2, msg = #{msg}, update_time = NOW() WHERE id = #{id} AND status = 0")
    int markSkip(@Param("id") Long id, @Param("msg") String msg);

    /**
     * 改回 PENDING
     * @ext 运营手动重试
     *
     * @param id 主键
     * @return 受影响行数
     */
    @Update("UPDATE execute_log SET status = 0, msg = NULL, execute_time = NULL, update_time = NOW() WHERE id = #{id} AND status IN (-1, -2)")
    int retryToPending(@Param("id") Long id);

    /**
     * 检查指定是否已有 PENDING/RUNNING/DONE 行
     *
     * @param bizSourceCode 业务来源 code
     * @param jobHandler    handler 名
     * @param scheduledDate 计划日期
     * @return 是否存在
     */
    default boolean existsByBizSourceJobDate(String bizSourceCode, String jobHandler, LocalDate scheduledDate) {
        return this.exists(new LambdaQueryWrapper<ExecuteLogDO>()
                .eq(ExecuteLogDO::getBizSourceCode, bizSourceCode)
                .eq(ExecuteLogDO::getJobHandler, jobHandler)
                .eq(ExecuteLogDO::getScheduledDate, scheduledDate)
                .in(ExecuteLogDO::getStatus, ExecuteLogEnum.Status.PENDING, ExecuteLogEnum.Status.RUNNING, ExecuteLogEnum.Status.DONE));
    }

    /**
     * 检查是否已存在任意 PENDING
     *
     * @param bizSourceCode 业务来源 code
     * @param jobHandler    handler 名
     * @return 是否存在
     */
    default boolean existsAnyPendingByCode(String bizSourceCode, String jobHandler) {
        return this.exists(new LambdaQueryWrapper<ExecuteLogDO>()
                .eq(ExecuteLogDO::getBizSourceCode, bizSourceCode)
                .eq(ExecuteLogDO::getJobHandler, jobHandler)
                .eq(ExecuteLogDO::getStatus, ExecuteLogEnum.Status.PENDING));
    }

    /**
     * 拉到点的 PENDING 列表
     * @ext ScanJob 主索引
     *
     * @param today   当前日期
     * @param nowTime 当前时刻
     * @param limit   限量
     * @return PENDING 列表
     */
    default List<ExecuteLogDO> listPendingDue(LocalDate today, LocalTime nowTime, int limit) {
        return this.selectList(new LambdaQueryWrapper<ExecuteLogDO>()
                .eq(ExecuteLogDO::getStatus, ExecuteLogEnum.Status.PENDING)
                .and(w -> w.lt(ExecuteLogDO::getScheduledDate, today)
                          .or(o -> o.eq(ExecuteLogDO::getScheduledDate, today).le(ExecuteLogDO::getScheduledTime, nowTime)))
                .orderByAsc(ExecuteLogDO::getScheduledDate)
                .orderByAsc(ExecuteLogDO::getScheduledTime)
                .last("LIMIT " + Math.max(1, limit)));
    }

    /**
     * 按 jobHandler 拉某业务对象的最早 PENDING
     *
     * @param bizSourceCode 业务来源 code
     * @param jobHandler    handler 名
     * @return 最早 PENDING, 无则 null
     */
    default ExecuteLogDO findEarliestPendingByCodeAndHandler(String bizSourceCode, String jobHandler) {
        return this.selectOne(new LambdaQueryWrapper<ExecuteLogDO>()
                .eq(ExecuteLogDO::getBizSourceCode, bizSourceCode)
                .eq(ExecuteLogDO::getJobHandler, jobHandler)
                .eq(ExecuteLogDO::getStatus, ExecuteLogEnum.Status.PENDING)
                .orderByAsc(ExecuteLogDO::getScheduledDate)
                .orderByAsc(ExecuteLogDO::getScheduledTime)
                .last("LIMIT 1"));
    }

    /**
     * 删除下所有 PENDING (留最早一条除外)
     *
     * @param code       业务来源 code
     * @param jobHandler handler 名
     * @return 受影响行数
     */
    @org.apache.ibatis.annotations.Delete(
        "DELETE FROM execute_log WHERE biz_source_code = #{code} AND job_handler = #{jobHandler} " +
        "AND status = 0 AND id != (" +
        "  SELECT min_id FROM (" +
        "    SELECT MIN(id) AS min_id FROM execute_log " +
        "    WHERE biz_source_code = #{code} AND job_handler = #{jobHandler} AND status = 0" +
        "  ) t" +
        ")")
    int deletePendingExceptEarliest(@Param("code") String code,
                                    @Param("jobHandler") String jobHandler);

    /**
     * 删除下全部 PENDING
     *
     * @param code       业务来源 code
     * @param jobHandler handler 名
     * @return 受影响行数
     */
    @org.apache.ibatis.annotations.Delete(
        "DELETE FROM execute_log WHERE biz_source_code = #{code} AND job_handler = #{jobHandler} AND status = 0")
    int deleteAllPending(@Param("code") String code,
                         @Param("jobHandler") String jobHandler);

    /**
     * 按 jobHandler 拉所有 PENDING
     * @ext reload 用
     *
     * @param jobHandler handler 名
     * @param limit      限量
     * @return PENDING 列表
     */
    default List<ExecuteLogDO> listPendingByJobHandler(String jobHandler, int limit) {
        return this.selectList(new LambdaQueryWrapper<ExecuteLogDO>()
                .eq(ExecuteLogDO::getJobHandler, jobHandler)
                .eq(ExecuteLogDO::getStatus, ExecuteLogEnum.Status.PENDING)
                .orderByAsc(ExecuteLogDO::getScheduledDate)
                .orderByAsc(ExecuteLogDO::getScheduledTime)
                .last("LIMIT " + Math.max(1, limit)));
    }

    /**
     * 统计在指定年月内 DONE 行的不同 scheduledDate 数
     *
     * @param code       业务来源 code
     * @param jobHandler handler 名
     * @param year       年
     * @param month      月
     * @return 不同日期数
     */
    default long countDoneDaysInMonth(String code, String jobHandler, int year, int month) {
        java.time.LocalDate from = java.time.LocalDate.of(year, month, 1);
        java.time.LocalDate to = from.plusMonths(1).minusDays(1);
        return this.selectCount(
                new LambdaQueryWrapper<ExecuteLogDO>()
                        .select(ExecuteLogDO::getScheduledDate)
                        .eq(ExecuteLogDO::getBizSourceCode, code)
                        .eq(ExecuteLogDO::getJobHandler, jobHandler)
                        .eq(ExecuteLogDO::getStatus, ExecuteLogEnum.Status.DONE)
                        .between(ExecuteLogDO::getScheduledDate, from, to));
    }

    /**
     * 列出指定日期范围内 DONE 行
     *
     * @param code       业务来源 code
     * @param jobHandler handler 名
     * @param from       起始日期
     * @param to         结束日期
     * @return DONE 列表
     */
    default List<ExecuteLogDO> listDoneInRange(String code, String jobHandler,
                                               java.time.LocalDate from, java.time.LocalDate to) {
        return this.selectList(
            new LambdaQueryWrapper<ExecuteLogDO>()
                .eq(ExecuteLogDO::getBizSourceCode, code)
                .eq(ExecuteLogDO::getJobHandler, jobHandler)
                .eq(ExecuteLogDO::getStatus, ExecuteLogEnum.Status.DONE)
                .between(ExecuteLogDO::getScheduledDate, from, to)
                .orderByAsc(ExecuteLogDO::getScheduledDate)
                .orderByAsc(ExecuteLogDO::getScheduledTime));
    }

    /**
     * 拉 jobHandler 在 endTime 之前的全部 PENDING
     *
     * @param jobHandler handler 名
     * @param endTime    截止时刻
     * @param ids        指定 id 列表
     * @return PENDING 列表
     */
    default List<ExecuteLogDO> listPendingBefore(String jobHandler, LocalDateTime endTime, List<Long> ids) {
        LambdaQueryWrapper<ExecuteLogDO> w = new LambdaQueryWrapper<ExecuteLogDO>()
                .eq(ExecuteLogDO::getJobHandler, jobHandler)
                .eq(ExecuteLogDO::getStatus, ExecuteLogEnum.Status.PENDING);
        if (ids != null && !ids.isEmpty()) {
            w.in(ExecuteLogDO::getId, ids);
        } else {
            LocalDate date = endTime.toLocalDate();
            LocalTime time = endTime.toLocalTime();
            w.and(x -> x.lt(ExecuteLogDO::getScheduledDate, date)
                        .or(o -> o.eq(ExecuteLogDO::getScheduledDate, date).le(ExecuteLogDO::getScheduledTime, time)));
        }
        return this.selectList(w.orderByAsc(ExecuteLogDO::getScheduledDate).orderByAsc(ExecuteLogDO::getScheduledTime));
    }

    /**
     * 拉 jobHandler 全部活跃 PENDING
     *
     * @param jobHandler handler 名
     * @return PENDING 列表
     */
    default List<ExecuteLogDO> listAllPendingByHandler(String jobHandler) {
        return this.selectList(new LambdaQueryWrapper<ExecuteLogDO>()
                .eq(ExecuteLogDO::getJobHandler, jobHandler)
                .eq(ExecuteLogDO::getStatus, ExecuteLogEnum.Status.PENDING));
    }

    /**
     * 按 bizSourceCode 范围 + jobHandler 拉 PENDING
     *
     * @param jobHandler  handler 名
     * @param sourceCodes 业务来源 code 列表
     * @return PENDING 列表
     */
    default List<ExecuteLogDO> listPendingByCodesAndHandler(String jobHandler, List<String> sourceCodes) {
        if (sourceCodes == null || sourceCodes.isEmpty()) {
            return List.of();
        }
        return this.selectList(new LambdaQueryWrapper<ExecuteLogDO>()
                .eq(ExecuteLogDO::getJobHandler, jobHandler)
                .in(ExecuteLogDO::getBizSourceCode, sourceCodes)
                .eq(ExecuteLogDO::getStatus, ExecuteLogEnum.Status.PENDING));
    }
}
