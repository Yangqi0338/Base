package com.newzkl.platform.base.common.ddd.model.enums.goods;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author niu
 * @description: 商品关系枚举
 * @date 2023/12/6 14:45
 */
public class GoodsRelationEnum {


    @AllArgsConstructor
    @Getter
    public enum GoodsRelation implements IEnum<Integer> {
        /** 二市场-商品 */
        TWO_MARKET_GOODS(2, "二市场-商品"),

        /** 市场选品-商品 */
        SELECT_GOODS(3, "市场选品-商品");

        @EnumValue
        @JsonValue
        private Integer code;
        private String value;
    }

    @Getter
    @AllArgsConstructor
    public enum Field implements IEnum<Integer> {
        /** 销量 */
        SALE_NUMBER(0, "mgr.sell_num"),
        /** 金额 */
        MONEY(1, "sp.sale_price_began");

        @EnumValue
        @JsonValue
        private Integer code;
        private String value;

        public static Field getByCode(Integer code) {
            return Stream.of(Field.values())
                    .filter(extension -> extension.getCode().equals(code))
                    .findFirst()
                    .orElse(null);
        }
    }
}
