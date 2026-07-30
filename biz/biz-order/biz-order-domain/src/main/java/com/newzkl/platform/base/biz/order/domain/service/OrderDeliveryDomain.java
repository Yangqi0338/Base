package com.newzkl.platform.base.biz.order.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderDelivery;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryUpdateReq;

import java.util.List;
import java.util.Map;

/**
 * 订单发货领域服务（原子业务能力）
 * @author sijiwang
 */
public interface OrderDeliveryDomain {

    /**
     * 创建发货单（仅主单+明细+SKU数量更新）
     */
    OrderDelivery createDelivery(OrderDeliveryCreateReq req);

    /**
     * 修改发货物流信息
     */
    OrderDelivery updateDelivery(OrderDeliveryUpdateReq req);

    /**
     * 根据ID查询发货详情（含明细）
     */
    OrderDelivery getDeliveryDetail(Long deliveryId);

    /**
     * 分页查询发货单列表
     */
    Page<OrderDelivery> pageDelivery(OrderDeliveryPageReq pageReq);

    /**
     * 按 SPU 交易单号查询发货信息, 按 skuId 分组
     *
     * <p>迁移补充: 支撑旧 {@code /sale/deliver/orderDeliverInfo} 端点,
     * 旧实现按发货单 item JSON 里的 skuId 分组, 此处改用发货明细表分组。</p>
     *
     * @param spuOrderNo SPU 交易单号
     * @return skuId -> 该 SKU 命中的发货单列表
     */
    Map<Long, List<OrderDelivery>> deliverInfoBySpuOrderNo(String spuOrderNo);
}