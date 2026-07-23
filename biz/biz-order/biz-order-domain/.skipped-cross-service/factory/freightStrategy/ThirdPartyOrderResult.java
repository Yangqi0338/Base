package com.newzkl.platform.base.biz.order.domain.factory.freightStrategy;

import java.util.List;

/**
 * 第三方订单结果适配接口（统一不同第三方的返回结果格式）
 */
public interface ThirdPartyOrderResult {
    /**
     * 获取第三方订单号
     */
    String getOrderSn();

    /**
     * 获取SKU ID列表（格式由第三方定义，系统内部不解析）
     */
    String getSkuIds();

    /**
     * 获取子订单列表（部分第三方可能返回多个子订单）
     */
    List<? extends ThirdPartyOrderResult> getSubOrders();

    String getOrderRes();

    String getOrderReq();
}