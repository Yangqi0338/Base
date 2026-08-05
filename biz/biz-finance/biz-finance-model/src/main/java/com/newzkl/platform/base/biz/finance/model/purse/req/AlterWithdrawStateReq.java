package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.common.ddd.model.enums.audit.AuditEnum;
import lombok.Data;

/**
 * @author niu
 * @description: 更新提现状态req
 * @date 2023/12/27 15:03
 */
@Data
public class AlterWithdrawStateReq {

    /** 主键ID */
    private Long id;

    /**
     * 状态 0：申请中  1：成功 2：失败 3:退汇
     */
    private AuditEnum.WithdrawSate state;

    /**
     * 三方交易单号
     */
    private String tripartiteTradeNo;

    /**
     * 到账时间
     */
    private String finishTime;
}
