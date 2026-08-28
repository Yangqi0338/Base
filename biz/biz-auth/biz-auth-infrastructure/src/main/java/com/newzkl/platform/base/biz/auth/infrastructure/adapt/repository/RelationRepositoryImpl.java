package com.newzkl.platform.base.biz.auth.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RelationRepository;
import com.newzkl.platform.base.biz.auth.infrastructure.dao.PermissionRelationDAO;
import com.newzkl.platform.base.biz.auth.infrastructure.entity.PermissionRelationDO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
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
    public void deleteBySource(AccountEnum.Client client, PermissionEnum.RelationType type, Collection<String> sources) {
        if (CollUtil.isEmpty(sources)) {
            return;
        }
        permissionRelationDAO.delete(permissionRelationDAO.getLw(client, type, sources));
    }

    @Override
    public void deleteByTarget(AccountEnum.Client client, PermissionEnum.RelationType type, Collection<String> targets) {
        if (CollUtil.isEmpty(targets)) {
            return;
        }
        permissionRelationDAO.delete(permissionRelationDAO.getLwByTarget(client, type, targets));
    }

    @Override
    public List<PermissionRelationDTO> listBySource(AccountEnum.Client client, PermissionEnum.RelationType type, Collection<String> sources) {
        if (CollUtil.isEmpty(sources)) {
            return List.of();
        }
        List<PermissionRelationDO> list = permissionRelationDAO.selectList(permissionRelationDAO.getLw(client, type, sources));
        return TransferUtils.transfers(list, PermissionRelationDTO::new);
    }

    @Override
    public List<PermissionRelationDTO> listByTarget(AccountEnum.Client client, PermissionEnum.RelationType type, Collection<String> targets) {
        if (CollUtil.isEmpty(targets)) {
            return List.of();
        }
        List<PermissionRelationDO> list = permissionRelationDAO.selectList(permissionRelationDAO.getLwByTarget(client, type, targets));
        return TransferUtils.transfers(list, PermissionRelationDTO::new);
    }
}
