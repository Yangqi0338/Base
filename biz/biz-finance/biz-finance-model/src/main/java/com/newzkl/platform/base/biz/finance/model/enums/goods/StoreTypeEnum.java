package com.newzkl.platform.base.biz.finance.model.enums.goods;


import lombok.Getter;

/**
 * 市场类型
 */
@Getter
public enum StoreTypeEnum {

    /**
     * 个人
     */
    PERSON("PERSON", "个人"),

    /** 品牌 */
    BRAND("BRAND", "品牌");

    private final String code;


    private final String desc;

    StoreTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getByCode(String code) {
        for (StoreTypeEnum typeEnum : values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum.getDesc();
            }
        }
        return null;
    }
}
