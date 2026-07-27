package com.newzkl.platform.base.biz.finance.model.support.api;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.finance.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 上级链路结果 (跨域 user 结构降级为 finance 本地 DTO)。
 *
 * <p>迁移: 原 {@code com.zkl.scm.user.model.account.res.UpIdRes};
 * 原依赖 {@code BizUtil.getOperatorLevelUpEnumList} 已内联, 去除跨域耦合。</p>
 *
 * @author muc_fang
 */
@Data
public class UpIdRes implements Serializable {
    /**
     * roleId列表
     */
    private String roleIdList;
    /**
     * 直属上级ID
     */
    private Long oneId;
    /**
     * 直属上级RoleId
     */
    private RoleEnum.CompanyRole directRoleId;
    /**
     * 多级上级ID
     */
    private List<Long> upId;
    /**
     * 多级RoleId
     */
    private List<String> pRoleIdList;

    /**
     * 获取层级关系的角色
     * 不为空
     */
    public static List<RoleEnum.CompanyRole> getOperatorEarningRole(UpIdRes res) {
        List<RoleEnum.CompanyRole> roleList = new ArrayList<>();
        if (res == null) {
            return roleList;
        }
        roleList.add(CollUtil.getFirst(operatorLevelUpEnumList(res.getRoleIdList())));
        if (CollUtil.isNotEmpty(res.getPRoleIdList())) {
            // 父id倒序
            List<RoleEnum.CompanyRole> pCompanyRoleList = res.getPRoleIdList().stream().map(pRole ->
                            CollUtil.getFirst(operatorLevelUpEnumList(res.getRoleIdList()))
                    ).sorted(Comparator.comparingInt(RoleEnum.CompanyRole::getLevel).reversed())
                    .collect(Collectors.toList());
            roleList.addAll(pCompanyRoleList);
        }

        return roleList;
    }

    /**
     * 获取运营商端升级角色列表 (原 BizUtil.getOperatorLevelUpEnumList 内联)。
     *
     * @param roleIdStr 角色ID串
     * @return 运营商端匹配角色列表 (永不为 null)
     */
    private static List<RoleEnum.CompanyRole> operatorLevelUpEnumList(String roleIdStr) {
        if (roleIdStr == null) {
            return new ArrayList<>();
        }
        return Stream.of(RoleEnum.CompanyRole.values())
                .filter(it -> CommonEnum.Client.OPERATOR.equals(it.getClient()))
                .filter(it -> roleIdStr.contains(it.getCodeStr()))
                .collect(Collectors.toList());
    }

    public static RoleEnum.CompanyRole getLastEarningUserRoleId(UpIdRes res) {
        return CollUtil.getLast(getOperatorEarningRole(res).stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }
}
