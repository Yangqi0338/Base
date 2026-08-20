package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
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
     * 帐号状态 (查询)
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
    private AccountEnum.Client client;

    /**
     * 设置单个邀请人账号ID (内部包装为列表)
     *
     * @param inviteAccountId 邀请人账号ID
     */
    public void setInviteAccountId(Long inviteAccountId) {
        this.inviteAccountIdList = doWrapperList(this.inviteAccountIdList, inviteAccountId);
    }

    /**
     * 设置单个用户名 (内部包装为列表)
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.usernameList = doWrapperList(this.usernameList, username);
    }

    /**
     * 角色列表
     */
    private List<AccountEnum.Identity> identityList;


}
