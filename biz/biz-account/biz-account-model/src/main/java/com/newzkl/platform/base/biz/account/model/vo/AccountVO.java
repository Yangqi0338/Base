package com.newzkl.platform.base.biz.account.model.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.constant.AccountErrorCode;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
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
    /**
     * 下级数量
     */
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
     * 手机号
     */
    private String phone;
    /**
     * 归属端
     */
    private CommonEnum.Client client;
    /**
     * 头像（平台账号端使用，与 head 语义一致）
     */
    private String face;

    /**
     * @param registerRole  要注册的角色
     * @param username      用户名
     * @param password      密码
     * @param state         状态
     * @return
     */
    public AccountVO init(List<RoleEnum.CompanyRole> registerRole, String username, String password, AccountEnum.State state) {
        if (CollUtil.isEmpty(registerRole)) {
            throw new PlatformException(AccountErrorCode.NOT_AVAIL_ROLE);
        }
        this.username = username;
        this.password = password;

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
            throw new PlatformException(AccountErrorCode.PARAM_ERROR, "不支持多端角色同时注册");
        }
        this.client = CollUtil.getFirst(clientList);
        //设置邀请码
        this.yqm = BizUtil.generate6code();
        return this;
    }

    public String getNewPassword(String rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword);
    }
}