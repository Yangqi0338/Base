package com.newzkl.platform.base.biz.account.model.res;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

import java.util.List;

/**
 * 子账号详情
 *
 * @author KC
 */
@Data
public class SubAccountDetailRes {
    /**
     * 账号id
     */
    private Long id;
    /**
     * 登录账号
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 状态
     */
    private AccountEnum.State state;
    /**
     * 来源
     */
    private AccountEnum.Origin origin;
    /**
     * 已绑角色 id 集合
     */
    private List<Long> roleIds;
}
