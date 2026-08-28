package com.newzkl.platform.base.biz.account.domain.adapt.api;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;

import java.util.List;
import java.util.Map;

public interface PermissionApi {
    Map<Long, List<Long>> findRoleByAccountIdList(AccountEnum.Client client, List<Long> accountIdList);

    /**
     * 为账号绑定角色, 全量替换该账号在指定端下的角色集合并重算派生权限
     *
     * @param client     所属端
     * @param accountId  账号ID
     * @param roleIdList 角色ID集合, 空集视为清空
     */
    void bindRoles(AccountEnum.Client client, Long accountId, List<Long> roleIdList);

    /**
     * 追加绑定指定端超级管理员角色到账号并重算派生权限
     *
     * <p>按 (client, code=SUPER_ADMIN) 解析角色, 追加(非替换)到账号既有角色集合。
     * 端无该角色则静默跳过</p>
     *
     * @param client    所属端
     * @param accountId 账号ID
     */
    void bindSuperAdmin(AccountEnum.Client client, Long accountId);

}
