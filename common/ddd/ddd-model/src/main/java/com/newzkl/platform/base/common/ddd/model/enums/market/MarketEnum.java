package com.newzkl.platform.base.common.ddd.model.enums.market;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 市场枚举（共享内核）
 *
 * @author niu
 */
public class MarketEnum {

    /**
     * 数量类型
     */
    @Getter
    @AllArgsConstructor
    public enum NumType implements IEnum<Integer> {
        /** 渠道商绑定数 */
        SUB_BIND_NUM(1, "渠道商绑定数"),
        /** 销量 */
        SELL_NUM(2, "销量"),
        /** 销售额 */
        SELL_AMOUNT(3, "销售额"),
        /** 商品数量 */
        GOODS_NUM(4, "商品数量");

        @EnumValue
        @JsonValue
        private final Integer type;
        private final String info;

        @Override
        public Integer getCode() {
            return type;
        }

        @Override
        public String getValue() {
            return info;
        }
    }

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
}
