package com.newzkl.platform.base.biz.market.model.vo.relation;

import lombok.Data;

/**
 * 市场商品关系表中的商品信息
 */
@Data
public class MarketGoodsInfoVO {

    /**
     * 赠送LT积分
     */
    private Integer giftLTPoints;

    /**
     * 商品标签
     */
    private String label;

    /**
     * 任务红包比例
     */
    private Integer taskRedPacketRatio;

}
