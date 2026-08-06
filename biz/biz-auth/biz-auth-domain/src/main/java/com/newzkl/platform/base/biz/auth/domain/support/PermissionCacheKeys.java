package com.newzkl.platform.base.biz.auth.domain.support;

/**
 * 权限缓存 key 集合
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
        return "perm:account:" + accountId;
    }

    /**
     * 账号角色 code 列表缓存 key
     *
     * @param accountId 账号ID
     * @return 缓存 key
     * @ext key = role:account:{accountId}
     */
    public static String accountRole(long accountId) {
        return "role:account:" + accountId;
    }
}
