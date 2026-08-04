package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户账号
 *
 * @author fang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AccountQuery extends BizPageQuery {
    /**
     * 账号类型
     */
    private AccountEnum.SubUserType accountType;
    /**
     * 主账号id
     */
    private Long mainAccountId;
    /**
     * 父ID (查询)
     */
    private Long pid;
    /**
     * 昵称 (查询)
     */
    private String nickname;
    /**
     * 用户
     */
    private String username;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 搜索 (查询)
     */
    private String search;
    /**
     * 用户列表
     */
    private List<String> usernameList;
    /**
     * 帐号状态（0正常 1停用） (查询)
     */
    private AccountEnum.State state;
    /**
     * 状态大于
     */
    private AccountEnum.State stateOver;
    /**
     * 邀请人账号ID
     */
    private List<Long> inviteAccountIdList;
    /**
     * 父id列表
     */
    private String pidList;
    /**
     * 手机号 (查询)
     */
    private String phone;

    /**
     * 凭证 (username | phone)
     */
    private String credential;

    /**
     * 邀请码
     */
    private String yqm;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 端
     */
    private CommonEnum.Client client;

    public void setInviteAccountId(Long inviteAccountId) {
        this.inviteAccountIdList = doWrapperList(this.inviteAccountIdList, inviteAccountId);
    }

    /**
     * 用户
     */
    public void setUsername(String username) {
        this.usernameList = doWrapperList(this.usernameList, username);
    }

    /**
     * 角色列表
     */
    private List<RoleEnum.CompanyRole> roleList;

    /**
     * 角色
     */
    private RoleEnum.CompanyRole role;
}
