package com.newzkl.platform.base.common.ddd.model.enums.order;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @Description: 订单相关枚举
 * @Author: niu
 * @Date: 2023/4/27 17:01
 */
public class OrderEnum {

    /**
     * 状态
     */
    @Getter
    @AllArgsConstructor
    public enum State implements IEnum<Integer> {

        NEW(0, "新订单"),
        MEMBER_WAIT_PAY(1,"C端待付款"),
        CHANNEL_WAIT_PAY(2,"渠道商待付款"),
        OPERATOR_WAIT_PAY(3,"运营商待付款"),
        SENDING(4, "派发中"),
        WAIT_DELIVERY(6,"待发货"),
        WAIT_RECEIVE(8,"待收货"),
        DOWN_RECEIVE(10,"已收货"),
        SUCCESS(12,"已完成"),
        REFUNDING(14,"售后中"),// 给统计售后数量用的，正向订单并无此状态
        CLOSE(99,"已关闭"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        public static State getByCode(Integer code) {
            return Stream.of(State.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }

        /**
         * 修改收货地址的状态集合
         */
        public static Set<OrderEnum.State> getAllowModifyShipStates() {
            // 初始化允许修改的状态
            Set<OrderEnum.State> allowStates = new HashSet<>();
            allowStates.add(NEW);
            allowStates.add(MEMBER_WAIT_PAY);
            allowStates.add(CHANNEL_WAIT_PAY);
            allowStates.add(OPERATOR_WAIT_PAY);
            allowStates.add(SENDING);
            allowStates.add(WAIT_DELIVERY);
            // 返回不可变集合，避免被意外修改
            return Collections.unmodifiableSet(allowStates);
        }

        /**
         * 允许取消的订单状态集合 用户
         */
        public static Set<OrderEnum.State> getMemberCancelOrderStates() {
            // 初始化允许修改的状态
            Set<OrderEnum.State> allowStates = new HashSet<>();
            allowStates.add(NEW);
            allowStates.add(MEMBER_WAIT_PAY);
            // 返回不可变集合，避免被意外修改
            return Collections.unmodifiableSet(allowStates);
        }

        /**
         * 允许取消的订单状态集合 渠道商
         */
        public static Set<OrderEnum.State> getChannelCancelOrderStates() {
            // 初始化允许修改的状态
            Set<OrderEnum.State> allowStates = new HashSet<>();
            allowStates.add(NEW);
            allowStates.add(MEMBER_WAIT_PAY);
            allowStates.add(CHANNEL_WAIT_PAY);
            // 返回不可变集合，避免被意外修改
            return Collections.unmodifiableSet(allowStates);
        }

        /**
         * 允许仅退款的状态
         */
        public static Set<OrderEnum.State> getMoneyOrderStates() {
            Set<OrderEnum.State> allowStates = new HashSet<>();
            allowStates.add(CHANNEL_WAIT_PAY);
            allowStates.add(OPERATOR_WAIT_PAY);
            allowStates.add(SENDING);
            allowStates.add(WAIT_DELIVERY);
            // 返回不可变集合，避免被意外修改
            return Collections.unmodifiableSet(allowStates);
        }

        public static Set<OrderEnum.State> getMoneyGoodsOrderStates() {
            Set<OrderEnum.State> allowStates = new HashSet<>();
            allowStates.add(WAIT_RECEIVE);
            allowStates.add(DOWN_RECEIVE);
            // 返回不可变集合，避免被意外修改
            return Collections.unmodifiableSet(allowStates);
        }
    }

    @Getter
    @AllArgsConstructor
    public enum OrderType implements IEnum<Integer> {

        CHANNEL(0, "渠道商选品下单"),
        MEMBER(1, "c端铺货下单"),
        OPERATOR_PACK(2, "运营商礼包下单")
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;

        public static OrderType getByCode(Integer code) {
            return Stream.of(OrderType.values())
                    .filter(it -> it.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }
}
