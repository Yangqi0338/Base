package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 标准全量账号结果
 * @author muc_fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountRes extends BaseRes {
    /**
     * 账号注销时间
     */
    protected LocalDateTime cancelTime;
    /**
     * 层级关系
     */
    private String pidList;
    /**
     * 顶层主账号id (主账号自身=0, 子账号=顶层主账号id)
     */
    private Long mainAccountId;
    /**
     * 账号来源
     */
    private AccountEnum.Origin origin;
    /**
     * 三方账户权限
     */
    protected CommonEnum.YesOrNo tripartiteAccountPermission;
    /**
     * 三方账户id
     */
    protected String tripartiteAccountId;
    /**
     * 父ID
     */
    private Long pid;
    /**
     * 昵称 (查询)
     */
    private String nickname;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 登录凭证
     */
    private String username;
    /**
     * 帐号状态
     */
    private AccountEnum.State state;
    /**
     * 邀请码
     */
    private String yqm;
    /**
     * 是否新账号
     */
    private boolean isOld;
    /**
     * 邀请人账号ID
     */
    private Long inviteAccountId;
    /**
     * 头像
     */
    private String head;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 归属端
     */
    private AccountEnum.Client client;
}
