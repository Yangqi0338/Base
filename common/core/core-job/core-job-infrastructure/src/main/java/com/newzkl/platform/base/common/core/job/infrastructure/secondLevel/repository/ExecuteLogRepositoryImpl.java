package com.newzkl.platform.base.common.core.job.infrastructure.secondLevel.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.common.core.job.model.dto.ExecuteLogDTO;
import com.newzkl.platform.base.common.core.job.model.model.ExecuteLogEnum;
import com.newzkl.platform.base.common.core.job.model.req.ExecuteLogQuery;
import com.newzkl.platform.base.common.core.job.domain.secondLevel.ExecuteLogRepository;
import com.newzkl.platform.base.common.core.job.infrastructure.secondLevel.dao.JobExecuteLogDAO;
import com.newzkl.platform.base.common.core.job.infrastructure.secondLevel.entity.ExecuteLogDO;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 执行日志仓储实现
 */
@Slf4j
@Repository("jobExecuteLogRepositoryImpl")
public class ExecuteLogRepositoryImpl extends RepositorySupport implements ExecuteLogRepository {

    @Autowired
    private JobExecuteLogDAO executeLogDAO;

    @Override
    public Long insert(ExecuteLogDTO dto) {
        ExecuteLogDO entity = TransferUtils.transfer(dto, ExecuteLogDO.class);
        executeLogDAO.insert(entity);
        return entity.getId();
    }

    @Override
    public int batchSave(List<ExecuteLogDTO> dtos) {
        /*
         * MyBatis-Plus BaseMapper.insertOrUpdate(Collection): 按 id 是否存在自动选 insert/update
         * fillByDiff 主路径: 一次同时落 insert + update
         */
        if (dtos == null || dtos.isEmpty()) {
            return 0;
        }
        List<ExecuteLogDO> entities = TransferUtils.transfers(dtos, ExecuteLogDO.class);
        executeLogDAO.insertOrUpdate(entities);
        return entities.size();
    }

    @Override
    public Page<ExecuteLogDTO> page(ExecuteLogQuery query) {
        Page<ExecuteLogDO> page = executeLogDAO.selectPage(RepositorySupport.page(query), executeLogDAO.getLw(query));
        Page<ExecuteLogDTO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(TransferUtils.transfers(page.getRecords(), ExecuteLogDTO.class));
        return result;
    }

    @Override
    public ExecuteLogDTO getById(Long id) {
        ExecuteLogDO entity = executeLogDAO.selectById(id);
        return entity == null ? null : TransferUtils.transfer(entity, ExecuteLogDTO.class);
    }

    @Override
    public List<ExecuteLogDTO> listPendingDue(LocalDate today, LocalTime nowTime, int limit) {
        List<ExecuteLogDO> list = executeLogDAO.listPendingDue(today, nowTime, limit);
        return list == null || list.isEmpty() ? new ArrayList<>() : TransferUtils.transfers(list, ExecuteLogDTO.class);
    }

    @Override
    public boolean tryLockToRunning(Long id) {
        return executeLogDAO.tryLockToRunning(id) == 1;
    }

    @Override
    public boolean markDone(Long id, Long bizId) {
        return executeLogDAO.markDone(id, bizId) == 1;
    }

    @Override
    public boolean markFailed(Long id, String msg) {
        return executeLogDAO.markFailed(id, msg) == 1;
    }

    @Override
    public boolean markSkip(Long id, String msg) {
        return executeLogDAO.markSkip(id, msg) == 1;
    }

    @Override
    public boolean retryToPending(Long id) {
        return executeLogDAO.retryToPending(id) == 1;
    }

    @Override
    public boolean existsByBizSourceJobDate(String bizSourceCode, String jobHandler, LocalDate scheduledDate) {
        return executeLogDAO.existsByBizSourceJobDate(bizSourceCode, jobHandler, scheduledDate);
    }

    @Override
    public ExecuteLogDTO findEarliestPendingByCodeAndHandler(String bizSourceCode, String jobHandler) {
        ExecuteLogDO entity = executeLogDAO.findEarliestPendingByCodeAndHandler(bizSourceCode, jobHandler);
        return entity == null ? null : TransferUtils.transfer(entity, ExecuteLogDTO.class);
    }

    @Override
    public List<ExecuteLogDTO> listPendingByJobName(String jobName, int limit) {
        List<ExecuteLogDO> list = executeLogDAO.listPendingByJobHandler(jobName, limit);
        return list == null || list.isEmpty()
                ? new ArrayList<>()
                : TransferUtils.transfers(list, ExecuteLogDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPendingByCodeAndJobHandler(String code, String jobHandler, String cancelMsg) {
        ExecuteLogDTO earliest = findEarliestPendingByCodeAndHandler(code, jobHandler);
        if (earliest == null) {
            return;
        }
        executeLogDAO.deletePendingExceptEarliest(code, jobHandler);
        markSkip(earliest.getId(), cancelMsg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSkipNodeBeforeEarliest(String code, String jobHandler, String auditMsg) {
        ExecuteLogDTO earliest = findEarliestPendingByCodeAndHandler(code, jobHandler);
        if (earliest == null) {
            log.warn("[insertSkipNodeBeforeEarliest] no PENDING for code={} handler={}", code, jobHandler);
            return;
        }
        LocalDateTime due = earliest.dueDateTime();
        LocalDateTime audit = due == null ? LocalDateTime.now() : due.minusSeconds(1);
        ExecuteLogDTO skip = new ExecuteLogDTO();
        skip.setBizType(earliest.getBizType());
        skip.setBizSourceCode(code);
        skip.setJobHandler(jobHandler);
        skip.setJobName(earliest.getJobName());
        skip.setScheduledDate(audit.toLocalDate());
        skip.setScheduledTime(audit.toLocalTime().withNano(0));
        skip.setStatus(ExecuteLogEnum.Status.SKIP);
        skip.setSource(ExecuteLogEnum.Source.MQ);
        skip.setAction(ExecuteLogEnum.Action.SCHEDULED);
        skip.setMsg(auditMsg);
        skip.setExecuteTime(LocalDateTime.now());
        insert(skip);
    }

    @Override
    public long countDoneDaysInMonth(String code, String jobHandler, java.time.YearMonth ym) {
        if (code == null || code.isEmpty() || jobHandler == null || ym == null) {
            return 0;
        }
        return executeLogDAO.countDoneDaysInMonth(code, jobHandler, ym.getYear(), ym.getMonthValue());
    }

    @Override
    public List<ExecuteLogDTO> listDoneInRange(String code, String jobHandler, LocalDate from, LocalDate to) {
        if (code == null || code.isEmpty() || jobHandler == null) {
            return new ArrayList<>();
        }
        List<ExecuteLogDO> list = executeLogDAO.listDoneInRange(code, jobHandler, from, to);
        return list == null || list.isEmpty()
                ? new ArrayList<>()
                : TransferUtils.transfers(list, ExecuteLogDTO.class);
    }

    @Override
    public List<ExecuteLogDTO> listPendingBefore(String jobHandler, LocalDateTime endTime, List<Long> ids) {
        List<ExecuteLogDO> list = executeLogDAO.listPendingBefore(jobHandler, endTime, ids);
        return list == null || list.isEmpty()
                ? new ArrayList<>()
                : TransferUtils.transfers(list, ExecuteLogDTO.class);
    }

    @Override
    public List<ExecuteLogDTO> listAllPendingByHandler(String jobHandler) {
        List<ExecuteLogDO> list = executeLogDAO.listAllPendingByHandler(jobHandler);
        return list == null || list.isEmpty()
                ? new ArrayList<>()
                : TransferUtils.transfers(list, ExecuteLogDTO.class);
    }

    @Override
    public boolean updateScheduledTime(Long id, LocalDate scheduledDate, LocalTime scheduledTime) {
        ExecuteLogDO update = new ExecuteLogDO();
        update.setId(id);
        update.setScheduledDate(scheduledDate);
        update.setScheduledTime(scheduledTime);
        return executeLogDAO.updateById(update) == 1;
    }

    @Override
    public boolean deletePendingById(Long id) {
        return executeLogDAO.delete(new LambdaQueryWrapper<ExecuteLogDO>()
                .eq(ExecuteLogDO::getId, id)
                .eq(ExecuteLogDO::getStatus, ExecuteLogEnum.Status.PENDING)) == 1;
    }

    @Override
    public int batchDeletePendingByIds(List<Long> ids) {
        /*
         * 单 SQL: DELETE FROM execute_log WHERE id IN (...) AND status = 0
         */
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return executeLogDAO.delete(new LambdaQueryWrapper<ExecuteLogDO>()
                .in(ExecuteLogDO::getId, ids)
                .eq(ExecuteLogDO::getStatus, ExecuteLogEnum.Status.PENDING));
    }

    @Override
    public List<ExecuteLogDTO> listPendingByCodesAndHandler(String jobHandler, List<String> sourceCodes) {
        if (sourceCodes == null || sourceCodes.isEmpty()) {
            return new ArrayList<>();
        }
        List<ExecuteLogDO> list = executeLogDAO.listPendingByCodesAndHandler(jobHandler, sourceCodes);
        return list == null || list.isEmpty()
                ? new ArrayList<>()
                : TransferUtils.transfers(list, ExecuteLogDTO.class);
    }

    @Override
    public boolean existsAnyPendingByCode(String code, String jobHandler) {
        if (code == null || code.isEmpty() || jobHandler == null) {
            return false;
        }
        return executeLogDAO.existsAnyPendingByCode(code, jobHandler);
    }
}
