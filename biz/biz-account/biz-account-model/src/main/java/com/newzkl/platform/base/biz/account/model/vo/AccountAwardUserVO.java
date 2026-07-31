package com.newzkl.platform.base.biz.account.model.vo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountAwardUserVO extends BaseRes {

    /** 昵称 */
    private String nickname;

    /** 真实姓名 */
    private String realName;


    /** 账号 */
    private String username;

    /** 角色名称 */
    private String roleName;

    /** 角色ID列表 */
    @JsonIgnore
    private String roleIdList;

    public String getSubRoleIdListDesc() {
        return RoleEnum.CompanyRole.transferValue(this.roleIdList);
    }

}
