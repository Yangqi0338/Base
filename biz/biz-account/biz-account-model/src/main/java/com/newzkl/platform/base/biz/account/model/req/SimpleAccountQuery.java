package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BusinessPageQuery;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import lombok.Data;

@Data
public class SimpleAccountQuery extends BusinessPageQuery {
    /**
     * 角色ID
     */
    private RoleEnum.CompanyRole role;
    /**
     * 用户名称
     */
    private String username;
}
