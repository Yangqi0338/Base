package com.newzkl.platform.base.biz.account.action.cmd;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色接口入参命令集。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.RoleCmd}, 字段名沿用旧命名以免改动前端契约。</p>
 *
 * @author KC
 */
public class RoleCmd {

    /**
     * 开通码兑换状态修改入参。
     *
     * @author KC
     */
    @Data
    public static class StateEdit implements Serializable {

        /**
         * 开通码 ID
         */
        @NotNull(message = "id?")
        private Long id;

        /**
         * 兑换状态: 0 未兑换 1 已兑换
         */
        @NotNull(message = "useState?")
        private Integer useState;
    }
}
