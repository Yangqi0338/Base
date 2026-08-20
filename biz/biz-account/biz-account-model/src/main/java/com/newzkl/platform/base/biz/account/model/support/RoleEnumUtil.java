package com.newzkl.platform.base.biz.account.model.support;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 角色枚举工具 (account 域)
 * <p>原 {@code com.zkl.scm.util.biz.ScmUtil} 中依赖 {@code RoleEnum} 的方法迁移至此,
 * 避免 common core-utils 反向依赖 account-model 造成循环依赖.</p>
 *
 * @author KC
 */
public final class RoleEnumUtil {

    private RoleEnumUtil() {
    }

    /**
     * 获取指定客户端下的角色流
     *
     * @param client 客户端
     * @return 角色流
     */
    public static Stream<AccountEnum.Identity> findClientRoleList(AccountEnum.Client client) {
        return java.util.Arrays.stream(AccountEnum.Identity.values())
                .filter(it -> it.getClient().equals(client));
    }

    /**
     * 获取指定客户端命中 roleIdStr 的升级角色列表 (按 level 升序)
     *
     * @param client    客户端
     * @param roleIdStr 角色ID串
     * @return 角色列表
     */
    public static List<AccountEnum.Identity> getLevelUpEnumList(AccountEnum.Client client, String roleIdStr) {
        return findClientRoleList(client)
                .filter(it -> roleIdStr.contains(it.getCodeStr()))
                .sorted(Comparator.comparingInt(AccountEnum.Identity::getLevel))
                .collect(Collectors.toList());
    }

    /**
     * 获取指定客户端命中 roleIdStr 的首个角色列表
     *
     * @param client    客户端
     * @param roleIdStr 角色ID串
     * @return 角色列表
     */
    public static List<AccountEnum.Identity> getFirstEnumList(AccountEnum.Client client, String roleIdStr) {
        return findClientRoleList(client)
                .filter(it -> roleIdStr.contains(it.getCodeStr()))
                .collect(Collectors.toList());
    }

    /**
     * 获取同端所有角色ID
     *
     * @param client 客户端
     * @return 角色ID列表
     */
    public static List<Long> findClientRoleIdList(AccountEnum.Client client) {
        return findClientRoleList(client)
                .map(AccountEnum.Identity::getCode)
                .collect(Collectors.toList());
    }

    /**
     * 按等级正序, 获取同端指定角色下一等级的所有角色
     *
     * @param client    客户端
     * @param startRole 起始角色
     * @return 角色列表
     */
    public static List<AccountEnum.Identity> getLevelEnumList(AccountEnum.Client client, AccountEnum.Identity startRole) {
        return findClientRoleList(client)
                .filter(it -> startRole == null || it.getLevel() > startRole.getLevel())
                .sorted(Comparator.comparingInt(AccountEnum.Identity::getLevel))
                .collect(Collectors.toList());
    }
}
