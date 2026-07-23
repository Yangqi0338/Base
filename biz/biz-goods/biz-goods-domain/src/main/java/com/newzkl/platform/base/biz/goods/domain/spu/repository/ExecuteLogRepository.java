package com.newzkl.platform.base.biz.goods.domain.spu.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.model.goods.entity.executeLog.ExecuteLog;
import com.newzkl.platform.base.biz.goods.model.goods.query.executeLog.ExecuteLogQuery;

import java.util.List;

/**
* 操作日志
* @author fang
*/
public interface ExecuteLogRepository {

    void executeLogSave(ExecuteLog executeLog);

    ExecuteLog executeLog(Long id);

    Page<ExecuteLog> executeLogPage(ExecuteLogQuery query);

    List<ExecuteLog> executeLogList(ExecuteLogQuery query);

}
