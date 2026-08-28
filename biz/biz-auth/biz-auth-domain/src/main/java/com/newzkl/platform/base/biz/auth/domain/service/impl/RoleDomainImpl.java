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
import com.newzkl.platform.base.biz.auth.model.permission.vo.RoleRes;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionRelationDTO;

import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        AccountEnum.Client client = SecurityUtils.getClient();
        RoleDTO dto = new RoleDTO();
        dto.setClient(client);
        dto.setCode(req.getCode());
        dto.setName(req.getName());
        dto.setDescription(req.getDescription());
        dto.setSort(req.getSort());
        // (client, code) 唯一由 MySQL 引擎兜底, 不先查 exists; 撞键转业务异常
        try {
            return roleRepository.insert(dto);
        } catch (DuplicateKeyException e) {
            throw new PlatformException(BaseErrorCode.EXIST_DATA, "角色 code");
        }
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
        AccountEnum.Client client = SecurityUtils.getClient();
        RoleDTO dto = roleRepository.getById(id);
        if (dto == null) {
            return;
        }
        String roleCode = dto.getCode();
        List<Long> affectedAccountIds = relationDomain.listSources(client, PermissionEnum.RelationType.ACCOUNT_ROLE, roleCode)
                .stream().map(Long::valueOf).toList();
        relationRepository.deleteByTarget(client, PermissionEnum.RelationType.ACCOUNT_ROLE, List.of(roleCode));
        relationRepository.deleteBySource(client, PermissionEnum.RelationType.ROLE_PERMISSION, List.of(roleCode));
        roleRepository.deleteById(id);
        recalculator.recalc(client, affectedAccountIds);
    }

    @Override
    public RoleRes detail(Long id) {
        AccountEnum.Client client = SecurityUtils.getClient();
        RoleDTO dto = roleRepository.getById(id);
        if (dto == null) {
            return null;
        }
        RoleRes vo = TransferUtils.transfer(dto, RoleRes::new);
        String roleCode = dto.getCode();
        List<Long> accountIds = relationDomain.listSources(client, PermissionEnum.RelationType.ACCOUNT_ROLE, roleCode)
                .stream().map(Long::valueOf).toList();
        List<Long> permIds = relationDomain.listTargets(client, PermissionEnum.RelationType.ROLE_PERMISSION, roleCode)
                .stream().map(Long::valueOf).toList();
        vo.setAccountIds(accountIds);
        vo.setPermissionIds(permIds);
//        vo.setAccountCount(accountIds.size());
//        vo.setPermissionCount(permIds.size());
        return vo;
    }

    @Override
    public Page<RoleRes> page(RoleQuery query) {
        AccountEnum.Client client = SecurityUtils.getClient();
        query.setClient(client);
        Page<RoleDTO> page = roleRepository.page(query);
        Page<RoleRes> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<RoleRes> records = page.getRecords().stream().map(d -> {
            RoleRes vo = TransferUtils.transfer(d, RoleRes::new);
//            vo.setAccountCount(relationDomain.listSourceIds(client, PermissionEnum.RelationType.ACCOUNT_ROLE, d.getId()).size());
//            vo.setPermissionCount(relationDomain.listTargetIds(client, PermissionEnum.RelationType.ROLE_PERMISSION, d.getId()).size());
            return vo;
        }).toList();
        voPage.setRecords(records);
        return voPage;
    }

    @Override
    public List<RoleRes> listAll() {
        return TransferUtils.transfers(roleRepository.listAll(SecurityUtils.getClient()), RoleRes::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindAccounts(Long roleId, Collection<Long> accountIds) {
        AccountEnum.Client client = SecurityUtils.getClient();
        RoleDTO role = roleRepository.getById(roleId);
        if (role == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "角色");
        }
        // 端隔离: 只能绑定同端账号, 角色本身须属当前端
        if (role.getClient() != client) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "角色不属当前端, 禁止跨端绑定");
        }
        String roleCode = role.getCode();
        Set<Long> oldBound = relationDomain.listSources(client, PermissionEnum.RelationType.ACCOUNT_ROLE, roleCode)
                .stream().map(Long::valueOf).collect(Collectors.toSet());
        Set<Long> newBound = accountIds == null ? new HashSet<>() : new HashSet<>(accountIds);
        Set<Long> affected = new HashSet<>(oldBound);
        affected.addAll(newBound);

        relationRepository.deleteByTarget(client, PermissionEnum.RelationType.ACCOUNT_ROLE, List.of(roleCode));
        if (CollUtil.isNotEmpty(newBound)) {
            List<PermissionRelationDTO> toInsert = newBound.stream().distinct().map(aid -> {
                PermissionRelationDTO d = new PermissionRelationDTO();
                d.setClient(client);
                d.setType(PermissionEnum.RelationType.ACCOUNT_ROLE);
                d.setSource(String.valueOf(aid));
                d.setTarget(roleCode);
                d.setOrigin(PermissionEnum.Source.DIRECT);
                return d;
            }).toList();
            relationRepository.insertBatch(toInsert);
        }
        recalculator.recalc(client, affected);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(Long roleId, Collection<Long> permissionIds) {
        AccountEnum.Client client = SecurityUtils.getClient();
        RoleDTO role = roleRepository.getById(roleId);
        if (role == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "角色");
        }
        String roleCode = role.getCode();
        Collection<String> permKeys = permissionIds == null ? List.of()
                : permissionIds.stream().map(String::valueOf).toList();
        relationDomain.replace(client, PermissionEnum.RelationType.ROLE_PERMISSION, roleCode, permKeys);
        List<Long> affected = relationDomain.listSources(client, PermissionEnum.RelationType.ACCOUNT_ROLE, roleCode)
                .stream().map(Long::valueOf).toList();
        recalculator.recalc(client, affected);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindRoles(AccountEnum.Client client, Long accountId, Collection<Long> roleIds) {
        Set<Long> targetRoleIds = roleIds == null ? new HashSet<>() : new HashSet<>(roleIds);
        Set<String> roleCodes = new HashSet<>();
        // 端隔离: 逐一校验角色存在且属当前端, 禁止绑定跨端或不存在角色
        if (CollUtil.isNotEmpty(targetRoleIds)) {
            List<RoleDTO> roles = roleRepository.listByIds(targetRoleIds);
            if (roles.size() != targetRoleIds.size()) {
                throw new PlatformException(BaseErrorCode.NODATA, "角色");
            }
            boolean crossClient = roles.stream().anyMatch(r -> r.getClient() != client);
            if (crossClient) {
                throw new PlatformException(BaseErrorCode.CUSTOM, "角色不属当前端, 禁止跨端绑定");
            }
            roles.forEach(r -> roleCodes.add(r.getCode()));
        }
        relationDomain.replace(client, PermissionEnum.RelationType.ACCOUNT_ROLE, String.valueOf(accountId), roleCodes);
        recalculator.recalc(client, List.of(accountId));
    }

    /** 端超级管理员角色 code */
    private static final String SUPER_ADMIN_CODE = "SUPER_ADMIN";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindSuperAdmin(AccountEnum.Client client, Long accountId) {
        RoleDTO role = roleRepository.getByCode(client, SUPER_ADMIN_CODE);
        if (role == null) {
            // 端未配置 SUPER_ADMIN 角色, 静默跳过
            return;
        }
        // 追加语义: 并入既有角色集合, 已绑则幂等
        Set<String> roleCodes = new HashSet<>(
                relationDomain.listTargets(client, PermissionEnum.RelationType.ACCOUNT_ROLE, String.valueOf(accountId)));
        roleCodes.add(role.getCode());
        relationDomain.replace(client, PermissionEnum.RelationType.ACCOUNT_ROLE, String.valueOf(accountId), roleCodes);
        recalculator.recalc(client, List.of(accountId));
    }
}
