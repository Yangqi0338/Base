package com.newzkl.platform.base.biz.auth.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RelationRepository;
import com.newzkl.platform.base.biz.auth.infrastructure.dao.PermissionRelationDAO;
import com.newzkl.platform.base.biz.auth.infrastructure.entity.PermissionRelationDO;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionRelationDTO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 权限关系仓储实现
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class RelationRepositoryImpl implements RelationRepository {

    private final PermissionRelationDAO permissionRelationDAO;

    @Override
    public void insertBatch(List<PermissionRelationDTO> dtos) {
        if (CollUtil.isEmpty(dtos)) {
            return;
        }
        for (PermissionRelationDTO dto : dtos) {
            permissionRelationDAO.insert(TransferUtils.transfer(dto, PermissionRelationDO::new));
        }
    }

    @Override
    public void deleteBySource(PermissionEnum.RelationType type, Collection<Long> sourceIds) {
        if (CollUtil.isEmpty(sourceIds)) {
            return;
        }
        permissionRelationDAO.delete(permissionRelationDAO.getLw(type, sourceIds));
    }

    @Override
    public void deleteByTarget(PermissionEnum.RelationType type, Collection<Long> targetIds) {
        if (CollUtil.isEmpty(targetIds)) {
            return;
        }
        permissionRelationDAO.delete(permissionRelationDAO.getLwByTarget(type, targetIds));
    }

    @Override
    public List<PermissionRelationDTO> listBySource(PermissionEnum.RelationType type, Collection<Long> sourceIds) {
        if (CollUtil.isEmpty(sourceIds)) {
            return List.of();
        }
        List<PermissionRelationDO> list = permissionRelationDAO.selectList(permissionRelationDAO.getLw(type, sourceIds));
        return TransferUtils.transfers(list, PermissionRelationDTO::new);
    }

    @Override
    public List<PermissionRelationDTO> listByTarget(PermissionEnum.RelationType type, Collection<Long> targetIds) {
        if (CollUtil.isEmpty(targetIds)) {
            return List.of();
        }
        List<PermissionRelationDO> list = permissionRelationDAO.selectList(permissionRelationDAO.getLwByTarget(type, targetIds));
        return TransferUtils.transfers(list, PermissionRelationDTO::new);
    }
}
