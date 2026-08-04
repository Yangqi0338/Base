package com.newzkl.platform.base.common.ddd.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * 供应商结算节点类型（共享内核）
 *
 * <p>对应供应商结算配置 periodSetConfig.orderType 字段, 决定待结算单何时生成结算时间节点。
 * 原 building 误用 {@link RoleEnum.OrderType}(仅 0/1) 承载此语义, 导致值 2 反序列化丢失、
 * {@code >1}/{@code equals(2)} 判断恒不成立。此枚举补齐第三档 {@link #COMPLETE_DELAY}。</p>
 *
 * @author KC
 */
@Getter
@AllArgsConstructor
public enum SettleType {
    /** 订单完成即结算 */
    ORDER_SUCCESS(0, "订单完成"),
    /** 收货完成即结算 */
    RECEIVE(1, "收货完成"),
    /** 订单完成后 N 天结算, N 取 periodSetConfig.orderTypeDay */
    COMPLETE_DELAY(2, "订单完成后N天结算"),
    ;

    @JsonValue
    @EnumValue
    private final Integer code;
    private final String value;

    /**
     * 根据编码获取枚举
     *
     * @param code 编码
     * @return 匹配的枚举, 未匹配返回 null
     */
    public static SettleType getByCode(Integer code) {
        return Stream.of(SettleType.values())
                .filter(it -> it.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
