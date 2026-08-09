package com.newzkl.platform.base.biz.activity.infrastructure.entity;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import com.newzkl.platform.base.common.core.model.money.Money;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 往期奖金池归档
 *
 * @author 活动
 */
@Data
@TableName("bonus_pool_archive")
public class BonusPoolArchiveDO extends BaseDO {
    /**
     * 奖金池id
     */
    private Long bonusPoolId;

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 活动id
     */
    private String activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 结算id
     */
    private String settlementId;

    /**
     * 结算名称
     */
    private String settlementName;


    /**
     * 订单信息
     */
    private String orderInfo;

    public JSONObject getOrderInfoJson() {
        if (orderInfo == null) {
            return null;
        }
        return JSONObject.parseObject(orderInfo);
    }


    /**
     * 预计分红
     */
    private String estimatedDividend;

    public JSONObject getEstimatedDividendJson() {
        if (estimatedDividend == null) {
            return null;
        }
        return JSONObject.parseObject(estimatedDividend);
    }


    /**
     * 实际分红
     */
    private String actualDividend;

    public JSONObject getActualDividendJson() {
        if (actualDividend == null) {
            return null;
        }
        return JSONObject.parseObject(actualDividend);
    }


    /**
     * 奖金池参与人员
     */
    private String poolParticipant;


    /**
     * 奖金池参与人员id
     */
    private String poolParticipantList;

    public JSONObject getPoolParticipantListJson() {
        if (poolParticipantList == null) {
            return null;
        }
        return JSONObject.parseObject(poolParticipantList);
    }


    /**
     * 分红方式
     */
    private String dividendMethod;


    /**
     * 结算周期
     */
    private String dividendCycle;

    /**
     * 确认时间
     */
    private LocalDateTime confirmTime;


    /**
     * 奖金池活动描述
     */
    private String bonusDesc;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 其他配置
     */
    private Long otherConfig;

    /**
     * 订单奖金 (Money, 落库 BIGINT 分)
     */
    private Money orderBonus;

    /**
     * 自定义奖金
     */
    private Money customBonus;

    /**
     * 最终结算奖金
     */
    private Money settleBonus;

    /**
     * 结算方式
     * @ext 0-未结算, 1-订单, 2-自定义
     */
    private Integer settleType;

    /**
     * 状态
     *
     * {@link ActivityEnum.SettleState}
     */
    private String state;

    /**
     * 分红状态
     */
    private String dividendStatus;

    /**
     * 订单金额占比
     */
    private Integer orderAmountsRate;

    /**
     * 开始时间
     */
    private LocalDate startTime;

    /**
     * 结束时间
     */
    private LocalDate endTime;

}
