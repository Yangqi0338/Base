package com.newzkl.platform.base.biz.auth.domain.support;

import com.newzkl.platform.base.common.core.redis.RedisEnum;

/**
 * 权限缓存 key 集合
 *
 * <p>key 统一收拢至 {@link RedisEnum.Key} 管理(COMMON 无模块前缀, 保持旧 key 值兼容)。</p>
 *
 * @author KC
 */
public final class PermissionCacheKeys {

    /**
     * 账号权限缓存 TTL, 单位秒
     */
    public static final long TTL_SECONDS = 1800L;

    private PermissionCacheKeys() {
    }

    /**
     * 账号权限 code 列表缓存 key
     *
     * @param accountId 账号ID
     * @return 缓存 key
     * @ext key = perm:account:{accountId}
     */
    public static String accountPerm(long accountId) {
        return RedisEnum.Key.ACCOUNT_PERM.getCode(accountId);
    }

    /**
     * 账号角色 code 列表缓存 key
     *
     * @param accountId 账号ID
     * @return 缓存 key
     * @ext key = role:account:{accountId}
     */
    public static String accountRole(long accountId) {
        return RedisEnum.Key.ACCOUNT_ROLE.getCode(accountId);
    }
}