package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 连连交易结果回调 (转出 / 支付结果查询) 报文。
 *
 * <p>迁移自 new-scm {@code application.utils.model.res.QueryPaymentResult}。</p>
 *
 * <p>迁移裁剪: 旧类还带 {@code payerInfo} / {@code payeeInfo} 两个明细数组,
 * 全仓无任何读取点 (回调只用 {@code txn_status} / {@code accp_txno} / {@code orderInfo.txn_seqno}),
 * 故不迁; 反序列化由 hutool {@code JSONUtil.toBean} 忽略未知字段, 不影响解析。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TripartiteTxnNotifyRes extends LianLianBaseRes {

    /**
     * 交易类型 (USER_TOPUP / GENERAL_CONSUME / INNER_FUND_EXCHANGE 等)。
     */
    private String txn_type;

    /**
     * 账务日期。
     */
    private String accounting_date;

    /**
     * 支付完成时间。
     */
    private String finish_time;

    /**
     * ACCP 系统交易单号。
     */
    private String accp_txno;

    /**
     * 渠道交易单号。
     */
    private String chnl_txno;

    /**
     * 支付交易状态, 取值见 {@code PurseEnum.TripartiteTxnStatus}。业务以此字段为准。
     */
    private String txn_status;

    /**
     * 商户订单信息。
     */
    private TripartiteOrderInfo orderInfo;
}
