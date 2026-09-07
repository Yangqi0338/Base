package com.newzkl.platform.base.biz.order.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.api.*;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.OrderDomain;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.OrderStateRecordQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.res.*;

import com.newzkl.platform.base.biz.order.model.support.api.EarningsConfigRpcVO;
import com.newzkl.platform.base.biz.order.model.support.api.StoreRPCVO;
import com.newzkl.platform.base.common.ddd.facade.ApiSpuVO;
import com.newzkl.platform.base.biz.order.model.support.api.order.*;
import com.newzkl.platform.base.common.ddd.facade.*;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.logistics.LogisticsMethod;
import com.newzkl.platform.base.common.core.logistics.LogisticsTrack;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
        String orderNo = BusinessCodeUtil.generate(BusinessType.ORDER);
        List<SkuOrderDTO> skuOrderList = new ArrayList<>();
        // SpuOrder 层折叠: 运费原落 spu_order, 现落同 spu 首个 sku, 避免多 sku 重复累加
        Set<Long> freightFilledSpuId = new HashSet<>();
        LocalDateTime now = LocalDateTime.now();
        // 构建订单数据
        if(StrUtil.isEmpty(orderCreateCommand.getOutOrderNo())){
            orderCreateCommand.setOutOrderNo(orderId.toString());
        }
        for (OrderGoodsInfoVO orderGoodsInfoVO : goodsInfo) {
            Long spuId = orderGoodsInfoVO.getSpuId();
            // 迁移: 原 redisClient.getCacheObjectAuto 缓存下沉 infra, domain 直调 repository
            EarningsEnum.SettleType settleOrderType = orderRepository.settleOrderType(orderGoodsInfoVO.getSupplierId());
            SkuOrderDTO skuOrder = buildSkuOrder(orderGoodsInfoVO,orderNo,channelNowServiceFee,earningsConfigRpcVO,settleOrderType,now);
            if(freightFilledSpuId.add(spuId)){
                skuOrder.setFreightAmount(Optional.ofNullable(goodsFreight.get(spuId)).orElse(Money.ZERO));
            }
            skuOrderList.add(skuOrder);
        }
        //生成交易单
        OrderDTO order = new OrderDTO();
        order.init(orderCreateCommand, orderId, skuOrderList);
        order.setOrderNo(orderNo);
        // 填充数据
        fillIn(order,orderCreateCommand,memberOrderCreateCommand,skuOrderList.get(0));
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
        OrderCreateRes orderCreateRes = new OrderCreateRes(orderId, order.getOrderNo(), order.getOrderState(), orderAgg);
        //前端使用的支付倒计时
        orderCreateRes.setRemainTime(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return orderCreateRes;
    }

    @Override
    public void orderAggSave(OrderAgg orderAgg) {
        try {
            orderRepository.orderSave(orderAgg.getOrder());
            orderRepository.skuOrderSave(orderAgg.getSkuOrderList());
        } catch (DuplicateKeyException e) {
            log.error("订单持久化异常:", e);
            ThrowsException.exception(BaseErrorCode.EXIST_DATA);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendOrder(List<String> orderNoList) {
        if(ObjectUtil.isEmpty(orderNoList)){
            return;
        }
        //批量修改订单状态
        orderRepository.batchUpdateOrderState(orderNoList, OrderEnum.State.SENDING, OrderEnum.State.WAIT_DELIVERY, null);
        orderRepository.batchUpdateSkuOrderState(orderNoList, OrderEnum.State.SENDING, OrderEnum.State.WAIT_DELIVERY);

        orderNoList.forEach(orderNo ->{
            OrderAgg orderAgg = orderAgg(orderNo);
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(orderAgg.getOrder()), OrderEnum.State.SENDING,OrderEnum.State.WAIT_DELIVERY, AccountEnum.Identity.PLATFORM.getCode(), AccountEnum.Identity.PLATFORM);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeliverRes deliverCreate(DeliverCommand deliverCommand) {
        // 快递公司名归一到编码表官方名称: deliverCreate / fullDeliver / splitDeliver 三条入口都过这里,
        // 落库前拦住简称与别名, 否则 orderTrack 只能靠单号识别兜底, 识别不出整条轨迹就查不到
        deliverCommand.setExpressCompanyName(LogisticsMethod.normalizeCompanyName(deliverCommand.getExpressCompanyName()));
        OrderDTO order = orderRepository.order(deliverCommand.getSpuOrderId());
        OrderVO orderVO = TransferUtils.transfer(order, OrderVO.class);
        if(order.getShipVO() != null){
            deliverCommand.setExpressMobile(order.getShipVO().getShipPhone());
        }
        DeliverRes deliverRes = new DeliverRes();
        //订单检查
        if(OrderEnum.State.WAIT_DELIVERY != order.getOrderState()){
            ThrowsException.exception(DeliverErrorCode.ORDER_STATE_CANNOT, deliverCommand.getSpuOrderId());
        }
        //已发货信息查询
        List<AlreadyDeliverRes> alreadyDeliverResList = null;
        Map<Long, AlreadyDeliverRes> alreadyDeliverResMap = null;
        if(ObjectUtil.isEmpty(deliverCommand.getDeliverItemCommandList())){
            //整单发货, 订单发货信息查询. 发货参数赋值为指定发货参数
            alreadyDeliverResList = orderRepository.getAlreadyDeliverResList(orderVO.getOrderNo(), null);
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
            alreadyDeliverResList = orderRepository.getAlreadyDeliverResList(orderVO.getOrderNo(), skuIds);
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
        deliver.init(deliverCommand, orderVO);
        //持久化发货单聚合
        orderRepository.deliverSave(deliver);
        List<DeliverItemVO> deliverItemList = deliver.getItem();
        for (DeliverItemVO deliverItem : deliverItemList) {
            int count = orderRepository.updateSkuDeliverCount(orderVO.getOrderNo(), deliverItem);
            if (count != 1) {
                ThrowsException.exception(DeliverErrorCode.UPDATE_DELIVER_NUM);
            }
        }
        //状态修改
        if(ObjectUtil.isNotEmpty(deliverSuccessSkuIdList)){
            //修改已发货完成的SKU订单状态
            List<String> deliverSuccessSkuOrderNoList = orderRepository.querySkuOrderNoList(orderVO.getOrderNo(), deliverSuccessSkuIdList);
            SkuOrderCommand skuOrderCommand = new SkuOrderCommand();
            skuOrderCommand.setDeliveredTime(DateUtil.toLocalDateTime(new Date()));
            orderRepository.batchUpdateSkuOrderState(deliverSuccessSkuOrderNoList, OrderEnum.State.WAIT_DELIVERY, OrderEnum.State.WAIT_RECEIVE, skuOrderCommand);
            //触发订单状态同步
            tripOrderChange(null, deliverSuccessSkuOrderNoList);
        }
        //返回设置
        deliverRes.setOrderVO(orderVO);
        return deliverRes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReceiveSkuOrderRes receiveSkuOrder(String orderNo, List<String> skuOrderNoList) {
        OrderDTO order = orderRepository.order(orderNo);
        if(ObjectUtil.isEmpty(order)){
            ThrowsException.exception(BaseErrorCode.PARAM, "不存在的订单号:" + orderNo);
        }
        // 防呆B
        if (order.getOrderState() == OrderEnum.State.DOWN_RECEIVE){
            return new ReceiveSkuOrderRes();
        }
        if (order.getOrderState() != OrderEnum.State.WAIT_RECEIVE){
            ThrowsException.exception(BaseErrorCode.PARAM, "订单状态非可收货状态:" + orderNo);
        }
        if(ObjectUtil.isEmpty(skuOrderNoList)){
            skuOrderNoList = orderRepository.querySkuOrderNoList(order.getOrderNo(), null);
        }
        // 1. 收货
        SkuOrderCommand skuOrderCommand = new SkuOrderCommand();
        skuOrderCommand.setReceiveTime(DateUtil.toLocalDateTime(new Date()));
        int editSkuCount = orderRepository.batchUpdateSkuOrderState(skuOrderNoList, OrderEnum.State.WAIT_RECEIVE, OrderEnum.State.DOWN_RECEIVE, skuOrderCommand);
        if(editSkuCount != skuOrderNoList.size()){
            ThrowsException.exception(BaseErrorCode.PARAM, "存在状态异常的SKU订单");
        }
        // 2. 结算判断
        List<Long> waitSettlementOrderId = new ArrayList<>();
        List<SkuOrderDTO> waitSettlementOrder = new ArrayList<>();
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderNoList(skuOrderNoList);
        List<SkuOrderDTO> skuOrderVOList = orderRepository.skuOrderList(skuOrderQuery).getRecords();
        // SpuOrder 层折叠: 渠道类型原落 spu_order, 现落 sku_order, 取首个 sku 判定
        if(ObjectUtil.isNotEmpty(skuOrderVOList) && SpuEnum.ChannelType.SELECTION == skuOrderVOList.get(0).getSpuChannelType()){
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
        tripOrderChange(null, skuOrderNoList);
        if (Objects.isNull(SecurityUtils.getAccountId())){
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(order), order.getOrderState(),OrderEnum.State.DOWN_RECEIVE, AccountEnum.Identity.PLATFORM.getCode(), AccountEnum.Identity.PLATFORM);
        }else {
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(order), order.getOrderState(),OrderEnum.State.DOWN_RECEIVE,SecurityUtils.getAccountId(),SecurityUtils.getIdentity());
        }

        return new ReceiveSkuOrderRes(waitSettlementOrderId, TransferUtils.transfers(waitSettlementOrder, SkuOrderVO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompleteSkuOrderRes completeSkuOrder(String orderNo, List<String> skuOrderNoList) {
        OrderDTO order = orderRepository.order(orderNo);
        // 1. 完成
        SkuOrderCommand skuOrderCommand = new SkuOrderCommand();
        orderRepository.batchUpdateSkuOrderState(skuOrderNoList, OrderEnum.State.DOWN_RECEIVE, OrderEnum.State.SUCCESS, skuOrderCommand);
        // 2. 结算与分润
        List<Long> waitSettlementOrderId = new ArrayList<>();
        List<SkuOrderDTO> waitSettlementOrder = new ArrayList<>();
        EarningsEnum.SettleType settleType = null;
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderNoList(skuOrderNoList);
        List<SkuOrderDTO> skuOrderVOList = orderRepository.skuOrderList(skuOrderQuery).getRecords();
        // SpuOrder 层折叠: 渠道类型原落 spu_order, 现落 sku_order, 取首个 sku 判定
        if(ObjectUtil.isNotEmpty(skuOrderVOList) && SpuEnum.ChannelType.SELECTION == skuOrderVOList.get(0).getSpuChannelType()){
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
        settleReq.setAccountId(order.getChannelId());
        settleReq.setSettleAmount(order.getMemberAmount());
        settleReq.setJoinSettleOrderNo(order.getId());
        balancePayApi.channelSettle(settleReq);

        // 3 触发订单状态同步
        tripOrderChange(null, skuOrderNoList);
        if (Objects.isNull(SecurityUtils.getAccountId())){
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(order), order.getOrderState(),OrderEnum.State.SUCCESS, AccountEnum.Identity.PLATFORM.getCode(), AccountEnum.Identity.PLATFORM);
        }else {
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(order), order.getOrderState(),OrderEnum.State.SUCCESS,SecurityUtils.getAccountId(),SecurityUtils.getIdentity());
        }

        return new CompleteSkuOrderRes(waitSettlementOrderId, waitSettlementOrder,settleType);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String orderNo, String cancelReason) {
        //批量修改订单状态
        OrderAggVO orderAggVO = orderRepository.orderAggVO(orderNo);
        if (Objects.isNull(orderAggVO) || Objects.isNull(orderAggVO.getOrderVO()) || CollUtil.isEmpty(orderAggVO.getSkuOrderList())){
            ThrowsException.exception(BaseErrorCode.PARAM, "订单不存在id为："+orderNo);
        }
        if (orderAggVO.getOrderVO().getOrderState() == OrderEnum.State.CLOSE){
            return;
        }
        Set<OrderEnum.State> allowModifyStates = OrderEnum.State.getMemberCancelOrderStates();
        if(!allowModifyStates.contains(orderAggVO.getOrderVO().getOrderState())){
            ThrowsException.exception(BaseErrorCode.CUSTOM, "仅能取消未支付的订单！");
        }
        // SpuOrder 层折叠: ext 落 order 表 JSON 列, 直接传对象不再 toJSONString
        OrderExt updateOrderExt = Optional.ofNullable(cancelReason)
                .filter(StrUtil::isNotEmpty)
                .map(reason -> {
                    OrderExt orderExt = Optional.ofNullable(orderAggVO.getOrderVO().getOrderExt()).orElseGet(OrderExt::new);
                    orderExt.setCancelReason(reason);
                    orderExt.setCloseReason("买家主动取消订单");
                    return orderExt;
                })
                .orElse(null);
        batchUpdateOrderState(Collections.singletonList(orderNo), orderAggVO.getOrderVO().getOrderState(), OrderEnum.State.CLOSE, updateOrderExt);

        OrderDTO order = TransferUtils.transfer(orderAggVO.getOrderVO(), OrderDTO.class);
        localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(order), order.getOrderState(),OrderEnum.State.CLOSE,SecurityUtils.getAccountId(),SecurityUtils.getIdentity());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void channelCancelOrder(String orderNo, String cancelReason) {
        OrderAggVO orderAggVO = orderRepository.orderAggVO(orderNo);
        if (Objects.isNull(orderAggVO) || Objects.isNull(orderAggVO.getOrderVO()) || CollUtil.isEmpty(orderAggVO.getSkuOrderList())){
            ThrowsException.exception(BaseErrorCode.PARAM, "订单不存在id为："+orderNo);
        }
        if (orderAggVO.getOrderVO().getOrderState() == OrderEnum.State.CLOSE){
            return;
        }
        Set<OrderEnum.State> allowModifyStates = OrderEnum.State.getChannelCancelOrderStates();
        if(!allowModifyStates.contains(orderAggVO.getOrderVO().getOrderState())){
            ThrowsException.exception(BaseErrorCode.CUSTOM, "仅能取消未支付的订单！");
        }
        // SpuOrder 层折叠: ext 落 order 表 JSON 列, 直接传对象不再 toJSONString
        OrderExt updateOrderExt = Optional.ofNullable(cancelReason)
                .filter(StrUtil::isNotEmpty)
                .map(reason -> {
                    OrderExt orderExt = Optional.ofNullable(orderAggVO.getOrderVO().getOrderExt()).orElseGet(OrderExt::new);
                    orderExt.setCancelReason(reason);
                    orderExt.setCloseReason(reason);
                    return orderExt;
                })
                .orElse(null);
        batchUpdateOrderState(Collections.singletonList(orderNo), orderAggVO.getOrderVO().getOrderState(), OrderEnum.State.CLOSE, updateOrderExt);
        if (orderAggVO.getOrderVO().getOrderState() == OrderEnum.State.CHANNEL_WAIT_PAY){
            SellAfterRefundReq sellAfterRefundReq = new SellAfterRefundReq();
            sellAfterRefundReq.setOrderId(orderAggVO.getOrderVO().getId());
            // FIXME
//            sellAfterRefundReq.setSellAfterOrderNo(orderNo);
            MemberRefundRes memberRefundRes = balancePayApi.sellAfterRefund(sellAfterRefundReq);
            if (StrUtil.isNotBlank(memberRefundRes.getRefundWarnMsg())) {
                // 迁移: Base PlatformException 仅 (ErrorCode, String...) 构造, 去掉 WIP 臆造的 boolean 告警标记入参
                throw new PlatformException(OrderErrorCode.REFUND_FAIL, memberRefundRes.getRefundWarnMsg());
            }
        }
        OrderDTO order = TransferUtils.transfer(orderAggVO.getOrderVO(), OrderDTO.class);
        if (Objects.isNull(SecurityUtils.getAccountId())){
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(order), order.getOrderState(), OrderEnum.State.CLOSE, AccountEnum.Identity.PLATFORM.getCode(), AccountEnum.Identity.PLATFORM);
        }else {
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(order), order.getOrderState(), OrderEnum.State.CLOSE, SecurityUtils.getAccountId(), SecurityUtils.getIdentity());
        }
    }

    /**
     * 触发订单状态同步
     *
     * <p>SpuOrder 层折叠: 原中间的 spuOrderId 参数删除, 子层唯一为 sku_order</p>
     *
     * @param orderNoList 交易单ID列表, 可为 null
     * @param skuOrderNoList SKU订单ID列表, 可为 null
     * @return 状态变更结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TripOrderChangeRes tripOrderChange(List<String> orderNoList, List<String> skuOrderNoList) {
        TripOrderChangeRes tripOrderChangeRes = new TripOrderChangeRes();
        if(ObjectUtil.isEmpty(orderNoList)){
            orderNoList = new ArrayList<>();
        }
        if(ObjectUtil.isNotEmpty(skuOrderNoList)){
            orderNoList.addAll(orderRepository.orderNoBySkuOrderNo(skuOrderNoList));
        }
        if(orderNoList.isEmpty()){
            return tripOrderChangeRes;
        }
        List<OrderStateCheckDTO> orderStateCheckRes = orderRepository.checkOrderState(orderNoList);
        for (OrderStateCheckDTO state : orderStateCheckRes) {
            if(!state.getCurrentState().equals(state.getToState())) {
                //修改交易单状态
                orderRepository.batchUpdateOrderState(Collections.singletonList(state.getOrderNo()), state.getCurrentState(), state.getToState(), null);
                tripOrderChangeRes.getOrderStateChange().add(new TripOrderChangeRes.Item(state.getId(), state.getCurrentState(), state.getToState()));
                // 订单状态业务事件 订阅方自行决定是否通知开发者
                localMessageApi.publishOrderState(state.getOrderType(), state.getChannelId(), state.getOutOrderNo(), state.getCurrentState(), state.getToState());
                //确认收货通知怡亚通
            }
        }
        return tripOrderChangeRes;
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

    /**
     * 交易单字段补全
     *
     * <p>SpuOrder 层折叠: 原填 SpuOrderDTO, 现填 OrderDTO。商品维度字段(spuChannelType/spuSaleType/
     * supplierId/dealerId/spuName/spuImg/settleSendState)下沉 sku_order, 门店与下单人展示字段进 orderExt,
     * memberAmount 由 {@code OrderDTO#init} 统一汇总</p>
     */
    private void fillIn(OrderDTO order, OrderCreateCommand orderCreateCommand,
                        MemberOrderCreateCommand memberOrderCreateCommand, SkuOrderDTO skuOrder){
        order.setOrderType(orderCreateCommand.getOrderType());
        order.setOutOrderNo(orderCreateCommand.getOutOrderNo());
        order.setChannelId(orderCreateCommand.getChannelId());
        order.setOperatorId(skuOrder.getOperatorId());
        order.setSpuId(String.valueOf(skuOrder.getSpuId()));
        order.setDiscountAmount(Money.ZERO);
        order.setOrderState(OrderEnum.State.NEW);
        order.setShipVO(orderCreateCommand.getShipVO());
        order.setRemark(orderCreateCommand.getRemark());
        order.setOrderStateLog(OrderEnum.State.NEW.toString());
        order.setStoreId(memberOrderCreateCommand.getStoreId());
        order.setAccountId(memberOrderCreateCommand.getAccountId());
        order.setMemberId(memberOrderCreateCommand.getAccountId());
        order.setCloseTime(LocalDateTime.now().plusMinutes(30));
        order.setRefund(CommonEnum.YesOrNo.NO);
        OrderExt orderExt = new OrderExt();
        orderExt.setStoreId(memberOrderCreateCommand.getStoreId());
        orderExt.setStoreAccount(memberOrderCreateCommand.getStoreAccount());
        orderExt.setStoreName(memberOrderCreateCommand.getStoreName());
        orderExt.setStoreHead(memberOrderCreateCommand.getStoreHead());
        orderExt.setUserAccount(memberOrderCreateCommand.getUserAccount());
        orderExt.setUserName(memberOrderCreateCommand.getUserName());
        orderExt.setNickName(memberOrderCreateCommand.getNickName());
        orderExt.setMemberId(memberOrderCreateCommand.getAccountId());
        order.setOrderExt(orderExt);
    }

    private SkuOrderDTO buildSkuOrder(OrderGoodsInfoVO orderGoodsInfoVO, String orderNo,
                                      ChannelNowServiceFeeRes channelNowServiceFee,
                                      EarningsConfigRpcVO earningsConfigRpcVO, EarningsEnum.SettleType settleOrderType, LocalDateTime time){

        SkuOrderDTO skuOrder = new SkuOrderDTO();
        skuOrder.setFreightAmount(Money.ZERO);
        skuOrder.setDiscountAmount(Money.ZERO);
        skuOrder.setId(SnowflakeGenerator.getSnowflakeId());
        skuOrder.setOrderNo(orderNo);
        skuOrder.setSkuOrderNo(BusinessCodeUtil.generate(BusinessType.ORDER_SKU));
        skuOrder.setSpuId(orderGoodsInfoVO.getSpuId());
        skuOrder.setSkuImg(orderGoodsInfoVO.getImg());
        skuOrder.setSpuChannelType(orderGoodsInfoVO.getSpuChannelType());
        //计算订单金额
        skuOrder.setGoodsAmount(orderGoodsInfoVO.getSalePrice().multiply(orderGoodsInfoVO.getNum()));
        skuOrder.setSupplierAmount(orderGoodsInfoVO.getSupplyPrice().multiply(orderGoodsInfoVO.getNum()));
        skuOrder.setStoreAmount(orderGoodsInfoVO.getStorePrice().multiply(orderGoodsInfoVO.getNum()));

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
                deliver.setExpressCompanyName(LogisticsMethod.normalizeCompanyName(deliverCommand.getExpressCompanyName()));
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
    public Map<Long, Money> validateOrderShipChange(OrderAgg orderAgg, com.newzkl.platform.base.common.ddd.model.vo.ShipVO shipVO) {
        // 1. 基础校验：订单聚合对象非空
        if (Objects.isNull(orderAgg) || Objects.isNull(orderAgg.getOrder())
                || CollUtil.isEmpty(orderAgg.getSkuOrderList())) {
            throw new PlatformException(BaseErrorCode.NODATA, "订单不存在！");
        }

        OrderDTO order = orderAgg.getOrder();
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
        // SpuOrder 层折叠: spuId 原取自 spu_order, 现按 sku_order 的 spuId 去重
        List<Long> spuIdList = skuOrderList.stream().map(SkuOrderDTO::getSpuId).distinct().collect(Collectors.toList());
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
     *
     * <p>SpuOrder 层折叠: 运费只落同 spu 首个 sku, 故按 sku 侧 spuId 分组求和 = 原 spu 级运费</p>
     */
    @Override
    public void validateFreightUnchanged(OrderAgg orderAgg, Map<Long, Money> goodsFreight) {
        Map<Long, List<SkuOrderDTO>> skuGroupBySpu = orderAgg.getSkuOrderList().stream()
                .collect(Collectors.groupingBy(SkuOrderDTO::getSpuId));
        skuGroupBySpu.forEach((spuId, skuList) -> {
            Money oldFreight = Money.sumBy(skuList, SkuOrderDTO::getFreightAmount);
            Money newFreight = Optional.ofNullable(goodsFreight.get(spuId)).orElse(Money.ZERO);
            if (!Objects.equals(oldFreight, newFreight)) {
                throw new PlatformException(BaseErrorCode.NODATA, "运费发生变动，无法修改，请重新下单");
            }
        });
    }

    /**
     * 数据库层更新收货地址（封装，便于复用）
     */
    @Override
    public void updateOrderShipDb(String orderNo, com.newzkl.platform.base.common.ddd.model.vo.ShipVO shipVOJson) {
        orderRepository.updateOrderShip(orderNo, shipVOJson);
    }

    // ------------------------------ 简化后的changeOrderShip方法 ------------------------------
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeOrderShip(OrderShipCommand command) {
        // 1. 查询订单聚合对象
        OrderAgg orderAgg = orderAgg(command.getOrderNo());

        // 2. 调用核心校验逻辑（复用所有前置校验）
        Map<Long, Money> goodsFreight = validateOrderShipChange(orderAgg, command.getShipVO());

        // 3. 校验运费是否变动
        validateFreightUnchanged(orderAgg, goodsFreight);

        // 4. 执行数据库更新
        updateOrderShipDb(command.getOrderNo(), command.getShipVO());

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
        return buildOrderAgg(orderAgg, orderRepository.order(orderId));
    }

    @Override
    public OrderAgg orderAgg(String orderNo) {
        OrderAgg orderAgg = new OrderAgg();
        orderAgg.setInit(false);
        return buildOrderAgg(orderAgg, orderRepository.order(orderNo));
    }

    private OrderAgg buildOrderAgg(OrderAgg orderAgg, OrderDTO order) {
        orderAgg.setOrder(order);
        // 子表关联键为 order_no, 交易单不存在时不可放行空条件查询(否则捞全表 sku_order)
        if (order == null) {
            orderAgg.setSkuOrderList(new ArrayList<>());
            return orderAgg;
        }
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderNo(order.getOrderNo());
        List<SkuOrderDTO> skuOrderList = orderRepository.skuOrderList(skuOrderQuery).getRecords();
        orderAgg.setSkuOrderList(skuOrderList);
        return orderAgg;
    }

    @Override
    public Page<OrderVO> orderPage(OrderQuery orderQuery) {
        Page<OrderDTO> dtoPage = orderRepository.orderList(orderQuery);
        return TransferUtils.transferPage(dtoPage, OrderVO.class);
    }

    @Override
    public void batchUpdateOrderState(List<String> orderNoList, OrderEnum.State sourceState, OrderEnum.State toState, OrderExt orderExt) {
        orderRepository.batchUpdateOrderState(orderNoList, sourceState, toState, orderExt);
        orderRepository.batchUpdateSkuOrderState(orderNoList, sourceState, toState);
    }

    @Override
    public List<OrderDTO> listDOByOrderStateAndUpdateTimeLessThan(OrderEnum.State orderState, LocalDateTime updateTime) {
        return orderRepository.listDOByOrderStateAndUpdateTimeLessThan(orderState, updateTime);
    }

    @Override
    public void orderEdit(OrderDTO orderEdit) {
        orderRepository.orderSave(orderEdit);
    }

    @Override
    public Map<Long, List<DeliverVO>> orderDeliverInfo(String orderNo) {
        // 迁移: 原 domain 直连 deliverDAO 违依赖硬线, 查询下沉 infra, domain 直调 repository
        List<DeliverVO> deliverVOS = orderRepository.deliverListByOrderNo(orderNo);
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
    public List<LogisticsTrack> orderTrack(String orderNo) {
        List<DeliverVO> deliverVOS = orderRepository.deliverListByOrderNo(orderNo);
        if (CollUtil.isEmpty(deliverVOS)) {
            return Collections.emptyList();
        }
        List<LogisticsTrack> tracks = new ArrayList<>(deliverVOS.size());
        for (DeliverVO deliverVO : deliverVOS) {
            if (StrUtil.isBlank(deliverVO.getExpressNo())) {
                continue;
            }
            try {
                tracks.add(LogisticsMethod.queryTrack(deliverVO.getExpressCompanyName(), deliverVO.getExpressNo()));
            } catch (Exception e) {
                // 单包裹查不到不阻断整单展示: 降级为无节点轨迹, 前端仍能看到公司与单号
                log.warn(StrUtil.format("查询快递轨迹失败 orderNo={} expressNo={} msg={}",
                        orderNo, deliverVO.getExpressNo(), e.getMessage()));
                tracks.add(degradeTrack(deliverVO));
            }
        }
        return tracks;
    }

    /**
     * 轨迹查询失败降级
     *
     * @param deliverVO 发货单
     * @return 仅含公司与单号的空轨迹
     */
    private LogisticsTrack degradeTrack(DeliverVO deliverVO) {
        LogisticsTrack track = new LogisticsTrack();
        track.setExpressCompanyName(deliverVO.getExpressCompanyName());
        track.setExpressNo(deliverVO.getExpressNo());
        track.setSigned(false);
        track.setNodes(Collections.emptyList());
        return track;
    }

    /**
     * 填充物流信息
     */
    private void buildDeliver(Map<Long, List<DeliverVO>> map, OrderAggVO spuOrderAggVO) {
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
        Assert.hasText(entity.getOrderNo(), "交易单号不能为空");
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
    public List<OrderStateRecordVO> recordListByOrderNo(String orderNo) {
        OrderStateRecordQuery query = new OrderStateRecordQuery();
        query.setOrderNo(orderNo);
        return recordPage(query).getRecords();
    }

    @Override
    public OrderCreateRes createOrder(OrderGoodsCheckV2Res data, OrderCreateCommand orderCreateCommand) {
        List<StoreGoodsDetailRpcVO> goodsInfo = data.getGoodsInfo();
        Map<Long, Money> goodsFreight = data.getGoodsFreight();

        // 获取渠道商当前服务费比例
        // 迁移(Q2): 缓存移交 infra(@Cacheable), domain 直调 repository, 不碰 redisClient
        ChannelNowServiceFeeRes channelNowServiceFee = channelApi.queryNowServiceFee(goodsInfo.get(0).getChannelId());
        // 获取渠道商分润配置
        EarningsConfigRpcVO earningsConfigRpcVO = orderRepository.channelEarningsConfig(goodsInfo.get(0).getChannelId());
        Long orderId = SnowflakeGenerator.getSnowflakeId();
        String orderNo = BusinessCodeUtil.generate(BusinessType.ORDER);
        List<SkuOrderDTO> skuOrderList = new ArrayList<>();
        // SpuOrder 层折叠: 运费原落 spu_order, 现落同 spu 首个 sku, 避免多 sku 重复累加
        Set<Long> freightFilledSpuId = new HashSet<>();
        LocalDateTime now = LocalDateTime.now();
        // 构建订单数据
        if(StrUtil.isEmpty(orderCreateCommand.getOutOrderNo())){
            orderCreateCommand.setOutOrderNo(orderId.toString());
        }
        for (StoreGoodsDetailRpcVO orderGoodsInfoVO : goodsInfo) {
            Long spuId = orderGoodsInfoVO.getGoodsId();
            // 迁移(Q2): settleOrderType 缓存移交 infra(@Cacheable), domain 直调 repository
            EarningsEnum.SettleType settleOrderType = orderRepository.settleOrderType(orderGoodsInfoVO.getSupplierId());
            SkuOrderDTO skuOrder = buildSkuOrder(orderGoodsInfoVO,orderNo,channelNowServiceFee,earningsConfigRpcVO,settleOrderType,now);
            if(freightFilledSpuId.add(spuId)){
                skuOrder.setFreightAmount(Optional.ofNullable(goodsFreight.get(spuId)).orElse(Money.ZERO));
            }
            skuOrderList.add(skuOrder);
        }
        //生成交易单
        OrderDTO order = new OrderDTO();
        order.init(orderCreateCommand, orderId, skuOrderList);
        order.setOrderNo(orderNo);
        fillIn(order,orderCreateCommand,skuOrderList.get(0));
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
        OrderCreateRes orderCreateRes = new OrderCreateRes(orderId, order.getOrderNo(), order.getOrderState(), orderAgg);
        //前端使用的支付倒计时
        orderCreateRes.setRemainTime(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        return orderCreateRes;
    }

    @Override
    public void savePrePayOrder(OrderCreateRes order, MemberOrderCreateCommand memberOrderCreateCommand) {
        orderRepository.savePrePayOrder(order, memberOrderCreateCommand);
    }

    private SkuOrderDTO buildSkuOrder(StoreGoodsDetailRpcVO orderGoodsInfoVO, String orderNo,
                                      ChannelNowServiceFeeRes channelNowServiceFee,
                                      EarningsConfigRpcVO earningsConfigRpcVO, EarningsEnum.SettleType settleOrderType, LocalDateTime time){

        SkuOrderDTO skuOrder = new SkuOrderDTO();
        skuOrder.setFreightAmount(Money.ZERO);
        skuOrder.setDiscountAmount(Money.ZERO);
        skuOrder.setId(SnowflakeGenerator.getSnowflakeId());
        skuOrder.setOrderNo(orderNo);
        skuOrder.setSkuOrderNo(BusinessCodeUtil.generate(BusinessType.ORDER_SKU));
        skuOrder.setSpuId(orderGoodsInfoVO.getGoodsId());
        skuOrder.setSkuImg(orderGoodsInfoVO.getSkuImg());
        skuOrder.setSpuChannelType(orderGoodsInfoVO.getChannelType());
        skuOrder.setStoreId(orderGoodsInfoVO.getStoreId());
        //计算订单金额
        skuOrder.setGoodsAmount(orderGoodsInfoVO.getSupplierPrice().multiply(orderGoodsInfoVO.getBugNum()));
        skuOrder.setSupplierAmount(orderGoodsInfoVO.getSpuSupplyPrice().multiply(orderGoodsInfoVO.getBugNum()));
        skuOrder.setStoreAmount(orderGoodsInfoVO.getSellPrice().multiply(orderGoodsInfoVO.getBugNum()));

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

    /**
     * 交易单公共字段回填
     *
     * <p>SpuOrder 层折叠: 原回填 SpuOrderDTO, 现直接回填 OrderDTO;
     * 商品维度字段(spuChannelType/spuSaleType/supplierId/dealerId/spuName/spuImg)已下沉 sku_order 不再冗余</p>
     */
    private void fillIn(OrderDTO order, OrderCreateCommand orderCreateCommand, SkuOrderDTO skuOrder){
        order.setOrderType(orderCreateCommand.getOrderType());
        order.setOutOrderNo(orderCreateCommand.getOutOrderNo());
        order.setChannelId(orderCreateCommand.getChannelId());
        order.setOperatorId(skuOrder.getOperatorId());
        order.setSpuId(String.valueOf(skuOrder.getSpuId()));
        order.setStoreId(skuOrder.getStoreId());
        order.setDiscountAmount(Money.ZERO);
        order.setOrderState(OrderEnum.State.NEW);
        order.setShipVO(orderCreateCommand.getShipVO());
        order.setRemark(orderCreateCommand.getRemark());
        order.setOrderStateLog(OrderEnum.State.NEW.toString());

        order.setAccountId(SecurityUtils.getAccountId());
        order.setMemberId(SecurityUtils.getAccountId());
        order.setCloseTime(LocalDateTime.now().plusMinutes(30));
        OrderExt orderExt = new OrderExt();
        orderExt.setStoreId(skuOrder.getStoreId());
        //查门店信息
        List<StoreRPCVO> storeRPCVOS = goodsApi.batchQueryStoreInfo(Collections.singletonList(skuOrder.getStoreId()));
        if (CollUtil.isNotEmpty(storeRPCVOS)){
            StoreRPCVO storeRPCVO = storeRPCVOS.get(0);
            orderExt.setStoreHead(storeRPCVO.getLogo());
            orderExt.setStoreName(storeRPCVO.getName());
        }
        //查门店im账号
        AccountGroupVO accountInfo = accountApi.channelInfo(skuOrder.getStoreId());
        if (Objects.nonNull(accountInfo)){
            orderExt.setStoreAccount(accountInfo.getUserAccount());
        }
        AccountGroupVO memberAccount = accountApi.accountInfo(SecurityUtils.getAccountId());
        if (Objects.nonNull(memberAccount)){
            orderExt.setUserAccount(memberAccount.getUserAccount());
            orderExt.setUserName(memberAccount.getNickname());
            orderExt.setNickName(memberAccount.getNickname());
        }
        orderExt.setMemberId(SecurityUtils.getAccountId());
        order.setOrderExt(orderExt);
        order.setRefund(CommonEnum.YesOrNo.NO);
    }
}
