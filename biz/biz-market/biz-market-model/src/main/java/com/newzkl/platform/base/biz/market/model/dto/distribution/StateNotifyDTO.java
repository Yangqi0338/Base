package com.newzkl.platform.base.biz.market.model.dto.distribution;

import com.newzkl.platform.base.common.ddd.model.enums.goods.StoreGoodsEnum;
import lombok.Data;

@Data
public class StateNotifyDTO {

    /**
     * 商品状态
     * @see StoreGoodsEnum.State
     */
    private StoreGoodsEnum.State goodsState;

    private Long channelId;

    /**
     * 铺货id
     */
    private Long distributionId;

    /**
     * 商品id
     */
    private Long goodsId;

}
