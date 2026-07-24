package com.newzkl.platform.base.biz.finance.model.purse.vo;

import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

/**
 * @author niu
 * @description: 提现记录vo
 * @date 2023/12/27 15:28
 */
@Data
public class WithdrawRecordVO extends BaseDO {

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
