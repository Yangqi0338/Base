package com.newzkl.platform.base.biz.socialbang.domain.strategy.service.method.impl;

import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import com.newzkl.platform.base.biz.socialbang.model.strategy.aggregates.StrategyRich;
import com.newzkl.platform.base.biz.socialbang.model.strategy.res.DrawMethodRes;
import com.newzkl.platform.base.biz.socialbang.model.strategy.vo.StrategyBriefVO;
import com.newzkl.platform.base.biz.socialbang.domain.strategy.service.method.DrawMethod;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author niu
 * @description: 平台甄选师抽取方法
 * @date 2024/1/9 15:06
 */
@Component
public class PlatformSelectorDrawMethodImpl implements DrawMethod {
    @Override
    public List<DrawMethodRes> doDraw(final StrategyRich strategyRich) {
        StrategyBriefVO strategy = strategyRich.getStrategy();
        // 获取策略扩展信息，并转化为对象,内容为各等级甄选师分润占比，对应等级甄选师平均分配
        String extInfo = strategy.getExtInfo();
        // 获取配置可得奖金甄选师等级对应的甄选师列表
        return null;
    }

    @Override
    public Integer methodMode() {
        return ActivityEnum.StrategyMode.SELECTOR_LEVEL.getMode();
    }
}
