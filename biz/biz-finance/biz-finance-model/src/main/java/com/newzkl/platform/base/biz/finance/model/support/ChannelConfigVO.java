package com.newzkl.platform.base.biz.finance.model.support;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

import java.io.Serializable;

/**
 * 数智门店配置
 */
@Data
public class ChannelConfigVO implements Serializable {

    //数智门店价格
    /**
     * 系统售价
     */
    private Money systemPrice;

    /**
     * 系统原价
     */
    private Money systemOriginalPrice;

    /**
     * 折扣力度：折
     */
    private Integer discount;


    //采购金充值
    /**
     * 充值最低金额
     */
    private Money minimumRechargeAmount;


    //商品席位购买
    /**
     * 席位原价
     */
    private Money seatOriginalPrice;

    /**
     * 购买最小数量
     */
    private Integer purchaseMinimumNum;


    //提现规则
    /**
     * 最小提现金额
     */
    private Money minimumWithdrawalAmount;

    /**
     * 提现手续费
     * 千分制：15 = 0.015 (比率, 非金额)
     */
    private Integer withdrawalFee;

    /**
     * 单日提现最高金额
     */
    private Money maximumDailyWithdrawalAmount;

}
