package com.newzkl.platform.base.biz.auth.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.PermissionRepository;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RelationRepository;
import com.newzkl.platform.base.biz.auth.domain.service.PermissionDomain;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionDTO;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionListDTO;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionRelationDTO;
import com.newzkl.platform.base.biz.auth.model.permission.req.PermissionReq;
import com.newzkl.platform.base.biz.auth.model.permission.vo.PermissionTreeVO;
import com.newzkl.platform.base.biz.auth.model.permission.vo.PermissionVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限领域服务实现
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class PermissionDomainImpl implements PermissionDomain {

    private final PermissionRepository permissionRepository;
    private final RelationRepository relationRepository;
    private final AccountPermissionRecalculator recalculator;

    @Override
    public Long create(PermissionReq req) {
        if (req.getType() != PermissionEnum.Type.MENU) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "仅支持创建菜单权限");
        }
        if (permissionRepository.getByCode(req.getCode()) != null) {
            throw new PlatformException(BaseErrorCode.EXIST_DATA, "权限 code");
        }
        PermissionDTO dto = new PermissionDTO();
        dto.setPid(req.getPid());
        dto.setType(PermissionEnum.Type.MENU);
        dto.setCode(req.getCode());
        dto.setName(req.getName());
        dto.setRoute(req.getRoute());
        dto.setIcon(req.getIcon());
        dto.setSort(req.getSort());
        return permissionRepository.insert(dto);
    }

    @Override
    public void update(PermissionReq req) {
        PermissionDTO existing = permissionRepository.getById(req.getId());
        if (existing == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "权限");
        }
        if (existing.getType() == PermissionEnum.Type.FUNC) {
            existing.setName(req.getName());
            existing.setSort(req.getSort());
        } else {
            existing.setPid(req.getPid());
            existing.setName(req.getName());
            existing.setRoute(req.getRoute());
            existing.setIcon(req.getIcon());
            existing.setSort(req.getSort());
        }
        permissionRepository.update(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        PermissionDTO existing = permissionRepository.getById(id);
        if (existing == null) {
            return;
        }
        if (existing.getType() == PermissionEnum.Type.FUNC) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "功能权限不可手工删除, 请通过同步");
        }
        permissionRepository.softDeleteByIds(List.of(id));
    }

    @Override
    public PermissionVO detail(Long id) {
        return TransferUtils.transfer(permissionRepository.getById(id), PermissionVO::new);
    }

    @Override
    public List<PermissionTreeVO> tree(PermissionEnum.Type type) {
        List<PermissionDTO> all = permissionRepository.listByType(type);
        Map<Long, PermissionTreeVO> idMap = all.stream()
                .collect(Collectors.toMap(PermissionDTO::getId, d -> TransferUtils.transfer(d, PermissionTreeVO::new)));
        List<PermissionTreeVO> roots = new ArrayList<>();
        for (PermissionDTO dto : all) {
            PermissionTreeVO node = idMap.get(dto.getId());
            if (dto.getPid() == null || dto.getPid() == 0L) {
                roots.add(node);
            } else {
                PermissionTreeVO parent = idMap.get(dto.getPid());
                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    roots.add(node);
                }
            }
        }
        return roots;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importMenu(PermissionListDTO menus) {
        if (CollUtil.isEmpty(menus)) {
            return 0;
        }
        Map<String, Long> codeToId = new HashMap<>();
        permissionRepository.listByType(PermissionEnum.Type.MENU)
                .forEach(d -> codeToId.put(d.getCode(), d.getId()));
        int[] count = {0};
        importRecursive(menus, 0L, codeToId, count);
        return count[0];
    }

    private void importRecursive(List<PermissionDTO> nodes, Long parentId, Map<String, Long> codeToId, int[] count) {
        for (PermissionDTO m : nodes) {
            m.setType(PermissionEnum.Type.MENU);
            m.setPid(parentId);
            if (m.getSort() == null) {
                m.setSort(0);
            }
            Long id = codeToId.get(m.getCode());
            if (id == null) {
                id = permissionRepository.insert(m);
                codeToId.put(m.getCode(), id);
            } else {
                m.setId(id);
                permissionRepository.update(m);
            }
            count[0]++;
            if (CollUtil.isNotEmpty(m.getChildren())) {
                importRecursive(m.getChildren(), id, codeToId, count);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncAccountsByPermission(Long permissionId) {
        if (permissionRepository.getById(permissionId) == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "权限");
        }
        List<Long> roleIds = relationRepository
                .listByTarget(PermissionEnum.RelationType.ROLE_PERMISSION, List.of(permissionId))
                .stream().map(PermissionRelationDTO::getSourceId).distinct().toList();
        if (roleIds.isEmpty()) {
            return 0;
        }
        Set<Long> accountIds = relationRepository
                .listByTarget(PermissionEnum.RelationType.ACCOUNT_ROLE, roleIds)
                .stream().map(PermissionRelationDTO::getSourceId).collect(Collectors.toSet());
        if (accountIds.isEmpty()) {
            return 0;
        }
        recalculator.recalc(accountIds);
        return accountIds.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SyncResult syncFunc(List<PermissionDTO> classNodes) {
        Map<String, PermissionDTO> existing = permissionRepository.listByType(PermissionEnum.Type.FUNC)
                .stream().collect(Collectors.toMap(PermissionDTO::getCode, d -> d));
        Set<String> seenCodes = new HashSet<>();
        int[] createUpdate = {0, 0};

        for (PermissionDTO classNode : classNodes) {
            Long classId = upsertFunc(existing, classNode.getCode(), 0L, classNode.getRoute(),
                    classNode.getName(), seenCodes, createUpdate);
            if (CollUtil.isNotEmpty(classNode.getChildren())) {
                for (PermissionDTO methodNode : classNode.getChildren()) {
                    upsertFunc(existing, methodNode.getCode(), classId, methodNode.getRoute(),
                            methodNode.getName(), seenCodes, createUpdate);
                }
            }
        }

        List<Long> orphanIds = existing.values().stream()
                .filter(d -> !seenCodes.contains(d.getCode()))
                .map(PermissionDTO::getId)
                .toList();
        if (!orphanIds.isEmpty()) {
            permissionRepository.softDeleteByIds(orphanIds);
            List<Long> affectedRoles = relationRepository
                    .listByTarget(PermissionEnum.RelationType.ROLE_PERMISSION, orphanIds).stream()
                    .map(PermissionRelationDTO::getSourceId).distinct().toList();
            if (!affectedRoles.isEmpty()) {
                List<Long> affectedAccounts = relationRepository
                        .listByTarget(PermissionEnum.RelationType.ACCOUNT_ROLE, affectedRoles).stream()
                        .map(PermissionRelationDTO::getSourceId).distinct().toList();
                if (!affectedAccounts.isEmpty()) {
                    recalculator.recalc(affectedAccounts);
                }
            }
        }
        return new SyncResult(createUpdate[0], createUpdate[1], orphanIds.size());
    }

    private Long upsertFunc(Map<String, PermissionDTO> existing, String code, Long pid, String route,
                            String name, Set<String> seenCodes, int[] createUpdate) {
        seenCodes.add(code);
        PermissionDTO old = existing.get(code);
        if (old == null) {
            PermissionDTO dto = new PermissionDTO();
            dto.setType(PermissionEnum.Type.FUNC);
            dto.setCode(code);
            dto.setPid(pid);
            dto.setRoute(route);
            dto.setName(name);
            dto.setSort(0);
            Long id = permissionRepository.insert(dto);
            createUpdate[0]++;
            return id;
        }
        old.setPid(pid);
        old.setRoute(route);
        old.setName(name);
        permissionRepository.update(old);
        createUpdate[1]++;
        return old.getId();
    }
}
