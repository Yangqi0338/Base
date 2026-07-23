package com.newzkl.platform.base.biz.order.domain.adapt.repository;

import com.newzkl.platform.base.biz.order.model.order.dto.OrderDeliveryItem;
import java.util.List;

/**
 * 订单发货明细仓储接口
 * @author sijiwang
 */
public interface OrderDeliveryItemRepository {

    /**
     * 批量保存明细
     */
    void saveBatch(List<OrderDeliveryItem> items);

    /**
     * 根据发货单号查询明细
     */
    List<OrderDeliveryItem> findByDeliveryNo(String deliveryNo);

    /**
     * 根据sku单号查询
     */
    List<OrderDeliveryItem> selectBySkuOrderNos(List<String> skuOrderNos);

}