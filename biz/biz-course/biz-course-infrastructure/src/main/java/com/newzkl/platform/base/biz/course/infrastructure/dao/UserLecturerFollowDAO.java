package com.newzkl.platform.base.biz.course.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.course.infrastructure.entity.UserLecturerFollowDO;
import com.newzkl.platform.base.biz.course.model.follow.query.UserFollowQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户关注讲师 DAO
 *
 * @author KC
 */
@Mapper
public interface UserLecturerFollowDAO extends BaseMapper<UserLecturerFollowDO> {

    /**
     * 构建分页查询条件
     *
     * <p>逻辑删除即取关, MyBatis-Plus 默认过滤, 故本条件天然只返回在关注状态的记录。</p>
     *
     * @param query 查询条件
     * @return 查询条件包装
     */
    default BaseLambdaQueryWrapper<UserLecturerFollowDO> getLw(UserFollowQuery query) {
        return new BaseLambdaQueryWrapper<UserLecturerFollowDO>()
                .notEmptyIn(UserLecturerFollowDO::getId, query.getIdList())
                .notNullEq(UserLecturerFollowDO::getUserId, query.getUserId())
                .between(UserLecturerFollowDO::getCreateTime, query.getCreateTime())
                .orderByDesc(UserLecturerFollowDO::getFollowTime);
    }
}
