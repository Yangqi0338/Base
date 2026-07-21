package com.newzkl.platform.base.biz.account.model.req.cmd;

import com.newzkl.platform.base.biz.account.model.enums.AccountEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 启用禁用
 *
 * @author sijiwang
 */
@Data
public class AdminDisableAccountCommand {

    @NotNull(message = "id?")
    private Long id;

    /**
     * 帐号状态（1正常 0 用户主动注销 -1 平台禁用 ）
     */
    @NotNull(message = "state?")
    private AccountEnum.State state;
}
