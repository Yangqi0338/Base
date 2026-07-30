package com.newzkl.platform.base.biz.user.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.user.infrastructure.entity.MemberTaskRecordDO;
import com.newzkl.platform.base.biz.user.model.task.record.query.MemberTaskRecordQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员任务进度 Mapper
 *
 * @author KC
 */
@Mapper
public interface MemberTaskRecordDAO extends BaseMapper<MemberTaskRecordDO> {

    /**
     * 组装会员任务进度查询条件
     *
     * @param query 查询条件
     * @return 条件构造器
     */
    default BaseLambdaQueryWrapper<MemberTaskRecordDO> getLw(MemberTaskRecordQuery query) {
        BaseLambdaQueryWrapper<MemberTaskRecordDO> lw = new BaseLambdaQueryWrapper<>();
        lw.notNullEq(MemberTaskRecordDO::getMemberId, query.getMemberId())
                .notNullEq(MemberTaskRecordDO::getTaskNum, query.getTaskNum())
                .notNullEq(MemberTaskRecordDO::getTaskType, query.getTaskType())
                .notNullEq(MemberTaskRecordDO::getTaskStatus, query.getTaskStatus());
        lw.between(MemberTaskRecordDO::getCreateTime,
                new String[]{query.getStartTime(), query.getEndTime()});
        lw.orderBy(query);
        return lw;
    }
}
