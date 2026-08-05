package com.newzkl.platform.base.biz.activity.domain.strategy.service.method.impl;

import com.newzkl.platform.base.common.ddd.model.enums.activity.ActivityEnum;
import com.newzkl.platform.base.biz.activity.model.strategy.aggregates.StrategyRich;
import com.newzkl.platform.base.biz.activity.model.strategy.res.DrawMethodRes;
import com.newzkl.platform.base.biz.activity.domain.strategy.service.method.DrawMethod;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 奖金池抽取方法实现
 * @Author: niu
 * @Date: 2024/1/9 14:56
 */
@Component("bonusPoolDrawMethod")
public class BonusPoolDrawMethodImpl implements DrawMethod {


    @Override
    public List<DrawMethodRes> doDraw(StrategyRich strategyRich) {
        return null;
    }

    @Override
    public Integer methodMode() {
        return ActivityEnum.StrategyMode.CONTRIBUTE.getMode();
    }
}
