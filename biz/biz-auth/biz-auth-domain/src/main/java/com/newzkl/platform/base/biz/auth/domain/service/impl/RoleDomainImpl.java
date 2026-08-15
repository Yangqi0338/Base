package com.newzkl.platform.base.biz.auth.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RelationRepository;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RoleRepository;
import com.newzkl.platform.base.biz.auth.domain.service.RelationDomain;
import com.newzkl.platform.base.biz.auth.domain.service.RoleDomain;
import com.newzkl.platform.base.biz.auth.model.permission.dto.RoleDTO;
import com.newzkl.platform.base.biz.auth.model.permission.req.RoleQuery;
import com.newzkl.platform.base.biz.auth.model.permission.req.RoleReq;
import com.newzkl.platform.base.biz.auth.model.permission.vo.RoleVO;
import com.newzkl.platform.base.common.ddd.model.enums.auth.RelationEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionRelationDTO;

import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色领域服务实现
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class RoleDomainImpl implements RoleDomain {

    private final RoleRepository roleRepository;
    private final RelationRepository relationRepository;
    private final RelationDomain relationDomain;
    private final AccountPermissionRecalculator recalculator;

    @Override
    public Long create(RoleReq req) {
        if (roleRepository.getByCode(req.getCode()) != null) {
            throw new PlatformException(BaseErrorCode.EXIST_DATA, "角色 code");
        }
        RoleDTO dto = new RoleDTO();
        dto.setCode(req.getCode());
        dto.setName(req.getName());
        dto.setDescription(req.getDescription());
        dto.setSort(req.getSort());
        return roleRepository.insert(dto);
    }

    @Override
    public void update(RoleReq req) {
        RoleDTO dto = roleRepository.getById(req.getId());
        if (dto == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "角色");
        }
        dto.setName(req.getName());
        dto.setDescription(req.getDescription());
        dto.setSort(req.getSort());
        roleRepository.update(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        RoleDTO dto = roleRepository.getById(id);
        if (dto == null) {
            return;
        }
        List<Long> affectedAccountIds = relationDomain.listSourceIds(RelationEnum.Type.ACCOUNT_ROLE, id);
        relationRepository.deleteByTarget(RelationEnum.Type.ACCOUNT_ROLE, List.of(id));
        relationRepository.deleteBySource(RelationEnum.Type.ROLE_PERMISSION, List.of(id));
        roleRepository.deleteById(id);
        recalculator.recalc(affectedAccountIds);
    }

    @Override
    public RoleVO detail(Long id) {
        RoleDTO dto = roleRepository.getById(id);
        if (dto == null) {
            return null;
        }
        RoleVO vo = TransferUtils.transfer(dto, RoleVO::new);
        List<Long> accountIds = relationDomain.listSourceIds(RelationEnum.Type.ACCOUNT_ROLE, id);
        List<Long> permIds = relationDomain.listTargetIds(RelationEnum.Type.ROLE_PERMISSION, id);
        vo.setAccountIds(accountIds);
        vo.setPermissionIds(permIds);
        vo.setAccountCount(accountIds.size());
        vo.setPermissionCount(permIds.size());
        vo.setHalfCheckedPermissionIds(List.of());
        return vo;
    }

    @Override
    public Page<RoleVO> page(RoleQuery query) {
        Page<RoleDTO> page = roleRepository.page(query);
        Page<RoleVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<RoleVO> records = page.getRecords().stream().map(d -> {
            RoleVO vo = TransferUtils.transfer(d, RoleVO::new);
            vo.setAccountCount(relationDomain.listSourceIds(RelationEnum.Type.ACCOUNT_ROLE, d.getId()).size());
            vo.setPermissionCount(relationDomain.listTargetIds(RelationEnum.Type.ROLE_PERMISSION, d.getId()).size());
            return vo;
        }).toList();
        voPage.setRecords(records);
        return voPage;
    }

    @Override
    public List<RoleVO> listAll() {
        return TransferUtils.transfers(roleRepository.listAll(), RoleVO::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindAccounts(Long roleId, Collection<Long> accountIds) {
        RoleDTO role = roleRepository.getById(roleId);
        if (role == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "角色");
        }
        Set<Long> oldBound = new HashSet<>(relationDomain.listSourceIds(RelationEnum.Type.ACCOUNT_ROLE, roleId));
        Set<Long> newBound = accountIds == null ? new HashSet<>() : new HashSet<>(accountIds);
        Set<Long> affected = new HashSet<>(oldBound);
        affected.addAll(newBound);

        relationRepository.deleteByTarget(RelationEnum.Type.ACCOUNT_ROLE, List.of(roleId));
        if (CollUtil.isNotEmpty(newBound)) {
            List<PermissionRelationDTO> toInsert = newBound.stream().distinct().map(aid -> {
                PermissionRelationDTO d = new PermissionRelationDTO();
                d.setType(RelationEnum.Type.ACCOUNT_ROLE);
                d.setSourceId(aid);
                d.setTargetId(roleId);
                d.setSource(RelationEnum.Source.DIRECT);
                return d;
            }).toList();
            relationRepository.insertBatch(toInsert);
        }
        recalculator.recalc(affected);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(Long roleId, Collection<Long> permissionIds) {
        relationDomain.replace(RelationEnum.Type.ROLE_PERMISSION, roleId, permissionIds);
        List<Long> affected = relationDomain.listSourceIds(RelationEnum.Type.ACCOUNT_ROLE, roleId);
        recalculator.recalc(affected);
    }
}
