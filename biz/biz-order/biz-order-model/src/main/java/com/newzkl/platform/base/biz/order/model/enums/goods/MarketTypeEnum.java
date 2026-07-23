package com.newzkl.platform.base.biz.order.model.enums.goods;


import lombok.Getter;

/**
 * 市场类型
 */
@Getter
public enum MarketTypeEnum {

    /**
     * 关闭
     */
    GENERAL("GENERAL", "普通市场"),

    /**
     * 开启
     */
    SPECIAL("SPECIAL", "专区市场"),

    /**
     * 开启
     */
    GOLD_ZONE("GOLD_ZONE", "黄金专区"),
    ;

    private final String code;


    private final String desc;

    MarketTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
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
