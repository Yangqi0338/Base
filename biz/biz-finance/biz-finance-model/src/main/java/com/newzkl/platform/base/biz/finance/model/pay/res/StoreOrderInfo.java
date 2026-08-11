package com.newzkl.platform.base.biz.finance.model.pay.res;

import lombok.Data;

import java.io.Serializable;

/**
 * @author niu
 * @description: 充值订单信息
 * @date 2023/12/19 15:27
 */
@Data
public class StoreOrderInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String storeName;

    private Long storeType;

    private String address;

    private String contactName;

    private String contactPhone;

    /*
     * 是否是渠道商
     * */
    private Boolean isChannel;
}
