package com.newzkl.platform.base.biz.finance.model.enums.order;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 运费相关枚举
 * @Author: niu
 * @Date: 2023/4/27 17:01
 */
public class RefundEnum {

    @AllArgsConstructor
    @Getter
    public enum RefundType {
        /**
         * 仅退款
         */
        MONEY(0, "仅退款"),
        /** 退货退款 */
        MONEY_GOODS(1, "退货退款"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String info;

        public static RefundType getByCode(Integer code) {
            if (code == null) {
                return null;
            }
            for (RefundType refundType : RefundType.values()) {
                if (code.equals(refundType.getCode())) {
                    return refundType;
                }
            }
            return null;
        }
    }

    /**
     * 状态
     * !!! 售后中的状态请配置 >= 0 and < 10
     */
    @Getter
    @AllArgsConstructor
    public enum State {
        /** 待渠道商审核 */
        CHANNEL_WAIT(0, "待渠道商审核"),
        /** 待供应商审核 */
        SUPPLIER_WAIT(2, "待供应商审核"),
        /** 待提交物流 */
        FREIGHT_WAIT(4, "待提交物流"),
        /** 待确认收货 */
        RECEIVE_WAIT(6, "待确认收货"),
        /** 待平台介入 */
        PLATFORM_WAIT(7, "待平台介入"),
        /** 平台介入中 */
        PLATFORM_ING(8, "平台介入中"),
        /** 退款中 */
        MONEY_ING(9, "退款中"),
        /** 已完成 */
        SUCCESS(10, "已完成"),
        /** 已拒绝 */
        REFUSE(-2, "已拒绝"),
        /** 已关闭 */
        CLOSE(-4, "已关闭"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String info;

        public static State getByCode(Integer code) {
            if (code == null) {
                return null;
            }
            for (State state : State.values()) {
                if (code.equals(state.getCode())) {
                    return state;
                }
            }
            return null;
        }
    }
}
