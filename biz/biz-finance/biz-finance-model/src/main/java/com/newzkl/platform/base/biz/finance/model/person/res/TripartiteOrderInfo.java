package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;

/**
 * 连连交易回调中的商户订单信息节点。
 *
 * <p>迁移自 new-scm {@code application.utils.model.res.QueryPaymentOrderInfo}。</p>
 *
 * <p>{@code txn_seqno} 即平台侧业务单号 (支付单/提现单 ID), 回调据此定位本地业务数据。</p>
 *
 * @author KC
 */
@Data
public class TripartiteOrderInfo {

    /**
     * 商户交易流水号 (平台业务单号)。
     */
    private String txn_seqno;

    /**
     * 商户系统交易时间。
     */
    private String txn_time;

    /**
     * 订单总金额, 单位元。
     */
    private Double total_amount;

    /**
     * 订单描述。
     */
    private String order_info;
}
