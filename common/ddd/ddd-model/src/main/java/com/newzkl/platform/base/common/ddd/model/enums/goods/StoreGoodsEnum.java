package com.newzkl.platform.base.common.ddd.model.enums.goods;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 分销相关枚举
 */
@Data
public class StoreGoodsEnum {

    //来源
    @Getter
    @AllArgsConstructor
    public enum State implements IEnum<Integer> {
        /**
         * 上架
         */
        LISTED(0, "上架"),
        /** 下架 */
        UNLISTED(1, "下架"),
        /** 平台下架 */
        PLATFORM_UNLISTED(-1, "平台下架"),
        /** 待上架 */
        PENDING_LISTING(2, "待上架"),
        PLATFORM_STORE_UNLISTED(3, "平台门店下架"),
        ;
        @EnumValue
        @JsonValue
        private Integer code;
        private String value;

        public static State getState(Integer code) {
            for (State state : State.values()) {
                if (state.getCode().equals(code)) {
                    return state;
                }
            }
            return null;
        }

    }

    /**
     * 来源
     * 其余值为市场id
     */
    @Getter
    @AllArgsConstructor
    public enum Source implements IEnum<Long> {
        /** 自营 */
        SELF_OPERATED(0L, "自营"),
        /** 样板店 */
        MODEL_SHOP(1L, "样板店"),
        /** 商品直铺 */
        GOODS_DISTRIBUTION(2L, "商品直铺"),
        ;
        @EnumValue
        @JsonValue
        private Long code;
        private String value;
    }

}
