package com.newzkl.platform.base.biz.order.model.enums.goods;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 门店样式枚举
 */
@Data
public class StoreStyleEnum {

    /**
     * 门店样式类型
     */
    @Getter
    @AllArgsConstructor
    public enum Type {
        /**
         * 其它
         */
        OTHERS(0, "其它"),
        /** 默认 */
        DEFAULT(1, "默认"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;
        private final String value;
    }

    /**
     * 门店页面类型
     */
    @Getter
    @AllArgsConstructor
    public enum PageType {
        HOME_PAGE("首页"),
        ;
        @EnumValue
        @JsonValue
        private final String value;
    }

}
