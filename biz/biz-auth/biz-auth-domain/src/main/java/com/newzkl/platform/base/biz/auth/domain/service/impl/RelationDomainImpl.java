package com.newzkl.platform.base.biz.auth.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RelationRepository;
import com.newzkl.platform.base.biz.auth.domain.service.RelationDomain;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
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
    public void replace(AccountEnum.Client client, PermissionEnum.RelationType type, String source, Collection<String> targets) {
        relationRepository.deleteBySource(client, type, List.of(source));
        if (CollUtil.isEmpty(targets)) {
            return;
        }
        PermissionEnum.Source origin = type == PermissionEnum.RelationType.ACCOUNT_PERMISSION
                ? PermissionEnum.Source.ROLE_DERIVED
                : PermissionEnum.Source.DIRECT;
        List<PermissionRelationDTO> dtos = targets.stream().distinct().map(t -> {
            PermissionRelationDTO d = new PermissionRelationDTO();
            d.setClient(client);
            d.setType(type);
            d.setSource(source);
            d.setTarget(t);
            d.setOrigin(origin);
            return d;
        }).toList();
        relationRepository.insertBatch(dtos);
    }

    @Override
    public List<String> listTargets(AccountEnum.Client client, PermissionEnum.RelationType type, String source) {
        return relationRepository.listBySource(client, type, List.of(source)).stream()
                .map(PermissionRelationDTO::getTarget).toList();
    }

    @Override
    public List<String> listSources(AccountEnum.Client client, PermissionEnum.RelationType type, String target) {
        return relationRepository.listByTarget(client, type, List.of(target)).stream()
                .map(PermissionRelationDTO::getSource).toList();
    }
}
