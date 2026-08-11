package com.newzkl.platform.base.biz.auth.domain.service;

import com.newzkl.platform.base.common.ddd.model.enums.auth.RelationEnum;

import java.util.Collection;
import java.util.List;

/**
 * 权限关系领域服务
 *
 * @author KC
 */
public interface RelationDomain {

    /**
     * 全量替换某源对象的关系目标
     *
     * @param type      关系类型
     * @param sourceId  源对象ID
     * @param targetIds 目标对象ID集合
     */
    void replace(RelationEnum.Type type, Long sourceId, Collection<Long> targetIds);

    /**
     * 列出源对象关联的目标ID
     *
     * @param type     关系类型
     * @param sourceId 源对象ID
     * @return 目标ID列表
     */
    List<Long> listTargetIds(RelationEnum.Type type, Long sourceId);

    /**
     * 列出目标对象关联的源ID
     *
     * @param type     关系类型
     * @param targetId 目标对象ID
     * @return 源ID列表
     */
    List<Long> listSourceIds(RelationEnum.Type type, Long targetId);
}
