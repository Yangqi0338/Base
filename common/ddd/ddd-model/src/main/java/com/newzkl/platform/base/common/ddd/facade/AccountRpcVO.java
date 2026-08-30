package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户账号
 *
 * @author fang
 */
@Data
public class AccountRpcVO extends BaseRes {
    /**
     * 父ID
     */
    private Long pid;
    /**
     * 父ID
     */
    private AccountEnum.Client client;
    /**
     * 账号注销时间
     */
    protected LocalDateTime cancelTime;
    /**
     * 三方账户权限
     */
    protected CommonEnum.YesOrNo tripartiteAccountPermission;
    /**
     * 三方账户id
     */
    protected String tripartiteAccountId;
    /**
     * 层级关系
     */
    private String pidList;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 登录凭证
     * NOTE 可以是手机号、自定账号名
     */
    private String username;
    /**
     * 昵称 (查询)
     */
    private String nickname;
    /**
     * 密码
     */
    private String password;
    /**
     * 帐号状态
     */
    private AccountEnum.State state;
    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 邀请人账号ID
     * NOTE 仅邀请动作会赋值, 大部分情况等同于pid
     */
    private Long inviteAccountId;
    /**
     * 身份集合
     */
    private String identityList;
    /**
     * 邀请码
     */
    private String yqm;
    /**
     * 头像
     */
    private String head;
    /**
     * 手机号
     */
    private String phone;
    private Boolean old;
}