package com.newzkl.platform.base.biz.account.model.res;


import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * APP用户账号
 *
 * @author fang
 */
@Data
@NoArgsConstructor
public class RoleVO implements Serializable {
    /**
     * 角色名
     */
    public String roleName;
    /**
     * 角色id
     */
    private List<RoleEnum.CompanyRole> roleList;
    /**
     * 展示名称
     */
    private String showName;
    /**
     * 身份
     */
    private CommonEnum.Client client;
}