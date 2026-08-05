package com.newzkl.platform.base.common.ddd.model.enums.store;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.newzkl.platform.base.common.core.model.enums.IEnum;
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
    public enum Source implements IEnum<Integer> {
        MARKET(0, "市场"),
        ;
        @EnumValue
        @JsonValue
        private Integer code;
        private String value;
    }

    //物流方式
    @Getter
    @AllArgsConstructor
    public enum LogisticsType implements IEnum<Integer> {
        EXPRESSAGE(0, "快递"),
        PICK_UP(1, "自提"),
        ;
        @EnumValue
        @JsonValue
        private Integer code;
        private String value;
    }

    //发货时限
    @Getter
    @AllArgsConstructor
    public enum DeliveryTimeType implements IEnum<Integer> {
        WITHIN_2_DAYS(0, "2日内"),
        MORE_2_DAYS(1, "大于2日"),
        ;
        @EnumValue
        @JsonValue
        private Integer code;
        private String value;
    }

    //状态
    @Getter
    @AllArgsConstructor
    public enum status implements IEnum<Integer> {
        LISTED(0, "上架"),
        UNLISTED(1, "下架"),
        ;
        @EnumValue
        @JsonValue
        private Integer code;
        private String value;
    }

}
