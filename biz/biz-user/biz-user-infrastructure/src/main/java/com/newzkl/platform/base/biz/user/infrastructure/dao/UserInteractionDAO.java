package com.newzkl.platform.base.biz.user.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.user.infrastructure.entity.UserInteractionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户互动操作 Mapper。
 *
 * @author sijiwang
 */
@Mapper
public interface UserInteractionDAO extends BaseMapper<UserInteractionDO> {

}
