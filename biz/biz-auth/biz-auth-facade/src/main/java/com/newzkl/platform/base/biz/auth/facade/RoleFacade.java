package com.newzkl.platform.base.biz.auth.facade;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;

import java.util.List;
import java.util.Map;

public interface RoleFacade {

    Map<Long, List<Long>> findRoleByAccount(AccountEnum.Client client, List<Long> accountIdList);

    /**
     * 为账号绑定角色, 全量替换并重算派生权限, 同步清账号权限/角色缓存
     *
     * @param client     所属端
     * @param accountId  账号ID
     * @param roleIdList 角色ID集合, 空集视为清空
     */
    void bindRoles(AccountEnum.Client client, Long accountId, List<Long> roleIdList);

    /**
     * 追加绑定指定端超级管理员角色 (code=SUPER_ADMIN) 到账号并重算派生权限, 同步清缓存
     *
     * @param client    所属端
     * @param accountId 账号ID
     */
    void bindSuperAdmin(AccountEnum.Client client, Long accountId);
}
