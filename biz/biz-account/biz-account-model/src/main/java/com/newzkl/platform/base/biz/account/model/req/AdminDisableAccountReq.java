package com.newzkl.platform.base.biz.account.model.req;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.account.model.req.cmd.AdminDisableAccountCommand;
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
    private CommonEnum.Client client;

}
