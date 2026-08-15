package com.newzkl.platform.base.biz.auth.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.auth.model.permission.req.RoleQuery;
import com.newzkl.platform.base.biz.auth.model.permission.req.RoleReq;
import com.newzkl.platform.base.biz.auth.model.permission.vo.RoleVO;


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
    RoleVO detail(Long id);

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<RoleVO> page(RoleQuery query);

    /**
     * 列出全部角色
     *
     * @return 角色列表
     */
    List<RoleVO> listAll();

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
}
