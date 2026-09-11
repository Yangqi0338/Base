package com.newzkl.platform.base.biz.store.model.store.entity;

import lombok.Data;

import java.io.Serializable;

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