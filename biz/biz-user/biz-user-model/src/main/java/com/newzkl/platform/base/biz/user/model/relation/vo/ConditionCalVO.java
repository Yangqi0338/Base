package com.newzkl.platform.base.biz.user.model.relation.vo;

import cn.hutool.core.util.NumberUtil;
import com.newzkl.platform.base.biz.user.model.enums.LevelEnum;
import com.newzkl.platform.base.biz.user.model.relation.req.ConditionReq;
import com.newzkl.platform.base.biz.user.model.relation.res.condition.Condition;
import com.newzkl.platform.base.biz.user.model.relation.res.condition.PackCondition;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 升级条件计算值对象
 *
 * @author muc_fang
 */
@Data
public class ConditionCalVO extends ArrayList<Condition> implements Condition {

    /**
     * 条件有效性类型
     */
    private Integer conditionJudgeType;
    /**
     * 礼包条件
     */
    private PackCondition pack;

    @Override
    public double isMeet(ConditionReq conditionCommand) {
        if (conditionCommand.getPackInfo() != null && pack != null) {
            return pack.isMeet(conditionCommand);
        }
        List<Double> progressList = new ArrayList<>();
        for (Condition condition : this) {
            double subProgress = condition.isMeet(conditionCommand) * 100;
            if (LevelEnum.ConditionJudgeType.AND.getCode().equals(conditionJudgeType)) {
                progressList.add(subProgress * (1 / this.size()));
            } else {
                progressList.add(subProgress);
            }
        }
        double progress = progressList.stream().mapToDouble(it -> it).max().orElse(0.0);
        return NumberUtil.roundDown(progress, 1).doubleValue();
    }
}
