package com.newzkl.platform.base.common.ddd.model.enums.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 门店样式枚举
 */
@Data
public class StoreStyleEnum {

    //类型
    @Getter
    @AllArgsConstructor
    public enum Type {
        OTHERS(0,"其它"),
        DEFAULT(1,"默认"),
        ;
        private Integer code;
        private String value;
    }

    //页面类型
    @Getter
    @AllArgsConstructor
    public enum PageType {
        HOME_PAGE("首页"),
        ;
        private String value;
    }

    /**
     * 样板店订单类型
     */
    public enum ModeShopOrderType {

        ORDER("下单"),
        PAY("支付"),
        ;
        private String desc;

        ModeShopOrderType(String desc) {
            this.desc = desc;
        }
    }
}
