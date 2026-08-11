package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.spu.repository.ExecuteLogRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.assembler.ExecuteLogAssembler;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.GoodsExecuteLogDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.ExecuteLogDO;
import com.newzkl.platform.base.biz.goods.model.goods.entity.executeLog.ExecuteLog;
import com.newzkl.platform.base.biz.goods.model.goods.query.executeLog.ExecuteLogQuery;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 操作日志
 *
 * @author fang
 */
@Repository("goodsExecuteLogRepositoryImpl")
@RequiredArgsConstructor
public class ExecuteLogRepositoryImpl implements ExecuteLogRepository {

    private final GoodsExecuteLogDAO executeLogDAO;
    private final ExecuteLogAssembler executeLogAssembler;

    @Override
    public void executeLogSave(ExecuteLog executeLog) {
        executeLogDAO.insert(executeLogAssembler.executeLogToDO(executeLog));
    }

    @Override
    public ExecuteLog executeLog(Long id) {
        ExecuteLogDO executeLogDO = executeLogDAO.selectById(id);
        return executeLogAssembler.doToExecuteLog(executeLogDO);
    }

    @Override
    public Page<ExecuteLog> executeLogPage(ExecuteLogQuery query) {
        Page<ExecuteLogDO> executeLogDOPage = executeLogDAO.selectPage(RepositorySupport.page(query), new BaseLambdaQueryWrapper<ExecuteLogDO>().notEmptyIn(ExecuteLogDO::getId, query.getIdList()).eq(ExecuteLogDO::getId, query.getId()));
        return TransferUtils.transferPage(executeLogDOPage, executeLogAssembler::doToExecuteLog);
    }

    @Override
    public List<ExecuteLog> executeLogList(ExecuteLogQuery query) {
        List<ExecuteLogDO> executeLogDOList = executeLogDAO.selectList(RepositorySupport.page(query), new BaseLambdaQueryWrapper<ExecuteLogDO>().notEmptyIn(ExecuteLogDO::getId, query.getIdList()).eq(ExecuteLogDO::getId, query.getId()));
        return TransferUtils.transfers(executeLogDOList, executeLogAssembler::doToExecuteLog);
    }
}
