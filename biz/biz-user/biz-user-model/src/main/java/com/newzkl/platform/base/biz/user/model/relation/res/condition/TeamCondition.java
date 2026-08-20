package com.newzkl.platform.base.biz.user.model.relation.res.condition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.LevelEnum;
import com.newzkl.platform.base.biz.user.model.relation.req.ConditionReq;
import com.newzkl.platform.base.biz.user.model.relation.req.TeamUserCountReq;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 团队条件
 *
 * @author fang
 */
@Data
public class TeamCondition implements Condition {

    /**
     * 数量
     */
    private int count;
    /**
     * 等级
     */
    private List<Integer> level;
    /**
     * 角色
     */
    private List<AccountEnum.Identity> identity;

    @Override
    public double isMeet(ConditionReq conditionCommand) {
        Map<Integer, List<TeamUserCountReq>> teamAddMap = conditionCommand.getTeamAddMap();
        if (teamAddMap == null) {
            return 0.0;
        }
        int countParam = 0;
        for (LevelEnum.AmountConditionScope conditionScope : LevelEnum.AmountConditionScope.values()) {
            List<TeamUserCountReq> teamAddList = teamAddMap.getOrDefault(conditionScope.getCode(), CollUtil.newArrayList());
            countParam += teamAddList.stream()
                    .filter(it -> it.getType() != null && identity.contains(it.getType()))
                    .filter(it -> it.getLevel() != null && level.contains(it.getLevel()))
                    .mapToInt(TeamUserCountReq::getCount).sum();
        }
        return NumberUtil.div(countParam, count);
    }
}
