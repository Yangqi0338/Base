package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.req.cmd.CodeUpdateUsernameCommand;
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
    private CommonEnum.Client client;

    /**
     * 账号id
     */
    private Long accountId;

}
