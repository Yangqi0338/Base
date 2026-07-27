package com.newzkl.platform.base.biz.order.action.cmd;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 交易域 controller 入参命令集。
 *
 * <p>迁移自旧 {@code com.zkl.scm.model.web.IdObj} 等通用入参壳。字段名沿用旧命名 ({@code id}),
 * 以免改动前端契约。</p>
 *
 * @author KC
 */
public class OrderCmd {

    /**
     * 单 ID 入参 (旧 {@code IdObj})。
     *
     * @author KC
     */
    @Data
    public static class ID implements Serializable {

        /**
         * 主键 ID
         */
        @NotNull(message = "id?")
        private Long id;
    }
}
