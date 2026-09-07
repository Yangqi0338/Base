package com.newzkl.platform.base.biz.account.action.cmd;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.BIEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 统计/适配接口入参命令集
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.CountCmd}。字段名一律沿用旧命名
 * ({@code roleId} / {@code accountId} / {@code inviteId}), 以免改动前端契约。</p>
 *
 * @author KC
 */
public class CountCmd {

    /**
     * 角色详情入参
     *
     * @author KC
     */
    @Data
    public static class UserAccount implements Serializable {

        /**
         * 身份列表
         */
        @NotNull
        private List<AccountEnum.Identity> identityList;

        /**
         * 账号 ID
         */
        @NotNull
        private Long accountId;
    }
}
