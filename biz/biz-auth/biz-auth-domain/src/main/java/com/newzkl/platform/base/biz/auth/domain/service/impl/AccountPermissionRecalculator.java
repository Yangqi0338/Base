package com.newzkl.platform.base.biz.auth.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RelationRepository;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionRelationDTO;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 账号权限派生重算
 *
 * <p>account → ACCOUNT_ROLE → role → ROLE_PERMISSION → permission, 重建 ACCOUNT_PERMISSION 派生表并驱逐 Redis 缓存,
 * 软删权限由 @TableLogic 自动过滤, 无需手工判 deleted。</p>
 *
 * <p>端隔离: 派生关系与 Redis 缓存 key 均带 client, 保证各端账号权限互不串扰</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountPermissionRecalculator {

    private final RelationRepository relationRepository;

    /**
     * 重算某端账号集合的派生权限
     *
     * @param client     所属端
     * @param accountIds 账号ID集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void recalc(AccountEnum.Client client, Collection<Long> accountIds) {
        if (CollUtil.isEmpty(accountIds)) {
            return;
        }

        List<String> accountKeys = accountIds.stream().map(String::valueOf).toList();
        relationRepository.deleteBySource(client, PermissionEnum.RelationType.ACCOUNT_PERMISSION, accountKeys);

        // account(id 字符串) → role(code): source=accountId, target=roleCode
        Map<String, List<String>> accRoles = relationRepository
                .listBySource(client, PermissionEnum.RelationType.ACCOUNT_ROLE, accountKeys).stream()
                .collect(Collectors.groupingBy(PermissionRelationDTO::getSource,
                        Collectors.mapping(PermissionRelationDTO::getTarget, Collectors.toList())));

        // role(code) → permission(id 字符串): source=roleCode, target=permId
        Set<String> roleCodes = accRoles.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        Map<String, List<String>> rolePerms = relationRepository
                .listBySource(client, PermissionEnum.RelationType.ROLE_PERMISSION, roleCodes).stream()
                .collect(Collectors.groupingBy(PermissionRelationDTO::getSource,
                        Collectors.mapping(PermissionRelationDTO::getTarget, Collectors.toList())));

        List<PermissionRelationDTO> toInsert = new ArrayList<>();
        for (Map.Entry<String, List<String>> e : accRoles.entrySet()) {
            String accountKey = e.getKey();
            Set<String> perms = e.getValue().stream()
                    .flatMap(roleCode -> rolePerms.getOrDefault(roleCode, List.of()).stream())
                    .collect(Collectors.toSet());
            for (String permKey : perms) {
                PermissionRelationDTO d = new PermissionRelationDTO();
                d.setClient(client);
                d.setType(PermissionEnum.RelationType.ACCOUNT_PERMISSION);
                d.setSource(accountKey);
                d.setTarget(permKey);
                d.setOrigin(PermissionEnum.Source.ROLE_DERIVED);
                toInsert.add(d);
            }
        }
        relationRepository.insertBatch(toInsert);

        String clientCode = client.getCode();
        for (Long id : accountIds) {
            RedisUtil.del(RedisEnum.Key.ACCOUNT_PERM.getCode(clientCode, id), RedisEnum.Key.ACCOUNT_ROLE.getCode(clientCode, id));
        }
        log.info("[recalc] done client={} accountIds={} insert={}", clientCode, accountIds, toInsert.size());
    }
}
