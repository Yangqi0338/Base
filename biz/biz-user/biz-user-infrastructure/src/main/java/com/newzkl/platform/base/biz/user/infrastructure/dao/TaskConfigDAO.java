package com.newzkl.platform.base.biz.user.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.user.infrastructure.entity.TaskConfigDO;
import com.newzkl.platform.base.biz.user.model.task.config.query.TaskConfigQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 营销任务类型 Mapper
 *
 * @author KC
 */
@Mapper
public interface TaskConfigDAO extends BaseMapper<TaskConfigDO> {

    /**
     * 组装任务类型查询条件
     *
     * @param query 查询条件
     * @return 条件构造器
     */
    default BaseLambdaQueryWrapper<TaskConfigDO> getLw(TaskConfigQuery query) {
        BaseLambdaQueryWrapper<TaskConfigDO> lw = new BaseLambdaQueryWrapper<>();
        lw.notNullEq(TaskConfigDO::getTaskType, query.getTaskType())
                .notNullEq(TaskConfigDO::getTaskGroup, query.getTaskGroup());
        lw.orderBy(query);
        return lw;
    }
}
