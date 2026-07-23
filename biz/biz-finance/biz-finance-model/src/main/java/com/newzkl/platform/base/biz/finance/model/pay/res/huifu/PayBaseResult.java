package com.newzkl.platform.base.biz.finance.model.pay.res.huifu;


import java.io.Serializable;

public interface PayBaseResult extends Serializable {

    default Long getTradeNo() {
        return 0L;
    }

    default String getThirdTradeNo() {
        return "0";
    }

}
