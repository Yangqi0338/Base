package com.newzkl.platform.base.biz.account.action.cmd;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 统计/适配接口入参命令集。
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.CountCmd}。字段名一律沿用旧命名
 * ({@code roleId} / {@code accountId} / {@code inviteId}), 以免改动前端契约。</p>
 *
 * @author KC
 */
public class CountCmd {

    /**
     * 角色详情入参。
     *
     * @author KC
     */
    @Data
    public static class UserAccount implements Serializable {

        /**
         * 角色 ID
         */
        @NotNull
        private Long roleId;

        /**
         * 账号 ID
         */
        @NotNull
        private Long accountId;
    }

    /**
     * 账号 ID 入参。
     *
     * @author KC
     */
    @Data
    public static class ID implements Serializable {

        /**
         * 账号 ID
         */
        @NotNull
        private Long accountId;
    }

    /**
     * 供应商列表入参。
     *
     * @author KC
     */
    @Data
    public static class SupplierPage implements Serializable {

        /**
         * 邀请人账号 ID
         */
        private Long inviteId;
    }

    /**
     * 渠道商列表入参。
     *
     * @author KC
     */
    @Data
    public static class ChannelPage implements Serializable {

        /**
         * 上级 (旧: 上级交易师) 账号 ID
         */
        private Long inviteId;
    }

    /**
     * 甄选师列表入参。
     *
     * @author KC
     */
    @Data
    public static class SelectorPage implements Serializable {

        /**
         * 上级甄选师账号 ID
         */
        private Long inviteId;
    }

    /**
     * 交易师列表入参。
     *
     * @author KC
     */
    @Data
    public static class DealerPage implements Serializable {

        /**
         * 所属运营商账号 ID
         */
        private Long inviteId;
    }
}
