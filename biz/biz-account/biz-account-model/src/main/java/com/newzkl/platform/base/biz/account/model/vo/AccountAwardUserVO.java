package com.newzkl.platform.base.biz.account.model.vo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountAwardUserVO extends BaseRes {

    private String nickname;

    private String realName;


    private String username;

    private String roleName;

    @JsonIgnore
    private String roleIdList;

    public String getSubRoleIdListDesc() {
        return RoleEnum.CompanyRole.transferValue(this.roleIdList);
    }

}
