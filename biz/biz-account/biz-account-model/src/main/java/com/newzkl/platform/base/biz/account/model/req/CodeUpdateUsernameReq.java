package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.req.cmd.CodeUpdateUsernameCommand;
import com.newzkl.platform.base.common.ddd.model.auth.OauthRole;
import com.newzkl.platform.base.common.ddd.model.auth.OauthUserId;
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
    @OauthRole
    private CommonEnum.Client client;

    /**
     * 账号id
     */
    @OauthUserId
    private Long accountId;

}
