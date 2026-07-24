package com.newzkl.platform.base.biz.account.model.vo;


import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 用户账号
 *
 * @author fang
 */
@Data
public class AccountRegisterVO extends BaseRes {
    /**
     * 注册角色
     * PLATFORM(0L,"平台用户"),
     * SUPPLIER(1001L,"供应商"),
     * CHANNEL(1002L,"渠道商"),
     * PARTNER(1003L,"合伙人"),
     * DEALER(1005L,"交易师"),
     * OPERATOR(1004L,"运营商"),
     * SELECTOR(1006L,"甄选师"),
     */
    private RoleEnum.CompanyRole role;
    /**
     * 用户名
     */
    @NotEmpty(message = "username?")
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 邀请码
     */
    private String yqm;
}