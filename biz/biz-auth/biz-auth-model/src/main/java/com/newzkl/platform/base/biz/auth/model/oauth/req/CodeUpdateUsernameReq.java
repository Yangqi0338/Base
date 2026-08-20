package com.newzkl.platform.base.biz.auth.model.oauth.req;


import com.newzkl.platform.base.common.ddd.model.auth.OauthIdentity;
import com.newzkl.platform.base.common.ddd.model.auth.OauthUserId;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

/**
 * 验证码修改用户名
 *
 * @author ruoyi
 */
@Data
public class CodeUpdateUsernameReq extends CodeUpdateUsernameCommand {

    /**
     * 归属端
     */
    @OauthIdentity
    private AccountEnum.Client client;

    /**
     * 账号id
     */
    @OauthUserId
    private Long accountId;

}
