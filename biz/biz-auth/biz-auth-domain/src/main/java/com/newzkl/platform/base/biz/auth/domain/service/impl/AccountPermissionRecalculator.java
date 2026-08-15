package com.newzkl.platform.base.biz.auth.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RelationRepository;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionRelationDTO;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.ddd.model.enums.auth.RelationEnum;
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
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountPermissionRecalculator {

    private final RelationRepository relationRepository;

    /**
     * 重算账号集合的派生权限
     *
     * @param accountIds 账号ID集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void recalc(Collection<Long> accountIds) {
        if (CollUtil.isEmpty(accountIds)) {
            return;
        }

        relationRepository.deleteBySource(RelationEnum.Type.ACCOUNT_PERMISSION, accountIds);

        Map<Long, List<Long>> accRoles = relationRepository
                .listBySource(RelationEnum.Type.ACCOUNT_ROLE, accountIds).stream()
                .collect(Collectors.groupingBy(PermissionRelationDTO::getSourceId,
                        Collectors.mapping(PermissionRelationDTO::getTargetId, Collectors.toList())));

        Set<Long> roleIds = accRoles.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        Map<Long, List<Long>> rolePerms = relationRepository
                .listBySource(RelationEnum.Type.ROLE_PERMISSION, roleIds).stream()
                .collect(Collectors.groupingBy(PermissionRelationDTO::getSourceId,
                        Collectors.mapping(PermissionRelationDTO::getTargetId, Collectors.toList())));

        List<PermissionRelationDTO> toInsert = new ArrayList<>();
        for (Map.Entry<Long, List<Long>> e : accRoles.entrySet()) {
            Long accountId = e.getKey();
            Set<Long> perms = e.getValue().stream()
                    .flatMap(roleId -> rolePerms.getOrDefault(roleId, List.of()).stream())
                    .collect(Collectors.toSet());
            for (Long permId : perms) {
                PermissionRelationDTO d = new PermissionRelationDTO();
                d.setType(RelationEnum.Type.ACCOUNT_PERMISSION);
                d.setSourceId(accountId);
                d.setTargetId(permId);
                d.setSource(RelationEnum.Source.ROLE_DERIVED);
                toInsert.add(d);
            }
        }
        relationRepository.insertBatch(toInsert);

        for (Long id : accountIds) {
            RedisUtil.del(RedisEnum.Key.ACCOUNT_PERM.getCode(id), RedisEnum.Key.ACCOUNT_ROLE.getCode(id));
        }
        log.info("[recalc] done accountIds={} insert={}", accountIds, toInsert.size());
    }
}
