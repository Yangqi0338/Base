package com.newzkl.platform.base.biz.user.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.user.infrastructure.entity.UserInteractionDO;
import com.newzkl.platform.base.biz.user.model.interaction.query.InteractionQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户互动操作 Mapper
 *
 * @author sijiwang
 */
@Mapper
public interface UserInteractionDAO extends BaseMapper<UserInteractionDO> {

    default BaseLambdaQueryWrapper<UserInteractionDO> getLw(InteractionQuery query) {
        BaseLambdaQueryWrapper<UserInteractionDO> queryWrapper = new BaseLambdaQueryWrapper<UserInteractionDO>()
                .notEmptyIn(UserInteractionDO::getUserId, query.getUserIdList())
                .notEmptyEq(UserInteractionDO::getTargetType, query.getTargetType())
                .notEmptyEq(UserInteractionDO::getActionType, query.getActionType())
                .notEmptyEq(UserInteractionDO::getStoreId, query.getStoreId())
                .notEmptyEq(UserInteractionDO::getTargetId, query.getTargetId())
                ;
        queryWrapper.orderByDesc(UserInteractionDO::getId);
        return queryWrapper;
    }
}
