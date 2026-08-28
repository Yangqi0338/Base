package com.newzkl.platform.base.biz.auth.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.auth.infrastructure.entity.PermissionRelationDO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
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
     * 按端、关系类型与源对象集合构建查询条件
     *
     * @param client    所属端, 为空则不限端(内部派生复用)
     * @param type      关系类型
     * @param sources 源对象标识集合
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<PermissionRelationDO> getLw(AccountEnum.Client client, PermissionEnum.RelationType type, Collection<String> sources) {
        return new BaseLambdaQueryWrapper<PermissionRelationDO>()
                .notNullEq(PermissionRelationDO::getClient, client)
                .notNullEq(PermissionRelationDO::getType, type)
                .notEmptyIn(PermissionRelationDO::getSource, sources);
    }

    /**
     * 按端、关系类型与目标对象集合构建查询条件
     *
     * @param client    所属端, 为空则不限端(内部派生复用)
     * @param type      关系类型
     * @param targets 目标对象标识集合
     * @return 查询包装器
     */
    default BaseLambdaQueryWrapper<PermissionRelationDO> getLwByTarget(AccountEnum.Client client, PermissionEnum.RelationType type, Collection<String> targets) {
        return new BaseLambdaQueryWrapper<PermissionRelationDO>()
                .notNullEq(PermissionRelationDO::getClient, client)
                .notNullEq(PermissionRelationDO::getType, type)
                .notEmptyIn(PermissionRelationDO::getTarget, targets);
    }
}
