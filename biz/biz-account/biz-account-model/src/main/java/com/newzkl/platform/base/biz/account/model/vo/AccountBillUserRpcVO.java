package com.newzkl.platform.base.biz.account.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;


@Data
public class AccountBillUserRpcVO implements Serializable {
    /**
     * 账号ID
     */
    private Long id;


    /**
     * 账号用户名
     */
    private String username;


    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 角色
     */
    private RoleEnum.CompanyRole role;

    @JsonIgnore
    public String roleIdList;
}
