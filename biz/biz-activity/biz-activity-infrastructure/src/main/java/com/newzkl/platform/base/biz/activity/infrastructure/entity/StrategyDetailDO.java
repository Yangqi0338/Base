package com.newzkl.platform.base.biz.activity.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 策略明细
 *
 * @author niu
 */
@Data
@TableName("strategy_detail")
public class StrategyDetailDO implements Serializable {
    /**
     * id
     */
    private Long id;

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

    private static final long serialVersionUID = 1L;
}
