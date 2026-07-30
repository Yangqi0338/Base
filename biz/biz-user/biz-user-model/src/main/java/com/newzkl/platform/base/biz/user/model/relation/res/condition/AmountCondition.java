package com.newzkl.platform.base.biz.user.model.relation.res.condition;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import com.newzkl.platform.base.biz.user.model.enums.LevelEnum;
import com.newzkl.platform.base.biz.user.model.relation.req.ConditionReq;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 金额条件
 *
 * @author fang
 */
@Data
public class AmountCondition implements Condition {

    /**
     * 业绩金额
     */
    private Integer amount;
    /**
     * 业务包含范围 0 直属下级 1 非直属下级
     */
    private List<Integer> scope;

    @Override
    public double isMeet(ConditionReq conditionCommand) {
        Map<Integer, Integer> amountScopeMap = conditionCommand.getAmountScopeMap();
        if (amountScopeMap == null) {
            return 0.0;
        }
        Integer amountParam = amountScopeMap.getOrDefault(null, 0);
        for (LevelEnum.AmountConditionScope conditionScope : LevelEnum.AmountConditionScope.values()) {
            if (scope.contains(conditionScope.getCode())) {
                amountParam = MapUtil.getInt(amountScopeMap, conditionScope.getCode(), 0);
            }
        }
        return NumberUtil.div(amountParam, amount).doubleValue();
    }
}
