package com.newzkl.platform.base.biz.user.model.relation.res.condition;

import com.newzkl.platform.base.biz.user.model.relation.req.ConditionReq;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 条件抽象
 *
 * @author fang
 */
public interface Condition {

    /**
     * 是否满足
     *
     * @param conditionCommand 条件请求
     * @return 满足度
     */
    double isMeet(ConditionReq conditionCommand);

    /**
     * 条件满足类型
     */
    @Getter
    @AllArgsConstructor
    enum ConditionType {
        /** 购买的包裹等级条件 */
        PACK(-1, "pack", "购买的包裹等级条件", PackCondition.class),
        /** 直属|非直属下级的总流水条件 */
        AMOUNT(0, "amount", "直属|非直属下级的总流水条件", AmountCondition.class),
        /** 商品数量条件 */
        GOODS(1, "goods", "商品数量条件", GoodsCondition.class),
        /** 团队成员条件 */
        TEAM(2, "team", "团队成员条件", TeamCondition.class),
        /** 直属下级条件(部门) */
        TEAM_DIRECT(3, "teamDirect", "直属下级条件(部门)", TeamDirectCondition.class),
        /** 非直属下级条件(部门下) */
        TEAM_NO_DIRECT(4, "teamNoDirect", "非直属下级条件(部门下)", TeamNoDirectCondition.class),
        ;
        private final Integer code;
        private final String key;
        private final String desc;
        private final Class<? extends Condition> clazz;
    }

}
