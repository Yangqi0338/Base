package com.newzkl.platform.base.common.core.mq.model.notify;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 开放平台通知枚举
 *
 * <p>底层跨系统通知的服务/业务类型分类。上移自 new-scm common-model
 * {@code com.zkl.scm.model.constants.message.NotifyEnums}, 供 NotifyUtil 与各业务域共用。</p>
 *
 * @author muc_fang
 */
public class NotifyEnums {

    /**
     * 服务类型
     */
    @Getter
    @AllArgsConstructor
    public enum ServiceType {

        /** 商品 */
        GOODS(1, "商品"),
        /** 订单 */
        ORDER(2, "订单"),
        /** BOSS */
        BOSS(3, "BOSS"),
        ;

        private final Integer code;
        private final String value;

        /**
         * 按 code 取枚举
         *
         * @param code 服务类型 code
         * @return 匹配枚举, 无则 null
         */
        public static ServiceType getByCode(Integer code) {
            return Stream.of(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
        }
    }

    /**
     * 业务类型 商品
     */
    @Getter
    @AllArgsConstructor
    public enum GoodsType {

        /** SKU-规格删除 */
        DELETE_SKU(2, "SKU-规格删除"),
        /** SPU-基本信息 */
        UPDATE_SPU(3, "SPU-基本信息"),
        /** SKU-规格变更 */
        UPDATE_SKU(4, "SKU-规格变更"),
        /** SPU-上下架 */
        UPDATE_SPU_STATE(5, "SPU-上下架"),
        /** SKU-价格变更 */
        UPDATE_SPU_PRICE(6, "SKU-价格变更"),
        ;

        private final Integer code;
        private final String value;

        /**
         * 按 code 取枚举
         *
         * @param code 商品业务类型 code
         * @return 匹配枚举, 无则 null
         */
        public static GoodsType getByCode(Integer code) {
            return Stream.of(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
        }
    }

    /**
     * 业务类型 订单
     */
    @Getter
    @AllArgsConstructor
    public enum OrderType {

        /** 订单状态变更 */
        TRADE_STATE(1, "订单状态变更"),
        /** 订单商品发货 */
        DELIVERY(2, "订单商品发货"),
        /** 售后单状态变更 */
        REFUND_STATE(3, "售后单状态变更"),
        ;

        private final Integer code;
        private final String value;

        /**
         * 按 code 取枚举
         *
         * @param code 订单业务类型 code
         * @return 匹配枚举, 无则 null
         */
        public static OrderType getByCode(Integer code) {
            return Stream.of(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
        }
    }

    /**
     * 业务类型 BOSS
     */
    @Getter
    @AllArgsConstructor
    public enum BossType {

        /** 兑换码使用通知 */
        CDK(1, "兑换码使用通知"),
        ;

        private final Integer code;
        private final String value;

        /**
         * 按 code 取枚举
         *
         * @param code BOSS 业务类型 code
         * @return 匹配枚举, 无则 null
         */
        public static BossType getByCode(Integer code) {
            return Stream.of(values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
        }
    }
}
