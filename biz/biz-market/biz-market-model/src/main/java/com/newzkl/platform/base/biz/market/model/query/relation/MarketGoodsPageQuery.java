package com.newzkl.platform.base.biz.market.model.query.relation;

import com.newzkl.platform.base.common.ddd.model.query.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author niu
 * @description: 查询市场商品请求对象
 * @date 2023/12/8 11:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MarketGoodsPageQuery extends PageQuery {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 市场id
     */
    private Long marketId;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 销售价左
     */
    private Integer salePriceL;

    /**
     * 销售价右
     */
    private Integer salePriceR;

    private String spuState;

}
