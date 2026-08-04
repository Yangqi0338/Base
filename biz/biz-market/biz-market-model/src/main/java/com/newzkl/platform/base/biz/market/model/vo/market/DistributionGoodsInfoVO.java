package com.newzkl.platform.base.biz.market.model.vo.market;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

/**
 * @author niu
 * @description: 铺货商品信息
 * @date 2024/4/2 14:51
 */
@Data
public class DistributionGoodsInfoVO {

    /**
     * skuId
     */
    private Long skuId = 0L;

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 销售价格 (Money, 落库 BIGINT 分)
     */
    private Money sellPrice;

    /**
     * 市场id
     */
    private Long marketId;

    /** 分类ID */
    private Long categoryId;

    /**
     * 零售价(渠道商的供货价) (Money, 落库 BIGINT 分)
     */
    private Money unitPrice;

    /**
     * 来源
     */
    private String source;

}
