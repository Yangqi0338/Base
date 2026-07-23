package com.newzkl.platform.base.biz.store.model.store.msg;

import lombok.Data;

import java.io.Serializable;

/**
 * 门店客户支付消息
 */
@Data
public class StoreAccountPayMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 渠道商ID
     */
    private Long channelId;

    /**
     * 支付金额：分
     */
    private Integer payAmount;

}