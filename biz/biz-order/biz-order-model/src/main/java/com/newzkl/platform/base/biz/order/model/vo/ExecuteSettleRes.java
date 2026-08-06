package com.newzkl.platform.base.biz.order.model.vo;

import com.newzkl.platform.base.common.core.model.money.Money;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/1/2915:17
 */
@Data
@AllArgsConstructor
public class ExecuteSettleRes {
    private Long settleRecordId;
    private Money settleMoneyTotal;
}
