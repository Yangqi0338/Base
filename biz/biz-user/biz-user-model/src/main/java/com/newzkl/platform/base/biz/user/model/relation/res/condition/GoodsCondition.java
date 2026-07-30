package com.newzkl.platform.base.biz.user.model.relation.res.condition;

import cn.hutool.core.util.NumberUtil;
import com.newzkl.platform.base.biz.user.model.relation.req.ConditionReq;
import lombok.Data;

/**
 * 供应商商品数量条件
 *
 * @author fang
 */
@Data
public class GoodsCondition implements Condition {

    /**
     * 商品数量
     */
    private Integer count;
    /**
     * 商品状态
     */
    private Integer state;

    @Override
    public double isMeet(ConditionReq conditionCommand) {
        Integer goodsAuditCount = conditionCommand.getGoodsAuditCount();
        if (goodsAuditCount == null) {
            return 0.0;
        }
        return NumberUtil.div(goodsAuditCount, count).doubleValue();
    }
}
