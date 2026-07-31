package com.newzkl.platform.base.biz.order.facade.model.api.order;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class ApiSyncAmountRecordVO {

    /**
     * 金额
     */
    BigDecimal amount ;
}
