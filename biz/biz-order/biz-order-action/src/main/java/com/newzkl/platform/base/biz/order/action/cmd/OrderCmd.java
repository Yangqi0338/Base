package com.newzkl.platform.base.biz.order.action.cmd;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 交易域 controller 入参命令集
 *
 * <p>迁移自旧 {@code com.zkl.scm.model.web.IdObj} 等通用入参壳。字段名沿用旧命名 ({@code id}),
 * 以免改动前端契约。</p>
 *
 * @author KC
 */
public class OrderCmd {

    /**
     * 单 ID 入参 (旧 {@code IdObj})
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

    /**
     * ID 列表入参 (旧 {@code IdListObj})
     *
     * @author KC
     */
    @Data
    public static class IdList implements Serializable {

        /**
         * 主键 ID 列表
         */
        @NotEmpty(message = "idList?")
        private List<Long> idList;
    }

    /**
     * 取消订单入参 (旧 {@code MemberCancelOrderCommand})
     *
     * @author KC
     */
    @Data
    public static class Cancel implements Serializable {

        /**
         * 订单主键 ID
         */
        @NotNull(message = "id?")
        private Long id;

        /**
         * 取消原因
         */
        private String cancelReason;
    }

    /**
     * 修改订单收货地址入参 (旧 {@code OrderShipCommand})
     *
     * <p>旧版入参携带完整 {@code ShipVO} 快照, Base 改为只传收货地址主键
     * {@code shipId}, 由领域层按地址 ID 重新取快照 —— 避免前端可篡改地址内容。
     * {@code storeId}/{@code accountId} 旧版由 controller 从登录态注入, 不在入参里。</p>
     *
     * @author KC
     */
    @Data
    public static class ChangeShip implements Serializable {

        /**
         * 订单主键 ID
         */
        @NotNull(message = "orderId?")
        private Long orderId;

        /**
         * 收货地址主键 ID
         */
        @NotNull(message = "shipId?")
        private Long shipId;
    }

    /**
     * 完成订单入参 (旧 {@code OrderCmd.CompleteOrder})
     *
     * <p>旧版按 {@code spuOrderId} + {@code skuOrderId} 完成单条 SKU, Base 的
     * {@code UpdateOrderService#completeOrder} 按主订单号整单完成, 故只保留
     * {@code spuOrderId} 用于换算主订单号; {@code skuOrderId} 保留在契约里但不参与调用,
     * 见 {@code OrderController} 类级 javadoc。</p>
     *
     * @author KC
     */
    @Data
    public static class CompleteOrder implements Serializable {

        /**
         * SPU 订单主键 ID
         */
        @NotNull(message = "spuOrderId?")
        private Long spuOrderId;

        /**
         * SKU 订单主键 ID (契约保留, 当前不参与调用)
         */
        private Long skuOrderId;
    }
}
