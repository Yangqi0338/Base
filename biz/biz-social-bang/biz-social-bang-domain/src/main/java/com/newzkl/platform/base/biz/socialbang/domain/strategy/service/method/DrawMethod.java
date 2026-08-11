package com.newzkl.platform.base.biz.socialbang.domain.strategy.service.method;


import com.newzkl.platform.base.biz.socialbang.model.strategy.aggregates.StrategyRich;
import com.newzkl.platform.base.biz.socialbang.model.strategy.res.DrawMethodRes;

import java.util.List;

/**
 * 抽取方法
 * @Author: niu
 * @Date: 2024/1/8 17:35
 */
public interface DrawMethod {

    /**
     * 执行抽取
     * @param strategyRich 策略信息
     * @return 返回抽取结果
     */
    List<DrawMethodRes> doDraw(StrategyRich strategyRich);

    /**
     * 策略类型
     * @return 值
     */
    Integer methodMode();
}
