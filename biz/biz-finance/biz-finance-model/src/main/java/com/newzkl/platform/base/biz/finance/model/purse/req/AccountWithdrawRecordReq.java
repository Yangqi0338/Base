package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * @author 提现记录
 */
@Data
public class AccountWithdrawRecordReq extends BaseRes {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 金额
     */
    private Money amount;

    /**
     * 提货积分
     */
    private Integer goodsPoints;

    /**
     * 配置
     */
    private String config;

    /**
     * 提现时间
     */
    private String finishTime;

    /**
     * 三方交易单号
     */
    private String tripartiteTradeNo;

    /**
     * 状态
     */
    private Integer state;
}