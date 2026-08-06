package com.newzkl.platform.base.biz.activity.domain.strategy.service.draw;


import com.newzkl.platform.base.biz.activity.model.strategy.aggregates.StrategyRich;
import com.newzkl.platform.base.biz.activity.model.strategy.req.DrawReq;
import com.newzkl.platform.base.biz.activity.model.strategy.res.DrawMethodRes;
import com.newzkl.platform.base.biz.activity.model.strategy.vo.StrategyBriefVO;
import com.newzkl.platform.base.biz.activity.domain.strategy.service.method.DrawMethod;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 抽取执行基类
 * @Author: niu
 * @Date: 2022/6/10 11:15
 */
@Service
public class AbstractDrawBase extends DrawStrategySupport implements DrawExec {

    @Override
    public PlatformResult<List<DrawMethodRes>> doDrawExec(DrawReq req) {

        // 1、获取抽取策略
        StrategyRich strategyRich = super.queryStrategyRich(req.getStrategyId());
        strategyRich.setChannelId(req.getChannelId());
        StrategyBriefVO strategy = strategyRich.getStrategy();
        // 2、获取抽取策略
        DrawMethod drawMethod = drawMethodMap.get(strategy.getStrategyMode());
        // 3、执行抽取方法
        List<DrawMethodRes> res = drawMethod.doDraw(strategyRich);
        // 4、返回抽取结果
        return PlatformResult.success(res);
    }
}
