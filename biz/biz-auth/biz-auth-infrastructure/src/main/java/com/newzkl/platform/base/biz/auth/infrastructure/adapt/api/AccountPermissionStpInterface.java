package com.newzkl.platform.base.biz.auth.infrastructure.adapt.api;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.biz.auth.domain.adapt.repository.RelationRepository;
import com.newzkl.platform.base.biz.auth.model.permission.dto.PermissionRelationDTO;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;
import com.newzkl.platform.base.common.ddd.model.properties.AuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
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
    public static final long TTL_SECONDS = 1800L;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginId id = parse(loginId);
        String cacheKey = RedisEnum.Key.ACCOUNT_PERM.getCode(id.client().getCode(), id.accountId());
        List<String> permissionList = RedisUtil.get(cacheKey);
        if (permissionList != null) {
            return permissionList;
        }

        List<String> roleList = getRoleList(loginId, loginType);
        if (CollUtil.contains(roleList, AuthProperties.superAdminRoleCode)) {
            permissionList = CollUtil.newArrayList("*");
        }else {
            List<PermissionRelationDTO> rels = relationRepository.listBySource(
                    id.client(), PermissionEnum.RelationType.ACCOUNT_PERMISSION, List.of(String.valueOf(id.accountId())));
            permissionList = rels.stream().map(PermissionRelationDTO::getTarget).collect(Collectors.toList());
        }

        RedisUtil.set(cacheKey, permissionList, TTL_SECONDS, TimeUnit.SECONDS);
        return permissionList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginId id = parse(loginId);
        String cacheKey = RedisEnum.Key.ACCOUNT_ROLE.getCode(id.client().getCode(), id.accountId());
        List<String> cached = RedisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        List<PermissionRelationDTO> rels = relationRepository.listBySource(
                id.client(), PermissionEnum.RelationType.ACCOUNT_ROLE, List.of(String.valueOf(id.accountId())));
        List<String> codes = rels.stream().map(PermissionRelationDTO::getTarget).collect(Collectors.toList());
        RedisUtil.set(cacheKey, codes, TTL_SECONDS, TimeUnit.SECONDS);
        return codes;
    }

    /**
     * 切割 loginId, 登录端以 {@code accountId#clientCode} 拼接
     *
     * @param loginId Sa-Token loginId
     * @return 账号ID + 端解析结果
     */
    private LoginId parse(Object loginId) {
        List<String> raw = StrUtil.split(loginId.toString(), "#");
        return new LoginId(Long.parseLong(CollUtil.getFirst(raw)), AccountEnum.Client.getByCode(CollUtil.getLast(raw)));
    }

    /**
     * loginId 解析结果
     *
     * @param accountId  账号ID
     * @param client     端枚举, 用于关系查询
     */
    private record LoginId(Long accountId, AccountEnum.Client client) {
    }
}
