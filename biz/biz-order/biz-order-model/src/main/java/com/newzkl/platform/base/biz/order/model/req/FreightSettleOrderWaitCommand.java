package com.newzkl.platform.base.biz.order.model.req;

import com.newzkl.platform.base.common.core.model.money.Money;

import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/2915:47
 */
@Data
public class FreightSettleOrderWaitCommand {
    private Long spuOrderId;
    private Long spuId;
    /**
     * 供应商ID
     */
    private Long supplierId;
    private Money amount;
}
