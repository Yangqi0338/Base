package com.newzkl.platform.base.biz.order.model.enums.order;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @Description: 运费相关枚举
 * @Author: niu
 * @Date: 2023/4/27 17:01
 */
public class OrderEnum {

    /**
     * 状态
     */
    @Getter
    @AllArgsConstructor
    public enum State {
        /**
         * 新订单
         */
        NEW(0, "新订单"),
        /** C端待付款 */
        MEMBER_WAIT_PAY(1, "C端待付款"),
        /** 渠道商待付款 */
        CHANNEL_WAIT_PAY(2, "渠道商待付款"),
        /** 运营商待付款 */
        OPERATOR_WAIT_PAY(3, "运营商待付款"),
        /** 派发中 */
        SENDING(4, "派发中"),
        /** 待发货 */
        WAIT_DELIVERY(6, "待发货"),
        /** 待收货 */
        WAIT_RECEIVE(8, "待收货"),
        /** 已收货 */
        DOWN_RECEIVE(10, "已收货"),
        /** 已完成 */
        SUCCESS(12, "已完成"),
        /** 已关闭 */
        CLOSE(99, "已关闭"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String info;

        public static OrderEnum.State getByCode(Integer code) {
            return Stream.of(OrderEnum.State.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }

        /**
         * 修改收货地址的状态集合
         */
        public static Set<Integer> getAllowModifyShipStates() {
            // 初始化允许修改的状态
            Set<Integer> allowStates = new HashSet<>();
            allowStates.add(NEW.getCode());
            allowStates.add(MEMBER_WAIT_PAY.getCode());
            allowStates.add(CHANNEL_WAIT_PAY.getCode());
            allowStates.add(OPERATOR_WAIT_PAY.getCode());
            allowStates.add(SENDING.getCode());
            allowStates.add(WAIT_DELIVERY.getCode());
            // 返回不可变集合，避免被意外修改
            return Collections.unmodifiableSet(allowStates);
        }

        /**
         * 允许取消的订单状态集合 用户
         */
        public static Set<Integer> getMemberCancelOrderStates() {
            // 初始化允许修改的状态
            Set<Integer> allowStates = new HashSet<>();
            allowStates.add(NEW.getCode());
            allowStates.add(MEMBER_WAIT_PAY.getCode());
            // 返回不可变集合，避免被意外修改
            return Collections.unmodifiableSet(allowStates);
        }

        /**
         * 允许取消的订单状态集合 渠道商
         */
        public static Set<Integer> getChannelCancelOrderStates() {
            // 初始化允许修改的状态
            Set<Integer> allowStates = new HashSet<>();
            allowStates.add(NEW.getCode());
            allowStates.add(MEMBER_WAIT_PAY.getCode());
            allowStates.add(CHANNEL_WAIT_PAY.getCode());
            // 返回不可变集合，避免被意外修改
            return Collections.unmodifiableSet(allowStates);
        }

        /**
         * 允许仅退款的状态
         */
        public static Set<Integer> getMoneyOrderStates() {
            Set<Integer> allowStates = new HashSet<>();
            allowStates.add(CHANNEL_WAIT_PAY.getCode());
            allowStates.add(OPERATOR_WAIT_PAY.getCode());
            allowStates.add(SENDING.getCode());
            allowStates.add(WAIT_DELIVERY.getCode());
            // 返回不可变集合，避免被意外修改
            return Collections.unmodifiableSet(allowStates);
        }

        public static Set<Integer> getMoneyGoodsOrderStates() {
            Set<Integer> allowStates = new HashSet<>();
            allowStates.add(WAIT_RECEIVE.getCode());
            allowStates.add(DOWN_RECEIVE.getCode());
            // 返回不可变集合，避免被意外修改
            return Collections.unmodifiableSet(allowStates);
        }
    }

    @Getter
    @AllArgsConstructor
    public enum OrderType {
        /** 渠道商选品下单 */
        CHANNEL(0, "渠道商选品下单"),
        /** c端铺货下单 */
        MEMBER(1, "c端铺货下单"),
        /** 运营商礼包下单 */
        OPERATOR_PACK(2, "运营商礼包下单"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String info;

        public static OrderType getByCode(Integer code) {
            return Stream.of(OrderType.values())
                    .filter(it -> it.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * 支付方式
     */
    @Getter
    @AllArgsConstructor
    public enum PayType {
        /** 直接 */
        DIRECT(0, "直接"),
        /** 微信 */
        WX(1, "微信"),
        /** 支付宝 */
        ALIPAY(2, "支付宝"),
        /** 采购金 */
        PURCHASE(3, "采购金"),
        /**
         * 兑换码
         */
        CDK(4, "兑换码"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String info;

        public static PayType getByCode(Integer code) {
            return Stream.of(PayType.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * 购买方式
     */
    @Getter
    @AllArgsConstructor
    public enum BuyMode {
        /** 激活 */
        ACTIVATE(0, "激活"),
        /** 自购 */
        SELF_BUYING(1, "自购"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String info;
    }

    /**
     * 订单来源
     */
    @Getter
    @AllArgsConstructor
    public enum SourceType {
        /** 激活 */
        MMT(0, "脉脉通"),
        /** 自购 */
        LT(1, "乐态"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String info;
    }
}
