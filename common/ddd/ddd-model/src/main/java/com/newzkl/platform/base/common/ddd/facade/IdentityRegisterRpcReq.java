package com.newzkl.platform.base.common.ddd.facade;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.req.BaseReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sijiwang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IdentityRegisterRpcReq extends BaseReq {
    /**
     * 所属端
     */
    private AccountEnum.Client client;
    /**
     * 父ID
     */
    private Long pid;
    /**
     * 登录名称
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 邀请人ID
     */
    private Long inviteAccountId;
    /**
     * 角色ID集合
     */
    private AccountEnum.Identity identity;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 层级关系(逗号拼接的祖先账号id链, 末尾含 ','), 注册时若账号已存在需与原值拼接
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
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String head;
    /**
     * 是否首次注册此角色(true: 全新注册, 执行仅首次的初始化; false: 复用已注销账号 id 覆盖注册)
     */
    private boolean registerOnce = true;
    /**
     * 是否旧
     */
    private boolean old = false;
    /**
     * 微信 openId, 小程序注册时换码后落身份表
     */
    private String openId;
    /**
     * 微信 unionId, 小程序注册时换码后落身份表
     */
    private String unionId;
}
