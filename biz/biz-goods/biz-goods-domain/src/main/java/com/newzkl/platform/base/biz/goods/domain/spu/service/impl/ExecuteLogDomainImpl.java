package com.newzkl.platform.base.biz.goods.domain.spu.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.ExecuteLogRepository;
import com.newzkl.platform.base.biz.goods.domain.spu.service.ExecuteLogDomain;
import com.newzkl.platform.base.biz.goods.model.goods.entity.executeLog.ExecuteLog;
import com.newzkl.platform.base.biz.goods.model.goods.query.executeLog.ExecuteLogQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 操作日志
* @author fang
*/
@Service
@RequiredArgsConstructor
public class ExecuteLogDomainImpl implements ExecuteLogDomain {

    private final ExecuteLogRepository executeLogRepository;

    @Override
    public Long executeLogSave(Integer type, Long targetId, String executeUserName, String oldData, String updateDate) {
        ExecuteLog executeLog = new ExecuteLog();
        executeLog.setType(type);
        executeLog.setTargetId(targetId);
        executeLog.setExecuteUserName(executeUserName);
        executeLog.setOldData(oldData);
        executeLog.setUpdateData(updateDate);
        executeLogRepository.executeLogSave(executeLog);
        return executeLog.getId();
    }

    @Override
    public ExecuteLog executeLog(Long executeLogId) {
        return executeLogRepository.executeLog(executeLogId);
    }

    @Override
    public Page<ExecuteLog> executeLogPage(ExecuteLogQuery query) {
        return executeLogRepository.executeLogPage(query);
    }

    @Override
    public List<ExecuteLog> executeLogList(ExecuteLogQuery query) {
        return executeLogRepository.executeLogList(query);
    }
}
