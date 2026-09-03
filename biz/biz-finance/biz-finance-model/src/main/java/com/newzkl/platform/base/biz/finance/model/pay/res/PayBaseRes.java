package com.newzkl.platform.base.biz.finance.model.pay.res;


import java.io.Serializable;

public interface PayBaseRes extends Serializable {

    default String getTradeNo() {
        return "0";
    }

    default String getThirdTradeNo() {
        return "0";
    }

}
