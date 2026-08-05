package com.newzkl.platform.base.common.core.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 模块枚举 (Redis key 前缀用)
 *
 * <p>各 biz 域原按域复制同名枚举, 现合并至 core-model 单一副本, key 前缀语义保持一致</p>
 *
 * @author sijiwang
 */
@Getter
@AllArgsConstructor
public enum ModuleEnum implements IEnum<String> {
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

    @JsonValue
    private final String code;
    private final String desc;

    /**
     * 根据编码获取枚举
     *
     * @param code 模块编码
     * @return 匹配的枚举, 无匹配返回 null
     */
    public static ModuleEnum getByCode(String code) {
        for (ModuleEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 获取枚举描述 (IEnum 契约, 委托 desc)
     *
     * @return 枚举描述
     */
    @Override
    public String getValue() {
        return desc;
    }
}
