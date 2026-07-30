package com.newzkl.platform.base.biz.user.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.user.infrastructure.entity.TaskInfoDO;
import com.newzkl.platform.base.biz.user.model.task.info.query.TaskInfoQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 营销任务 Mapper
 *
 * @author KC
 */
@Mapper
public interface TaskInfoDAO extends BaseMapper<TaskInfoDO> {

    /**
     * 组装任务查询条件
     *
     * @param query 查询条件
     * @return 条件构造器
     */
    default BaseLambdaQueryWrapper<TaskInfoDO> getLw(TaskInfoQuery query) {
        BaseLambdaQueryWrapper<TaskInfoDO> lw = new BaseLambdaQueryWrapper<>();
        lw.notEmptyLike(TaskInfoDO::getTaskName, query.getTaskName())
                .notNullEq(TaskInfoDO::getTaskNum, query.getTaskNum())
                .notNullEq(TaskInfoDO::getTaskType, query.getTaskType());
        lw.ge(query.getStartTime() != null, TaskInfoDO::getStartTime, query.getStartTime())
                .le(query.getEndTime() != null, TaskInfoDO::getEndTime, query.getEndTime());
        lw.orderBy(query);
        return lw;
    }
}
