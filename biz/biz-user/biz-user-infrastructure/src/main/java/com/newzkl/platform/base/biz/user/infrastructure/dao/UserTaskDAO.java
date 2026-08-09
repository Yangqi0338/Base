package com.newzkl.platform.base.biz.user.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.user.infrastructure.entity.UserTaskDO;
import com.newzkl.platform.base.biz.user.model.relation.query.UserTaskQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户任务(user_task) Mapper
 *
 * <p>迁移说明：源 DAO 被 import 但无文件，据用法补建。getLw 使用原生 MP LambdaQueryWrapper。</p>
 *
 * @author kc
 */
@Mapper
public interface UserTaskDAO extends BaseMapper<UserTaskDO> {

    /**
     * 构造用户任务查询包装器
     *
     * @param query 查询条件
     * @return LambdaQueryWrapper
     */
    default BaseLambdaQueryWrapper<UserTaskDO> getLw(UserTaskQuery query) {
        BaseLambdaQueryWrapper<UserTaskDO> queryWrapper = new BaseLambdaQueryWrapper<UserTaskDO>()
                .notEmptyEq(UserTaskDO::getType, query.getType())
                .notEmptyEq(UserTaskDO::getSeq, query.getSeq())
                .notEmptyEq(UserTaskDO::getCount, query.getCount())
                .notEmptyEq(UserTaskDO::getForeignId, query.getForeignId())
                .notEmptyEq(UserTaskDO::getStatus, query.getStatus())
                .notEmptyIn(UserTaskDO::getId, query.getIdList());
        return queryWrapper;
    }

}
