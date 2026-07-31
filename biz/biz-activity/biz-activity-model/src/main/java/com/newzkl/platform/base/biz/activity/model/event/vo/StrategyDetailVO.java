package com.newzkl.platform.base.biz.activity.model.event.vo;

import lombok.Data;

/**
 * @Description: 策略详情
 * @Author: niu
 * @Date: 2024/1/9 17:10
 */
@Data
public class StrategyDetailVO {

    /**
     * 策略详情id
     */
    private Long detailId;

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 奖品ID 0表示现金，不限量
     */
    private Long awardId;

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
