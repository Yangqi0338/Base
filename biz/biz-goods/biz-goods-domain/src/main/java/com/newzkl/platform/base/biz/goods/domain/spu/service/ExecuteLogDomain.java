package com.newzkl.platform.base.biz.goods.domain.spu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.entity.executeLog.ExecuteLog;
import com.newzkl.platform.base.biz.goods.model.goods.query.executeLog.ExecuteLogQuery;

import java.util.List;

/**
* 操作日志
* @author fang
*/
public interface ExecuteLogDomain {

    /**
     * 操作日志创建
     * @param type
     * @param targetId
     * @param executeUserName
     * @param oldData
     * @param updateDate
     */
    Long executeLogSave(Integer type, Long targetId, String executeUserName, String oldData, String updateDate);

    /**
     * 操作日志实体
     * @param executeLogId
     * @return
     */
    ExecuteLog executeLog(Long executeLogId);

    Page<ExecuteLog> executeLogPage(ExecuteLogQuery query);

    List<ExecuteLog> executeLogList(ExecuteLogQuery query);

}
