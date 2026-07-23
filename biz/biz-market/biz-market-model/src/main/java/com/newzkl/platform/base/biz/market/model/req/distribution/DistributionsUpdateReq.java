package com.newzkl.platform.base.biz.market.model.req.distribution;


import com.newzkl.platform.base.biz.market.model.enums.DistributionEnum;
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
     * 零售价
     */
    private Integer sellPrice;

}