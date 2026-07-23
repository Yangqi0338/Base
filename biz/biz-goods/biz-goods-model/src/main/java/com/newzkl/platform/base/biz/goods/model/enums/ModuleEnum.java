package com.newzkl.platform.base.biz.goods.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作类型枚举
 *
 * @author sijiwang
 */
@Getter
@AllArgsConstructor
public enum ModuleEnum {
    /**
     * 通用
     */
    COMMON("", "通用"),
    /**
     * 订单
     */
    ORDER("order", "订单"),
    /** 用户 */
    USER("user", "用户"),
    /** 财务 */
    FINANCE("finance", "财务"),
    /** 商品 */
    GOODS("goods", "商品"),
    /** 互动 */
    IM("im", "互动"),
    /** 活动 */
    ACTIVITY("activity", "活动"),
    /** 开放接口 */
    OPENAPI("openapi", "开放接口"),
    /** 网关 */
    GATEWAY("gateway", "网关"),
    ;

    private final String code;
    private final String desc;

    /**
     * 根据编码获取枚举
     */
    public static ModuleEnum getByCode(String code) {
        for (ModuleEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}