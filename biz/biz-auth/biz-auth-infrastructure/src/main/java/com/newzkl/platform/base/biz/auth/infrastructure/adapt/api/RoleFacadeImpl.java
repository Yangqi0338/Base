package com.newzkl.platform.base.biz.auth.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RelationRepository;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RoleRepository;
import com.newzkl.platform.base.biz.auth.domain.service.RoleDomain;
import com.newzkl.platform.base.biz.auth.facade.RoleFacade;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionRelationDTO;
import com.newzkl.platform.base.biz.auth.model.permission.dto.RoleDTO;
import com.newzkl.platform.base.common.core.utils.common.CommonUtil;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoleFacadeImpl implements RoleFacade {

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleDomain roleDomain;

    @Override
    public Map<Long, List<Long>> findRoleByAccount(AccountEnum.Client client, List<Long> accountIdList) {
        List<String> accountKeys = accountIdList.stream().map(String::valueOf).toList();
        List<PermissionRelationDTO> relationDTOS = relationRepository.listBySource(client, PermissionEnum.RelationType.ACCOUNT_ROLE, accountKeys);
        // relation 角色侧存 code, 对外契约仍返 roleId, 按端全量角色建 code→id 映射反查
        Map<String, Long> codeToId = roleRepository.listAll(client).stream()
                .collect(Collectors.toMap(RoleDTO::getCode, RoleDTO::getId, (a, b) -> a));
        return accountIdList.stream().collect(CommonUtil.toKeyMap((accountId) -> {
            String accountKey = String.valueOf(accountId);
            return relationDTOS.stream().filter(item -> item.getSource().equals(accountKey))
                    .map(PermissionRelationDTO::getTarget)
                    .map(codeToId::get)
                    .filter(Objects::nonNull)
                    .toList();
        }));
    }

    @Override
    public Map<Long, List<String>> findRoleCodeByAccount(AccountEnum.Client client, List<Long> accountIdList) {
        List<String> accountKeys = accountIdList.stream().map(String::valueOf).toList();
        List<PermissionRelationDTO> relationDTOS = relationRepository.listBySource(client, PermissionEnum.RelationType.ACCOUNT_ROLE, accountKeys);
        // relation 角色侧本就存 code, 直接返 target, 不做 code→id 反查
        return accountIdList.stream().collect(CommonUtil.toKeyMap((accountId) -> {
            String accountKey = String.valueOf(accountId);
            return relationDTOS.stream().filter(item -> item.getSource().equals(accountKey))
                    .map(PermissionRelationDTO::getTarget)
                    .toList();
        }));
    }

    @Override
    public void bindRoles(AccountEnum.Client client, Long accountId, List<Long> roleIdList) {
        roleDomain.bindRoles(client, accountId, roleIdList);
    }

    @Override
    public void bindSuperAdmin(AccountEnum.Client client, Long accountId) {
        roleDomain.bindSuperAdmin(client, accountId);
    }
}
