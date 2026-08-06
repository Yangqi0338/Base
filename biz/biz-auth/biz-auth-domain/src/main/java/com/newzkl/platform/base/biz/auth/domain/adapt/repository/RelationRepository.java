package com.newzkl.platform.base.biz.auth.domain.adapt.repository;

import com.newzkl.platform.base.biz.auth.model.enums.RelationEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionRelationDTO;

import java.util.Collection;
import java.util.List;

/**
 * 权限关系仓储
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
     * 按类型与源对象集合删除关系
     *
     * @param type      关系类型
     * @param sourceIds 源对象ID集合
     */
    void deleteBySource(RelationEnum.Type type, Collection<Long> sourceIds);

    /**
     * 按类型与目标对象集合删除关系
     *
     * @param type      关系类型
     * @param targetIds 目标对象ID集合
     */
    void deleteByTarget(RelationEnum.Type type, Collection<Long> targetIds);

    /**
     * 按类型与源对象集合列出关系
     *
     * @param type      关系类型
     * @param sourceIds 源对象ID集合
     * @return 关系列表
     */
    List<PermissionRelationDTO> listBySource(RelationEnum.Type type, Collection<Long> sourceIds);

    /**
     * 按类型与目标对象集合列出关系
     *
     * @param type      关系类型
     * @param targetIds 目标对象ID集合
     * @return 关系列表
     */
    List<PermissionRelationDTO> listByTarget(RelationEnum.Type type, Collection<Long> targetIds);
}
