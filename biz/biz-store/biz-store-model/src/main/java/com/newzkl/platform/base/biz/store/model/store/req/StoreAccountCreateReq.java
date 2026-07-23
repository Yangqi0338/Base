package com.newzkl.platform.base.biz.store.model.store.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 门店客户
 */
@Data
public class StoreAccountCreateReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 客户id
     */
    private Long accountId;

}