package com.newzkl.platform.base.biz.goods.infrastructure.goods.assembler;

import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.ExecuteLogDO;
import com.newzkl.platform.base.biz.goods.model.goods.entity.executeLog.ExecuteLog;
import com.newzkl.platform.base.common.ddd.model.BaseConvert;
import org.mapstruct.Mapper;

/**
* 操作日志
* @author fang
*/
@Mapper(componentModel = "spring", uses = BaseConvert.class)
public interface ExecuteLogAssembler {

    ExecuteLogDO executeLogToDO(ExecuteLog executeLog);

    ExecuteLog doToExecuteLog(ExecuteLogDO executeLogDO);
}
