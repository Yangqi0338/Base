package com.newzkl.platform.base.biz.auth.domain.service;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.auth.PermissionEnum;

import java.util.Collection;
import java.util.List;

/**
 * 权限关系领域服务
 *
 * <p>端隔离: 关系读写均带 client, 保证账号只与同端角色/权限绑定</p>
 *
 * @author KC
 */
public interface RelationDomain {

    /**
     * 全量替换某源对象在指定端下的关系目标
     *
     * @param client  所属端
     * @param type    关系类型
     * @param source  源对象标识, account 侧为账号 id 字符串, role 侧为角色 code
     * @param targets 目标对象标识集合, role 侧为角色 code, permission 侧为权限 id 字符串
     */
    void replace(AccountEnum.Client client, PermissionEnum.RelationType type, String source, Collection<String> targets);

    /**
     * 列出源对象在指定端下关联的目标标识
     *
     * @param client 所属端
     * @param type   关系类型
     * @param source 源对象标识
     * @return 目标标识列表
     */
    List<String> listTargets(AccountEnum.Client client, PermissionEnum.RelationType type, String source);

    /**
     * 列出目标对象在指定端下关联的源标识
     *
     * @param client 所属端
     * @param type   关系类型
     * @param target 目标对象标识
     * @return 源标识列表
     */
    List<String> listSources(AccountEnum.Client client, PermissionEnum.RelationType type, String target);
}
