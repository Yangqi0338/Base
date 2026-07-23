package com.newzkl.platform.base.biz.account.model.res;


import lombok.Data;

@Data
public class AppHomePageDataVO {

    /**
     * 总收益
     */
    private Integer totalIncome;

    /**
     * 礼包收益(直推奖)
     */
    private Integer packIncome;

    /**
     * 分红奖金
     */
    private Integer dividendBonus;


    /**
     * 今日总收益
     */
    private Integer todayTotalIncome;

    /**
     * 今日礼包收益(直推奖)
     */
    private Integer todayPackIncome;

    /**
     * 今日分红奖金
     */
    private Integer todayDividendBonus;


    /**
     * 直推用户
     */
    private Long directReferralUserCount;


    /**
     * 供应商
     */
    private Long supplierCount;

}
