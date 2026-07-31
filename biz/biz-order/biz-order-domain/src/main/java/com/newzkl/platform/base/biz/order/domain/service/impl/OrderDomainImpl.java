package com.newzkl.platform.base.biz.order.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.newzkl.platform.base.biz.order.domain.adapt.api.BalancePayApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.ChannelSettleReq;
import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsSpuApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.MemberRefundRes;
import com.newzkl.platform.base.biz.order.domain.adapt.api.OrderGoodsCheckApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.SellAfterRefundReq;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.IOrderDomain;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.res.*;
import com.newzkl.platform.base.biz.order.model.support.api.ChannelNowServiceFeeRes;
import com.newzkl.platform.base.biz.order.model.support.api.EarningsConfigRpcVO;
import com.newzkl.platform.base.biz.order.model.support.api.openapi.ApiSpuVO;
import com.newzkl.platform.base.biz.order.model.support.api.order.GoodsVO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckReq;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckRes;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsInfoVO;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.model.constant.DeliverErrorCode;
import com.newzkl.platform.base.common.ddd.model.constant.OrderErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/129:35
 */
@Service
@RequiredArgsConstructor
public class OrderDomainImpl implements IOrderDomain {

    private final IOrderRepository orderRepository;
    private final GoodsSpuApi spuFacade;

    // 迁移: 原 @DubboReference IBalancePayApi/IOrderGoodsFacade 违 domain 依赖硬线, 改走 adapt/api 出站端口
    private final BalancePayApi balancePayApi;

    private final OrderGoodsCheckApi orderGoodsFacade;

    @Override
    public OrderCreateRes createOrder(OrderGoodsCheckRes data, OrderCreateCommand orderCreateCommand,
                                      MemberOrderCreateCommand memberOrderCreateCommand) {
        OrderEnum.State state = null;
        if(OrderEnum.OrderType.MEMBER == orderCreateCommand.getOrderType()){
            state = OrderEnum.State.MEMBER_WAIT_PAY;
        }else if(OrderEnum.OrderType.Channel == orderCreateCommand.getOrderType()){
            state = OrderEnum.State.CHANNEL_WAIT_PAY;
        }
        List<OrderGoodsInfoVO> goodsInfo = data.getGoodsInfo();
        Map<Long, Integer> goodsFreight = data.getGoodsFreight();
        //获取渠道商当前服务费比例
//        String feeRedisKey = RedisEnum.Key.FEE_CONFIG.getCode(orderCreateCommand.getChannelId().toString());
//        ChannelNowServiceFeeRes channelNowServiceFee = redisClient.getCacheObject(feeRedisKey);
//        if(channelNowServiceFee == null){
        ChannelNowServiceFeeRes channelNowServiceFee = orderRepository.queryChannelNowServiceFee(orderCreateCommand.getChannelId());
//            redisClient.setCacheObjectTimeOut(feeRedisKey, channelNowServiceFee, 24, TimeUnit.HOURS);
//        }
        //获取渠道商分润配置
        // 迁移: 原 domain 直连 redisClient 缓存违依赖硬线, 缓存下沉 infra(@Cacheable), domain 直调 repository
        EarningsConfigRpcVO earningsConfigRpcVO = orderRepository.channelEarningsConfig(orderCreateCommand.getChannelId());
        orderCreateCommand.setOperatorId(earningsConfigRpcVO.getUpOperatorId());
        Long orderId = SnowflakeIdAble.getSnowflakeId();
        Map<Long, Long> spuOrderIdMap = new HashMap<>();
        List<SkuOrder> skuOrderList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        // 构建订单数据
        if(StrUtil.isEmpty(orderCreateCommand.getOutOrderNo())){
            orderCreateCommand.setOutOrderNo(orderId.toString());
        }
        for (OrderGoodsInfoVO orderGoodsInfoVO : goodsInfo) {
            Long spuId = orderGoodsInfoVO.getSpuId();
            if(!spuOrderIdMap.containsKey(spuId)){
                Long spuOrderId = SnowflakeIdAble.getSnowflakeId();
                spuOrderIdMap.put(spuId, spuOrderId);
            }

            // 迁移: 原 redisClient.getCacheObjectAuto 缓存下沉 infra, domain 直调 repository
            RoleEnum.OrderType settleOrderType = orderRepository.settleOrderType(orderGoodsInfoVO.getSupplierId());
            Integer settleOrderTypeCode = settleOrderType == null ? null : settleOrderType.getCode();
            skuOrderList.add(buildSkuOrder(orderGoodsInfoVO,spuOrderIdMap.get(spuId),orderId,channelNowServiceFee,earningsConfigRpcVO,settleOrderTypeCode,now));
        }
        //根据sku订单
        Map<Long,SpuOrder> spuOrderMap = skuOrderList.parallelStream().collect(Collectors.groupingBy(SkuOrder::getSpuOrderId,
                Collectors.collectingAndThen(Collectors.toList(),m->{
                    SkuOrder skuOrder = m.parallelStream().findFirst().get();
                    SpuOrder spuOrder = new SpuOrder();
                    spuOrder.setId(skuOrder.getSpuOrderId());
                    spuOrder.setSkuCount(m.parallelStream().mapToInt(SkuOrder::getCount).sum());
                    spuOrder.setSupplierAmount(m.parallelStream().mapToInt(SkuOrder::getSupplierAmount).sum());
                    spuOrder.setGoodsAmount(m.parallelStream().mapToInt(SkuOrder::getGoodsAmount).sum());
                    spuOrder.setStoreAmount(m.parallelStream().mapToInt(SkuOrder::getStoreAmount).sum());
                    spuOrder.setFreightAmount(goodsFreight.get(skuOrder.getSpuId()));
                    spuOrder.setServiceAmount(m.parallelStream().mapToInt(SkuOrder::getTotalServiceChange).sum());
                    skuOrder.setCreateTime(now);
                    // 填充数据
                    fillIn(spuOrder,orderCreateCommand,memberOrderCreateCommand,skuOrder);
                    if (spuOrder.getMemberAmount() == null){
                        spuOrder.setMemberAmount(spuOrder.getStoreAmount()+spuOrder.getFreightAmount()-spuOrder.getDiscountAmount());
                    }
                    return spuOrder;
                })));
        List<SpuOrder> spuOrderList = new ArrayList<>(spuOrderMap.values());
        //生成交易单
        Order order = new Order();
        order.init(orderCreateCommand, orderId, spuOrderList);
        OrderSnapVO orderSnapVO = new OrderSnapVO();
        orderSnapVO.setLocalGoods(data.getLocalGoods());
        orderSnapVO.setOutGoods(data.getOutGoods());
        order.setOrderSnapVO(orderSnapVO);
        order.setOrderState(state);
        order.setStoreId(memberOrderCreateCommand.getStoreId());
        order.setAccountId(memberOrderCreateCommand.getAccountId());
        order.setUserName(memberOrderCreateCommand.getUserName());
        order.setNickname(memberOrderCreateCommand.getNickName());
        order.setCreateTime(now);
        //组装交易单聚合
        OrderAgg orderAgg = new OrderAgg();
        orderAgg.setOrder(order);
        orderAgg.setSkuOrderList(skuOrderList);
        orderAgg.setSpuOrderList(spuOrderList);
        OrderCreateRes orderCreateRes = new OrderCreateRes(orderId, new ArrayList<>(spuOrderMap.keySet()), order.getOrderState(), orderAgg);
        //前端使用的支付倒计时
        orderCreateRes.setRemainTime(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return orderCreateRes;
    }

    @Override
    public void orderAggSave(OrderAgg orderAgg) {
        orderRepository.orderAggSave(orderAgg);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendOrder(List<Long> orderIdList) {
        if(ObjectUtil.isEmpty(orderIdList)){
            return;
        }
        //批量修改订单状态
        orderRepository.batchUpdateOrderState(orderIdList, OrderEnum.State.SENDING, OrderEnum.State.WAIT_DELIVERY);
        orderRepository.batchUpdateSpuOrderStateByOrderId(orderIdList, OrderEnum.State.SENDING, OrderEnum.State.WAIT_DELIVERY, null);
        orderRepository.batchUpdateSkuOrderStateByOrderId(orderIdList, OrderEnum.State.SENDING, OrderEnum.State.WAIT_DELIVERY);

        orderIdList.forEach(id ->{
            OrderAgg orderAgg = orderRepository.orderAgg(id);
            orderRepository.sendOrderNewRecordEvent(orderAgg.getSpuOrderList(), OrderEnum.State.SENDING,OrderEnum.State.WAIT_DELIVERY,RoleEnum.CompanyRole.PLATFORM.getCode(),RoleEnum.CompanyRole.PLATFORM);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeliverRes deliverCreate(DeliverCommand deliverCommand) {
        SpuOrderVO spuOrderVO = orderRepository.spuOrderVO(deliverCommand.getSpuOrderId());
        deliverCommand.setExpressMobile(spuOrderVO.getShipPhone());
        if(spuOrderVO == null) {
            ThrowsException.exception(BaseErrorCode.PARAM, "不存在的订单ID:" + deliverCommand.getSpuOrderId());
        }
        DeliverRes deliverRes = new DeliverRes();
        //订单检查
        if(OrderEnum.State.WAIT_DELIVERY != spuOrderVO.getOrderState()){
            ThrowsException.exception(DeliverErrorCode.ORDER_STATE_CANNOT, deliverCommand.getSpuOrderId());
        }
        //已发货信息查询
        List<AlreadyDeliverRes> alreadyDeliverResList = null;
        Map<Long, AlreadyDeliverRes> alreadyDeliverResMap = null;
        if(ObjectUtil.isEmpty(deliverCommand.getDeliverItemCommandList())){
            //整单发货, 订单发货信息查询. 发货参数赋值为指定发货参数
            alreadyDeliverResList = orderRepository.getAlreadyDeliverResList(spuOrderVO.getId(), null);
            List<DeliverItemCommand> deliverItemList = TransferUtils.transfers(alreadyDeliverResList, new Function<AlreadyDeliverRes, DeliverItemCommand>() {
                @Override
                public DeliverItemCommand apply(AlreadyDeliverRes alreadyDeliverRes) {
                    DeliverItemCommand deliverItemCommand = new DeliverItemCommand();
                    deliverItemCommand.setSkuId(alreadyDeliverRes.getSkuId());
                    deliverItemCommand.setCount(alreadyDeliverRes.getOrderCount() - alreadyDeliverRes.getDeliverCount());
                    return deliverItemCommand;
                }
            });
            deliverCommand.setDeliverItemCommandList(deliverItemList);
            alreadyDeliverResMap = alreadyDeliverResList.stream().collect(Collectors.toMap(AlreadyDeliverRes::getSkuId, Function.identity()));
        }else {
            //拆单发货
            List<Long> skuIds = deliverCommand.getDeliverItemCommandList().stream().map(DeliverItemCommand::getSkuId).collect(Collectors.toList());
            alreadyDeliverResList = orderRepository.getAlreadyDeliverResList(spuOrderVO.getId(), skuIds);
            alreadyDeliverResMap = alreadyDeliverResList.stream().collect(Collectors.toMap(AlreadyDeliverRes::getSkuId, Function.identity()));
        }
        //商品检查
        List<Long> deliverSuccessSkuIdList = new ArrayList<>();
        for (DeliverItemCommand deliverItemCommand : deliverCommand.getDeliverItemCommandList()) {
            AlreadyDeliverRes alreadyDeliverRes = alreadyDeliverResMap.get(deliverItemCommand.getSkuId());
            if(alreadyDeliverRes == null){
                ThrowsException.exception(BaseErrorCode.PARAM, "不存在的SKU_ID:" + deliverItemCommand.getSkuId());
            }
            if(deliverItemCommand.getCount() == null) {
                deliverItemCommand.setCount(alreadyDeliverRes.getOrderCount() - alreadyDeliverRes.getDeliverCount());
            }
            if(deliverItemCommand.getCount() > alreadyDeliverRes.getOrderCount() - alreadyDeliverRes.getDeliverCount()){
                ThrowsException.exception(DeliverErrorCode.DELIVER_OVER, deliverItemCommand.getSkuId());
            }
            if(deliverItemCommand.getCount() == alreadyDeliverRes.getOrderCount() - alreadyDeliverRes.getDeliverCount()){
                deliverSuccessSkuIdList.add(deliverItemCommand.getSkuId());
            }
        }
        //初始化发货单聚合
        Deliver deliver = new Deliver();
        deliver.init(deliverCommand, spuOrderVO);
        //持久化发货单聚合
        orderRepository.deliverSave(deliver);
        //状态修改
        if(ObjectUtil.isNotEmpty(deliverSuccessSkuIdList)){
            //修改已发货完成的SKU订单状态
            List<Long> deliverSuccessSkuOrderIdList = orderRepository.querySkuOrderIdList(deliverCommand.getSpuOrderId(), deliverSuccessSkuIdList);
            SkuOrderCommand skuOrderCommand = new SkuOrderCommand();
            skuOrderCommand.setDeliveredTime(DateUtil.toLocalDateTime(new Date()));
            orderRepository.batchUpdateSkuOrderState(deliverSuccessSkuOrderIdList, OrderEnum.State.WAIT_DELIVERY, OrderEnum.State.WAIT_RECEIVE, skuOrderCommand);
            //触发订单状态同步
            tripSpuOrderChange(null, null, deliverSuccessSkuOrderIdList);
        }
        //返回设置
        deliverRes.setSpuOrderVO(spuOrderVO);
        return deliverRes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReceiveSkuOrderRes receiveSkuOrder(Long spuOrderId, List<Long> skuOrderIdList) {
        SpuOrder spuOrder = orderRepository.spuOrder(spuOrderId);
        if(ObjectUtil.isEmpty(skuOrderIdList)){
            skuOrderIdList = orderRepository.querySkuOrderIdList(spuOrderId, null);
        }
        if(ObjectUtil.isEmpty(spuOrder)){
            ThrowsException.exception(BaseErrorCode.PARAM, "不存在的订单ID:" + spuOrderId);
        }
        // 防呆B
        if (spuOrder.getOrderState() == OrderEnum.State.DOWN_RECEIVE){
            return new ReceiveSkuOrderRes();
        }
        if (spuOrder.getOrderState() != OrderEnum.State.WAIT_RECEIVE){
            ThrowsException.exception(BaseErrorCode.PARAM, "订单状态非可收货状态:" + spuOrderId);
        }
        // 1. 收货
        SkuOrderCommand skuOrderCommand = new SkuOrderCommand();
        skuOrderCommand.setReceiveTime(DateUtil.toLocalDateTime(new Date()));
        int editSkuCount = orderRepository.batchUpdateSkuOrderState(skuOrderIdList, OrderEnum.State.WAIT_RECEIVE, OrderEnum.State.DOWN_RECEIVE, skuOrderCommand);
        if(editSkuCount != skuOrderIdList.size()){
            ThrowsException.exception(BaseErrorCode.PARAM, "存在状态异常的SKU订单");
        }
        // 2. 结算判断
        List<Long> waitSettlementOrderId = new ArrayList<>();
        List<SkuOrderVO> waitSettlementOrder = new ArrayList<>();
        if(SpuEnum.ChannelType.SELECTION == spuOrder.getSpuChannelType()){
            SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
            skuOrderQuery.setIdList(skuOrderIdList);
            List<SkuOrderVO> skuOrderVOList = orderRepository.skuOrderVOList(skuOrderQuery).getRecords();
            //准备结算信息
            for (SkuOrderVO skuOrderVO : skuOrderVOList) {
                RoleEnum.OrderType settleOrderType = skuOrderVO.getSettleOrderType();
                if(settleOrderType == null){
                    settleOrderType = orderRepository.settleOrderType(skuOrderVO.getSupplierId());
                }
                if(settleOrderType == null) {
                    ThrowsException.exception(BaseErrorCode.PARAM, "供应商结算配置异常");
                }
                if(RoleEnum.OrderType.RECEIVE == settleOrderType){
                    if(CommonEnum.YesOrNo.NO == skuOrderVO.getSettleSendState()){
                        waitSettlementOrder.add(skuOrderVO);
                        waitSettlementOrderId.add(skuOrderVO.getId());
                    }
                }
            }
            //修改结算状态
            if(ObjectUtil.isNotEmpty(waitSettlementOrderId)){
                SkuOrderQuery skuQuery = new SkuOrderQuery();
                skuQuery.setIdList(waitSettlementOrderId);
                SkuOrder sku = new SkuOrder();
                sku.setSettleSendState(CommonEnum.YesOrNo.YES);
                orderRepository.skuOrderEditByQuery(sku, skuQuery);
            }
        }
        // 3 触发订单状态同步
        tripSpuOrderChange(null, null, skuOrderIdList);
        if (Objects.isNull(SecurityUtils.getAccountId())){
            orderRepository.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(),OrderEnum.State.DOWN_RECEIVE,RoleEnum.CompanyRole.PLATFORM.getCode(),RoleEnum.CompanyRole.PLATFORM);
        }else {
            orderRepository.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(),OrderEnum.State.DOWN_RECEIVE,SecurityUtils.getAccountId(),SecurityUtils.getRole());
        }

        return new ReceiveSkuOrderRes(waitSettlementOrderId, waitSettlementOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompleteSkuOrderRes completeSkuOrder(Long spuOrderId, List<Long> skuOrderIdList) {
        SpuOrder spuOrder = orderRepository.spuOrder(spuOrderId);
        // 1. 完成
        SkuOrderCommand skuOrderCommand = new SkuOrderCommand();
        orderRepository.batchUpdateSkuOrderState(skuOrderIdList, OrderEnum.State.DOWN_RECEIVE, OrderEnum.State.SUCCESS, skuOrderCommand);
        // 2. 结算与分润
        List<Long> waitSettlementOrderId = new ArrayList<>();
        List<SkuOrderVO> waitSettlementOrder = new ArrayList<>();
        RoleEnum.OrderType settleType = null;
        if(SpuEnum.ChannelType.SELECTION == spuOrder.getSpuChannelType()){
            SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
            skuOrderQuery.setIdList(skuOrderIdList);
            List<SkuOrderVO> skuOrderVOList = orderRepository.skuOrderVOList(skuOrderQuery).getRecords();
            //准备结算信息
            for (SkuOrderVO skuOrderVO : skuOrderVOList) {
                RoleEnum.OrderType settleOrderType = skuOrderVO.getSettleOrderType();
                if(settleOrderType == null){
                    settleOrderType = orderRepository.settleOrderType(skuOrderVO.getSupplierId());
                }
                if(settleOrderType == null) {
                    ThrowsException.exception(BaseErrorCode.PARAM, "供应商结算配置异常");
                }
                settleType = settleOrderType;
                if(RoleEnum.OrderType.ORDER_SUCCESS == settleOrderType || settleOrderType.equals(2)){
                    if(CommonEnum.YesOrNo.NO == skuOrderVO.getSettleSendState()){
                        waitSettlementOrder.add(skuOrderVO);
                        waitSettlementOrderId.add(skuOrderVO.getId());
                    }
                }
                //触发分润
                // 迁移: 原 domain 拼 core-mq 常量 Tag.EARNING 违依赖硬线, tag 拼接下沉 infra, domain 传业务ID
                orderRepository.wakeUpEarningMessage(skuOrderVO.getId());
            }
            //修改结算状态
            if(ObjectUtil.isNotEmpty(waitSettlementOrderId)){
                SkuOrderQuery skuQuery = new SkuOrderQuery();
                skuQuery.setIdList(waitSettlementOrderId);
                SkuOrder sku = new SkuOrder();
                sku.setSettleSendState(CommonEnum.YesOrNo.YES);
                orderRepository.skuOrderEditByQuery(sku, skuQuery);
            }
        }

        // 将订单收益转到渠道商收益账户
        ChannelSettleReq settleReq = new ChannelSettleReq();
        settleReq.setAccountId(spuOrder.getChannelId());
        settleReq.setSettleAmount(spuOrder.getMemberAmount());
        settleReq.setJoinSettleOrderNo(spuOrder.getId());
        balancePayApi.channelSettle(settleReq);

        // 3 触发订单状态同步
        tripSpuOrderChange(null, null, skuOrderIdList);
        if (Objects.isNull(SecurityUtils.getAccountId())){
            orderRepository.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(),OrderEnum.State.SUCCESS,RoleEnum.CompanyRole.PLATFORM.getCode(),RoleEnum.CompanyRole.PLATFORM);
        }else {
            orderRepository.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(),OrderEnum.State.SUCCESS,SecurityUtils.getAccountId(),SecurityUtils.getRole());
        }

        return new CompleteSkuOrderRes(waitSettlementOrderId, waitSettlementOrder,settleType);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void memberCancelOrder(Long spuOrderId, String cancelReason) {
        //批量修改订单状态
        SpuOrderAggVO spuOrderAggVO = orderRepository.spuOrderAggVO(spuOrderId);
        if (Objects.isNull(spuOrderAggVO) || Objects.isNull(spuOrderAggVO.getSpuOrderVO()) || CollUtil.isEmpty(spuOrderAggVO.getSkuOrderList())){
            ThrowsException.exception(BaseErrorCode.PARAM, "订单不存在id为："+spuOrderId);
        }
        if (spuOrderAggVO.getSpuOrderVO().getOrderState() == OrderEnum.State.CLOSE){
            return;
        }
        Set<OrderEnum.State> allowModifyStates = OrderEnum.State.getMemberCancelOrderStates();
        if(!allowModifyStates.contains(spuOrderAggVO.getSpuOrderVO().getOrderState())){
            ThrowsException.exception(BaseErrorCode.CUSTOM, "仅能取消未支付的订单！");
        }
        String updateSpuOrderExt = Optional.ofNullable(cancelReason)
                .filter(StrUtil::isNotEmpty)
                .map(reason -> {
                    SpuOrderExt spuOrderExt = Optional.ofNullable(spuOrderAggVO.getSpuOrderVO().getSpuOrderExt())
                            .filter(StrUtil::isNotEmpty)
                            .map(extStr -> JSONObject.parseObject(extStr, SpuOrderExt.class))
                            .orElse(new SpuOrderExt());
                    spuOrderExt.setCancelReason(reason);
                    spuOrderExt.setCloseReason("买家主动取消订单");
                    return JSONObject.toJSONString(spuOrderExt);
                })
                .orElse(null);
        Long orderId = spuOrderAggVO.getSpuOrderVO().getOrderId();
        batchUpdateOrderState(Collections.singletonList(orderId), spuOrderAggVO.getSpuOrderVO().getOrderState(), OrderEnum.State.CLOSE,updateSpuOrderExt);

        SpuOrder spuOrder = new SpuOrder();
        BeanUtils.copyProperties(spuOrderAggVO.getSpuOrderVO(), spuOrder);
        orderRepository.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(),OrderEnum.State.CLOSE,SecurityUtils.getAccountId(),SecurityUtils.getRole());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void channelCancelOrder(Long spuOrderId, String cancelReason) {
        SpuOrderAggVO spuOrderAggVO = orderRepository.spuOrderAggVO(spuOrderId);
        if (Objects.isNull(spuOrderAggVO) || Objects.isNull(spuOrderAggVO.getSpuOrderVO()) || CollUtil.isEmpty(spuOrderAggVO.getSkuOrderList())){
            ThrowsException.exception(BaseErrorCode.PARAM, "订单不存在id为："+spuOrderId);
        }
        if (spuOrderAggVO.getSpuOrderVO().getOrderState() == OrderEnum.State.CLOSE){
            return;
        }
        Set<OrderEnum.State> allowModifyStates = OrderEnum.State.getChannelCancelOrderStates();
        if(!allowModifyStates.contains(spuOrderAggVO.getSpuOrderVO().getOrderState())){
            ThrowsException.exception(BaseErrorCode.CUSTOM, "仅能取消未支付的订单！");
        }
        String updateSpuOrderExt = Optional.ofNullable(cancelReason)
                .filter(StrUtil::isNotEmpty)
                .map(reason -> {
                    SpuOrderExt spuOrderExt = Optional.ofNullable(spuOrderAggVO.getSpuOrderVO().getSpuOrderExt())
                            .filter(StrUtil::isNotEmpty)
                            .map(extStr -> JSONObject.parseObject(extStr, SpuOrderExt.class))
                            .orElse(new SpuOrderExt());
                    spuOrderExt.setCancelReason(reason);
                    spuOrderExt.setCloseReason(reason);
                    return JSONObject.toJSONString(spuOrderExt);
                })
                .orElse(null);
        Long orderId = spuOrderAggVO.getSpuOrderVO().getOrderId();
        batchUpdateOrderState(Collections.singletonList(orderId), spuOrderAggVO.getSpuOrderVO().getOrderState(), OrderEnum.State.CLOSE, updateSpuOrderExt);
        if (spuOrderAggVO.getSpuOrderVO().getOrderState() == OrderEnum.State.CHANNEL_WAIT_PAY){
            SellAfterRefundReq sellAfterRefundReq = new SellAfterRefundReq();
            sellAfterRefundReq.setOrderNo(orderId);
            sellAfterRefundReq.setSellAfterOrderNo(orderId);
            MemberRefundRes memberRefundRes = balancePayApi.sellAfterRefund(sellAfterRefundReq);
            if (StrUtil.isNotBlank(memberRefundRes.getRefundWarnMsg())) {
                // 迁移: Base PlatformException 仅 (ErrorCode, String...) 构造, 去掉 WIP 臆造的 boolean 告警标记入参
                throw new PlatformException(OrderErrorCode.REFUND_FAIL, memberRefundRes.getRefundWarnMsg());
            }
        }
        SpuOrder spuOrder = new SpuOrder();
        BeanUtils.copyProperties(spuOrderAggVO.getSpuOrderVO(), spuOrder);
        if (Objects.isNull(SecurityUtils.getAccountId())){
            orderRepository.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(), OrderEnum.State.CLOSE, RoleEnum.CompanyRole.PLATFORM.getCode(),RoleEnum.CompanyRole.PLATFORM);
        }else {
            orderRepository.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(), OrderEnum.State.CLOSE, SecurityUtils.getAccountId(), SecurityUtils.getRole());
        }
    }

    /**
     * 触发订单状态同步
     * @param orderId
     * @param spuOrderId
     * @param skuOrderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TripSpuOrderChangeRes tripSpuOrderChange(List<Long> orderId, List<Long> spuOrderId, List<Long> skuOrderId) {
        TripSpuOrderChangeRes tripSpuOrderChangeRes = new TripSpuOrderChangeRes();
        if(ObjectUtil.isEmpty(orderId)){
            orderId = new ArrayList<>();
        }
        if(ObjectUtil.isNotEmpty(spuOrderId) || ObjectUtil.isNotEmpty(skuOrderId)){
            List<Long> orderIdQuery = orderRepository.orderIdBySpuSkuOrderId(spuOrderId, skuOrderId);
            orderId.addAll(orderIdQuery);
        }
        if(orderId.size() == 0){
            return tripSpuOrderChangeRes;
        }
        List<OrderStateCheckRes> spuOrderStateCheckRes = orderRepository.checkSpuOrderState(orderId);
        for (OrderStateCheckRes state : spuOrderStateCheckRes) {
            if(!state.getCurrentState().equals(state.getToState())){
                //修改订单状态
                orderRepository.batchUpdateSpuOrderState(Collections.singletonList(state.getId()), state.getCurrentState(), state.getToState());
            }
        }
        List<OrderStateCheckRes> orderStateCheckRes = orderRepository.checkOrderState(orderId);
        for (OrderStateCheckRes state : orderStateCheckRes) {
            if(!state.getCurrentState().equals(state.getToState())) {
                //修改交易单状态
                orderRepository.batchUpdateOrderState(Collections.singletonList(state.getId()), state.getCurrentState(), state.getToState());
                tripSpuOrderChangeRes.getOrderStateChange().add(new TripSpuOrderChangeRes.Item(state.getId(), state.getCurrentState(), state.getToState()));
                //开发者通知
                orderRepository.orderStateNotify(state.getOrderType(), state.getChannelId(), state.getOutOrderNo(), state.getCurrentState(), state.getToState());
                //确认收货通知怡亚通
//                if(OrderEnum.State.DOWN_RECEIVE == state.getToState()){
//                    String outOrderId = orderRepository.queryOutOrderId(state.getId());
//                    if(StrUtil.isNotEmpty(outOrderId)){
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
    @Override
    public void freightSettleSuccessNotify(List<Long> spuIdList) {
        SpuOrderQuery spuOrderQuery = new SpuOrderQuery();
        spuOrderQuery.setIdList(spuIdList);
        SpuOrder spu = new SpuOrder();
        spu.setSettleSendState(CommonEnum.YesOrNo.YES);
        orderRepository.spuOrderEditByQuery(spu, spuOrderQuery);
    }

    @Override
    public void savePrePayOrder(OrderCreateRes order, MemberOrderCreateCommand memberOrderCreateCommand) {
        orderRepository.savePrePayOrder(order, memberOrderCreateCommand);
    }

    @Override
    public OrderCreateRes getPrePayOrder(MemberOrderCreateCommand memberOrderCreateCommand) {
        OrderCreateRes prePayOrder = orderRepository.getPrePayOrder(memberOrderCreateCommand);
        if (Objects.nonNull(prePayOrder)){
            long remainingTime = orderRepository.getPrePayOrderExpire(memberOrderCreateCommand);
            prePayOrder.setRemainTime(remainingTime);
        }
        return prePayOrder;
    }

    @Override
    public void delPrePayOrder(MemberOrderCreateCommand memberOrderCreateCommand) {
        orderRepository.delPrePayOrder(memberOrderCreateCommand);
    }

    private void fillIn(SpuOrder spuOrder, OrderCreateCommand orderCreateCommand,
                        MemberOrderCreateCommand memberOrderCreateCommand, SkuOrder skuOrder){
        spuOrder.setOrderType(orderCreateCommand.getOrderType());
        spuOrder.setOutOrderNo(orderCreateCommand.getOutOrderNo());
        spuOrder.setOrderId(skuOrder.getOrderId());
        spuOrder.setSpuChannelType(skuOrder.getSpuChannelType());
        spuOrder.setSpuSaleType(skuOrder.getSpuSaleType());
        spuOrder.setChannelId(orderCreateCommand.getChannelId());
        spuOrder.setMerchantId(memberOrderCreateCommand.getMerchantId());
        spuOrder.setSupplierId(skuOrder.getSupplierId());
        spuOrder.setDealerId(skuOrder.getDealerId());
        spuOrder.setOperatorId(skuOrder.getOperatorId());
        spuOrder.setSpuId(skuOrder.getSpuId());
        spuOrder.setSpuName(skuOrder.getSpuName());
        spuOrder.setSpuImg(skuOrder.getSpuImg());
        spuOrder.setDiscountAmount(0);
        spuOrder.setOrderState(OrderEnum.State.NEW);
        ShipVO shipVO = orderCreateCommand.getShipVO();
        spuOrder.setShipVO(shipVO);
        spuOrder.setShipPhone(shipVO.getShipPhone());
        spuOrder.setRemark(orderCreateCommand.getRemark());
        spuOrder.setOrderStateLog(OrderEnum.State.NEW.toString());
        spuOrder.setSettleSendState(CommonEnum.YesOrNo.NO);
        spuOrder.setStoreId(memberOrderCreateCommand.getStoreId());
        spuOrder.setAccountId(memberOrderCreateCommand.getAccountId());
        spuOrder.setMemberId(memberOrderCreateCommand.getAccountId());
        spuOrder.setCloseTime(LocalDateTime.now().plusMinutes(30));
        SpuOrderExt spuOrderExt = new SpuOrderExt();
        spuOrderExt.setStoreAccount(memberOrderCreateCommand.getStoreAccount());
        spuOrderExt.setStoreName(memberOrderCreateCommand.getStoreName());
        spuOrder.setStoreName(memberOrderCreateCommand.getStoreName());//兼容前端现有逻辑
        spuOrderExt.setStoreHead(memberOrderCreateCommand.getStoreHead());
        spuOrder.setStoreHead(memberOrderCreateCommand.getStoreHead());
        spuOrderExt.setUserAccount(memberOrderCreateCommand.getUserAccount());
        spuOrderExt.setUserName(memberOrderCreateCommand.getUserName());
        spuOrderExt.setNickName(memberOrderCreateCommand.getNickName());
        spuOrderExt.setMemberId(memberOrderCreateCommand.getAccountId());
        spuOrder.setSpuOrderExt(spuOrderExt);
        spuOrder.setRefund(0);
        if (spuOrder.getSpuChannelType() == SpuEnum.ChannelType.CUSTOM){
            spuOrder.setMemberAmount(spuOrder.getStoreAmount()+spuOrder.getFreightAmount()-spuOrder.getDiscountAmount());
        }
    }

    private SkuOrder buildSkuOrder(OrderGoodsInfoVO orderGoodsInfoVO,Long spuOrderId,Long orderId,
                                   ChannelNowServiceFeeRes channelNowServiceFee,
                                   EarningsConfigRpcVO earningsConfigRpcVO, Integer settleOrderType,LocalDateTime time){

        SkuOrder skuOrder = new SkuOrder();
        skuOrder.setFreightAmount(0);
        skuOrder.setDiscountAmount(0);
        skuOrder.setId(SnowflakeIdAble.getSnowflakeId());
        skuOrder.setOrderId(orderId);
        skuOrder.setSpuId(orderGoodsInfoVO.getSpuId());
        skuOrder.setSkuImg(orderGoodsInfoVO.getImg());
        skuOrder.setSpuOrderId(spuOrderId);
        skuOrder.setSpuChannelType(orderGoodsInfoVO.getSpuChannelType());
        //计算订单金额
        if(SpuEnum.ChannelType.CUSTOM == orderGoodsInfoVO.getSpuChannelType()){
            skuOrder.setStoreAmount(orderGoodsInfoVO.getStorePrice() * orderGoodsInfoVO.getNum());
        }else if (SpuEnum.ChannelType.SELECTION == orderGoodsInfoVO.getSpuChannelType() ||
                SpuEnum.ChannelType.OUT == orderGoodsInfoVO.getSpuChannelType()){
            skuOrder.setGoodsAmount(orderGoodsInfoVO.getSalePrice() * orderGoodsInfoVO.getNum());
            skuOrder.setSupplierAmount(orderGoodsInfoVO.getSupplyPrice() * orderGoodsInfoVO.getNum());
            Integer storePrice = orderGoodsInfoVO.getStorePrice() == null ? 0 : orderGoodsInfoVO.getStorePrice();
            skuOrder.setStoreAmount(storePrice * orderGoodsInfoVO.getNum());
        }else {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }

        skuOrder.buildServiceChange(channelNowServiceFee);

        skuOrder.setOrderState(OrderEnum.State.NEW);
        skuOrder.setOrderStateLog(OrderEnum.State.NEW.toString());
        skuOrder.setSupplierId(orderGoodsInfoVO.getSupplierId());
        if(earningsConfigRpcVO.getUpDealerId() != null){
            skuOrder.setDealerId(earningsConfigRpcVO.getUpDealerId());
        }
        if(earningsConfigRpcVO.getUpOperatorId() != null){
            skuOrder.setOperatorId(earningsConfigRpcVO.getUpOperatorId());
        }
        skuOrder.setTwoMarketId(orderGoodsInfoVO.getTwoMarketId());
        if(StrUtil.isEmpty(orderGoodsInfoVO.getOutSkuId())){
            skuOrder.setOutSkuId("0");
        } else {
            skuOrder.setOutSkuId(orderGoodsInfoVO.getOutSkuId());
        }
        skuOrder.setSkuId(orderGoodsInfoVO.getSkuId());
        skuOrder.setCount(orderGoodsInfoVO.getNum());
        skuOrder.setSpuName(orderGoodsInfoVO.getSpuName());
        skuOrder.setSpuImg(orderGoodsInfoVO.getSpuImg());
        skuOrder.setSkuSaleAttribute(orderGoodsInfoVO.getSaleAttributeJson());
        skuOrder.setSpuName(orderGoodsInfoVO.getSpuName());
        skuOrder.setSkuWeight(orderGoodsInfoVO.getWeight().doubleValue());
        skuOrder.setSkuVolume(orderGoodsInfoVO.getVolume().doubleValue());
        skuOrder.setSkuSalePrice(orderGoodsInfoVO.getSalePrice());
        skuOrder.setSkuSupplierPrice(orderGoodsInfoVO.getSupplyPrice());
        skuOrder.setSkuStorePrice(orderGoodsInfoVO.getStorePrice() == null ? 0 : orderGoodsInfoVO.getStorePrice());
        skuOrder.setDeliverCount(0);
        skuOrder.setRefundedCount(0);
        skuOrder.setRefundingCount(0);
        skuOrder.setSettleOrderType(settleOrderType);
        skuOrder.setCreateTime(time);
        return skuOrder;
    }

    @Override
    public void deliverEdit(DeliverCodeCommand deliverCommand) {
        if(deliverCommand.getId() != null){
            if(deliverCommand.getId() > 0){
                Deliver deliver = new Deliver();
                deliver.setId(deliverCommand.getId());
                deliver.setExpressNo(deliverCommand.getExpressNo());
                deliver.setExpressCompanyName(deliverCommand.getExpressCompanyName());
                deliver.setExpressMobile(deliverCommand.getExpressMobile());
                orderRepository.deliverEdit(deliver);
            }else {
                orderRepository.deliverDelete(-deliverCommand.getId());
            }
        }else {
            ThrowsException.exception(BaseErrorCode.PARAM, "ID不能为空！");
        }
    }

    @Override
    public Map<Long, Integer> validateOrderShipChange(OrderAgg orderAgg,ShipVO shipVO) {
        // 1. 基础校验：订单聚合对象非空
        if (Objects.isNull(orderAgg) || Objects.isNull(orderAgg.getOrder())
                || CollUtil.isEmpty(orderAgg.getSpuOrderList())
                || CollUtil.isEmpty(orderAgg.getSkuOrderList())) {
            throw new PlatformException(BaseErrorCode.NODATA, "订单不存在！");
        }

        Order order = orderAgg.getOrder();
        List<SpuOrder> spuOrderList = orderAgg.getSpuOrderList();
        List<SkuOrder> skuOrderList = orderAgg.getSkuOrderList();

        // 2. 校验订单状态是否允许修改收货地址
        OrderEnum.State currentState = order.getOrderState();
        Set<OrderEnum.State> allowModifyStates = OrderEnum.State.getAllowModifyShipStates();
        if (!allowModifyStates.contains(currentState)) {
            String stateInfo = currentState != null ? currentState.getInfo() : "未知状态";
            throw new PlatformException(BaseErrorCode.UPDATE,
                    String.format("当前订单状态【%s】不允许变更收货地址！", stateInfo));
        }

        // 3. 构建商品校验列表并调用接口做基础校验
        List<GoodsVO> goodsList = skuOrderList.stream()
                .map(item -> {
                    GoodsVO goodsVO = new GoodsVO();
                    goodsVO.setSkuId(item.getSkuId());
                    goodsVO.setNum(item.getCount());
                    return goodsVO;
                }).collect(Collectors.toList());

        OrderGoodsCheckReq checkReq = new OrderGoodsCheckReq();
        checkReq.setChannelId(order.getChannelId()).setStoreId(order.getStoreId()).setShipAreaCode(shipVO.getShipAreaCode()).setShipProvinceCode(shipVO.getShipProvinceCode()).setShipCityCode(shipVO.getShipCityCode());
        PlatformResult<OrderGoodsCheckRes> checkResult = orderGoodsFacade.checkShip(checkReq, goodsList);
        if (!checkResult.isSuccess()) {
            ThrowsException.exception(BaseErrorCode.PARAM, checkResult.getMessage());
        }
        OrderGoodsCheckRes checkResData = checkResult.getData();

        // 4. 校验商品信息（SPU存在性 + 外部商品限制）
        List<Long> spuIdList = spuOrderList.stream().map(SpuOrder::getSpuId).collect(Collectors.toList());
        if (CollUtil.isEmpty(spuIdList)) {
            throw new PlatformException(BaseErrorCode.NODATA, "参数错误--未查询到商品订单");
        }

        List<ApiSpuVO> apiSpuVOS = spuFacade.apiSpuVOList(null, spuIdList);
        if (CollUtil.isEmpty(apiSpuVOS)) {
            throw new PlatformException(BaseErrorCode.NODATA, "参数错误--未查询到商品");
        }

        // 外部商品（channelType=2）不允许修改地址
        apiSpuVOS.forEach(apiSpuVO -> {
            if (apiSpuVO.getChannelType() == SpuEnum.ChannelType.OUT) {
                throw new PlatformException(BaseErrorCode.NODATA, "外部商品无法修改订单地址");
            }
        });

        // 5. 返回最新运费（用于后续对比是否变动）
        return checkResData.getGoodsFreight();
    }

    /**
     * 校验运费是否变动（独立封装，便于两处复用）
     */
    @Override
    public void validateFreightUnchanged(List<SpuOrder> spuOrderList, Map<Long, Integer> goodsFreight) {
        spuOrderList.forEach(spuOrder -> {
            Integer oldFreight = spuOrder.getFreightAmount();
            Integer newFreight = goodsFreight.get(spuOrder.getSpuId());
            if (!Objects.equals(oldFreight, newFreight)) {
                throw new PlatformException(BaseErrorCode.NODATA, "运费发生变动，无法修改，请重新下单");
            }
        });
    }

    /**
     * 数据库层更新收货地址（封装，便于复用）
     */
    @Override
    public void updateOrderShipDb(Long orderId, String shipVOJson) {
        orderRepository.updateOrderShip(orderId, shipVOJson);
    }

    // ------------------------------ 简化后的changeOrderShip方法 ------------------------------
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeOrderShip(OrderShipCommand command) {
        // 1. 查询订单聚合对象
        OrderAgg orderAgg = orderRepository.orderAgg(command.getOrderId());

        // 2. 调用核心校验逻辑（复用所有前置校验）
        Map<Long, Integer> goodsFreight = validateOrderShipChange(orderAgg, command.getShipVO());

        // 3. 校验运费是否变动
        validateFreightUnchanged(orderAgg.getSpuOrderList(), goodsFreight);

        // 4. 执行数据库更新
        updateOrderShipDb(command.getOrderId(), JSON.toJSONString(command.getShipVO()));

        return Boolean.TRUE;
    }


    @Override
    public Order order(Long orderId) {
        return orderRepository.order(orderId);
    }

    @Override
    public OrderAgg orderAgg(Long orderId) {
        return orderRepository.orderAgg(orderId);
    }

    @Override
    public void batchUpdateOrderState(List<Long> orderIdList, OrderEnum.State sourceState, OrderEnum.State toState,String spuOrderExt) {
        orderRepository.batchUpdateOrderState(orderIdList, sourceState, toState);
        orderRepository.batchUpdateSpuOrderStateByOrderId(orderIdList, sourceState, toState, spuOrderExt);
        orderRepository.batchUpdateSkuOrderStateByOrderId(orderIdList, sourceState, toState);
    }

    @Override
    public List<SpuOrder> listDOByOrderStateAndUpdateTimeLessThan(OrderEnum.State orderState, LocalDateTime updateTime) {
        return orderRepository.listDOByOrderStateAndUpdateTimeLessThan(orderState, updateTime);
    }

    @Override
    public void orderEdit(Order orderEdit) {
        orderRepository.orderEdit(orderEdit);
    }

    @Override
    public List<OrderStateCountVO> countOrderStateByChannel(Long channelId) {
        return orderRepository.countOrderStateByChannel(channelId);
    }

    @Override
    public List<OrderStateCountVO> countOrderStateByAccount(Long accountId) {
        return orderRepository.countOrderStateByAccount(accountId);
    }

    @Override
    public IndexCountRes indexCount(TimeQuery timeQuery) {
        // 迁移: 原 domain 直连 spuOrderDAO + ScmUtil(依赖 new-scm) 违依赖硬线,
        // 时间切片补全 + DAO 统计下沉 infra, domain 直调 repository
        IndexCountRes indexCountRes = new IndexCountRes();
        List<GroupCountRes> newGroupCountRes = orderRepository.orderCountComplete(timeQuery);
        indexCountRes.setGroupCountRes(newGroupCountRes);
        SpuOrderQuery spuOrderQuery = new SpuOrderQuery();
        indexCountRes.setOrderCount(orderRepository.spuOrderCount(spuOrderQuery));
        indexCountRes.setOrderAmount(orderRepository.spuOrderSumAmount(spuOrderQuery));
        return indexCountRes;
    }

    @Override
    public Map<Long, List<DeliverVO>> orderDeliverInfo(Long spuOrderId) {
        // 迁移: 原 domain 直连 deliverDAO 违依赖硬线, 查询下沉 infra, domain 直调 repository
        List<DeliverVO> deliverVOS = orderRepository.deliverListBySpuOrderId(spuOrderId);
        //组装数据
        Map<Long, List<DeliverVO>> deliverVOList = new HashMap<>();
        for (DeliverVO deliverVO : deliverVOS) {
            List<DeliverItemVO> deliverItemVOS = JSON.parseArray(deliverVO.getItem(), DeliverItemVO.class);
            for (DeliverItemVO deliverItemVO : deliverItemVOS) {
                if(!deliverVOList.containsKey(deliverItemVO.getSkuId())) {
                    deliverVOList.put(deliverItemVO.getSkuId(), new ArrayList<>());
                    deliverVOList.get(deliverItemVO.getSkuId()).add(deliverVO);
                } else {
                    deliverVOList.get(deliverItemVO.getSkuId()).add(deliverVO);
                }
            }
        }
        return deliverVOList;
    }

    @Override
    public Long spuOrderId(Long orderId, Long skuId) {
        // 迁移: 原 domain 直连 spuOrderDAO 违依赖硬线, 查询下沉 infra
        return orderRepository.spuOrderIdByOrderSku(orderId, skuId);
    }

    @Override
    public List<SpuOrderItemExcelVO> querySpuOrderItemExcelVO(SpuOrderQuery spuOrderQuery) {
        // 迁移: 原 domain 直连 spuOrderDAO/skuOrderDAO 违依赖硬线, 查询下沉 infra, domain 只留组装
        List<SpuOrderItemExcelVO> skuOrderVOList = orderRepository.querySpuOrderItemExcelVO(spuOrderQuery);
        for (SpuOrderItemExcelVO spuOrderItemExcelVO : skuOrderVOList) {
            List<SkuSaleAttributeVO> skuSaleAttribute = JSON.parseArray(spuOrderItemExcelVO.getAttribute(), SkuSaleAttributeVO.class);
            if(ObjectUtil.isNotEmpty(skuSaleAttribute)){
                String skuName = "";
                for (SkuSaleAttributeVO skuSaleAttributeVO : skuSaleAttribute) {
                    skuName = skuSaleAttributeVO.getValue() + ";";
                }
                spuOrderItemExcelVO.setAttribute(skuName.substring(0, skuName.length() - 1));
            }
            if(spuOrderItemExcelVO.getRefundingCount() > 0){
                spuOrderItemExcelVO.setRefunding("是");
            }else {
                spuOrderItemExcelVO.setRefunding("否");
            }
            spuOrderItemExcelVO.setOrderState(OrderEnum.State.getByCode(Integer.valueOf(spuOrderItemExcelVO.getOrderState())).getInfo());
            ShipVO shipVO = JSON.parseObject(spuOrderItemExcelVO.getShipVO(), ShipVO.class);
            spuOrderItemExcelVO.setShipName(shipVO.getShipName());
            spuOrderItemExcelVO.setShipPhone(shipVO.getShipPhone());
            String shipAddress = shipVO.getShipAddress() == null ? "":shipVO.getShipAddress();
            spuOrderItemExcelVO.setShipArea(shipVO.getShipArea() + "," + shipAddress);
            BigDecimal price = new BigDecimal(spuOrderItemExcelVO.getPrice()).divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
            spuOrderItemExcelVO.setPrice(price.toPlainString());
        }
        return skuOrderVOList;
    }

    /**
     * 填充物流信息
     */
    private void buildDeliver(Map<Long, List<DeliverVO>> map, SpuOrderAggVO spuOrderAggVO) {
        if(map != null) {
            for (SkuOrderVO skuOrderVO : spuOrderAggVO.getSkuOrderList()) {
                List<DeliverVO> deliverList = map.get(skuOrderVO.getSkuId());
                if (deliverList != null) {
                    skuOrderVO.setDeliverVOList(deliverList);
                }
            }
        }
    }
}
