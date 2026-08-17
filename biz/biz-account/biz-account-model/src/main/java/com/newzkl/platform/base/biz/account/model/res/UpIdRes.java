package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

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
}
