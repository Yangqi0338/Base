package com.newzkl.platform.base.common.ddd.model.enums.market;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 市场枚举（共享内核）
 *
 * @author niu
 */
public class MarketEnum {

    /**
     * 等级
     */
    @Getter
    @AllArgsConstructor
    public enum Level implements IEnum<Integer> {
        /**
         * 一级
         */
        ONE(1, "一级"),
        /** 二级 */
        TWO(2, "二级");

        @EnumValue
        @JsonValue
        private final Integer level;
        private final String info;

        @Override
        public Integer getCode() {
            return level;
        }

        @Override
        public String getValue() {
            return info;
        }
    }

    /**
     * 数量类型
     */
    @Getter
    @AllArgsConstructor
    public enum NumType implements IEnum<Integer> {
        /** 下级推广人数 */
        SUB_BIND_NUM(1, "下级推广人数"),
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
     * 市场类型
     */
    @Getter
    @AllArgsConstructor
    public enum Type implements IEnum<String> {
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
            for (Type typeEnum : values()) {
                if (typeEnum.getCode().equals(code)) {
                    return typeEnum.getDesc();
                }
            }
            return null;
        }
    }

    /**
     * 市场用户
     */
    @Getter
    @AllArgsConstructor
    public enum User implements IEnum<Integer> {
        /**
         * 渠道商
         */
        CHANNEL(3, AccountEnum.Identity.CHANNEL, "渠道商"),
        ;

        @EnumValue
        @JsonValue
        private final Integer type;
        private final AccountEnum.Identity identity;
        private final String info;

        @Override
        public Integer getCode() {
            return type;
        }

        @Override
        public String getValue() {
            return info;
        }

        public static Integer accountTypeByRole(AccountEnum.Identity identity) {
            return Arrays.stream(values())
                    .filter(it -> it.getIdentity() == identity)
                    .findFirst()
                    .map(User::getType)
                    .orElse(null);
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
