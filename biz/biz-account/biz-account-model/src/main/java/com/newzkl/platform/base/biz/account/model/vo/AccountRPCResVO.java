package com.newzkl.platform.base.biz.account.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class AccountRPCResVO implements Serializable {

    /**
     * ID
     */
    private Long id;
    /**
     * 名称 (查询)
     */
    private String name;
    /**
     * 登录名称(手机号) (查询)
     */
    private String username;

    private String nickName;

    @JsonIgnoreProperties
    private String roleIdList;
    /**
     * 角色ID
     */
    private RoleEnum.CompanyRole role;
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 下级账号列表
     */
    private List<Long> sonAccounts;

    /**
     * 只用于计算的金额
     */
    private Integer amount;
    /**
     * 头像
     */
    private String head;
}
