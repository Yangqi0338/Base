package com.newzkl.platform.base.biz.order.domain.factory.freightStrategy;

import com.zkl.scm.goods.rpc.model.order.OrderSkuVO;
import com.zkl.scm.openapi.facade.ThirdPartyOrderFacade;
import com.newzkl.platform.base.biz.order.model.order.dto.Order;
import com.newzkl.platform.base.biz.order.model.order.dto.OutOrder;


import java.util.List;

/**
 * 第三方下单策略接口（封装不同第三方的下单逻辑）
 */
public interface ThirdPartyOrderStrategy {
    /**
     * 创建第三方订单
     *
     * @param outGoods 外部商品列表
     * @param order    系统内部订单
     * @return 适配后的第三方订单结果
     */
    ThirdPartyOrderResult createOrder(List<OrderSkuVO> outGoods, Order order);

    /**
     * 将第三方订单结果转换为系统内部OutOrder实体
     *
     * @param result  第三方订单结果（已适配）
     * @param orderId 系统内部订单ID
     * @return 系统内部外部订单列表
     */
    List<OutOrder> buildOutOrders(ThirdPartyOrderResult result, Long orderId, List<OrderSkuVO> outGoods);

    void saveOutOrdersLog(ThirdPartyOrderResult result, ThirdPartyOrderFacade thirdPartyOrderService);
}