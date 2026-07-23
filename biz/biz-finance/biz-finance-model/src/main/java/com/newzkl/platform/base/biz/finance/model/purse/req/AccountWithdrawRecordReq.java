package com.newzkl.platform.base.biz.finance.model.purse.req;

import com.newzkl.platform.base.common.ddd.model.vo.BaseVO;
import lombok.Data;

/**
 * @author 提现记录
 */
@Data
public class AccountWithdrawRecordReq extends BaseVO {

    /**
     * 客户id
     */
    private Long accountId;

    /**
     * 金额
     */
    private Integer amount;

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