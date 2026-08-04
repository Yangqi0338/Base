package com.newzkl.platform.base.common.ddd.facade;

import lombok.Data;

/**
 * 余额支付返回 (facade 自带 model, 防腐)。
 *
 * @author KC
 */
@Data
public class BalancePayResult implements PayBaseResult {

    /**
     * 支付状态
     */
    private boolean payState;

    /**
     * 运营商支付状态
     */
    private boolean operatorPayState;
}
