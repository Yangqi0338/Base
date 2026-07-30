package com.newzkl.platform.base.biz.account.model.cdk.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色端开通码入参集合
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.RoleCmd}。
 * 旧 {@code javax.validation} 已按 Jakarta EE 9 迁为 {@code jakarta.validation}。</p>
 *
 * @author KC
 */
public class RoleCmd {

    /**
     * 开通码状态修改入参
     */
    @Data
    public static class StateEdit implements Serializable {

        /**
         * 开通码ID
         */
        @NotNull
        private Long id;

        /**
         * 兑换状态 0 未兑换 1 已兑换
         */
        @NotNull
        private Integer useState;
    }
}
