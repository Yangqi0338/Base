package com.newzkl.platform.base.biz.order.domain.spi;


import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderResult;
import com.newzkl.platform.base.biz.order.model.dto.OrderDTO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSkuVO;
import com.newzkl.platform.base.common.ddd.domain.StrategyNullableProcessor;

import java.util.List;

/**
 * 第三方下单策略接口（封装不同第三方的下单逻辑）
 */
public interface ThirdPartyOrderStrategy extends StrategyNullableProcessor {
    /**
     * 创建第三方订单
     * 
     * @param outGoods 外部商品列表
     * @param order    系统内部订单
     * @return 适配后的第三方订单结果
     */
    ThirdPartyOrderResult create(List<OrderSkuVO> outGoods, OrderDTO order);

    ThirdPartyOrderResult delivery(List<OrderSkuVO> outGoods, OrderDTO order);

    void compensation(ThirdPartyOrderDTO request);
}