package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连提现回调中的商户订单信息节点。
 *
 * <p>迁移自 new-scm {@code application.utils.model.withdraw.WithDrawalOrderInfo}。</p>
 *
 * <p>迁移调整: 旧类与 {@code QueryPaymentOrderInfo} 有 4 个字段完全重复,
 * 此处改为继承 {@link TripartiteOrderInfo}, 仅保留提现独有的手续费与附言。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WithdrawOrderInfo extends TripartiteOrderInfo {

    /**
     * 手续费金额, 单位元。
     */
    private Double fee_amount;

    /**
     * 交易附言, 单笔金额大于等于 5w 时连连要求必传。
     */
    private String postscript;
}
