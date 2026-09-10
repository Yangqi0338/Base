package com.newzkl.platform.base.biz.market.facade.model;


import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.ddd.model.enums.goods.StoreGoodsEnum;
import lombok.Data;

/**
 * 修改商品数据
 */
@Data
public class DistributionsRpcUpdateReq {

    /**
     * id
     */
    private Long id;

    /**
     * 状态
     * @see StoreGoodsEnum.State
     */
    private StoreGoodsEnum.State goodsState;

    /**
     * 商品信息
     */
    private String goodsInfo;

    /**
     * 零售价 (Money, 落库 BIGINT 分)
     */
    private Money sellPrice;

}