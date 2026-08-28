package com.newzkl.platform.base.biz.auth.domain.adapt.repository;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionRelationDTO;

import java.util.Collection;
import java.util.List;

/**
 * 权限关系仓储
 *
 * <p>端隔离: 查询/删除方法均带 client, 为空则不限端(仅内部派生重算等场景复用)</p>
 *
 * @author KC
 */
public interface RelationRepository {

    /**
     * 批量新增关系
     *
     * @param dtos 关系数据集合
     */
    void insertBatch(List<PermissionRelationDTO> dtos);

    /**
     * 按端、类型与源对象集合删除关系
     *
     * @param client    所属端, 为空则不限端
     * @param type      关系类型
     * @param sources 源对象标识集合
     */
    void deleteBySource(AccountEnum.Client client, PermissionEnum.RelationType type, Collection<String> sources);

    /**
     * 按端、类型与目标对象集合删除关系
     *
     * @param client    所属端, 为空则不限端
     * @param type      关系类型
     * @param targets 目标对象标识集合
     */
    void deleteByTarget(AccountEnum.Client client, PermissionEnum.RelationType type, Collection<String> targets);

    /**
     * 按端、类型与源对象集合列出关系
     *
     * @param client    所属端, 为空则不限端
     * @param type      关系类型
     * @param sources 源对象标识集合
     * @return 关系列表
     */
    List<PermissionRelationDTO> listBySource(AccountEnum.Client client, PermissionEnum.RelationType type, Collection<String> sources);

    /**
     * 按端、类型与目标对象集合列出关系
     *
     * @param client    所属端, 为空则不限端
     * @param type      关系类型
     * @param targets 目标对象标识集合
     * @return 关系列表
     */
    List<PermissionRelationDTO> listByTarget(AccountEnum.Client client, PermissionEnum.RelationType type, Collection<String> targets);
}
