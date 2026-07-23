package com.newzkl.platform.base.biz.store.model.enums;


import lombok.Getter;

/**
 * 市场类型
 */
@Getter
public enum StoreTypeEnum {

    /**
     * 关闭
     */
    PERSON("PERSON", "个人"),

    /**
     * 开启
     */
    BRAND("BRAND", "品牌");

    private String code;


    private String desc;

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
