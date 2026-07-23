package com.newzkl.platform.base.biz.market.model.res.relation;

import lombok.Data;

import java.util.List;

/**
 * @author niu
 * @description: 查询商品市场对应
 * @date 2024/4/9 17:11
 */
@Data
public class QueryGoodsMarketCorrespondenceRes {

    /**
     * 市场id
     */
    private Long marketId;

    /**
     * 商品集合
     */
    private List<Long> goodsId;
}
