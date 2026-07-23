package com.newzkl.platform.base.biz.goods.rpc.model.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author niu
 * @description: 订单商品交易res
 * @date 2024/5/7 16:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderGoodsCheckRes implements Serializable {

    private List<OrderGoodsInfoVO> goodsInfo;

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
