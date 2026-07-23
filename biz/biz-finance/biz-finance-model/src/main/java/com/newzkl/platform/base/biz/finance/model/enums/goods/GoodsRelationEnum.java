package com.newzkl.platform.base.biz.finance.model.enums.goods;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author niu
 * @description: 商品关系枚举
 * @date 2023/12/6 14:45
 */
public class GoodsRelationEnum {


    public enum GoodsRelation {
        /**
         * 一级市场-商品
         */
        ONE_MARKET_GOODS(1, "一级市场-商品"),

        /** 二市场-商品 */
        TWO_MARKET_GOODS(2, "二市场-商品"),

        /** 市场选品-商品 */
        SELECT_GOODS(3, "市场选品-商品");

        private Integer relationType;
        private String info;

        GoodsRelation(Integer relationType, String info) {
            this.relationType = relationType;
            this.info = info;
        }

        public Integer getRelationType() {
            return relationType;
        }

        public void setRelationType(Integer relationType) {
            this.relationType = relationType;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum Field {
        /** 销量 */
        SALE_NUMBER(0, "mgr.sell_num"),
        /** 金额 */
        MONEY(1, "sp.sale_price_began");
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
