package com.newzkl.platform.base.biz.order.application.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.zkl.scm.finance.model.purse.req.SellAfterRefundReq;
import com.zkl.scm.finance.model.purse.res.MemberRefundRes;
import com.zkl.scm.finance.rpc.facade.pay.IBalancePayFacade;
import com.zkl.scm.goods.rpc.facade.IDistributionRpcFacade;
import com.zkl.scm.goods.rpc.model.distribution.DistributionDetailVO;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.biz.order.model.enums.NotifyEnums;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.order.model.enums.user.identity.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderErrorCode;
import com.zkl.scm.openapi.facade.INotifyFacade;
import com.zkl.scm.openapi.model.NotifyEventContent;

import com.newzkl.platform.base.biz.order.application.service.IUpdateOrderService;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ISkuOrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ISpuOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.ICreateOrderDomain;
import com.newzkl.platform.base.biz.order.infrastructure.adapt.repository.OrderOperationRecordUtils;
import com.newzkl.platform.base.biz.order.model.order.dto.Order;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.CancelOrderReq;
import com.newzkl.platform.base.biz.order.model.order.req.ConfirmOrderReq;
import com.newzkl.platform.base.biz.order.model.order.req.OrderAddressUpdateReq;
import com.newzkl.platform.base.biz.order.model.order.res.CreateOrderRes;
import com.newzkl.platform.base.biz.order.model.order.res.OrderStateCheckRes;
import com.newzkl.platform.base.biz.order.model.order.res.TripSpuOrderChangeRes;
import com.newzkl.platform.base.biz.order.model.order.util.OrderRedisKeyUtils;
import com.newzkl.platform.base.biz.order.model.order.vo.GoodsFreightAggVO;
import com.zkl.scm.user.model.relation.vo.ShipAddressRpcVO;
import com.zkl.scm.user.rpc.facade.IShipAddressFacade;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.biz.TransactionUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sijiwang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateOrderServiceImpl implements IUpdateOrderService {

    @DubboReference
    private IDistributionRpcFacade distributionRpcFacade;
    @DubboReference
    private IShipAddressFacade shipAddressFacade;

    @DubboReference
    private IBalancePayFacade balancePayFacade;

    @DubboReference
    private INotifyFacade notifyFacade;

    private final OrderOperationRecordUtils orderOperationRecordUtil;

    private final IOrderRepository orderRepository;
    private final ISpuOrderRepository spuOrderRepository;
    private final ISkuOrderRepository skuOrderRepository;
    private final ICreateOrderDomain createOrderDomain;
    private final TransactionUtils transactionUtils;

    @Override
    public Boolean changeOrderShip(OrderAddressUpdateReq req) {
        // 1. 获取新地址
        ShipAddressRpcVO newAddress = shipAddressFacade.getAddressDetail(req.getShipId(), req.getAccountId());
        if (newAddress == null) {
            throw new ScmException(BaseErrorCode.NODATA, "收货地址不存在");
        }

        // 2. 读取Redis预订单
        String redisKey = OrderRedisKeyUtils.getPrePayOrderKey(req.getOrderNo(), req.getAccountId());
        CreateOrderRes cacheOrder = RedisUtil.get(redisKey);

        // ======================
        // 场景1：Redis 未提交订单
        // ======================
        if (cacheOrder != null && cacheOrder.getOrder() != null) {
            Order order = cacheOrder.getOrder();
            Set<Integer> allowStates = OrderEnum.State.getAllowModifyShipStates();
            if (!allowStates.contains(order.getOrderState())) {
                OrderEnum.State state = OrderEnum.State.getByCode(order.getOrderState());
                throw new ScmException(BaseErrorCode.UPDATE,
                        "订单状态不允许修改地址：" + (state != null ? state.getInfo() : "未知状态"));
            }

            // 重新计算运费
            Map<Long, Integer> newFreight = calculateNewFreight(cacheOrder, newAddress);
            // 校验运费不变
            validateFreightSame(cacheOrder, newFreight);
            // 更新地址并写回Redis
            CreateOrderRes updatedOrder = copyAndUpdateAddress(cacheOrder, newAddress);
            RedisUtil.set(redisKey, updatedOrder, OrderRedisKeyUtils.TIME_OUT);
            log.info("Redis订单地址修改成功：{}", req.getOrderNo());
            return true;
        }

        // ======================
        // 场景2：数据库已落库订单（使用编程式事务，解决事务失效）
        // ======================
        return transactionUtils.execute(status -> {
            Order dbOrder = orderRepository.getByOrderNo(req.getOrderNo());
            if (dbOrder == null) {
                throw new ScmException(BaseErrorCode.NODATA, "订单不存在");
            }

            // 状态校验
            Set<Integer> allowStates = OrderEnum.State.getAllowModifyShipStates();
            if (!allowStates.contains(dbOrder.getOrderState())) {
                OrderEnum.State state = OrderEnum.State.getByCode(dbOrder.getOrderState());
                throw new ScmException(BaseErrorCode.UPDATE,
                        "订单状态不允许修改地址：" + (state != null ? state.getInfo() : "未知状态"));
            }

            List<SpuOrder> spuOrders = spuOrderRepository.listByOrderNo(req.getOrderNo());
            List<SkuOrder> skuOrders = skuOrderRepository.listByOrderNo(req.getOrderNo());
            if (CollectionUtils.isEmpty(spuOrders) || CollectionUtils.isEmpty(skuOrders)) {
                throw new ScmException(BaseErrorCode.NODATA, "订单商品不存在");
            }

            // 构建临时结构用于运费计算
            CreateOrderRes tempOrder = new CreateOrderRes();
            tempOrder.setOrder(dbOrder);
            List<CreateOrderRes.OrderItem> items = new ArrayList<>();
            for (SpuOrder spu : spuOrders) {
                CreateOrderRes.OrderItem item = new CreateOrderRes.OrderItem();
                item.setSpuOrder(spu);
                item.setSkuOrders(skuOrders.stream()
                        .filter(s -> s.getSpuOrderNo().equals(spu.getSpuOrderNo()))
                        .collect(Collectors.toList()));
                items.add(item);
            }
            tempOrder.setOrderItems(items);

            // 重新计算运费并校验
            Map<Long, Integer> newFreight = calculateNewFreight(tempOrder, newAddress);
            validateFreightSame(tempOrder, newFreight);

            // 更新数据库地址
            Order updateOrder = new Order();
            updateOrder.setOrderNo(req.getOrderNo());
            updateOrder.setShipAddressId(newAddress.getId());
            updateOrder.setReceiptInfo(JSONObject.toJSONString(newAddress));
            updateOrder.setUpdateTime(LocalDateTime.now());
            orderRepository.updateById(updateOrder);

            for (SpuOrder spu : spuOrders) {
                spu.setShipAddressId(newAddress.getId());
                spu.setReceiptInfo(JSONObject.toJSONString(newAddress));
                spu.setUpdateTime(LocalDateTime.now());
                spuOrderRepository.updateById(spu);
            }

            log.info("数据库订单地址修改成功：{}", req.getOrderNo());
            return true;
        });
    }

    @Override
    public Boolean cancelOrder(CancelOrderReq req) {

        Order order = orderRepository.getByOrderNo(req.getOrderNo());
        if (order == null) {
            throw new ScmException(BaseErrorCode.NODATA, "订单不存在");
        }

        if (Objects.equals(order.getOrderState(), OrderEnum.State.CLOSE.getCode())){
            return  true;
        }
        Set<Integer> allowModifyStates;
        if (req.getCompanyRole() == null || req.getCompanyRole().equals(RoleEnum.CompanyRole.MEMBER.getCode())){
            allowModifyStates = OrderEnum.State.getMemberCancelOrderStates();
        }else {
            allowModifyStates = OrderEnum.State.getChannelCancelOrderStates();
        }
        if(!allowModifyStates.contains(order.getOrderState())){
            throw new ScmException(BaseErrorCode.CUSTOM, "当前订单状态不能取消！");
        }
        String cancelReason;
        if (StrUtil.isNotBlank(req.getCancelReason())) {
            cancelReason = req.getCancelReason();
        }else if (req.getCompanyRole() == null){
            cancelReason = "用户取消订单";
        }else if (req.getCompanyRole().equals(RoleEnum.CompanyRole.PLATFORM.getCode())){
            cancelReason = "平台取消订单";
        }else if (req.getCompanyRole().equals(RoleEnum.CompanyRole.MEMBER.getCode())){
            cancelReason = "用户取消订单";
        }else if (req.getCompanyRole().equals(RoleEnum.CompanyRole.SUPPLIER.getCode())){
            cancelReason = "供应商取消订单";
        }else if (req.getCompanyRole().equals(RoleEnum.CompanyRole.CHANNEL.getCode())){
             cancelReason = "渠道商取消订单";
        }else if (req.getCompanyRole().equals(RoleEnum.CompanyRole.OPERATOR.getCode())){
             cancelReason = "运营商取消订单";
        }else {
            cancelReason = "取消订单";
        }
        orderRepository.updateOrderChainStateByOrderNo(order.getOrderNo(), order.getOrderState(), OrderEnum.State.CLOSE.getCode(), cancelReason);

        if (Objects.equals(order.getOrderState(), OrderEnum.State.CHANNEL_WAIT_PAY.getCode())){
            SellAfterRefundReq sellAfterRefundReq = new SellAfterRefundReq();
            sellAfterRefundReq.setOrderNo(order.getId());
            sellAfterRefundReq.setSellAfterOrderNo(order.getId());
            MemberRefundRes memberRefundRes = balancePayFacade.sellAfterRefund(sellAfterRefundReq);
            if (StrUtil.isNotBlank(memberRefundRes.getRefundWarnMsg())) {
                throw new ScmException(OrderErrorCode.REFUND_FAIL, memberRefundRes.getRefundWarnMsg());
            }
        }
        List<SpuOrder> spuOrders = spuOrderRepository.listByOrderNo(req.getOrderNo());
        if (Objects.isNull(SecurityUtils.getAccountId())){
            orderOperationRecordUtil.sendOrderNewRecordEvent(spuOrders, order.getOrderState(), OrderEnum.State.CLOSE.getCode(), 0L,RoleEnum.CompanyRole.PLATFORM);
        }else {
            orderOperationRecordUtil.sendOrderNewRecordEvent(spuOrders, order.getOrderState(),OrderEnum.State.CLOSE.getCode(), SecurityUtils.getAccountId(),SecurityUtils.getRole());
        }
        return true;
    }

    @Override
    public Boolean confirmOrder(ConfirmOrderReq req) {
        Order order = orderRepository.getByOrderNo(req.getOrderNo());
        List<SpuOrder> spuOrders = spuOrderRepository.listByOrderNo(req.getOrderNo());
        if (order == null || CollectionUtils.isEmpty(spuOrders)) {
            throw new ScmException(BaseErrorCode.NODATA, "订单不存在");
        }
        if (Objects.equals(order.getOrderState(), OrderEnum.State.DOWN_RECEIVE.getCode())){
            return Boolean.TRUE;
        }
        if (!Objects.equals(order.getOrderState(), OrderEnum.State.WAIT_RECEIVE.getCode())){
            throw new ScmException(BaseErrorCode.PARAM, "订单状态非可收货状态:" + req.getOrderNo());
        }
        orderRepository.updateOrderChainStateByOrderNo(order.getOrderNo(), order.getOrderState(), OrderEnum.State.DOWN_RECEIVE.getCode(), null);
        List<SkuOrder> waitSettlementOrder = new ArrayList<>();
        spuOrders.forEach(spu -> {
            if (OrderEnum.OrderType.CHANNEL == spu.getOrderType()) {
                List<SkuOrder> skuOrders = skuOrderRepository.getBySpuOrderNo(spu.getSpuOrderNo());
                skuOrders.forEach(sku -> {
                    if (sku.getSettleOrderType() == null){
                        throw new ScmException(BaseErrorCode.PARAM, "供应商结算配置异常!");
                    }
                    if(RoleEnum.OrderType.RECEIVE.getCode().equals(sku.getSettleOrderType())){
                        if(RoleEnum.Switch.OFF.getCode().equals(sku.getSettleSendState())){
                            waitSettlementOrder.add(sku);
                        }
                    }
                });
            }
        });
        if (CollectionUtils.isNotEmpty(waitSettlementOrder)){
            waitSettlementOrder.forEach(v->v.setSettleSendState(RoleEnum.Switch.ON.getCode()));
            skuOrderRepository.batchUpdateSku(waitSettlementOrder);
        }
        if (Objects.isNull(SecurityUtils.getAccountId())){
            orderOperationRecordUtil.sendOrderNewRecordEvent(spuOrders, order.getOrderState(),OrderEnum.State.DOWN_RECEIVE.getCode(),0L,RoleEnum.CompanyRole.PLATFORM);
        }else {
            orderOperationRecordUtil.sendOrderNewRecordEvent(spuOrders, order.getOrderState(),OrderEnum.State.DOWN_RECEIVE.getCode(),SecurityUtils.getAccountId(),SecurityUtils.getRole());
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean completeOrder(ConfirmOrderReq req) {
        Order order = orderRepository.getByOrderNo(req.getOrderNo());
        List<SpuOrder> spuOrders = spuOrderRepository.listByOrderNo(req.getOrderNo());
        if (order == null || CollectionUtils.isEmpty(spuOrders)) {
            throw new ScmException(BaseErrorCode.NODATA, "订单不存在");
        }
        if (Objects.equals(order.getOrderState(), OrderEnum.State.SUCCESS.getCode())){
            return Boolean.TRUE;
        }
        if (!Objects.equals(order.getOrderState(), OrderEnum.State.DOWN_RECEIVE.getCode())){
            throw new ScmException(BaseErrorCode.PARAM, "订单状态非可确认状态:" + req.getOrderNo());
        }
        orderRepository.updateOrderChainStateByOrderNo(order.getOrderNo(), order.getOrderState(), OrderEnum.State.SUCCESS.getCode(), null);

        List<SkuOrder> waitSettlementOrder = new ArrayList<>();
        spuOrders.forEach(spu -> {
            if (OrderEnum.OrderType.CHANNEL == spu.getOrderType()) {
                List<SkuOrder> skuOrders = skuOrderRepository.getBySpuOrderNo(spu.getSpuOrderNo());
                skuOrders.forEach(sku -> {
                    if (sku.getSettleOrderType() == null){
                        throw new ScmException(BaseErrorCode.PARAM, "供应商结算配置异常!");
                    }
                    if(RoleEnum.OrderType.RECEIVE.getCode().equals(sku.getSettleOrderType())){
                        if(RoleEnum.Switch.OFF.getCode().equals(sku.getSettleSendState())){
                            waitSettlementOrder.add(sku);
                        }
                    }
                    // todo 触发分润
                });
            }
        });
        // todo 将订单收益转到渠道商收益账户

        if (CollectionUtils.isNotEmpty(waitSettlementOrder)){
            waitSettlementOrder.forEach(v->v.setSettleSendState(RoleEnum.Switch.ON.getCode()));
            skuOrderRepository.batchUpdateSku(waitSettlementOrder);
        }
        if (CollectionUtils.isNotEmpty(waitSettlementOrder)){
            waitSettlementOrder.forEach(v->v.setSettleSendState(RoleEnum.Switch.ON.getCode()));
            skuOrderRepository.batchUpdateSku(waitSettlementOrder);
        }
        if (Objects.isNull(SecurityUtils.getAccountId())){
            orderOperationRecordUtil.sendOrderNewRecordEvent(spuOrders, order.getOrderState(),OrderEnum.State.SUCCESS.getCode(),0L,RoleEnum.CompanyRole.PLATFORM);
        }else {
            orderOperationRecordUtil.sendOrderNewRecordEvent(spuOrders, order.getOrderState(),OrderEnum.State.SUCCESS.getCode(),SecurityUtils.getAccountId(),SecurityUtils.getRole());
        }
        return Boolean.TRUE;
    }

    @Override
    public TripSpuOrderChangeRes tripSpuOrderChange(List<String> orderNos, List<String> spuOrderNos,
                                                    List<String> skuOrderNos) {
        TripSpuOrderChangeRes tripSpuOrderChangeRes = new TripSpuOrderChangeRes();
        if(ObjectUtil.isEmpty(orderNos)){
            orderNos = new ArrayList<>();
        }
        if(ObjectUtil.isNotEmpty(spuOrderNos) || ObjectUtil.isNotEmpty(skuOrderNos)){
            List<String> orderIdQuery = skuOrderRepository.orderIdBySpuSkuOrderId(spuOrderNos, skuOrderNos);
            orderNos.addAll(orderIdQuery);
        }
        if(orderNos.isEmpty()){
            return tripSpuOrderChangeRes;
        }
        List<OrderStateCheckRes> spuOrderStateCheckRes = skuOrderRepository.checkSpuOrderState(orderNos);
        for (OrderStateCheckRes state : spuOrderStateCheckRes) {
            if(!state.getCurrentState().equals(state.getToState())){
                //修改订单状态
                orderRepository.updateOrderChainStateByOrderNo(state.getOrderNo(), state.getCurrentState(), state.getToState(),null);
            }
        }
        List<OrderStateCheckRes> orderStateCheckRes = spuOrderRepository.checkOrderState(orderNos);
        for (OrderStateCheckRes state : orderStateCheckRes) {
            if(!state.getCurrentState().equals(state.getToState())) {
                //修改交易单状态
                orderRepository.updateOrderChainStateByOrderNo(state.getOrderNo(), state.getCurrentState(), state.getToState(),null);
                tripSpuOrderChangeRes.getOrderStateChange().add(new TripSpuOrderChangeRes.Item(state.getOrderNo(), state.getCurrentState(), state.getToState()));
                //开发者通知
                if (OrderEnum.OrderType.CHANNEL.getCode().equals(state.getOrderType())) {
                    NotifyEventContent notifyEventContent = new NotifyEventContent();
                    notifyEventContent.setServiceType(NotifyEnums.ServiceType.ORDER.getCode());
                    notifyEventContent.setBusinessType(NotifyEnums.OrderType.TRADE_STATE.getCode());
//                    notifyEventContent.setEventInfo(com.alibaba.fastjson.JSONObject.toJSONString(new ApiOrderStateEvent(outOrderNo, currentState, toState)));
                    notifyFacade.batchSend(Collections.singletonList(state.getChannelId()), notifyEventContent);
                }
                //确认收货通知怡亚通
//                if(OrderEnum.State.DOWN_RECEIVE.getCode().equals(state.getToState())){
//                    String outOrderId = orderRepository.queryOutOrderId(state.getId());
//                    if(StringUtils.isNotEmpty(outOrderId)){
//                        Map<String, Object> confirmGoodsParams = new HashMap<>();
//                        confirmGoodsParams.put("orderSn", "outOrderId");
//                        confirmGoodsParams.put("operator", "");
//                        String confirmGoodsRes = YytClientUtil.httpPostRequest("scce/ctc/ctc/reseller/order/confirmReceive", confirmGoodsParams);
//                    }
//                }
            }
        }
        return tripSpuOrderChangeRes;
    }

    private Map<Long, Integer> calculateNewFreight(CreateOrderRes orderRes, ShipAddressRpcVO address) {
        List<Long> distIds = orderRes.getOrderItems().stream()
                .flatMap(it -> it.getSkuOrders().stream())
                .map(SkuOrder::getDistributionId)
                .distinct().collect(Collectors.toList());

        List<DistributionDetailVO> details = distributionRpcFacade.queryDistributionDetailByIds(distIds);
        if (CollectionUtils.isEmpty(details)) throw new ScmException(BaseErrorCode.PARAM, "商品信息异常");

        Map<Long, GoodsFreightAggVO> aggMap = createOrderDomain.aggregateGoodsFreightData(details);
        return createOrderDomain.calculateFreight(aggMap, address);
    }

    private void validateFreightSame(CreateOrderRes orderRes, Map<Long, Integer> newFreightMap) {
        Map<Long, Integer> oldMap = new HashMap<>();
        orderRes.getOrderItems().forEach(item ->
                item.getSkuOrders().forEach(sku ->
                        oldMap.put(sku.getSpuId(), sku.getFreightAmount().intValue())
                )
        );

        for (Map.Entry<Long, Integer> entry : newFreightMap.entrySet()) {
            Long spuId = entry.getKey();
            Integer now = entry.getValue();
            Integer old = oldMap.get(spuId);
            if (!Objects.equals(now, old)) {
                throw new ScmException(BaseErrorCode.UPDATE,
                        StrUtil.format("地址修改后运费发生变化，不允许修改 | SPU：%s | 原：%s | 新：%s"
                                , spuId, old, now));
            }
        }
    }

    private CreateOrderRes copyAndUpdateAddress(CreateOrderRes old, ShipAddressRpcVO addr) {
        CreateOrderRes neo = new CreateOrderRes();
        neo.setRemainTime(old.getRemainTime());
        neo.setOrder(TransferUtils.transfer(old.getOrder(), Order::new));
        neo.getOrder().setShipAddressId(addr.getId());
        neo.getOrder().setReceiptInfo(JSONObject.toJSONString(addr));

        List<CreateOrderRes.OrderItem> items = new ArrayList<>();
        old.getOrderItems().forEach(item -> {
            CreateOrderRes.OrderItem newItem = new CreateOrderRes.OrderItem();
            newItem.setSkuOrders(item.getSkuOrders());
            newItem.setSpuOrder(TransferUtils.transfer(item.getSpuOrder(), SpuOrder::new));
            newItem.getSpuOrder().setShipAddressId(addr.getId());
            newItem.getSpuOrder().setReceiptInfo(JSONObject.toJSONString(addr));
            items.add(newItem);
        });
        neo.setOrderItems(items);
        return neo;
    }
}