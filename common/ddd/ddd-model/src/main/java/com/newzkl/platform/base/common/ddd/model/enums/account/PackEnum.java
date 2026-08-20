package com.newzkl.platform.base.common.ddd.model.enums.account;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PackEnum {
    /**
     * 入会礼包订单状态枚举
     *
     * <p>迁移自旧 {@code com.zkl.scm.model.constants.admin.PackOrderEnum.State}，码值逐字保留。</p>
     *
     * @author KC
     */
    @Getter
    @AllArgsConstructor
    public enum PackOrderStateEnum {

        /**
         * 新订单
         */
        NEW(0, "新订单"),

        /**
         * 待付款
         */
        WAIT_PAY(2, "待付款"),

        /**
         * 待发货
         */
        WAIT_DELIVERY(4, "待发货"),

        /**
         * 待收货
         */
        WAIT_RECEIVE(6, "待收货"),

        /**
         * 已收货
         */
        DOWN_RECEIVE(8, "已收货"),

        /**
         * 已完成
         */
        SUCCESS(10, "已完成"),

        /**
         * 已关闭
         */
        CLOSE(-1, "已关闭"),
        ;

        /**
         * 状态编码
         */
        @EnumValue
        @JsonValue
        private final Integer code;

        /**
         * 状态说明
         */
        private final String info;
    }
}
