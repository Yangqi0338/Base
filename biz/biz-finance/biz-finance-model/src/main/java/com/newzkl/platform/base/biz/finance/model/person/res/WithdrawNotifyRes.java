package com.newzkl.platform.base.biz.finance.model.person.res;

import lombok.Data;

/**
 * 连连提现结果回调报文。
 *
 * <p>迁移自 new-scm {@code application.utils.model.withdraw.WithDrawNotify}。</p>
 *
 * <p>旧类不继承任何公共响应基类 (连连提现回调无 {@code ret_code} 节点), 迁移保持一致。</p>
 *
 * @author KC
 */
@Data
public class WithdrawNotifyRes {

    /**
     * ACCP 系统交易单号 (作为三方流水号回写业务单)。
     */
    private String accp_txno;

    /**
     * 交易状态, 取值见 {@code PurseEnum.TripartiteTxnStatus}。
     */
    private String txn_status;

    /**
     * 交易完成时间。
     */
    private String finish_time;

    /**
     * 商户订单信息, {@code txn_seqno} 即平台提现单 ID。
     */
    private WithdrawOrderInfo orderInfo;
}
