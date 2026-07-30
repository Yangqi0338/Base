package com.newzkl.platform.base.biz.auth.facade;

import com.newzkl.platform.base.biz.auth.facade.model.RoleInfo;

import java.util.List;

/**
 * 认证授权域对外契约 (inbound provider)
 *
 * <p>供其他域查角色与刷新权限缓存, 避免调用方直连 biz-auth 内部 domain / model。
 * 本接口只暴露 facade 自带的 {@code facade/model} 传输对象, 物理上不引用 biz-auth-model
 * (biz-auth-facade 的 pom 未声明该依赖)。</p>
 *
 * <p>方法集合以 biz-auth-domain 现有能力为准: 角色查询走 {@code RoleDomain}, 权限缓存刷新走
 * {@code AuthRepository#cacheUserUnFunctionUrls}。鉴权判定 (hasPermission) 目前由网关读
 * Redis 完成, 领域层无对应读方法, 故本契约暂不暴露, 待网关鉴权收口后再补。</p>
 *
 * @author KC
 */
public interface AuthApi {

    /**
     * 查角色基本信息
     *
     * @param roleId 角色 ID
     * @return 角色信息, 不存在返回 null
     */
    RoleInfo queryRole(Long roleId);

    /**
     * 批量查角色基本信息
     *
     * @param roleIdList 角色 ID 列表
     * @return 角色信息列表, 入参为空或无匹配时返回空列表
     */
    List<RoleInfo> queryRoles(List<Long> roleIdList);

    /**
     * 刷新账号的"无权限接口"缓存
     *
     * <p>登录成功或角色/权限变更后调用, 与登录流程中的同名调用语义一致。</p>
     *
     * @param accountId 账号 ID
     */
    void refreshUnPermissionCache(Long accountId);
}
