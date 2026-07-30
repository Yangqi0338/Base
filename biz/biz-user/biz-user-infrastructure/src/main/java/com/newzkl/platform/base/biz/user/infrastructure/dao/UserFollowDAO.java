package com.newzkl.platform.base.biz.user.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.user.infrastructure.entity.UserFollowDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户关注 Mapper
 *
 * @author sijiwang
 */
@Mapper
public interface UserFollowDAO extends BaseMapper<UserFollowDO> {
}
