package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

/**
 * 提现记录请求
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
     * 提现状态
     * @ext 编码值; 保留 Integer
     */
    private Integer state;
}