package com.newzkl.platform.base.biz.activity.model.strategy.vo;

import lombok.Data;

/**
 * @Description: 策略明细简要信息
 * @Author: niu
 * @Date: 2024/1/10 16:09
 */
@Data
public class StrategyDetailBriefVO {

    /**
     * 奖品ID
     */
    private String awardId;

    /**
     * 奖品名称
     */
    private String awardName;

    /**
     * 奖品库存
     */
    private Integer awardCount;

    /**
     * 奖品剩余库存
     */
    private Integer awardSurplusCount;

    /**
     * 策略内容
     */
    private String strategyContent;
}
