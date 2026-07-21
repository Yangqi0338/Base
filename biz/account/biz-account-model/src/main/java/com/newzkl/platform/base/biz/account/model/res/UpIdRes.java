package com.newzkl.platform.base.biz.account.model.res;
import com.newzkl.platform.base.biz.account.model.support.RoleEnumUtil;

import cn.hutool.core.collection.CollUtil;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/2216:51
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
        roleList.add(CollUtil.getFirst(RoleEnumUtil.getOperatorLevelUpEnumList(res.getRoleIdList())));
        if (CollUtil.isNotEmpty(res.getPRoleIdList())) {
            // 父id倒序
            List<RoleEnum.CompanyRole> pCompanyRoleList = res.getPRoleIdList().stream().map(pRole ->
                            CollUtil.getFirst(RoleEnumUtil.getOperatorLevelUpEnumList(res.getRoleIdList()))
                    ).sorted(Comparator.comparingInt(RoleEnum.CompanyRole::getLevel).reversed())
                    .collect(Collectors.toList());
            roleList.addAll(pCompanyRoleList);
        }

        return roleList;
    }

    public static RoleEnum.CompanyRole getLastEarningUserRoleId(UpIdRes res) {
        return CollUtil.getLast(getOperatorEarningRole(res).stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }
}
