package com.newzkl.platform.base.biz.socialbang.model.event.res;


import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 历史奖金池结算分页查询请求对象
 */
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
     * @ext 候选枚举 {@link com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum.SettleState}: PENDING_CONFIRMATION-待确认 CONFIRMED-已确认 DELETED-已删除
     */
    private String state;

    /**
     * 分红方式
     * @ext 候选枚举 {@link com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum.DividendMethod}: AVERAGE-平均分红 WEIGHT-加权分红
     */
    private String dividendMethod;

    /**
     * 起始开奖金额 (Money, 金额区间入参元, 比对 BIGINT 分列)
     */
    private Money startSettleBonus;

    /**
     * 结束开奖金额 (Money, 金额区间入参元, 比对 BIGINT 分列)
     */
    private Money endSettleBonus;

    /**
     * 结算类型
     */
    private Integer settleType;
}
