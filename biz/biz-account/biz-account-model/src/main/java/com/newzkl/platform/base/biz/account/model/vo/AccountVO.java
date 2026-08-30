package com.newzkl.platform.base.biz.account.model.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
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
     * 顶层主账号id (主账号自身=0, 子账号=顶层主账号id)
     */
    private Long mainAccountId;
    /**
     * 账号来源
     */
    private AccountEnum.Origin origin;
    /**
     * 真实姓名
     */
    private String realname;
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
     * 角色ID集合
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
    /**
     * 归属端
     */
    private AccountEnum.Client client;
    /**
     * 角色id列表
     */
    private List<Long> roleIdList;

    /**
     * @param registerRole  要注册的角色
     * @param username      用户名
     * @param password      密码
     * @param state         状态
     * @return
     */
    public AccountVO init(List<AccountEnum.Identity> registerRole, String username, String password, AccountEnum.State state) {
        if (CollUtil.isEmpty(registerRole)) {
            throw new PlatformException(AccountErrorCode.NOT_AVAIL_ROLE);
        }
        this.username = username;
        if (StrUtil.isNotBlank(password)) {
            this.password = new BCryptPasswordEncoder().encode(password);
        }

        //默认是启动状态
        this.state = Opt.ofNullable(state).orElse(AccountEnum.State.ENABLE);
        // 设置角色ID
        for (AccountEnum.Identity identity : registerRole) {
            if (!StrUtil.contains(this.identityList, identity.getCodeStr())) {
                this.identityList = IgnoreStrJoiner.of()
                        .append(this.identityList)
                        .append(identity.getCodeStr())
                        .toString();
            }
        }
        List<AccountEnum.Client> clientList = registerRole.stream().map(AccountEnum.Identity::getClient).distinct().toList();
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