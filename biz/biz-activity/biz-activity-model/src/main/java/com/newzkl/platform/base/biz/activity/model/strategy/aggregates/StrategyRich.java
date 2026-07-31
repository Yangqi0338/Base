package com.newzkl.platform.base.biz.activity.model.strategy.aggregates;

import com.newzkl.platform.base.biz.activity.model.strategy.vo.StrategyBriefVO;
import com.newzkl.platform.base.biz.activity.model.strategy.vo.StrategyDetailBriefVO;
import lombok.Data;

import java.util.List;

/**
 * @Description: 抽取策略聚合对象
 * @Author: niu
 * @Date: 2024/1/9 14:25
 */
@Data
public class StrategyRich {

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 渠道商id
     */
    private Long channelId;

    /**
     * 策略
     */
    private StrategyBriefVO strategy;

    /**
     * 策略明细
     */
    private List<StrategyDetailBriefVO> strategyDetailList;
}
