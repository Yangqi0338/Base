package com.newzkl.platform.base.biz.account.model.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.biz.account.model.enums.AuditEnum;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import com.newzkl.platform.base.biz.account.model.enums.identity.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.account.model.exception.AccountErrorCode;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.IgnoreStrJoiner;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户账号
 *
 * @author fang
 */
@Data
public class AccountVO extends BaseRes {
    /**
     * 主账号id
     */
    private Long mainAccountId;
    /**
     * 父ID
     */
    private Long pid;
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
     * 子用户类型
     */
    private AccountEnum.SubUserType subUserType;
    /**
     * 层级关系
     */
    private String pidList;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 父层级角色关系
     */
    private String pRoleList;
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
     * 实名认证信息
     */
    private String nameAuthInfo;
    /**
     * 密码
     */
    private String password;
    /**
     * 子账号数量
     */
    private Integer subAccountCount;
    /**
     * 下级数量
     */
    private Integer belowCount;
    /**
     * 帐号状态
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
     * 邀请人账号ID
     * NOTE 仅邀请动作会赋值, 大部分情况等同于pid
     */
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
     * 头像
     */
    private String head;
    /**
     * 是否旧账号
     */
    private boolean old;
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

    /**
     * @param registerRole  要注册的角色
     * @param username      用户名
     * @param password      密码
     * @param mainAccountId 父ID
     * @param state         状态
     * @return
     */
    public AccountVO init(List<RoleEnum.CompanyRole> registerRole, String username, String password, Long mainAccountId, AccountEnum.State state) {
        if (CollUtil.isEmpty(registerRole)) {
            throw new ScmException(AccountErrorCode.NOT_AVAIL_ROLE);
        }
        this.username = username;
        this.password = password;

        // 没有主账号自身就是主账号
        this.subUserType = AccountEnum.SubUserType.ACCOUNT;
        this.mainAccountId = Opt.ofNullable(mainAccountId).orElseGet(() -> {
            this.subUserType = AccountEnum.SubUserType.MAIN;
            return AccountEnum.MAIN_ACCOUNT_PID;
        });

        //默认是启动状态
        this.state = Opt.ofNullable(state).orElse(AccountEnum.State.ENABLE);
        // 设置角色ID
        for (RoleEnum.CompanyRole role : registerRole) {
            if (!StrUtil.contains(this.roleIdList, role.getCodeStr())) {
                this.roleIdList = IgnoreStrJoiner.of()
                        .append(this.roleIdList)
                        .append(role.getCodeStr())
                        .toString();
            }
        }
        List<CommonEnum.Client> clientList = registerRole.stream().map(RoleEnum.CompanyRole::getClient).distinct().toList();
        if (clientList.size() > 1) {
            throw new ScmException(AccountErrorCode.PARAM_ERROR, "不支持多端角色同时注册");
        }
        this.client = CollUtil.getFirst(clientList);
        //设置邀请码
        this.yqm = ScmUtil.generate6code();

        //默认实名认证审批状态
        this.nameAuthAuditState = AuditEnum.State.CUSTOM;
        this.subAccountCount = 0;
        this.belowCount = 0;
        return this;
    }

    public String getNewPassword(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }

    public boolean checkPassword(String password) {
        return SecurityUtils.matchesPassword(password, this.password);
    }
}