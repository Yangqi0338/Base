package com.newzkl.platform.base.biz.store.model.web;

import lombok.Data;

import java.io.Serializable;

/**
 * 渠道商分页查询
 */
@Data
public class EventTrackingReq implements Serializable {

    /**
     * 指标type
     */
    private Integer eventTrackingType;

    /**
     * 用户id
     */
    private Long accountId;

    /**
     * 店铺id
     */
    private Long storeId;


}
