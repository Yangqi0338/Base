package com.newzkl.platform.base.biz.store.model.enums;

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