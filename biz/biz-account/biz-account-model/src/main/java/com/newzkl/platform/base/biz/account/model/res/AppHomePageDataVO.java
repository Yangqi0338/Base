package com.newzkl.platform.base.biz.account.model.res;


import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

@Data
public class AppHomePageDataVO {

    /**
     * 总收益 (Money, 落库 BIGINT 分)
     */
    private Money totalIncome;

    /**
     * 礼包收益(直推奖) (Money, 落库 BIGINT 分)
     */
    private Money packIncome;

    /**
     * 分红奖金 (Money, 落库 BIGINT 分)
     */
    private Money dividendBonus;


    /**
     * 今日总收益 (Money, 落库 BIGINT 分)
     */
    private Money todayTotalIncome;

    /**
     * 今日礼包收益(直推奖) (Money, 落库 BIGINT 分)
     */
    private Money todayPackIncome;

    /**
     * 今日分红奖金 (Money, 落库 BIGINT 分)
     */
    private Money todayDividendBonus;


    /**
     * 直推用户
     */
    private Long directReferralUserCount;


    /**
     * 供应商
     */
    private Long supplierCount;

}
