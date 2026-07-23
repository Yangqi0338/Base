package com.newzkl.platform.base.biz.finance.model.support;

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
    private Integer systemPrice;

    /**
     * 系统原价
     */
    private Integer systemOriginalPrice;

    /**
     * 折扣力度：折
     */
    private Integer discount;


    //采购金充值
    /**
     * 充值最低金额
     */
    private Integer minimumRechargeAmount;


    //商品席位购买
    /**
     * 席位原价
     */
    private Integer seatOriginalPrice;

    /**
     * 购买最小数量
     */
    private Integer purchaseMinimumNum;


    //提现规则
    /**
     * 最小提现金额
     */
    private Integer minimumWithdrawalAmount;

    /**
     * 提现手续费
     * 千分制：15 = 0.015
     */
    private Integer withdrawalFee;

    /**
     * 单日提现最高金额
     */
    private Integer maximumDailyWithdrawalAmount;

}
