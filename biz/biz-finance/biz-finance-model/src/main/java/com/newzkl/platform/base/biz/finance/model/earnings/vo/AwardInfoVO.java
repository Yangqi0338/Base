package com.newzkl.platform.base.biz.finance.model.earnings.vo;


import com.newzkl.platform.base.biz.finance.model.enums.activity.ActivityEnum;
import lombok.Data;

/**
 * @author niu
 * @description: 订单商品信息
 * @date 2024/1/26 17:01
 */
@Data
public class AwardInfoVO {

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * {@link ActivityEnum.DividendMethod}
     * 分红方式
     * AVERAGE("AVERAGE", "平均分红"),
     * WEIGHT("WEIGHT","加权分红");
     */
    private String dividendMethod;

    /**
     * 分红周期 {@link ActivityEnum.DividendCycle}
     * 结算周期类型
     */
    private String settlementType;

    /**
     * 结算开始时间
     */
    private String settlementStartTime;

    /**
     * 结算结束周期
     */
    private String settlementEndTime;

    /**
     * 分润比例
     */
    private Double ratio;
}
