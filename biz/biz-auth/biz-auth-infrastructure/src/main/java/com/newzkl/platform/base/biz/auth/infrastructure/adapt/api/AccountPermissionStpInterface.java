package com.newzkl.platform.base.biz.auth.infrastructure.adapt.api;

import cn.dev33.satoken.stp.StpInterface;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.PermissionRepository;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RelationRepository;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RoleRepository;
import com.newzkl.platform.base.biz.auth.domain.support.PermissionCacheKeys;
import com.newzkl.platform.base.common.ddd.model.enums.auth.RelationEnum;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionDTO;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionRelationDTO;
import com.newzkl.platform.base.biz.auth.model.role.dto.RoleDTO;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Sa-Token 权限接入
 *
 * <p>账号 → 权限/角色 code 列表, 拉取模型: 先读 Redis 缓存, 未命中查派生关系表回填并缓存,
 * 平台管理员 (RoleEnum.CompanyRole.PLATFORM) 直返通配 {@code *}</p>
 *
 * @author KC
 */
@Component
@RequiredArgsConstructor
public class AccountPermissionStpInterface implements StpInterface {

    private final RelationRepository relationRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        long accountId = parseId(loginId);
        String cacheKey = PermissionCacheKeys.accountPerm(accountId);
        List<String> cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        if (SecurityUtils.getRole() == RoleEnum.CompanyRole.PLATFORM) {
            List<String> wildcard = List.of("*");
            RedisUtil.set(cacheKey, wildcard, PermissionCacheKeys.TTL_SECONDS, TimeUnit.SECONDS);
            return wildcard;
        }

        List<PermissionRelationDTO> rels = relationRepository.listBySource(
                RelationEnum.Type.ACCOUNT_PERMISSION, List.of(accountId));
        Set<Long> permIds = rels.stream().map(PermissionRelationDTO::getTargetId).collect(Collectors.toSet());
        if (permIds.isEmpty()) {
            RedisUtil.set(cacheKey, List.of(), PermissionCacheKeys.TTL_SECONDS, TimeUnit.SECONDS);
            return List.of();
        }
        List<String> codes = permissionRepository.listByIds(permIds).stream()
                .map(PermissionDTO::getCode)
                .toList();
        RedisUtil.set(cacheKey, codes, PermissionCacheKeys.TTL_SECONDS, TimeUnit.SECONDS);
        return codes;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        long accountId = parseId(loginId);
        String cacheKey = PermissionCacheKeys.accountRole(accountId);
        List<String> cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        List<PermissionRelationDTO> rels = relationRepository.listBySource(
                RelationEnum.Type.ACCOUNT_ROLE, List.of(accountId));
        Set<Long> roleIds = rels.stream().map(PermissionRelationDTO::getTargetId).collect(Collectors.toSet());
        List<String> codes = roleRepository.listByIds(roleIds).stream()
                .map(RoleDTO::getCode).toList();
        RedisUtil.set(cacheKey, codes, PermissionCacheKeys.TTL_SECONDS, TimeUnit.SECONDS);
        return codes;
    }

    private long parseId(Object loginId) {
        if (loginId instanceof Number n) {
            return n.longValue();
        }
        return Long.parseLong(loginId.toString());
    }
}
