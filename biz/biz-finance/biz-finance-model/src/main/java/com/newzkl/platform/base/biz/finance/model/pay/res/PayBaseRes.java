package com.newzkl.platform.base.biz.finance.model.pay.res;


import java.io.Serializable;

public interface PayBaseRes extends Serializable {

    default Long getTradeNo() {
        return 0L;
    }

    default String getThirdTradeNo() {
        return "0";
    }

}
