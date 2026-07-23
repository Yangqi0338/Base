package com.newzkl.platform.base.biz.order.model.order.req;

import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/2915:47
 */
@Data
public class FreightSettleOrderWaitReq {
    private Long spuOrderId;
    private Long spuId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    private Integer amount;
}
