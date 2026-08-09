package com.newzkl.platform.base.biz.activity.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

import java.io.Serializable;

/**
 * 策略明细
 *
 * @author niu
 */
@Data
@TableName("strategy_detail")
public class StrategyDetailDO extends BaseDO {

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 奖品ID
     * @ext 0 表示现金, 不限量
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
