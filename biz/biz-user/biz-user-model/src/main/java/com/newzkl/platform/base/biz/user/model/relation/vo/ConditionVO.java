package com.newzkl.platform.base.biz.user.model.relation.vo;

import com.newzkl.platform.base.biz.user.model.relation.res.condition.AmountCondition;
import com.newzkl.platform.base.biz.user.model.relation.res.condition.GoodsCondition;
import com.newzkl.platform.base.biz.user.model.relation.res.condition.PackCondition;
import com.newzkl.platform.base.biz.user.model.relation.res.condition.TeamCondition;
import com.newzkl.platform.base.biz.user.model.relation.res.condition.TeamDirectCondition;
import com.newzkl.platform.base.biz.user.model.relation.res.condition.TeamNoDirectCondition;
import lombok.Data;

import java.io.Serializable;

/**
 * 条件计算模型
 *
 * @author fang
 */
@Data
public class ConditionVO implements Serializable {

    /**
     * 条件有效性类型 0 满足任意一项 1 全部满足
     */
    private Integer conditionJudgeType;
    /**
     * 礼包条件
     */
    private PackCondition pack;
    /**
     * 订单流水条件
     */
    private AmountCondition amount;
    /**
     * 商品条件
     */
    private GoodsCondition goods;
    /**
     * 团队条件(伞下)
     */
    private TeamCondition team;
    /**
     * 直属下级条件(部门)
     */
    private TeamDirectCondition teamDirect;
    /**
     * 非直属下级条件
     */
    private TeamNoDirectCondition teamNoDirect;

}
