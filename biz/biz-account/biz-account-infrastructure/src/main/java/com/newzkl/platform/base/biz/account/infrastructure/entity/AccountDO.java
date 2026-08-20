package com.newzkl.platform.base.biz.account.infrastructure.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCode;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.autotable.annotation.*;
import org.dromara.autotable.annotation.enums.IndexTypeEnum;

import java.time.LocalDateTime;

/**
 * 用户账号
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class AccountDO extends BaseDO {
    /**
     * 主键ID
     */
    @PrimaryKey
    @TableId(type = IdType.ASSIGN_ID)
    protected Long id;
    /**
     * 所属端
     */
    @PrimaryKey
    private AccountEnum.Client client;
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
    @BusinessCode(value = BusinessType.USER_DEFAULT_NAME)
    private String nickname;
    /**
     * 登录名称
     */
    @Index
    private String username;
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
     * 邀请人ID
     */
    @Index
    private Long inviteAccountId;
    /**
     * 角色ID集合
     */
    private String identityList;
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
     * 头像
     */
    @BusinessCode(value = BusinessType.USER_DEFAULT_AVATAR)
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
