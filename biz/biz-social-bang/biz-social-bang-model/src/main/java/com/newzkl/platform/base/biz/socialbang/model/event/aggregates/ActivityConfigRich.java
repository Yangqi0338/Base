package com.newzkl.platform.base.biz.socialbang.model.event.aggregates;

import com.newzkl.platform.base.biz.socialbang.model.event.vo.ActivityOtherConfigVO;
import com.newzkl.platform.base.biz.socialbang.model.event.vo.ActivityVO;
import com.newzkl.platform.base.biz.socialbang.model.event.vo.StrategyVO;
import lombok.Data;

/**
 * @Description: 活动配置聚合对象
 * @Author: niu
 * @Date: 2022/6/1 10:20
 */
@Data
public class ActivityConfigRich {

    /**
     * 活动配置
     */
    private ActivityVO activity;

    /**
     * 活动其他配置
     */
    private ActivityOtherConfigVO activityOtherConfig;

    /**
     * 策略
     */
    private StrategyVO strategy;
}
