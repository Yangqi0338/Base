package com.newzkl.platform.base.biz.auth.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.PermissionRepository;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RelationRepository;
import com.newzkl.platform.base.biz.auth.domain.service.PermissionDomain;
import com.newzkl.platform.base.biz.auth.model.permission.req.PermissionQuery;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
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
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
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
        AccountEnum.Client client = SecurityUtils.getClient();
        if (req.getType() != PermissionEnum.Type.MENU) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "仅支持创建菜单权限");
        }
        if (permissionRepository.getByCode(client, req.getCode()) != null) {
            throw new PlatformException(BaseErrorCode.EXIST_DATA, "权限 code");
        }
        PermissionDTO dto = new PermissionDTO();
        dto.setClient(client);
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
    public List<PermissionTreeVO> tree(AccountEnum.Client client, PermissionEnum.Type type) {
        List<PermissionDTO> all = permissionRepository.listByType(client, type);
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
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
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
        AccountEnum.Client client = SecurityUtils.getClient();
        Map<String, Long> codeToId = new HashMap<>();
        permissionRepository.listByType(client, PermissionEnum.Type.MENU)
                .forEach(d -> codeToId.put(d.getCode(), d.getId()));
        int[] count = {0};
        importRecursive(client, menus, 0L, codeToId, count);
        return count[0];
    }

    private void importRecursive(AccountEnum.Client client, List<PermissionDTO> nodes, Long parentId, Map<String, Long> codeToId, int[] count) {
        for (PermissionDTO m : nodes) {
            m.setClient(client);
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
                importRecursive(client, m.getChildren(), id, codeToId, count);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncAccountsByPermission(Long permissionId) {
        AccountEnum.Client client = SecurityUtils.getClient();
        if (permissionRepository.getById(permissionId) == null) {
            throw new PlatformException(BaseErrorCode.NODATA, "权限");
        }
        List<String> roleCodes = relationRepository
                .listByTarget(client, PermissionEnum.RelationType.ROLE_PERMISSION, List.of(String.valueOf(permissionId)))
                .stream().map(PermissionRelationDTO::getSource).distinct().toList();
        if (roleCodes.isEmpty()) {
            return 0;
        }
        Set<Long> accountIds = relationRepository
                .listByTarget(client, PermissionEnum.RelationType.ACCOUNT_ROLE, roleCodes)
                .stream().map(r -> Long.valueOf(r.getSource())).collect(Collectors.toSet());
        if (accountIds.isEmpty()) {
            return 0;
        }
        recalculator.recalc(client, accountIds);
        return accountIds.size();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SyncResult syncFunc(List<PermissionDTO> classNodes) {
        // 扫描器已按端展开: 每个 classNode 自带 client, 同端一棵树。按端分组后逐端 upsert + 清孤儿, 各端 (client,code) 独立
        Map<AccountEnum.Client, List<PermissionDTO>> byClient = classNodes.stream()
                .collect(Collectors.groupingBy(PermissionDTO::getClient));
        int[] createUpdateDeleted = {0, 0, 0};
        for (Map.Entry<AccountEnum.Client, List<PermissionDTO>> e : byClient.entrySet()) {
            syncFuncForClient(e.getKey(), e.getValue(), createUpdateDeleted);
        }
        return new SyncResult(createUpdateDeleted[0], createUpdateDeleted[1], createUpdateDeleted[2]);
    }

    @Override
    public Page<PermissionVO> page(PermissionQuery query) {
        AccountEnum.Client client = SecurityUtils.getClient();
        query.setClient(client);
        Page<PermissionDTO> page = permissionRepository.page(query);

        return TransferUtils.transferPage(page, PermissionVO.class);
    }

    /**
     * 同步单端功能权限: upsert 命中节点, 软删该端孤儿并重算受影响账号
     *
     * @param client              所属端
     * @param classNodes          该端类节点树
     * @param createUpdateDeleted 累计计数 [新增, 更新, 软删]
     */
    private void syncFuncForClient(AccountEnum.Client client, List<PermissionDTO> classNodes, int[] createUpdateDeleted) {
        Map<String, PermissionDTO> existing = permissionRepository.listByType(client, PermissionEnum.Type.FUNC)
                .stream().collect(Collectors.toMap(PermissionDTO::getCode, d -> d));
        Set<String> seenCodes = new HashSet<>();
        int[] createUpdate = {0, 0};

        for (PermissionDTO classNode : classNodes) {
            Long classId = upsertFunc(client, existing, classNode.getCode(), 0L, classNode.getRoute(),
                    classNode.getName(), seenCodes, createUpdate);
            if (CollUtil.isNotEmpty(classNode.getChildren())) {
                for (PermissionDTO methodNode : classNode.getChildren()) {
                    upsertFunc(client, existing, methodNode.getCode(), classId, methodNode.getRoute(),
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
            List<String> orphanKeys = orphanIds.stream().map(String::valueOf).toList();
            List<String> affectedRoleCodes = relationRepository
                    .listByTarget(client, PermissionEnum.RelationType.ROLE_PERMISSION, orphanKeys).stream()
                    .map(PermissionRelationDTO::getSource).distinct().toList();
            if (!affectedRoleCodes.isEmpty()) {
                List<Long> affectedAccounts = relationRepository
                        .listByTarget(client, PermissionEnum.RelationType.ACCOUNT_ROLE, affectedRoleCodes).stream()
                        .map(r -> Long.valueOf(r.getSource())).distinct().toList();
                if (!affectedAccounts.isEmpty()) {
                    recalculator.recalc(client, affectedAccounts);
                }
            }
        }
        createUpdateDeleted[0] += createUpdate[0];
        createUpdateDeleted[1] += createUpdate[1];
        createUpdateDeleted[2] += orphanIds.size();
    }

    private Long upsertFunc(AccountEnum.Client client, Map<String, PermissionDTO> existing, String code, Long pid, String route,
                            String name, Set<String> seenCodes, int[] createUpdate) {
        seenCodes.add(code);
        PermissionDTO old = existing.get(code);
        if (old == null) {
            PermissionDTO dto = new PermissionDTO();
            dto.setClient(client);
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
