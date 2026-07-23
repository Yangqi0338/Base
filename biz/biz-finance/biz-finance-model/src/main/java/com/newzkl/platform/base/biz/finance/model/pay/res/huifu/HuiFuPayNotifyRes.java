package com.newzkl.platform.base.biz.finance.model.pay.res.huifu;


import lombok.Data;

import java.io.Serializable;

@Data
public class HuiFuPayNotifyRes extends HuiFuTradeRes implements Serializable {

    public static final String SUCCESS = "S";

    /**
     * S：成功、F：失败
     */
    private String trans_stat;

    public boolean isSuccess() {
        return SUCCESS.equals(this.trans_stat);
    }

}
