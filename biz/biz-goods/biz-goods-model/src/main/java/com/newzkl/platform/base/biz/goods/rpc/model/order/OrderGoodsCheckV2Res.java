package com.newzkl.platform.base.biz.goods.rpc.model.order;

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

    private List<StoreDistributionDetailRpcVO> goodsInfo;

    private Map<Long,Integer> goodsFreight;

    /**
     * 本地商品
     */
    private List<OrderSkuVO> localGoods;

    /**
     * 外部商品
     */
    private List<OrderSkuVO> outGoods;
}
