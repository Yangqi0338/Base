package com.newzkl.platform.base.biz.store.model.store.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 门店客户
 */
@Data
public class StoreAccountUpdateReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 0:未拉黑，1已拉黑
     */
    private Integer relationType;

}