package com.newzkl.platform.base.biz.order.model.enums.goods;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author niu
 * @description: 市场枚举
 * @date 2023/12/5 16:16
 */
public class MarketEnum {

    /**
     * 等级
     */
    @Getter
    @AllArgsConstructor
    public enum Level {
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
    }

    @Getter
    @AllArgsConstructor
    public enum NumType {
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
    }

    /**
     * 市场类型
     */
    @Getter
    @AllArgsConstructor
    public enum Type {
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
    public enum User {
        /**
         * 运营商
         */
        OPERATOR(1, RoleEnum.CompanyRole.OPERATOR, "运营商"),
        /**
         * 渠道商
         */
        CHANNEL(3, RoleEnum.CompanyRole.CHANNEL, "渠道商"),
        /**
         * 交易师
         */
        TRADERS(2, RoleEnum.CompanyRole.DEALER, "交易师");

        @EnumValue
        @JsonValue
        private final Integer type;
        private final RoleEnum.CompanyRole role;
        private final String info;

        public static Integer accountTypeByRoleId(Long roleId) {
            return Arrays.stream(values())
                    .filter(it -> it.getRole().getCode().equals(roleId))
                    .findFirst()
                    .map(User::getType)
                    .orElse(null);
        }
    }
}
