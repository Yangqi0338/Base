package com.newzkl.platform.base.biz.account.action.cmd;

import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.BIEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

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
         * 身份
         */
        @NotNull
        private AccountEnum.Identity identity;

        /**
         * 账号 ID
         */
        @NotNull
        private Long accountId;
    }

    /**
     * 账号 ID 入参
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
     * 供应商列表入参
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
     * 渠道商列表入参
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
     * 甄选师列表入参
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
     * 交易师列表入参
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

    /**
     * 交易走势入参
     *
     * <p>迁自旧 {@code BIOperatorQuery}: 旧类是通用 BI 查询, 含分页 / 汇总维度 /
     * 供应商 ID 列表等本端点用不到的字段, 且 {@code accountId} / {@code roleId} 的 getter
     * 内部回落到当前登录态。本端点只用到 {@code dimension} 一个入参 (账号与角色在
     * action 内按当前登录态钉死, 与旧行为一致), 故只收该字段</p>
     *
     * @author KC
     */
    @Data
    public static class SaleTrend implements Serializable {

        /**
         * 查询维度
         *
         * <p>旧 {@code @NotNull(groups=CheckCommand.class)} 分组校验, 中台不分组, 直接必填</p>
         */
        @NotNull(message = "查询维度未指定")
        private BIEnum.Dimension dimension;
    }
}
