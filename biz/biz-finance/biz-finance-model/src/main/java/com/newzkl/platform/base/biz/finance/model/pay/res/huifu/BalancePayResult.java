package com.newzkl.platform.base.biz.finance.model.pay.res.huifu;

import lombok.Data;

/**
 * 余额支付返回
 *
 * <p>迁移自 new-scm {@code com.zkl.scm.finance.rpc.model.res.BalancePayResult}。渠道商用采购金
 * (余额) 购买席位时即时结算成功, 无三方交易, 故 {@link PayBaseResult} 的单号 / 三方单号取默认 0</p>
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
