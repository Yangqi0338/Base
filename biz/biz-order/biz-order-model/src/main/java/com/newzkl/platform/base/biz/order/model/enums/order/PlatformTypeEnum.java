package com.newzkl.platform.base.biz.order.model.enums.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 平台类型枚举(三方平台)
 */
@Getter
@AllArgsConstructor
public enum PlatformTypeEnum {
    /**
     * 会订货
     */
    HUI_DING_HUO("HUI_DING_HUO", "会订货"),
    /** 乐态 */
    LE_TAI("LE_TAI", "乐态"),
    ;

    private final String code;

    private final String description;

    public static PlatformTypeEnum getByCode(String code) {
        for (PlatformTypeEnum type : PlatformTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的平台类型: " + code);
    }
}