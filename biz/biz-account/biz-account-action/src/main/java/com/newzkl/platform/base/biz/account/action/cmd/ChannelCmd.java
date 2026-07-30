package com.newzkl.platform.base.biz.account.action.cmd;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 渠道商接口入参命令集
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.ChannelCmd}。字段名一律沿用旧命名
 * ({@code channelId} / {@code dealerId}), 以免改动前端契约。旧类中的 {@code ID} / {@code IDList}
 * / {@code Edit} 三个内部类, 迁移后无对应存活端点使用, 未一并保留。</p>
 *
 * @author KC
 */
public class ChannelCmd {

    /**
     * 渠道商上级交易师修改入参
     *
     * @author KC
     */
    @Data
    public static class ChannelUpEdit implements Serializable {

        /**
         * 渠道商账号 ID
         */
        @NotNull
        private Long channelId;

        /**
         * 交易师账号 ID
         */
        @NotNull
        private Long dealerId;
    }
}
