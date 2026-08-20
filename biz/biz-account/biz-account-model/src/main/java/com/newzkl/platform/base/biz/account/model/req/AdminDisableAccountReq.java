package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.biz.account.model.req.cmd.AdminDisableAccountCommand;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.Data;

/**
 * 启用禁用
 *
 * @author sijiwang
 */
@Data
public class AdminDisableAccountReq extends AdminDisableAccountCommand {

    /**
     * 归属端
     */
    private AccountEnum.Client client;

}
