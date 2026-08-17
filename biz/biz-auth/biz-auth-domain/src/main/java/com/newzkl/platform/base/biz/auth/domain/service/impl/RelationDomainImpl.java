package com.newzkl.platform.base.biz.auth.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RelationRepository;
import com.newzkl.platform.base.biz.auth.domain.service.RelationDomain;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionRelationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 权限关系领域服务实现
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class RelationDomainImpl implements RelationDomain {

    private final RelationRepository relationRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replace(PermissionEnum.RelationType type, Long sourceId, Collection<Long> targetIds) {
        relationRepository.deleteBySource(type, List.of(sourceId));
        if (CollUtil.isEmpty(targetIds)) {
            return;
        }
        PermissionEnum.Source src = type == PermissionEnum.RelationType.ACCOUNT_PERMISSION
                ? PermissionEnum.Source.ROLE_DERIVED
                : PermissionEnum.Source.DIRECT;
        List<PermissionRelationDTO> dtos = targetIds.stream().distinct().map(t -> {
            PermissionRelationDTO d = new PermissionRelationDTO();
            d.setType(type);
            d.setSourceId(sourceId);
            d.setTargetId(t);
            d.setSource(src);
            return d;
        }).toList();
        relationRepository.insertBatch(dtos);
    }

    @Override
    public List<Long> listTargetIds(PermissionEnum.RelationType type, Long sourceId) {
        return relationRepository.listBySource(type, List.of(sourceId)).stream()
                .map(PermissionRelationDTO::getTargetId).toList();
    }

    @Override
    public List<Long> listSourceIds(PermissionEnum.RelationType type, Long targetId) {
        return relationRepository.listByTarget(type, List.of(targetId)).stream()
                .map(PermissionRelationDTO::getSourceId).toList();
    }
}
