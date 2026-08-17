package com.newzkl.platform.base.biz.auth.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.auth.infrastructure.entity.PermissionRelationDO;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

/**
 * 权限关系 DAO
 *
 * @author KC
 */
@Mapper
public interface PermissionRelationDAO extends BaseMapper<PermissionRelationDO> {

    /**
     * 按关系类型与源对象集合构建查询条件
     *
     * @param type      关系类型
     * @param sourceIds 源对象ID集合
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<PermissionRelationDO> getLw(PermissionEnum.RelationType type, Collection<Long> sourceIds) {
        return new BaseLambdaQueryWrapper<PermissionRelationDO>()
                .notNullEq(PermissionRelationDO::getType, type)
                .notEmptyIn(PermissionRelationDO::getSourceId, sourceIds);
    }

    /**
     * 按关系类型与目标对象集合构建查询条件
     *
     * @param type      关系类型
     * @param targetIds 目标对象ID集合
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<PermissionRelationDO> getLwByTarget(PermissionEnum.RelationType type, Collection<Long> targetIds) {
        return new BaseLambdaQueryWrapper<PermissionRelationDO>()
                .notNullEq(PermissionRelationDO::getType, type)
                .notEmptyIn(PermissionRelationDO::getTargetId, targetIds);
    }
}
