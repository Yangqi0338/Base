package com.newzkl.platform.base.biz.order.model.support.api.order;


import com.newzkl.platform.base.common.core.model.dto.Money;
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

    /** 商品信息 */
    private List<OrderGoodsInfoVO> goodsInfo;

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
