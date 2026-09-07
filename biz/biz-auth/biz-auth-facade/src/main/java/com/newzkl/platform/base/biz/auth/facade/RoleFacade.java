package com.newzkl.platform.base.biz.auth.facade;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;

import java.util.List;
import java.util.Map;

public interface RoleFacade {

    Map<Long, List<Long>> findRoleByAccount(AccountEnum.Client client, List<Long> accountIdList);

    /**
     * 按账号批量查角色编码列表
     *
     * <p>角色编码是业务能力的载体 (前端 v-permission 按 code 判定), 故对外直接返 code,
     * 不做 code→id 反查。relation 关系侧本就存 code。</p>
     *
     * @param client        所属端
     * @param accountIdList 账号 ID 列表
     * @return accountId → 角色编码列表, 无角色返回空列表
     */
    Map<Long, List<String>> findRoleCodeByAccount(AccountEnum.Client client, List<Long> accountIdList);

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
