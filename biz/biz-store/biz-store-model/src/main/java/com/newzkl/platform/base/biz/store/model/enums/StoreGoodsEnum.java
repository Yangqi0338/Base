package com.newzkl.platform.base.biz.store.model.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 门店商品枚举
 */
@Data
public class StoreGoodsEnum {

    //来源
    @Getter
    @AllArgsConstructor
    public enum Source {
        MARKET(0,"市场"),
        ;
        private Integer code;
        private String value;
    }

    //物流方式
    @Getter
    @AllArgsConstructor
    public enum LogisticsType {
        EXPRESSAGE(0,"快递"),
        PICK_UP(1,"自提"),
        ;
        private Integer code;
        private String value;
    }

    //发货时限
    @Getter
    @AllArgsConstructor
    public enum DeliveryTimeType {
        WITHIN_2_DAYS(0,"2日内"),
        MORE_2_DAYS(1,"大于2日"),
        ;
        private Integer code;
        private String value;
    }

    //状态
    @Getter
    @AllArgsConstructor
    public enum status {
        LISTED (0,"上架"),
        UNLISTED (1,"下架"),
        ;
        private Integer code;
        private String value;
    }

}
