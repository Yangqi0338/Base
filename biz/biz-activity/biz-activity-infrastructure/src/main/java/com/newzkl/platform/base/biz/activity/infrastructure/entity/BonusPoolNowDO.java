package com.newzkl.platform.base.biz.activity.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 本期奖金池
 *
 * @author niu
 */
@Data
@TableName
public class BonusPoolNowDO extends BaseDO {
    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 活动id
     */
    private String activityId;

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
     * 订单奖金
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
     * 状态
     * @ext 0-进行中, 1-已结算, 2-已作废
     */
    private Integer state;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
