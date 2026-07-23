package com.newzkl.platform.base.biz.order.application.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.model.enums.NotifyEnums;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.order.model.enums.user.identity.RoleEnum;
import com.zkl.scm.openapi.facade.INotifyFacade;
import com.zkl.scm.openapi.model.ApiDeliverEvent;
import com.zkl.scm.openapi.model.NotifyEventContent;
import com.zkl.scm.openapi.model.SkuCountDTO;
import com.newzkl.platform.base.biz.order.application.service.IOrderDeliveryAppService;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ISettleOrderWaitRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ISkuOrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ISpuOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.IOrderDeliveryDomain;
import com.newzkl.platform.base.biz.order.infrastructure.adapt.repository.OrderOperationRecordUtils;
import com.newzkl.platform.base.biz.order.model.order.dto.*;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryCreateReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderDeliveryUpdateReq;
import com.zkl.scm.user.rpc.facade.ISupplierFacade;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单发货应用服务实现（业务流程编排）
 *
 * @author sijiwang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDeliveryAppServiceImpl implements IOrderDeliveryAppService {

    private final IOrderDeliveryDomain orderDeliveryDomain;
    private final IOrderRepository orderRepository;
    private final ISkuOrderRepository skuOrderRepository;
    private final ISpuOrderRepository spuOrderRepository;
    private final ISettleOrderWaitRepository settleOrderWaitRepository;
    private final OrderOperationRecordUtils orderOperationRecordUtil;

    @DubboReference
    private INotifyFacade notifyFacade;
    @DubboReference
    private ISupplierFacade supplierFacade;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDelivery deliverOrder(OrderDeliveryCreateReq req) {
        // 1.领域层创建发货单
        OrderDelivery delivery = orderDeliveryDomain.createDelivery(req);
        String orderNo = req.getOrderNo();

        // 2.更新订单状态
        orderRepository.updateOrderChainStateByOrderNo(orderNo, OrderEnum.State.WAIT_DELIVERY.getCode(),
                OrderEnum.State.WAIT_RECEIVE.getCode(), null);

        // 3.外部订单通知
        Order order = orderRepository.getByOrderNo(orderNo);
        if (order.getIsExternalOrder() == 1) {
            NotifyEventContent content = new NotifyEventContent();
            content.setServiceType(NotifyEnums.ServiceType.ORDER.getCode());
            content.setBusinessType(NotifyEnums.OrderType.DELIVERY.getCode());

            List<SkuCountDTO> skuList = delivery.getItemList().stream().map(it -> {
                SkuCountDTO dto = new SkuCountDTO();
                dto.setSkuId(it.getSkuId());
                dto.setCount(it.getDeliveryQuantity());
                return dto;
            }).collect(Collectors.toList());

            ApiDeliverEvent event = new ApiDeliverEvent(order.getOutOrderNo(), skuList, req.getLogisticsName(),
                    req.getLogisticsNo());
            content.setEventInfo(JSONObject.toJSONString(event));
            notifyFacade.batchSend(Collections.singletonList(order.getChannelId()), content);
            log.info("[应用层] 外部订单发货通知成功 orderNo:{}", orderNo);
        }

        // 4.生成运费待结算
        List<SkuOrder> skuOrders = skuOrderRepository.listByOrderNo(orderNo);
        List<SettleOrderWait> settleList = new ArrayList<>();
        skuOrders.stream().filter(s -> s.getFreightAmount() != null && s.getFreightAmount() > 0)
                .filter(s -> Objects.equals(s.getSettleSendState(), RoleEnum.Switch.OFF.getCode())).forEach(sku -> {
                    SettleOrderWait wait = new SettleOrderWait();
                    wait.setSpuOrderNo(sku.getSpuOrderNo());
                    wait.setSkuOrderNo(sku.getSkuOrderNo());
                    wait.setSupplierId(sku.getSupplierId());
                    wait.setSpuId(sku.getSpuId());
                    wait.setSkuId(sku.getSkuId());
                    wait.setOrderMoney(sku.getFreightAmount().intValue());
                    wait.setType(1);
                    wait.setSettleState(RoleEnum.Switch.OFF.getCode());
                    wait.setRefundState(RoleEnum.Switch.OFF.getCode());
                    wait.setSkuCount(1);

                    Integer node = supplierFacade.settleOrderType(sku.getSupplierId());
                    wait.setSettleTimeNode(node > 1 ? -1L : 0L);
                    settleList.add(wait);

                    sku.setSettleSendState(RoleEnum.Switch.ON.getCode());
                    skuOrderRepository.updateById(sku);
                });

        if (!CollectionUtils.isEmpty(settleList)) {
            settleOrderWaitRepository.saveBatch(settleList);
        }

        // 5.记录操作日志
        List<SpuOrder> spuOrders = spuOrderRepository.listByOrderNo(orderNo);
        if (Objects.isNull(SecurityUtils.getAccountId())) {
            orderOperationRecordUtil.sendOrderNewRecordEvent(spuOrders, order.getOrderState(),
                    OrderEnum.State.WAIT_RECEIVE.getCode(), 0L, RoleEnum.CompanyRole.PLATFORM);
        } else {
            orderOperationRecordUtil.sendOrderNewRecordEvent(spuOrders, order.getOrderState(),
                    OrderEnum.State.WAIT_RECEIVE.getCode(), SecurityUtils.getAccountId(), SecurityUtils.getRole());
        }

        log.info("[应用层] 订单发货全流程完成 orderNo:{} deliveryNo:{}", orderNo, delivery.getDeliveryNo());
        return delivery;
    }

    @Override
    public OrderDelivery updateDelivery(OrderDeliveryUpdateReq req) {
        return orderDeliveryDomain.updateDelivery(req);
    }

    @Override
    public OrderDelivery getDeliveryDetail(Long deliveryId) {
        return orderDeliveryDomain.getDeliveryDetail(deliveryId);
    }

    @Override
    public Page<OrderDelivery> pageDelivery(OrderDeliveryPageReq pageReq) {
        return orderDeliveryDomain.pageDelivery(pageReq);
    }
}