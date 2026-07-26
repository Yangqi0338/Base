package com.newzkl.platform.base.biz.sys.infrastructure.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.AroleDO;
import com.newzkl.platform.base.biz.sys.model.arole.query.AroleQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 后台角色 DAO。
 *
 * @author KC
 */
@Mapper
public interface AroleDAO extends BaseMapper<AroleDO> {

    /**
     * 构建角色查询条件。
     *
     * @param query 角色查询
     * @return 查询条件
     */
    default LambdaQueryWrapper<AroleDO> getLw(AroleQuery query) {
        return new BaseLambdaQueryWrapper<AroleDO>()
                .notEmptyIn(AroleDO::getId, query.getIdList())
                .notEmptyLike(AroleDO::getName, query.getName())
                .orderByDesc(AroleDO::getCreateTime);
    }
}
