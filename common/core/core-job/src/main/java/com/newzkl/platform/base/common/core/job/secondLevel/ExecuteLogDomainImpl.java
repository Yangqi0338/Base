package com.newzkl.platform.base.common.core.job.secondLevel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongze.chicken.common.biz.dto.ExecuteLogDTO;
import com.zhongze.chicken.common.biz.req.ExecuteLogManualReq;
import com.zhongze.chicken.common.biz.req.ExecuteLogQuery;
import com.zhongze.chicken.common.biz.res.ExecuteLogVO;
import com.zhongze.chicken.common.enums.admin.ExecuteLogEnum;
import com.zhongze.chicken.common.exception.BaseErrorCode;
import com.zhongze.chicken.common.exception.BizException;
import com.zhongze.chicken.common.executeLog.ExecuteLogDomain;
import com.zhongze.chicken.common.executeLog.ExecuteLogRepository;
import com.zhongze.chicken.common.util.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;

/**
 * 执行日志 Domain 实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExecuteLogDomainImpl implements ExecuteLogDomain {

    private final ExecuteLogRepository executeLogRepository;

    @Override
    public Page<ExecuteLogVO> page(ExecuteLogQuery query) {
        Page<ExecuteLogDTO> page = executeLogRepository.page(query);
        return TransferUtils.transferPage(page, ExecuteLogVO.class);
    }

    @Override
    public ExecuteLogDTO getById(Long id) {
        return executeLogRepository.getById(id);
    }

    @Override
    public List<ExecuteLogDTO> listPendingDue(LocalDate today, LocalTime nowTime, int limit) {
        return executeLogRepository.listPendingDue(today, nowTime, limit);
    }

    @Override
    public boolean tryLockToRunning(Long id) {
        return executeLogRepository.tryLockToRunning(id);
    }

    @Override
    public boolean markDone(Long id, Long bizId) {
        return executeLogRepository.markDone(id, bizId);
    }

    @Override
    public boolean markFailed(Long id, String msg) {
        return executeLogRepository.markFailed(id, msg);
    }

    @Override
    public boolean skip(Long id, String msg, Long operatorId) {
        ExecuteLogDTO dto = executeLogRepository.getById(id);
        if (dto == null) {
            throw new BizException(BaseErrorCode.NOT_FOUND);
        }
        if (dto.getStatus() != ExecuteLogEnum.Status.PENDING) {
            throw new BizException(BaseErrorCode.BIZ_ERROR, "仅 PENDING 可 SKIP");
        }
        String fullMsg = String.format("管理员 %d 手动 SKIP: %s", operatorId, msg == null ? "" : msg);
        return executeLogRepository.markSkip(id, fullMsg);
    }

    @Override
    public boolean retry(Long id, Long operatorId) {
        ExecuteLogDTO dto = executeLogRepository.getById(id);
        if (dto == null) {
            throw new BizException(BaseErrorCode.NOT_FOUND);
        }
        if (dto.getStatus() != ExecuteLogEnum.Status.FAILED
                && dto.getStatus() != ExecuteLogEnum.Status.SKIP) {
            throw new BizException(BaseErrorCode.BIZ_ERROR, "仅 FAILED/SKIP 可重试");
        }
        log.info("[retry] operator={} id={}", operatorId, id);
        return executeLogRepository.retryToPending(id);
    }

    @Override
    public Long insertManual(ExecuteLogManualReq req, Long operatorId) {
        if (req.getScheduledDate() == null || req.getScheduledTime() == null) {
            throw new BizException("预计时间错误");
        }
        ExecuteLogDTO dto = new ExecuteLogDTO();
        dto.setBizType(req.getBizType());
        dto.setBizSourceCode(req.getBizSourceCode());
        dto.setJobHandler(req.getJobHandler());
        dto.setJobName(req.getJobName());
        dto.setScheduledDate(req.getScheduledDate());
        dto.setScheduledTime(req.getScheduledTime());
        dto.setStatus(ExecuteLogEnum.Status.PENDING);
        dto.setSource(ExecuteLogEnum.Source.MANUAL);
        dto.setAction(ExecuteLogEnum.Action.SCHEDULED);
        return executeLogRepository.insert(dto);
    }

    @Override
    public Long insertPending(ExecuteLogDTO dto) {
        return executeLogRepository.insert(dto);
    }

    @Override
    public int batchInsertPending(List<ExecuteLogDTO> dtos) {
        return executeLogRepository.batchSave(dtos);
    }

    @Override
    public boolean existsByBizSourceJobDate(String bizSourceCode, String jobHandler, LocalDate scheduledDate) {
        return executeLogRepository.existsByBizSourceJobDate(bizSourceCode, jobHandler, scheduledDate);
    }

    @Override
    public ExecuteLogDTO findEarliestPendingByCodeAndHandler(String bizSourceCode, String jobHandler) {
        return executeLogRepository.findEarliestPendingByCodeAndHandler(bizSourceCode, jobHandler);
    }

    @Override
    public List<ExecuteLogDTO> listPendingByJobName(String jobName, int limit) {
        return executeLogRepository.listPendingByJobName(jobName, limit);
    }

    @Override
    public void cancelPendingByCodeAndJobHandler(String code, String jobHandler, String cancelMsg) {
        executeLogRepository.cancelPendingByCodeAndJobHandler(code, jobHandler, cancelMsg);
    }

    @Override
    public void insertSkipNodeBeforeEarliest(String code, String jobHandler, String auditMsg) {
        executeLogRepository.insertSkipNodeBeforeEarliest(code, jobHandler, auditMsg);
    }

    @Override
    public long countDoneDaysInMonth(String code, String jobHandler, YearMonth ym) {
        return executeLogRepository.countDoneDaysInMonth(code, jobHandler, ym);
    }

    @Override
    public List<ExecuteLogDTO> listDoneInRange(String code, String jobHandler, LocalDate from, LocalDate to) {
        return executeLogRepository.listDoneInRange(code, jobHandler, from, to);
    }

    @Override
    public List<ExecuteLogDTO> listPendingBefore(String jobHandler, LocalDateTime endTime, List<Long> ids) {
        return executeLogRepository.listPendingBefore(jobHandler, endTime, ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(List<ExecuteLogDTO> processed) {
        /*
         * msg 非空 → FAILED；msg 空 → DONE（用 bizId）
         */
        for (ExecuteLogDTO entry : processed) {
            if (entry.getMsg() != null && !entry.getMsg().isBlank()) {
                executeLogRepository.markFailed(entry.getId(), entry.getMsg());
            } else {
                executeLogRepository.markDone(entry.getId(), entry.getBizId());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fillByDiff(String jobHandler, List<ExecuteLogDTO> expected) {
        /*
         * 全量 diff：拉 jobHandler 所有 PENDING 作为 existing
         */
        List<ExecuteLogDTO> existing = executeLogRepository.listAllPendingByHandler(jobHandler);
        fillByDiff(jobHandler, expected, existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fillByDiff(String jobHandler, List<ExecuteLogDTO> expected, List<ExecuteLogDTO> existing) {
        /*
         * diff key = bizSourceCode（同一业务对象同一 job 一次只一条 PENDING）
         * - 业务有库无 → INSERT (id 留空)
         * - 库有业务无 → DELETE PENDING (DONE/SKIP/FAILED 不动)
         * - 库时间 ≠ 业务期望时间 → UPDATE scheduled_date/time (复用 cur.id)
         *
         * 全程批量：toSave 一次 batchSave (insertOrUpdate)，toDeleteIds 一次 batchDeletePendingByIds
         */
        Map<String, ExecuteLogDTO> existingMap = new HashMap<>();
        for (ExecuteLogDTO e : existing) {
            existingMap.put(e.getBizSourceCode(), e);
        }
        Map<String, ExecuteLogDTO> expectedMap = new HashMap<>();
        for (ExecuteLogDTO e : expected) {
            expectedMap.put(e.getBizSourceCode(), e);
        }

        List<ExecuteLogDTO> toSave = new ArrayList<>();
        List<Long> toDeleteIds = new ArrayList<>();
        int insertCnt = 0, updateCnt = 0;
        for (ExecuteLogDTO exp : expected) {
            ExecuteLogDTO cur = existingMap.get(exp.getBizSourceCode());
            if (cur == null) {
                exp.setId(null);
                toSave.add(exp);
                insertCnt++;
            } else if (!Objects.equals(cur.getScheduledDate(), exp.getScheduledDate())
                    || !Objects.equals(cur.getScheduledTime(), exp.getScheduledTime())) {
                cur.setScheduledDate(exp.getScheduledDate());
                cur.setScheduledTime(exp.getScheduledTime());
                toSave.add(cur);
                updateCnt++;
            }
        }
        for (ExecuteLogDTO cur : existing) {
            if (!expectedMap.containsKey(cur.getBizSourceCode())) {
                toDeleteIds.add(cur.getId());
            }
        }

        if (!toSave.isEmpty()) {
            executeLogRepository.batchSave(toSave);
        }
        int deleteCnt = 0;
        if (!toDeleteIds.isEmpty()) {
            deleteCnt = executeLogRepository.batchDeletePendingByIds(toDeleteIds);
        }
        log.info("[fillByDiff] jobHandler={} expected={} existing={} insert={} update={} delete={}",
                jobHandler, expected.size(), existing.size(), insertCnt, updateCnt, deleteCnt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fillByDiffByCodes(String jobHandler, List<ExecuteLogDTO> expected, List<String> sourceCodes) {
        /*
         * 按 sourceCodes 范围拉 existing，再调通用 diff
         */
        if (sourceCodes == null || sourceCodes.isEmpty()) {
            log.warn("[fillByDiffByCodes] sourceCodes 空，跳过 jobHandler={}", jobHandler);
            return;
        }
        List<ExecuteLogDTO> existing = executeLogRepository.listPendingByCodesAndHandler(jobHandler, sourceCodes);
        fillByDiff(jobHandler, expected == null ? new ArrayList<>() : expected, existing);
    }
}