package com.newzkl.platform.base.biz.finance.model.enums.goods;

/**
 * 样板店订单类型
 */
public enum ModeShopOrderEnum {

    ORDER("下单"),
    PAY("支付"),
    ;
    private final String desc;

    ModeShopOrderEnum(String desc) {
        this.desc = desc;
    }
}