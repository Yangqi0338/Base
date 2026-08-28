package com.newzkl.platform.base.biz.order.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.api.*;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.OrderDomain;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.req.query.OrderStateRecordQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SpuOrderQuery;
import com.newzkl.platform.base.biz.order.model.res.*;

import com.newzkl.platform.base.biz.order.model.support.api.EarningsConfigRpcVO;
import com.newzkl.platform.base.biz.order.model.support.api.StoreRPCVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuVO;
import com.newzkl.platform.base.biz.order.model.support.api.order.*;
import com.newzkl.platform.base.common.ddd.facade.*;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import com.newzkl.platform.base.common.ddd.facade.ChannelSettleReq;
import com.newzkl.platform.base.common.ddd.facade.MemberRefundRes;
import com.newzkl.platform.base.common.ddd.facade.SellAfterRefundReq;
import com.newzkl.platform.base.common.ddd.model.constant.DeliverErrorCode;
import com.newzkl.platform.base.common.ddd.model.constant.OrderErrorCode;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.facade.ChannelNowServiceFeeRes;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.extension.ddl.DdlScriptErrorHandler.PrintlnLogErrorHandler.log;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/129:35
 */
@Service
@RequiredArgsConstructor
public class OrderDomainImpl implements OrderDomain {

    private final OrderRepository orderRepository;
    private final ChannelApi channelApi;
    private final LocalMessageApi localMessageApi;
    private final GoodsApi goodsApi;
    private final PayApi balancePayApi;
    private final AccountApi accountApi;

    @Override
    public OrderCreateRes createOrder(OrderGoodsCheckRes data, OrderCreateCommand orderCreateCommand,
                                      MemberOrderCreateCommand memberOrderCreateCommand) {
        OrderEnum.State state = null;
        if(OrderEnum.OrderType.MEMBER == orderCreateCommand.getOrderType()){
            state = OrderEnum.State.MEMBER_WAIT_PAY;
        }else if(OrderEnum.OrderType.CHANNEL == orderCreateCommand.getOrderType()){
            state = OrderEnum.State.CHANNEL_WAIT_PAY;
        }
        List<OrderGoodsInfoVO> goodsInfo = data.getGoodsInfo();
        Map<Long, Money> goodsFreight = data.getGoodsFreight();
        //获取渠道商当前服务费比例
//        String feeRedisKey = RedisEnum.Key.FEE_CONFIG.getCode(orderCreateCommand.getChannelId().toString());
//        ChannelNowServiceFeeRes channelNowServiceFee = redisClient.getCacheObject(feeRedisKey);
//        if(channelNowServiceFee == null){
        ChannelNowServiceFeeRes channelNowServiceFee = channelApi.queryNowServiceFee(orderCreateCommand.getChannelId());
//            redisClient.setCacheObjectTimeOut(feeRedisKey, channelNowServiceFee, 24, TimeUnit.HOURS);
//        }
        //获取渠道商分润配置
        // 迁移: 原 domain 直连 redisClient 缓存违依赖硬线, 缓存下沉 infra(@Cacheable), domain 直调 repository
        EarningsConfigRpcVO earningsConfigRpcVO = orderRepository.channelEarningsConfig(orderCreateCommand.getChannelId());
        orderCreateCommand.setOperatorId(earningsConfigRpcVO.getUpOperatorId());
        Long orderId = SnowflakeGenerator.getSnowflakeId();
        Map<Long, Long> spuOrderIdMap = new HashMap<>();
        List<SkuOrderDTO> skuOrderList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        // 构建订单数据
        if(StrUtil.isEmpty(orderCreateCommand.getOutOrderNo())){
            orderCreateCommand.setOutOrderNo(orderId.toString());
        }
        for (OrderGoodsInfoVO orderGoodsInfoVO : goodsInfo) {
            Long spuId = orderGoodsInfoVO.getSpuId();
            if(!spuOrderIdMap.containsKey(spuId)){
                Long spuOrderId = SnowflakeGenerator.getSnowflakeId();
                spuOrderIdMap.put(spuId, spuOrderId);
            }

            // 迁移: 原 redisClient.getCacheObjectAuto 缓存下沉 infra, domain 直调 repository
            EarningsEnum.SettleType settleOrderType = orderRepository.settleOrderType(orderGoodsInfoVO.getSupplierId());
            skuOrderList.add(buildSkuOrder(orderGoodsInfoVO,spuOrderIdMap.get(spuId),orderId,channelNowServiceFee,earningsConfigRpcVO,settleOrderType,now));
        }
        //根据sku订单
        Map<Long, SpuOrderDTO> spuOrderMap = skuOrderList.parallelStream().collect(Collectors.groupingBy(SkuOrderDTO::getSpuOrderId,
                Collectors.collectingAndThen(Collectors.toList(),m->{
                    SkuOrderDTO skuOrder = m.parallelStream().findFirst().get();
                    SpuOrderDTO spuOrder = new SpuOrderDTO();
                    spuOrder.setId(skuOrder.getSpuOrderId());
                    spuOrder.setSkuCount(m.parallelStream().mapToInt(SkuOrderDTO::getCount).sum());
                    spuOrder.setSupplierAmount(Money.sumBy(m, SkuOrderDTO::getSupplierAmount));
                    spuOrder.setGoodsAmount(Money.sumBy(m, SkuOrderDTO::getGoodsAmount));
                    spuOrder.setStoreAmount(Money.sumBy(m, SkuOrderDTO::getStoreAmount));
                    spuOrder.setFreightAmount(goodsFreight.get(skuOrder.getSpuId()));
                    spuOrder.setServiceAmount(Money.sumBy(m, SkuOrderDTO::getTotalServiceChange));
                    skuOrder.setCreateTime(now);
                    // 填充数据
                    fillIn(spuOrder,orderCreateCommand,memberOrderCreateCommand,skuOrder);
                    if (spuOrder.getMemberAmount() == null){
                        spuOrder.setMemberAmount(spuOrder.getStoreAmount().add(spuOrder.getFreightAmount()).subtract(spuOrder.getDiscountAmount()));
                    }
                    return spuOrder;
                })));
        List<SpuOrderDTO> spuOrderList = new ArrayList<>(spuOrderMap.values());
        //生成交易单
        OrderDTO order = new OrderDTO();
        order.init(orderCreateCommand, orderId, spuOrderList);
        OrderSnapVO orderSnapVO = new OrderSnapVO();
        orderSnapVO.setLocalGoods(data.getLocalGoods());
        orderSnapVO.setOutGoods(data.getOutGoods());
        order.setOrderSnapVO(orderSnapVO);
        order.setOrderState(state);
        order.setStoreId(memberOrderCreateCommand.getStoreId());
        order.setMemberId(memberOrderCreateCommand.getAccountId());
        orderSnapVO.setUsername(memberOrderCreateCommand.getUserName());
        orderSnapVO.setNickname(memberOrderCreateCommand.getNickName());
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
        try {
            orderRepository.orderSave(orderAgg.getOrder());
            orderRepository.spuOrderSave(TransferUtils.transfers(orderAgg.getSpuOrderList(), SpuOrderDTO.class));
            orderRepository.skuOrderSave(TransferUtils.transfers(orderAgg.getSkuOrderList(), SkuOrderDTO.class));
        } catch (DuplicateKeyException e) {
            log.error("订单持久化异常:", e);
            ThrowsException.exception(BaseErrorCode.EXIST_DATA);
        }
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
            OrderAgg orderAgg = orderAgg(id);
            localMessageApi.sendOrderNewRecordEvent(orderAgg.getSpuOrderList(), OrderEnum.State.SENDING,OrderEnum.State.WAIT_DELIVERY, AccountEnum.Identity.PLATFORM.getCode(), AccountEnum.Identity.PLATFORM);
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
                    deliverItemCommand.setCount(alreadyDeliverRes.getCount() - alreadyDeliverRes.getDeliverCount());
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
                deliverItemCommand.setCount(alreadyDeliverRes.getCount() - alreadyDeliverRes.getDeliverCount());
            }
            if(deliverItemCommand.getCount() > alreadyDeliverRes.getCount() - alreadyDeliverRes.getDeliverCount()){
                ThrowsException.exception(DeliverErrorCode.DELIVER_OVER, deliverItemCommand.getSkuId());
            }
            if(deliverItemCommand.getCount() == alreadyDeliverRes.getCount() - alreadyDeliverRes.getDeliverCount()){
                deliverSuccessSkuIdList.add(deliverItemCommand.getSkuId());
            }
        }
        //初始化发货单聚合
        Deliver deliver = new Deliver();
        deliver.init(deliverCommand, spuOrderVO);
        //持久化发货单聚合
        orderRepository.deliverSave(deliver);
        List<DeliverItemVO> deliverItemList = deliver.getItem();
        for (DeliverItemVO deliverItem : deliverItemList) {
            int count = orderRepository.updateSkuDeliverCount(deliverItem);
            if (count != 1) {
                ThrowsException.exception(DeliverErrorCode.UPDATE_DELIVER_NUM);
            }
        }
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
        SpuOrderDTO spuOrder = orderRepository.spuOrder(spuOrderId);
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
        List<SkuOrderDTO> waitSettlementOrder = new ArrayList<>();
        if(SpuEnum.ChannelType.SELECTION == spuOrder.getSpuChannelType()){
            SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
            skuOrderQuery.setIdList(skuOrderIdList);
            List<SkuOrderDTO> skuOrderVOList = orderRepository.skuOrderList(skuOrderQuery).getRecords();
            //准备结算信息
            for (SkuOrderDTO skuOrderVO : skuOrderVOList) {
                EarningsEnum.SettleType settleOrderType = skuOrderVO.getSettleOrderType();
                if(settleOrderType == null){
                    settleOrderType = orderRepository.settleOrderType(skuOrderVO.getSupplierId());
                }
                if(settleOrderType == null) {
                    ThrowsException.exception(BaseErrorCode.PARAM, "供应商结算配置异常");
                }
                if(EarningsEnum.SettleType.RECEIVE == settleOrderType){
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
                SkuOrderDTO sku = new SkuOrderDTO();
                sku.setSettleSendState(CommonEnum.YesOrNo.YES);
                orderRepository.skuOrderSave(sku, skuQuery);
            }
        }
        // 3 触发订单状态同步
        tripSpuOrderChange(null, null, skuOrderIdList);
        if (Objects.isNull(SecurityUtils.getAccountId())){
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(),OrderEnum.State.DOWN_RECEIVE, AccountEnum.Identity.PLATFORM.getCode(), AccountEnum.Identity.PLATFORM);
        }else {
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(),OrderEnum.State.DOWN_RECEIVE,SecurityUtils.getAccountId(),SecurityUtils.getIdentity());
        }

        return new ReceiveSkuOrderRes(waitSettlementOrderId, TransferUtils.transfers(waitSettlementOrder, SkuOrderVO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompleteSkuOrderRes completeSkuOrder(Long spuOrderId, List<Long> skuOrderIdList) {
        SpuOrderDTO spuOrder = orderRepository.spuOrder(spuOrderId);
        // 1. 完成
        SkuOrderCommand skuOrderCommand = new SkuOrderCommand();
        orderRepository.batchUpdateSkuOrderState(skuOrderIdList, OrderEnum.State.DOWN_RECEIVE, OrderEnum.State.SUCCESS, skuOrderCommand);
        // 2. 结算与分润
        List<Long> waitSettlementOrderId = new ArrayList<>();
        List<SkuOrderDTO> waitSettlementOrder = new ArrayList<>();
        EarningsEnum.SettleType settleType = null;
        if(SpuEnum.ChannelType.SELECTION == spuOrder.getSpuChannelType()){
            SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
            skuOrderQuery.setIdList(skuOrderIdList);
            List<SkuOrderDTO> skuOrderVOList = orderRepository.skuOrderList(skuOrderQuery).getRecords();
            //准备结算信息
            for (SkuOrderDTO skuOrderVO : skuOrderVOList) {
                EarningsEnum.SettleType settleOrderType = skuOrderVO.getSettleOrderType();
                if(settleOrderType == null){
                    settleOrderType = orderRepository.settleOrderType(skuOrderVO.getSupplierId());
                }
                if(settleOrderType == null) {
                    ThrowsException.exception(BaseErrorCode.PARAM, "供应商结算配置异常");
                }
                settleType = settleOrderType;
                if(EarningsEnum.SettleType.ORDER_SUCCESS == settleOrderType || EarningsEnum.SettleType.COMPLETE_DELAY == settleOrderType){
                    if(CommonEnum.YesOrNo.NO == skuOrderVO.getSettleSendState()){
                        waitSettlementOrder.add(skuOrderVO);
                        waitSettlementOrderId.add(skuOrderVO.getId());
                    }
                }
                //触发分润
                // 迁移: 原 domain 拼 core-mq 常量 Tag.EARNING 违依赖硬线, tag 拼接下沉 infra, domain 传业务ID
                localMessageApi.wakeUpEarningMessage(skuOrderVO.getId());
            }
            //修改结算状态
            if(ObjectUtil.isNotEmpty(waitSettlementOrderId)){
                SkuOrderQuery skuQuery = new SkuOrderQuery();
                skuQuery.setIdList(waitSettlementOrderId);
                SkuOrderDTO sku = new SkuOrderDTO();
                sku.setSettleSendState(CommonEnum.YesOrNo.YES);
                orderRepository.skuOrderSave(sku, skuQuery);
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
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(),OrderEnum.State.SUCCESS, AccountEnum.Identity.PLATFORM.getCode(), AccountEnum.Identity.PLATFORM);
        }else {
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(),OrderEnum.State.SUCCESS,SecurityUtils.getAccountId(),SecurityUtils.getIdentity());
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
                    SpuOrderExt spuOrderExt = spuOrderAggVO.getSpuOrderVO().getSpuOrderExt();
                    spuOrderExt.setCancelReason(reason);
                    spuOrderExt.setCloseReason("买家主动取消订单");
                    return JSONObject.toJSONString(spuOrderExt);
                })
                .orElse(null);
        Long orderId = spuOrderAggVO.getSpuOrderVO().getOrderId();
        batchUpdateOrderState(Collections.singletonList(orderId), spuOrderAggVO.getSpuOrderVO().getOrderState(), OrderEnum.State.CLOSE,updateSpuOrderExt);

        SpuOrderDTO spuOrder = new SpuOrderDTO();
        BeanUtils.copyProperties(spuOrderAggVO.getSpuOrderVO(), spuOrder);
        localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(),OrderEnum.State.CLOSE,SecurityUtils.getAccountId(),SecurityUtils.getIdentity());
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
                    SpuOrderExt spuOrderExt = spuOrderAggVO.getSpuOrderVO().getSpuOrderExt();
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
        SpuOrderDTO spuOrder = new SpuOrderDTO();
        BeanUtils.copyProperties(spuOrderAggVO.getSpuOrderVO(), spuOrder);
        if (Objects.isNull(SecurityUtils.getAccountId())){
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(), OrderEnum.State.CLOSE, AccountEnum.Identity.PLATFORM.getCode(), AccountEnum.Identity.PLATFORM);
        }else {
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(), OrderEnum.State.CLOSE, SecurityUtils.getAccountId(), SecurityUtils.getIdentity());
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
        List<OrderStateCheckDTO> spuOrderStateCheckRes = orderRepository.checkSpuOrderState(orderId);
        for (OrderStateCheckDTO state : spuOrderStateCheckRes) {
            if(!state.getCurrentState().equals(state.getToState())){
                //修改订单状态
                orderRepository.batchUpdateSpuOrderState(Collections.singletonList(state.getId()), state.getCurrentState(), state.getToState());
            }
        }
        List<OrderStateCheckDTO> orderStateCheckRes = orderRepository.checkOrderState(orderId);
        for (OrderStateCheckDTO state : orderStateCheckRes) {
            if(!state.getCurrentState().equals(state.getToState())) {
                //修改交易单状态
                orderRepository.batchUpdateOrderState(Collections.singletonList(state.getId()), state.getCurrentState(), state.getToState());
                tripSpuOrderChangeRes.getOrderStateChange().add(new TripSpuOrderChangeRes.Item(state.getId(), state.getCurrentState(), state.getToState()));
                //开发者通知
                orderRepository.orderStateNotify(state.getOrderType(), state.getChannelId(), state.getOutOrderNo(), state.getCurrentState(), state.getToState());
                //确认收货通知怡亚通
            }
        }
        return tripSpuOrderChangeRes;
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

    private void fillIn(SpuOrderDTO spuOrder, OrderCreateCommand orderCreateCommand,
                        MemberOrderCreateCommand memberOrderCreateCommand, SkuOrderDTO skuOrder){
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
        spuOrder.setDiscountAmount(Money.ZERO);
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
            spuOrder.setMemberAmount(spuOrder.getStoreAmount().add(spuOrder.getFreightAmount()).subtract(spuOrder.getDiscountAmount()));
        }
    }

    private SkuOrderDTO buildSkuOrder(OrderGoodsInfoVO orderGoodsInfoVO, Long spuOrderId, Long orderId,
                                      ChannelNowServiceFeeRes channelNowServiceFee,
                                      EarningsConfigRpcVO earningsConfigRpcVO, EarningsEnum.SettleType settleOrderType, LocalDateTime time){

        SkuOrderDTO skuOrder = new SkuOrderDTO();
        skuOrder.setFreightAmount(Money.ZERO);
        skuOrder.setDiscountAmount(Money.ZERO);
        skuOrder.setId(SnowflakeGenerator.getSnowflakeId());
        skuOrder.setOrderId(orderId);
        skuOrder.setSpuId(orderGoodsInfoVO.getSpuId());
        skuOrder.setSkuImg(orderGoodsInfoVO.getImg());
        skuOrder.setSpuOrderId(spuOrderId);
        skuOrder.setSpuChannelType(orderGoodsInfoVO.getSpuChannelType());
        //计算订单金额
        if(SpuEnum.ChannelType.CUSTOM == orderGoodsInfoVO.getSpuChannelType()){
            skuOrder.setStoreAmount(orderGoodsInfoVO.getStorePrice().multiply(orderGoodsInfoVO.getNum()));
        }else if (SpuEnum.ChannelType.SELECTION == orderGoodsInfoVO.getSpuChannelType() ||
                SpuEnum.ChannelType.OUT == orderGoodsInfoVO.getSpuChannelType()){
            skuOrder.setGoodsAmount(orderGoodsInfoVO.getSalePrice().multiply(orderGoodsInfoVO.getNum()));
            skuOrder.setSupplierAmount(orderGoodsInfoVO.getSupplyPrice().multiply(orderGoodsInfoVO.getNum()));
            skuOrder.setStoreAmount(orderGoodsInfoVO.getStorePrice().multiply(orderGoodsInfoVO.getNum()));
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
        skuOrder.setSkuStorePrice(orderGoodsInfoVO.getStorePrice() == null ? Money.ZERO : orderGoodsInfoVO.getStorePrice());
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
                orderRepository.deliverSave(deliver);
            }else {
                orderRepository.deliverDelete(-deliverCommand.getId());
            }
        }else {
            ThrowsException.exception(BaseErrorCode.PARAM, "ID不能为空！");
        }
    }

    @Override
    public Map<Long, Money> validateOrderShipChange(OrderAgg orderAgg,ShipVO shipVO) {
        // 1. 基础校验：订单聚合对象非空
        if (Objects.isNull(orderAgg) || Objects.isNull(orderAgg.getOrder())
                || CollUtil.isEmpty(orderAgg.getSpuOrderList())
                || CollUtil.isEmpty(orderAgg.getSkuOrderList())) {
            throw new PlatformException(BaseErrorCode.NODATA, "订单不存在！");
        }

        OrderDTO order = orderAgg.getOrder();
        List<SpuOrderDTO> spuOrderList = orderAgg.getSpuOrderList();
        List<SkuOrderDTO> skuOrderList = orderAgg.getSkuOrderList();

        // 2. 校验订单状态是否允许修改收货地址
        OrderEnum.State currentState = order.getOrderState();
        Set<OrderEnum.State> allowModifyStates = OrderEnum.State.getAllowModifyShipStates();
        if (!allowModifyStates.contains(currentState)) {
            String stateInfo = currentState != null ? currentState.getValue() : "未知状态";
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
        PlatformResult<OrderGoodsCheckRes> checkResult = goodsApi.checkShip(checkReq, goodsList);
        if (!checkResult.isSuccess()) {
            ThrowsException.exception(BaseErrorCode.PARAM, checkResult.getMessage());
        }
        OrderGoodsCheckRes checkResData = checkResult.getData();

        // 4. 校验商品信息（SPU存在性 + 外部商品限制）
        List<Long> spuIdList = spuOrderList.stream().map(SpuOrderDTO::getSpuId).collect(Collectors.toList());
        if (CollUtil.isEmpty(spuIdList)) {
            throw new PlatformException(BaseErrorCode.NODATA, "参数错误--未查询到商品订单");
        }

        List<ApiSpuVO> apiSpuVOS = goodsApi.apiSpuVOList(null, spuIdList);
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
    public void validateFreightUnchanged(List<SpuOrderDTO> spuOrderList, Map<Long, Money> goodsFreight) {
        spuOrderList.forEach(spuOrder -> {
            Money oldFreight = spuOrder.getFreightAmount();
            Money newFreight = goodsFreight.get(spuOrder.getSpuId());
            if (!Objects.equals(oldFreight, newFreight)) {
                throw new PlatformException(BaseErrorCode.NODATA, "运费发生变动，无法修改，请重新下单");
            }
        });
    }

    /**
     * 数据库层更新收货地址（封装，便于复用）
     */
    @Override
    public void updateOrderShipDb(Long orderId, ShipVO shipVOJson) {
        orderRepository.updateOrderShip(orderId, shipVOJson);
        orderRepository.updateSpuOrderShip(orderId, shipVOJson);
    }

    // ------------------------------ 简化后的changeOrderShip方法 ------------------------------
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeOrderShip(OrderShipCommand command) {
        // 1. 查询订单聚合对象
        OrderAgg orderAgg = orderAgg(command.getOrderId());

        // 2. 调用核心校验逻辑（复用所有前置校验）
        Map<Long, Money> goodsFreight = validateOrderShipChange(orderAgg, command.getShipVO());

        // 3. 校验运费是否变动
        validateFreightUnchanged(orderAgg.getSpuOrderList(), goodsFreight);

        // 4. 执行数据库更新
        updateOrderShipDb(command.getOrderId(), command.getShipVO());

        return Boolean.TRUE;
    }


    @Override
    public OrderDTO order(Long orderId) {
        return orderRepository.order(orderId);
    }

    @Override
    public OrderAgg orderAgg(Long orderId) {
        OrderAgg orderAgg = new OrderAgg();
        orderAgg.setInit(false);
        OrderDTO order = orderRepository.order(orderId);
        orderAgg.setOrder(order);

        SpuOrderQuery spuOrderQuery = new SpuOrderQuery();
        spuOrderQuery.setOrderId(orderId);
        List<SpuOrderDTO> spuOrderList = orderRepository.spuOrderList(spuOrderQuery).getRecords();
        orderAgg.setSpuOrderList(spuOrderList);

        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderId(orderId);
        List<SkuOrderDTO> skuOrderList = orderRepository.skuOrderList(skuOrderQuery).getRecords();
        orderAgg.setSkuOrderList(skuOrderList);
        return orderAgg;
    }

    @Override
    public Page<SpuOrderVO> spuOrderPage(SpuOrderQuery spuOrderQuery) {
        Page<SpuOrderDTO> dtoPage = orderRepository.spuOrderList(spuOrderQuery);
        return TransferUtils.transferPage(dtoPage,SpuOrderVO.class);
    }

    @Override
    public void batchUpdateOrderState(List<Long> orderIdList, OrderEnum.State sourceState, OrderEnum.State toState,String spuOrderExt) {
        orderRepository.batchUpdateOrderState(orderIdList, sourceState, toState);
        orderRepository.batchUpdateSpuOrderStateByOrderId(orderIdList, sourceState, toState, spuOrderExt);
        orderRepository.batchUpdateSkuOrderStateByOrderId(orderIdList, sourceState, toState);
    }

    @Override
    public List<SpuOrderDTO> listDOByOrderStateAndUpdateTimeLessThan(OrderEnum.State orderState, LocalDateTime updateTime) {
        return orderRepository.listDOByOrderStateAndUpdateTimeLessThan(orderState, updateTime);
    }

    @Override
    public void orderEdit(OrderDTO orderEdit) {
        orderRepository.orderSave(orderEdit);
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
            spuOrderItemExcelVO.setOrderState(OrderEnum.State.getByCode(Integer.valueOf(spuOrderItemExcelVO.getOrderState())).getValue());
            ShipVO shipVO = spuOrderItemExcelVO.getShipVO();
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

    @Override
    public OrderStateRecordEntity createStateRecord(OrderStateRecordEntity entity) {
        // 领域规则校验 - 新增必备字段非空校验
        Assert.notNull(entity, "订单状态记录不能为空");
        Assert.notNull(entity.getOrderId(), "订单ID不能为空");
        Assert.notNull(entity.getBeforeOrderState(), "变更前订单状态不能为空");
        Assert.notNull(entity.getAfterOrderState(), "变更后订单状态不能为空");
        Assert.notNull(entity.getOperateTime(), "操作时间不能为空");
        Assert.hasText(entity.getBeforeStateDesc(), "变更前订单状态描述不能为空");
        Assert.hasText(entity.getAfterStateDesc(), "变更后订单状态描述不能为空");
        Assert.isNull(entity.getId(), "新增时ID必须为空");

        // 初始化默认时间（未手动设置时自动填充）
        LocalDateTime now = LocalDateTime.now();
        if (entity.getCreateTime() == null) {
            entity.setCreateTime(now);
        }
        if (entity.getUpdateTime() == null) {
            entity.setUpdateTime(now);
        }

        // 调用仓储层保存
        return orderRepository.createStateRecord(entity);
    }

    @Override
    public Page<OrderStateRecordVO> recordPage(OrderStateRecordQuery query) {
        Page<OrderStateRecordEntity> dtoPage = orderRepository.recordPage(query);
        return TransferUtils.transferPage(dtoPage,OrderStateRecordVO.class);
    }

    @Override
    public List<OrderStateRecordVO> recordListBySpuOrderId(Long spuOrderId) {
        OrderStateRecordQuery query = new OrderStateRecordQuery();
        query.setSpuOrderId(spuOrderId);
        return recordPage(query).getRecords();
    }

    @Override
    public OrderCreateRes createOrder(OrderGoodsCheckV2Res data, OrderCreateCommand orderCreateCommand) {
        List<StoreDistributionDetailRpcVO> goodsInfo = data.getGoodsInfo();
        Map<Long, Money> goodsFreight = data.getGoodsFreight();

        // 获取渠道商当前服务费比例
        // 迁移(Q2): 缓存移交 infra(@Cacheable), domain 直调 repository, 不碰 redisClient
        ChannelNowServiceFeeRes channelNowServiceFee = channelApi.queryNowServiceFee(goodsInfo.get(0).getChannelId());
        // 获取渠道商分润配置
        EarningsConfigRpcVO earningsConfigRpcVO = orderRepository.channelEarningsConfig(goodsInfo.get(0).getChannelId());
        Long orderId = SnowflakeGenerator.getSnowflakeId();
        Map<Long, Long> spuOrderIdMap = new HashMap<>();
        List<SkuOrderDTO> skuOrderList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        // 构建订单数据
        if(StrUtil.isEmpty(orderCreateCommand.getOutOrderNo())){
            orderCreateCommand.setOutOrderNo(orderId.toString());
        }
        for (StoreDistributionDetailRpcVO orderGoodsInfoVO : goodsInfo) {
            Long spuId = orderGoodsInfoVO.getGoodsId();
            if(!spuOrderIdMap.containsKey(spuId)){
                Long spuOrderId = SnowflakeGenerator.getSnowflakeId();
                spuOrderIdMap.put(spuId, spuOrderId);
            }
            // 迁移(Q2): settleOrderType 缓存移交 infra(@Cacheable), domain 直调 repository
            EarningsEnum.SettleType settleOrderType = orderRepository.settleOrderType(orderGoodsInfoVO.getSupplierId());
            skuOrderList.add(buildSkuOrder(orderGoodsInfoVO,spuOrderIdMap.get(spuId),orderId,channelNowServiceFee,earningsConfigRpcVO,settleOrderType,now));
        }
        //根据sku订单
        Map<Long, SpuOrderDTO> spuOrderMap = skuOrderList.parallelStream().collect(Collectors.groupingBy(SkuOrderDTO::getSpuOrderId,
                Collectors.collectingAndThen(Collectors.toList(),m->{
                    SkuOrderDTO skuOrder = m.parallelStream().findFirst().get();
                    SpuOrderDTO spuOrder = new SpuOrderDTO();
                    spuOrder.setId(skuOrder.getSpuOrderId());
                    spuOrder.setSkuCount(m.parallelStream().mapToInt(SkuOrderDTO::getCount).sum());
                    spuOrder.setSupplierAmount(Money.sumBy(m, SkuOrderDTO::getSupplierAmount));
                    spuOrder.setGoodsAmount(Money.sumBy(m, SkuOrderDTO::getGoodsAmount));
                    spuOrder.setStoreAmount(Money.sumBy(m, SkuOrderDTO::getStoreAmount));
                    spuOrder.setFreightAmount(goodsFreight.get(skuOrder.getSpuId()));
                    spuOrder.setServiceAmount(Money.sumBy(m, SkuOrderDTO::getTotalServiceChange));
                    spuOrder.setCreateTime(now);
                    spuOrder.setStoreId(skuOrder.getStoreId());
                    // 填充数据
                    fillIn(spuOrder,orderCreateCommand,skuOrder);
                    if (spuOrder.getMemberAmount() == null){
                        spuOrder.setMemberAmount(spuOrder.getStoreAmount().add(spuOrder.getFreightAmount()).subtract(spuOrder.getDiscountAmount()));
                    }
                    return spuOrder;
                })));
        List<SpuOrderDTO> spuOrderList = new ArrayList<>(spuOrderMap.values());
        //生成交易单
        OrderDTO order = new OrderDTO();
        order.init(orderCreateCommand, orderId, spuOrderList);
        OrderSnapVO orderSnapVO = new OrderSnapVO();
        orderSnapVO.setLocalGoods(data.getLocalGoods());
        orderSnapVO.setOutGoods(data.getOutGoods());
        order.setOrderSnapVO(orderSnapVO);
        order.setOrderState(OrderEnum.State.NEW);
        order.setStoreId(goodsInfo.get(0).getStoreId());
        order.setMemberId(SecurityUtils.getAccountId());
        orderSnapVO.setUsername(SecurityUtils.getUsername());
        orderSnapVO.setNickname(SecurityUtils.getNickName());
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
    public void savePrePayOrder(OrderCreateRes order, MemberOrderCreateCommand memberOrderCreateCommand) {
        orderRepository.savePrePayOrder(order, memberOrderCreateCommand);
    }

    private SkuOrderDTO buildSkuOrder(StoreDistributionDetailRpcVO orderGoodsInfoVO, Long spuOrderId, Long orderId,
                                      ChannelNowServiceFeeRes channelNowServiceFee,
                                      EarningsConfigRpcVO earningsConfigRpcVO, EarningsEnum.SettleType settleOrderType, LocalDateTime time){

        SkuOrderDTO skuOrder = new SkuOrderDTO();
        skuOrder.setFreightAmount(Money.ZERO);
        skuOrder.setDiscountAmount(Money.ZERO);
        skuOrder.setId(SnowflakeGenerator.getSnowflakeId());
        skuOrder.setOrderId(orderId);
        skuOrder.setSpuId(orderGoodsInfoVO.getGoodsId());
        skuOrder.setSkuImg(orderGoodsInfoVO.getSkuImg());
        skuOrder.setSpuOrderId(spuOrderId);
        skuOrder.setSpuChannelType(orderGoodsInfoVO.getChannelType());
        skuOrder.setStoreId(orderGoodsInfoVO.getStoreId());
        //计算订单金额
        if(SpuEnum.ChannelType.CUSTOM == orderGoodsInfoVO.getChannelType()){
            skuOrder.setStoreAmount(orderGoodsInfoVO.getSellPrice().multiply(orderGoodsInfoVO.getBugNum()));
        }else if (SpuEnum.ChannelType.SELECTION == orderGoodsInfoVO.getChannelType() ||
                SpuEnum.ChannelType.OUT == orderGoodsInfoVO.getChannelType()){
            skuOrder.setGoodsAmount(orderGoodsInfoVO.getSupplierPrice().multiply(orderGoodsInfoVO.getBugNum()));
            skuOrder.setSupplierAmount(orderGoodsInfoVO.getSpuSupplyPrice().multiply(orderGoodsInfoVO.getBugNum()));
            skuOrder.setStoreAmount(orderGoodsInfoVO.getSellPrice().multiply(orderGoodsInfoVO.getBugNum()));
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
//        skuOrder.setTwoMarketId(orderGoodsInfoVO.getTwoMarketId());
        if(StrUtil.isEmpty(orderGoodsInfoVO.getOutSkuId())){
            skuOrder.setOutSkuId("0");
        } else {
            skuOrder.setOutSkuId(orderGoodsInfoVO.getOutSkuId());
        }
        skuOrder.setSkuId(orderGoodsInfoVO.getSkuId());
        skuOrder.setCount(orderGoodsInfoVO.getBugNum());
        skuOrder.setSpuName(orderGoodsInfoVO.getSpuName());
        skuOrder.setSpuImg(orderGoodsInfoVO.getSpuImg());
        skuOrder.setSkuSaleAttribute(orderGoodsInfoVO.getSaleAttribute());
        skuOrder.setSpuName(orderGoodsInfoVO.getSpuName());
        skuOrder.setSkuWeight(orderGoodsInfoVO.getWeight().doubleValue());
        skuOrder.setSkuVolume(orderGoodsInfoVO.getVolume().doubleValue());
        skuOrder.setSkuSalePrice(orderGoodsInfoVO.getSupplierPrice());
        skuOrder.setSkuSupplierPrice(orderGoodsInfoVO.getSkuSupplyPrice());
        skuOrder.setSkuStorePrice(orderGoodsInfoVO.getSellPrice());
        skuOrder.setDeliverCount(0);
        skuOrder.setRefundedCount(0);
        skuOrder.setRefundingCount(0);
        skuOrder.setSettleOrderType(settleOrderType);
        skuOrder.setCreateTime(time);
        return skuOrder;
    }

    private void fillIn(SpuOrderDTO spuOrder, OrderCreateCommand orderCreateCommand, SkuOrderDTO skuOrder){
        spuOrder.setOrderType(orderCreateCommand.getOrderType());
        spuOrder.setOutOrderNo(orderCreateCommand.getOutOrderNo());
        spuOrder.setOrderId(skuOrder.getOrderId());
        spuOrder.setSpuChannelType(skuOrder.getSpuChannelType());
        spuOrder.setSpuSaleType(skuOrder.getSpuSaleType());
        spuOrder.setChannelId(orderCreateCommand.getChannelId());
        spuOrder.setSupplierId(skuOrder.getSupplierId());
        spuOrder.setDealerId(skuOrder.getDealerId());
        spuOrder.setOperatorId(skuOrder.getOperatorId());
        spuOrder.setSpuId(skuOrder.getSpuId());
        spuOrder.setSpuName(skuOrder.getSpuName());
        spuOrder.setSpuImg(skuOrder.getSpuImg());
        spuOrder.setDiscountAmount(Money.ZERO);
        spuOrder.setOrderState(OrderEnum.State.NEW);
        spuOrder.setBenefitTripartiteId(orderCreateCommand.getBenefitTripartiteId());
        ShipVO shipVO = orderCreateCommand.getShipVO();
        spuOrder.setShipVO(shipVO);
        spuOrder.setShipPhone(shipVO.getShipPhone());
        spuOrder.setRemark(orderCreateCommand.getRemark());
        spuOrder.setOrderStateLog(OrderEnum.State.NEW.toString());
        spuOrder.setSettleSendState(CommonEnum.YesOrNo.NO);

        spuOrder.setAccountId(SecurityUtils.getAccountId());
        spuOrder.setMemberId(SecurityUtils.getAccountId());
        spuOrder.setCloseTime(LocalDateTime.now().plusMinutes(30));
        SpuOrderExt spuOrderExt = new SpuOrderExt();
        spuOrderExt.setStoreId(spuOrder.getStoreId());
        //查门店信息
        List<StoreRPCVO> storeRPCVOS = goodsApi.batchQueryStoreInfo(Collections.singletonList(spuOrder.getStoreId()));
        if (CollUtil.isNotEmpty(storeRPCVOS)){
            StoreRPCVO storeRPCVO = storeRPCVOS.get(0);
            spuOrderExt.setStoreHead(storeRPCVO.getLogo());
            spuOrderExt.setStoreName(storeRPCVO.getName());
        }
        //查门店im账号
        AccountGroupVO accountInfo = accountApi.channelInfo(spuOrder.getStoreId());
        if (Objects.nonNull(accountInfo)){
            spuOrderExt.setStoreAccount(accountInfo.getUserAccount());
        }
        AccountGroupVO memberAccount = accountApi.accountInfo(SecurityUtils.getAccountId());
        if (Objects.nonNull(memberAccount)){
            spuOrderExt.setUserAccount(memberAccount.getUserAccount());
            spuOrderExt.setUserName(memberAccount.getNickname());
            spuOrderExt.setNickName(memberAccount.getNickname());
        }
        spuOrderExt.setMemberId(SecurityUtils.getAccountId());
        spuOrder.setSpuOrderExt(spuOrderExt);
        spuOrder.setRefund(0);
        if (spuOrder.getSpuChannelType() == SpuEnum.ChannelType.CUSTOM){
            spuOrder.setMemberAmount(spuOrder.getStoreAmount().add(spuOrder.getFreightAmount()).subtract(spuOrder.getDiscountAmount()));
        }
    }
}
