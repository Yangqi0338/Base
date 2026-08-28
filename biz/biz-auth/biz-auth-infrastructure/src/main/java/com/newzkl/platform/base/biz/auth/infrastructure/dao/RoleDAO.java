package com.newzkl.platform.base.biz.auth.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.auth.infrastructure.entity.RoleDO;

import com.newzkl.platform.base.biz.auth.model.permission.req.RoleQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色 DAO
 *
 * @author KC
 */
@Mapper
public interface RoleDAO extends BaseMapper<RoleDO> {

    /**
     * 构建角色查询条件
     *
     * @param query 查询条件
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<RoleDO> getLw(RoleQuery query) {
        BaseLambdaQueryWrapper<RoleDO> wrapper = new BaseLambdaQueryWrapper<RoleDO>()
                .notNullEq(RoleDO::getClient, query.getClient())
                .notEmptyIn(RoleDO::getId, query.getIdList())
                .notEmptyEq(RoleDO::getCode, query.getCode())
                .likeList(query.getKeyword(), RoleDO::getName, RoleDO::getCode);
        wrapper.orderByAsc(RoleDO::getSort).orderByDesc(RoleDO::getCreateTime);
        return wrapper;
    }
}
