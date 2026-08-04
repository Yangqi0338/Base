package com.newzkl.platform.base.biz.order.model.support.api.order;

import com.newzkl.platform.base.common.core.model.dto.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * 订单商品交易res
 * @author sijiwang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderGoodsCheckV2Res implements Serializable {

    /** 商品信息 */
    private List<StoreDistributionDetailRpcVO> goodsInfo;

    /** 商品运费 */
    private Map<Long, Money> goodsFreight;

    /**
     * 本地商品
     */
    private List<OrderSkuVO> localGoods;

    /**
     * 外部商品
     */
    private List<OrderSkuVO> outGoods;
}
