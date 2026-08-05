package com.newzkl.platform.base.common.ddd.model.enums.market;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 市场类型枚举（共享内核）
 */
@Getter
@AllArgsConstructor
public enum MarketTypeEnum implements IEnum<String> {

    /**
     * 普通市场
     */
    GENERAL("GENERAL", "普通市场"),

    /**
     * 专区市场
     */
    SPECIAL("SPECIAL", "专区市场"),

    /**
     * 黄金专区
     */
    GOLD_ZONE("GOLD_ZONE", "黄金专区"),
    ;

    @EnumValue
    @JsonValue
    private final String code;

    private final String desc;

    @Override
    public String getValue() {
        return desc;
    }

    public static String getByCode(String code) {
        for (MarketTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getDesc();
            }
        }
        return null;
    }
}
