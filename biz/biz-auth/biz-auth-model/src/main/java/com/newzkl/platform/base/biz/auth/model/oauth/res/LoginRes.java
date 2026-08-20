package com.newzkl.platform.base.biz.auth.model.oauth.res;


import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

/**
 * 登录响应结果
 *
 * @author muc_fang
 * @date 2024/2/22 11:23
 */
@Data
public class LoginRes {
    /**
     * 账号信息
     */
    private LoginAccountRes accountVO;
    /**
     * Token
     */
    private String token;
    /**
     * 客户端
     */
    private AccountEnum.Client client;
    /**
     * 角色
     */
    private AccountEnum.Identity identity;
}
