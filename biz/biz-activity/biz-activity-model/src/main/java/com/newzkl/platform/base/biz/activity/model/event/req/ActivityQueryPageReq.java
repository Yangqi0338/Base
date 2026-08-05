package com.newzkl.platform.base.biz.activity.model.event.req;

import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import com.newzkl.platform.base.common.ddd.model.query.BizPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 活动分页查询请求对象
 * @Author: niu
 * @Date: 2024/1/2 10:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityQueryPageReq extends BizPageQuery {

    /**
     * 活动id
     */
    private Integer activityId;


    /**
     * 活动名称
     */
    private String activityName;


    /**
     * 分红周期 {@link ActivityEnum.DividendCycle} WEEKLY:周结算 MONTHLY:月结算
     */
    private String dividendCycle;


    /**
     * 分红方式 {@link ActivityEnum.DividendMethod} AVERAGE:平均分红 WEIGHT: 加权分红
     */
    private String dividendMethod;

    /**
     * 状态 {@link ActivityEnum.ExecuteState} PENDING:未生效;ACTIVE:生效中;CANCELLED:已作废
     */
    private String state;



}
