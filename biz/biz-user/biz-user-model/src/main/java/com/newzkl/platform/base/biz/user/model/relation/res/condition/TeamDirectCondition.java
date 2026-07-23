package com.newzkl.platform.base.biz.user.model.relation.res.condition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.newzkl.platform.base.biz.user.model.enums.LevelEnum;
import com.newzkl.platform.base.biz.user.model.relation.req.ConditionReq;
import com.newzkl.platform.base.biz.user.model.relation.req.TeamUserCountReq;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 团队部门(直推)条件：直推用户满足 count 个 level 的 role。
 *
 * @author muc_fang
 */
@Data
public class TeamDirectCondition implements Condition {

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
    private List<Long> role;

    @Override
    public double isMeet(ConditionReq conditionCommand) {
        Map<Integer, List<TeamUserCountReq>> teamAddMap = conditionCommand.getTeamAddMap();
        if (teamAddMap == null) {
            return 0.0;
        }
        int countParam = 0;

        List<TeamUserCountReq> teamAddList = teamAddMap.getOrDefault(LevelEnum.AmountConditionScope.DIRECT.getCode(), CollUtil.newArrayList());
        countParam += teamAddList.stream()
                .filter(it -> it.getType() != null && role.contains(it.getType()))
                .filter(it -> it.getLevel() != null && level.contains(it.getLevel()))
                .mapToInt(TeamUserCountReq::getCount).sum();

        return NumberUtil.div(countParam, count);
    }
}
