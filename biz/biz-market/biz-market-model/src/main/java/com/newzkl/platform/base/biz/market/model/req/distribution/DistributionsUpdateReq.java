package com.newzkl.platform.base.biz.market.model.req.distribution;


import com.newzkl.platform.base.common.ddd.model.enums.goods.DistributionEnum;
import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.Data;

/**
 * 修改商品数据
 */
@Data
public class DistributionsUpdateReq {

    /**
     * id
     */
    private Long id;

    /**
     * 状态
     * @see DistributionEnum.State
     */
    private Integer goodsState;

    /**
     * 商品信息
     */
    private String goodsInfo;

    /**
     * 零售价 (Money, 落库 BIGINT 分)
     */
    private Money sellPrice;

}