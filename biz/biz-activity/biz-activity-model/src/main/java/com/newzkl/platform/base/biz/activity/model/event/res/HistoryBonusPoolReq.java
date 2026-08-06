package com.newzkl.platform.base.biz.activity.model.event.res;

import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 历史奖金池查询请求对象
 * @Author: niu
 * @Date: 2024/1/16 16:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HistoryBonusPoolReq extends BizPageQuery {

    /**
     * 起始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

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
