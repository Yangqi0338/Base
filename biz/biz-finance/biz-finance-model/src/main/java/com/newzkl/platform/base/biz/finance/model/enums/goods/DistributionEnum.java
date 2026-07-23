package com.newzkl.platform.base.biz.finance.model.enums.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class DistributionEnum {

    //来源
    @Getter
    @AllArgsConstructor
    public enum State {
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
        PLATFORM_STORE_UNLISTED(3,"平台门店下架"),
        ;
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
    public enum Source {
        /** 自营 */
        SELF_OPERATED(0L, "自营"),
        /** 样板店 */
        MODEL_SHOP(1L, "样板店"),
        /** 商品直铺 */
        GOODS_DISTRIBUTION(2L, "商品直铺"),
        ;
        private Long code;
        private String value;
    }

}
