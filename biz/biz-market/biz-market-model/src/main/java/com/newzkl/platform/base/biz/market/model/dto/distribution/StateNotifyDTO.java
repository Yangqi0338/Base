package com.newzkl.platform.base.biz.market.model.dto.distribution;

import com.newzkl.platform.base.biz.market.model.enums.DistributionEnum;
import lombok.Data;

@Data
public class StateNotifyDTO {

    /**
     * 商品状态
     * @see DistributionEnum.State
     */
    private Integer goodsState;

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
