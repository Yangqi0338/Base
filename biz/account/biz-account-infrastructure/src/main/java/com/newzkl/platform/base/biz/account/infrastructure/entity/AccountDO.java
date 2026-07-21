package com.newzkl.platform.base.biz.account.infrastructure.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.ddd.model.BaseDO;
import com.newzkl.platform.base.biz.account.model.enums.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.Index;
import org.dromara.autotable.annotation.OldColumnName;
import org.dromara.autotable.annotation.PrimaryKey;

import java.time.LocalDateTime;

/**
 * 用户账号
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class AccountDO extends BaseDO {
    /*
     * 主键ID
     * */
    @PrimaryKey
    @TableId(type = IdType.ASSIGN_ID)
    protected Long id;
    /**
     * 所属端
     */
    @PrimaryKey
    private CommonEnum.Client client;
    /**
     * 主账号id
     */
    private Long mainAccountId;
    /**
     * 父ID
     */
    @Index
    private Long pid;
    /**
     * 父ID列表
     */
    @Index
    private String pidList;
    /**
     * 父层级角色关系
     */
    private String pRoleList;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 登录名称
     */
    @Index
    private String username;
    /**
     * 子用户类型
     */
    private AccountEnum.SubUserType subUserType;
    /**
     * 真实姓名
     */
    @OldColumnName("realname")
    private String realName;
    /**
     * 密码
     */
    private String password;
    /**
     * 状态
     */
    private AccountEnum.State state;
    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 实名认证审批状态
     */
    private AuditEnum.State nameAuthAuditState;
    /**
     * 子账号数量
     */
    private Integer subAccountCount;
    /**
     * 下级数量
     */
    private Integer belowCount;
    /**
     * 邀请人ID
     */
    @Index
    private Long inviteAccountId;
    /**
     * 角色ID集合
     */
    private String roleIdList;
    /**
     * 邀请码
     */
    private String yqm;
    /**
     * 手机号
     */
    @Index
    private String phone;
    /**
     * 账号(tencent IM用)
     */
    private String userAccount;
    /**
     * IM同步状态（0-未同步，1-已同步，2-同步失败）
     */
    private Integer imSyncStatus;

    /**
     * 头像
     */
    private String head;

    /**
     * 账号注销时间
     */
    private LocalDateTime cancelTime;

    /**
     * 三方账户权限
     */
    private CommonEnum.YesOrNo tripartiteAccountPermission;

    /**
     * 三方账户id
     */
    private String tripartiteAccountId;
}