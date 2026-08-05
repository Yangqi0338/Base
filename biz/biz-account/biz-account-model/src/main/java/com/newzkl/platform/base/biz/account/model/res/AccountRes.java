package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
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
     * 主账号id
     */
    private Long mainAccountId;
    /**
     * 账号注销时间
     */
    protected LocalDateTime cancelTime;
    /**
     * 层级关系
     */
    private String pidList;
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
     * 密码
     */
    private String password;
    /**
     * 父层级角色关系
     */
    private String pRoleList;
    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 实名认证信息
     */
    private String nameAuthInfo;
    /**
     * 登录凭证
     */
    private String username;
    /**
     * 子账号数量
     */
    private Integer subAccountCount;
    /**
     * 下级数量
     */
    private Integer belowCount;
    /**
     * 子用户类型
     */
    private AccountEnum.SubUserType subUserType;
    /**
     * 帐号状态
     */
    private AccountEnum.State state;
    /**
     * 实名认证审批状态
     */
    private AuditEnum.State nameAuthAuditState;
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
     * 角色ID集合
     */
    private String roleIdList;
    /**
     * 头像
     */
    private String head;
    /**
     * 职务id集合
     */
    private String jobIdList;
    /**
     * IM账号
     */
    private String userAccount;
    /**
     * IM同步状态
     */
    private AccountEnum.ImSyncState imSyncStatus;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 归属端
     */
    private CommonEnum.Client client;
}
