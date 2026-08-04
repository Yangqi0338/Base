package com.newzkl.platform.base.biz.goods.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author muc_fang
 * @Description: 订单枚举
 * @date 2023/4/2514:37
 */
public class NotifyEnums {
    /**
     * 服务类型
     */
    @Getter
    @AllArgsConstructor
    public enum ServiceType {
        /**
         * 商品
         */
        GOODS(1, "商品"),
        /** 订单 */
        ORDER(2, "订单"),
        /** BOSS */
        BOSS(3, "BOSS"),
        ;

        private final Integer code;
        private final String value;

        public static NotifyEnums.ServiceType getByCode(Integer code) {
            return Stream.of(NotifyEnums.ServiceType.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
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
        /** SKU-规格变更 */
        UPDATE_SKU(4, "SKU-规格变更"),
        /** SPU-基本信息 */
        UPDATE_SPU(3, "SPU-基本信息"),
        /** SPU-上下架 */
        UPDATE_spu_STATE(5, "SPU-上下架"),
        /** SKU-价格变更 */
        UPDATE_SPU_PRICE(6, "SKU-价格变更"),
        ;

        private final Integer code;
        private final String value;

        public static NotifyEnums.ServiceType getByCode(Integer code) {
            return Stream.of(NotifyEnums.ServiceType.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
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

        public static NotifyEnums.ServiceType getByCode(Integer code) {
            return Stream.of(NotifyEnums.ServiceType.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }

}
