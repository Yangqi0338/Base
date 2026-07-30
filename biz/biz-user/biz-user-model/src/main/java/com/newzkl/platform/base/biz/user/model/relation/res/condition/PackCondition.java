package com.newzkl.platform.base.biz.user.model.relation.res.condition;

import com.newzkl.platform.base.biz.user.model.relation.req.ConditionReq;
import com.newzkl.platform.base.biz.user.model.relation.res.PackGoodsInfo;
import lombok.Data;

/**
 * 礼包条件
 *
 * @author fang
 */
@Data
public class PackCondition implements Condition {
    /**
     * 价格
     */
    private Integer amount;
    /**
     * 礼包商品ID
     */
    private Long packGoodsId;
    /**
     * 礼包名称
     */
    private String packName;
    /**
     * 礼包图片
     */
    private String packImg;

    @Override
    public double isMeet(ConditionReq conditionCommand) {
        PackGoodsInfo packInfo = conditionCommand.getPackInfo();
        if (packInfo == null) {
            return 0.0;
        }
        return packInfo.getId().equals(packGoodsId) ? 100.0 : 0.0;
    }
}
