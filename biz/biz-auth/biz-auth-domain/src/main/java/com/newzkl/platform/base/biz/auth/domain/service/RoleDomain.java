package com.newzkl.platform.base.biz.auth.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.model.permission.req.RoleQuery;
import com.newzkl.platform.base.biz.auth.model.permission.req.RoleReq;
import com.newzkl.platform.base.biz.auth.model.permission.vo.RoleRes;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;


import java.util.Collection;
import java.util.List;

/**
 * 角色领域服务
 *
 * @author KC
 */
public interface RoleDomain {

    /**
     * 创建角色
     *
     * @param req 角色入参
     * @return 主键ID
     */
    Long create(RoleReq req);

    /**
     * 更新角色
     *
     * @param req 角色入参
     */
    void update(RoleReq req);

    /**
     * 删除角色, 同步清理关系并重算受影响账号
     *
     * @param id 主键
     */
    void delete(Long id);

    /**
     * 角色详情, 含绑定账号与权限
     *
     * @param id 主键
     * @return 角色视图
     */
    RoleRes detail(Long id);

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<RoleRes> page(RoleQuery query);

    /**
     * 列出全部角色
     *
     * @return 角色列表
     */
    List<RoleRes> listAll();

    /**
     * 绑定角色到账号集合, 全量替换
     *
     * @param roleId     角色ID
     * @param accountIds 账号ID集合
     */
    void bindAccounts(Long roleId, Collection<Long> accountIds);

    /**
     * 为角色分配权限, 全量替换并重算受影响账号
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID集合
     */
    void assignPermissions(Long roleId, Collection<Long> permissionIds);

    /**
     * 绑定角色到账号, 全量替换该账号在指定端下的角色集合并重算其派生权限
     *
     * <p>端隔离: 角色须全部属指定端, 存在跨端角色则拒绝。client 由调用方显式传入,
     * 以适配 RPC 边界 (不依赖登录态 ThreadLocal)。</p>
     *
     * @param client    所属端
     * @param accountId 账号ID
     * @param roleIds   角色ID集合, 空集视为清空
     */
    void bindRoles(AccountEnum.Client client, Long accountId, Collection<Long> roleIds);

    /**
     * 追加绑定指定端超级管理员角色到账号并重算派生权限
     *
     * <p>按 (client, code={@code SUPER_ADMIN}) 解析角色, 追加到账号既有角色集合(不清空),
     * 已绑则幂等。端无该角色则静默跳过。client 由调用方显式传入以适配 RPC 边界</p>
     *
     * @param client    所属端
     * @param accountId 账号ID
     */
    void bindSuperAdmin(AccountEnum.Client client, Long accountId);
}
