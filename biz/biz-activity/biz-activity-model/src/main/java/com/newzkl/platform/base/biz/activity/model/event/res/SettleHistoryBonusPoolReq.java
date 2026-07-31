package com.newzkl.platform.base.biz.activity.model.event.res;


import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class SettleHistoryBonusPoolReq extends BizPageQuery {

    /**
     * 起始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 结算ID
     */
    private String settlementId;


    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 结算状态
     */
    private String state;

    /**
     * 分红方式
     */
    private String dividendMethod;

    /**
     * 起始开奖金额
     */
    private Integer startSettleBonus;

    /**
     * 结束开奖金额
     */
    private Integer endSettleBonus;

    /**
     * 结算类型
     */
    private Integer settleType;
}
