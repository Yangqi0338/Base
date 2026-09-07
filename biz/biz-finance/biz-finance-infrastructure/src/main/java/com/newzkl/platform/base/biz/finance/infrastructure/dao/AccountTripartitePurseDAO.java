package com.newzkl.platform.base.biz.finance.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.finance.infrastructure.entity.AccountTripartitePurseDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * AccountTripartitePurseDAO
 *
 * <p>只增不查不维护: 仅依赖 {@link BaseMapper#insert} 落库, 自定义查询/更新/余额增减 SQL 已下线。</p>
 */
@Mapper
public interface AccountTripartitePurseDAO extends BaseMapper<AccountTripartitePurseDO> {
}
