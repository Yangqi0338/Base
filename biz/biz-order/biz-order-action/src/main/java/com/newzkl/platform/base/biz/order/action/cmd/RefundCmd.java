package com.newzkl.platform.base.biz.order.action.cmd;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 售后域 controller 入参命令集
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.interfaces.controller.RefundCmd}。字段名逐字沿用旧命名,
 * 以免改动前端契约; 仅 {@code spuOrderId} 的 Java 类型跟随 Base 交易域改造 (主键 ID 改业务单号)
 * 由 {@code Long} 调整为 {@code String}, 前端 JSON 数字值由 Jackson 自动转字符串, 报文形状不变。</p>
 *
 * @author KC
 */
public class RefundCmd {

    /**
     * 售后审核入参
     *
     * @author KC
     */
    @Data
    public static class Audit implements Serializable {

        /**
         * 售后单id
         */
        private Long refundId;

        /**
         * spu订单id (Base 侧语义为 SPU 交易单号) 和 售后单id二选一
         */
        private String spuOrderId;

        /**
         * 审核操作
         * 0 拒绝 1 通过
         */
        @NotNull
        private Integer execute;

        /**
         * 拒绝原因
         */
        private String reason;
    }

    /**
     * 平台介入处理入参
     *
     * @author KC
     */
    @Data
    public static class PlatformExecute implements Serializable {

        /**
         * 售后单id
         */
        private Long refundId;

        /**
         * 审核操作
         * 0 渠道商原因 1 供应商原因
         */
        private Integer execute;
    }
}
