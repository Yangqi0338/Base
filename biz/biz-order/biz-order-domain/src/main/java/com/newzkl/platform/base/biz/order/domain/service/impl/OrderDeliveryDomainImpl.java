package com.newzkl.platform.base.biz.order.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderDeliveryItemRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderDeliveryRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.SkuOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.OrderDeliveryDomain;
import com.newzkl.platform.base.biz.order.model.order.dto.Order;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderDelivery;
import com.newzkl.platform.base.biz.order.model.order.dto.OrderDeliveryItem;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryUpdateReq;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单发货领域服务（原子能力）
 * @author sijiwang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDeliveryDomainImpl implements OrderDeliveryDomain {

    private final OrderRepository orderRepository;
    private final SkuOrderRepository skuOrderRepository;
    private final OrderDeliveryRepository orderDeliveryRepository;
    private final OrderDeliveryItemRepository orderDeliveryItemRepository;

    private static final int TYPE_WHOLE = 1;
    private static final int TYPE_PART = 2;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDelivery createDelivery(OrderDeliveryCreateReq req) {
        // 1.查询订单
        Order order = orderRepository.getByOrderNo(req.getOrderNo());
        Assert.notNull(order, "订单不存在");
        Assert.isTrue(OrderEnum.State.WAIT_DELIVERY.getCode().equals(order.getOrderState()), "非待发货状态");

        // 2.SKU明细
        List<SkuOrder> skuOrders = skuOrderRepository.listByOrderNo(req.getOrderNo());
        Assert.notEmpty(skuOrders, "无商品明细");
        Map<String, SkuOrder> skuMap = skuOrders.stream()
                .collect(Collectors.toMap(SkuOrder::getSkuOrderNo, s -> s));

        // 3.构建发货项
        boolean isWhole = req.getItems() == null || req.getItems().isEmpty();
        List<OrderDeliveryCreateReq.DeliveryItemReq> items;
        if (isWhole) {
            items = new ArrayList<>();
            skuOrders.forEach(sku -> {
                OrderDeliveryCreateReq.DeliveryItemReq item = new OrderDeliveryCreateReq.DeliveryItemReq();
                item.setSkuOrderNo(sku.getSkuOrderNo());
                item.setDeliveryQuantity(sku.getBuyNum() - sku.getDeliveryQuantity());
                items.add(item);
            });
        } else {
            items = req.getItems();
        }

        // 4.校验数量
        items.forEach(item -> {
            SkuOrder sku = skuMap.get(item.getSkuOrderNo());
            Assert.notNull(sku, "SKU不存在");
            int canSend = sku.getBuyNum() - sku.getDeliveryQuantity();
            Assert.isTrue(item.getDeliveryQuantity() > 0 && item.getDeliveryQuantity() <= canSend, "发货数量不合法");
        });

        // 5.创建发货单
        String deliveryNo = BusinessCodeUtil.generate(BusinessType.ORDER_DELIVERY);
        OrderDelivery delivery = new OrderDelivery();
        delivery.setDeliveryNo(deliveryNo);
        delivery.setOrderNo(order.getOrderNo());
        delivery.setStoreId(order.getStoreId());
        delivery.setChannelId(order.getChannelId());
        delivery.setUserId(order.getUserId());
        delivery.setDeliveryType(isWhole ? TYPE_WHOLE : TYPE_PART);
        delivery.setDeliveryStatus(2);
        delivery.setTotalQuantity(items.stream().mapToInt(OrderDeliveryCreateReq.DeliveryItemReq::getDeliveryQuantity).sum());
        delivery.setLogisticsCode(req.getLogisticsCode());
        delivery.setLogisticsName(req.getLogisticsName());
        delivery.setLogisticsNo(req.getLogisticsNo());
        delivery.setExpressPhone(req.getExpressPhone());
        delivery.setShipAddressId(order.getShipAddressId());
        delivery.setReceiptInfo(order.getReceiptInfo());
        delivery.setOperatorId(req.getOperatorId());
        delivery.setOperatorRole(req.getOperatorRole());
        delivery.setOperatorName(req.getOperatorName());
        delivery.setRemark(req.getRemark());
        delivery.setDeliveryTime(LocalDateTime.now());

        OrderDelivery saved = orderDeliveryRepository.save(delivery);

        // 6.创建明细
        List<OrderDeliveryItem> itemList = new ArrayList<>();
        items.forEach(item -> {
            SkuOrder sku = skuMap.get(item.getSkuOrderNo());
            OrderDeliveryItem di = new OrderDeliveryItem();
            di.setDeliveryNo(deliveryNo);
            di.setOrderNo(order.getOrderNo());
            di.setSpuOrderNo(sku.getSpuOrderNo());
            di.setSkuOrderNo(sku.getSkuOrderNo());
            di.setSkuId(sku.getSkuId());
            di.setOutSkuId(sku.getOutSkuId());
            di.setSpuId(sku.getSpuId());
            di.setSupplierId(sku.getSupplierId());
            di.setDistributionId(sku.getDistributionId());
            di.setBuyNum(sku.getBuyNum());
            di.setDeliveryQuantity(item.getDeliveryQuantity());
            di.setSkuPrice(sku.getOrderPayableAmount() / sku.getBuyNum());
            di.setSkuTotalAmount(sku.getOrderPayableAmount());
            itemList.add(di);

            sku.setDeliveryQuantity(sku.getDeliveryQuantity() + item.getDeliveryQuantity());
            sku.setDeliveryTime(LocalDateTime.now());
            skuOrderRepository.updateById(sku);
        });
        orderDeliveryItemRepository.saveBatch(itemList);

        log.info("[领域层] 创建发货单成功 deliveryNo:{}", deliveryNo);
        return saved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDelivery updateDelivery(OrderDeliveryUpdateReq req) {
        OrderDelivery delivery = orderDeliveryRepository.findById(req.getId()).orElse(null);
        Assert.notNull(delivery, "发货单不存在");

        delivery.setLogisticsCode(req.getLogisticsCode());
        delivery.setLogisticsName(req.getLogisticsName());
        delivery.setLogisticsNo(req.getLogisticsNo());
        delivery.setExpressPhone(req.getExpressPhone());
        delivery.setRemark(req.getRemark());
        return orderDeliveryRepository.save(delivery);
    }

    @Override
    public OrderDelivery getDeliveryDetail(Long deliveryId) {
        OrderDelivery delivery = orderDeliveryRepository.findById(deliveryId).orElse(null);
        if (delivery == null) return null;
        List<OrderDeliveryItem> items = orderDeliveryItemRepository.findByDeliveryNo(delivery.getDeliveryNo());
        delivery.setItemList(items);
        return delivery;
    }

    @Override
    public Page<OrderDelivery> pageDelivery(OrderDeliveryPageReq pageReq) {
        return orderDeliveryRepository.pageQuery(pageReq);
    }
}