package com.newzkl.platform.base.biz.order.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.newzkl.platform.base.biz.order.application.service.OrderService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.*;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundRepository;
import com.newzkl.platform.base.biz.order.domain.service.*;
import com.newzkl.platform.base.biz.order.domain.spi.ThirdPartyOrderProcessor;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.res.CompleteSkuOrderRes;
import com.newzkl.platform.base.biz.order.model.res.DeliverRes;
import com.newzkl.platform.base.biz.order.model.res.ReceiveSkuOrderRes;
import com.newzkl.platform.base.common.ddd.facade.GoodsVO;
import com.newzkl.platform.base.common.ddd.facade.OrderGoodsCheckReq;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckRes;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSkuVO;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.common.ddd.utils.BizUtil;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.BalancePayReq;
import com.newzkl.platform.base.common.ddd.facade.BalancePayResult;
import com.newzkl.platform.base.common.ddd.facade.ChannelNowServiceFeeRes;
import com.newzkl.platform.base.common.ddd.facade.ThirdPartyOrderResult;
import com.newzkl.platform.base.common.ddd.model.constant.OrderErrorCode;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/129:45
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderDomain orderDomain;

    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;
    private final ChannelApi channelApi;

    private final PayApi payApi;

    private final SettleDomain settleDomain;

    private final LocalMessageApi localMessageApi;

    private final GoodsApi goodsApi;

    @Override
    public void receiveSkuOrder(String orderNo, List<String> skuOrderNoList) {
        // 收货
        ReceiveSkuOrderRes receiveSkuOrderRes = orderDomain.receiveSkuOrder(orderNo, skuOrderNoList);
        // 结算
        if(ObjectUtil.isNotEmpty(receiveSkuOrderRes.getWaitSettlementOrder())){
            List<SettleOrderWaitCommand> commandList = TransferUtils.transfers(receiveSkuOrderRes.getWaitSettlementOrder(), SettleOrderWaitCommand::new, (c, v)->{
                v.setSkuOrderId(c.getId());
                v.setOrderMoney(c.getSupplierAmount());
                v.setSkuCount(c.getCount());
                v.setType(0);
            });
            settleDomain.settleOrderWaitSave(commandList, EarningsEnum.SettleType.ORDER_SUCCESS);
        }
    }
    @Override
    public void completeSkuOrder(String orderNo, List<String> skuOrderNoList) {
        // 完成
        CompleteSkuOrderRes completeSkuOrderRes = orderDomain.completeSkuOrder(orderNo, skuOrderNoList);
        // 结算
        if (ObjectUtil.isNotEmpty(completeSkuOrderRes.getWaitSettlementOrder())) {
            List<SettleOrderWaitCommand> commandList = TransferUtils.transfers(completeSkuOrderRes.getWaitSettlementOrder(), SettleOrderWaitCommand::new, (c,v)->{
                v.setSkuOrderId(c.getId());
                v.setOrderMoney(c.getSupplierAmount());
                v.setSkuCount(c.getCount());
                v.setType(0);
            });
            settleDomain.settleOrderWaitSave(commandList,completeSkuOrderRes.getSettleType());
        }
    }
    @Override
    public Long channelRealOrderFreight(OrderCreateCommand orderCreateCommand){
        // 1、获取用户信息 包含渠道商id、运营商id
        // 2、获取商品信息、校验商品状态、库存并返回运费
        List<OrderItemCommand> orderGoodsList = orderCreateCommand.getOrderGoodsList();
        List<GoodsVO> goodsList = buildGoodsVO(orderGoodsList);
        PlatformResult<OrderGoodsCheckRes> result = goodsApi.orderCheck(buildOrderGoodsCheckReq(orderCreateCommand, new MemberOrderCreateCommand()), goodsList);
        Map<Long, Money> goodsFreight = result.getData().getGoodsFreight();
        return goodsFreight.values().stream().mapToLong(Money::getCent).sum();
    }

    public List<GoodsVO> buildGoodsVO(List<OrderItemCommand> orderGoodsList){
        List<GoodsVO> goodsList = new ArrayList<>();
        orderGoodsList.forEach(x->{
            GoodsVO goodsVO = new GoodsVO();
            goodsVO.setSkuId(x.getSkuId());
            goodsVO.setNum(x.getCount());
            goodsList.add(goodsVO);
        });
        return goodsList;
    }

    public OrderGoodsCheckReq buildOrderGoodsCheckReq(OrderCreateCommand orderCreateCommand, MemberOrderCreateCommand memberOrderCreateCommand) {
        OrderGoodsCheckReq req = new OrderGoodsCheckReq();
        req.setChannelId(orderCreateCommand.getChannelId());
        req.setStoreId(memberOrderCreateCommand.getStoreId());
        ShipVO shipVO = orderCreateCommand.getShipVO();
        req.setShipAreaCode(shipVO.getShipAreaCode());
        req.setShipCityCode(shipVO.getShipCityCode());
        req.setShipProvinceCode(shipVO.getShipProvinceCode());
        req.setShipArea(shipVO.getShipArea());
        return req;
    }

    @Override
    public void memberPaySuccess(String orderNo) {
        OrderAgg orderAgg = orderDomain.orderAgg(orderNo);
        doMemberPaySuccess(orderAgg);
    }

    @Override
    public void memberPaySuccess(Long orderId) {
        OrderAgg orderAgg = orderDomain.orderAgg(orderId);
        doMemberPaySuccess(orderAgg);
    }

    @Override
    public void orderDirectPay(String orderNo) {
        OrderAgg orderAgg = orderDomain.orderAgg(orderNo);
        // 仅 C 端待付款订单可直接支付
        BizUtil.checkInState(Collections.singletonList(OrderEnum.State.MEMBER_WAIT_PAY.getCode()), orderAgg.getOrder().getOrderState().getCode(), OrderErrorCode.STATE_ERROR);
        if (OrderEnum.OrderType.MEMBER != orderAgg.getOrder().getOrderType()) {
            ThrowsException.exception(BaseErrorCode.PARAM, "只有C端订单才能直接支付");
        }
        doMemberPaySuccess(orderAgg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deliverCreate(DeliverCommand deliverCommand) {
        //发货
        DeliverRes deliverRes = orderDomain.deliverCreate(deliverCommand);
        OrderVO orderVO = deliverRes.getOrderVO();
        //发货开发者通知
        List<SkuCountDTO> skuCountDTOList = TransferUtils.transfers(deliverCommand.getDeliverItemCommandList(), SkuCountDTO.class);
        ThirdPartyOrderProcessor.find().delivery(orderVO.getOutOrderNo(), skuCountDTOList, deliverCommand.getExpressCompanyName(), deliverCommand.getExpressNo(), orderVO.getChannelId());
        //结算运费
        if(CommonEnum.YesOrNo.NO == orderVO.getSettleSendState() && orderVO.getSupplierId() != null && orderVO.getSupplierId() > 100L){
            FreightSettleOrderWaitCommand freightSettleOrderWaitCommand = new FreightSettleOrderWaitCommand();
            freightSettleOrderWaitCommand.setOrderNo(orderVO.getOrderNo());
            freightSettleOrderWaitCommand.setSpuId(orderVO.getSpuId());
            freightSettleOrderWaitCommand.setSupplierId(orderVO.getSupplierId());
            freightSettleOrderWaitCommand.setAmount(orderVO.getFreightAmount());
            if(!freightSettleOrderWaitCommand.getAmount().equals(0)){
                settleDomain.freightSettleOrderWaitSave(Collections.singletonList(freightSettleOrderWaitCommand),
                        orderRepository.settleOrderType(orderVO.getSupplierId()));
            }
            // SpuOrder 层折叠: 结算发送状态下沉到 sku 级, 按交易单批量置位
            SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
            skuOrderQuery.setOrderNo(orderVO.getOrderNo());
            SkuOrderDTO skuEdit = new SkuOrderDTO();
            skuEdit.setSettleSendState(CommonEnum.YesOrNo.YES);
            orderRepository.skuOrderSave(skuEdit, skuOrderQuery);
        }
        OrderDTO order = TransferUtils.transfer(orderVO, OrderDTO.class);
        localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(order), order.getOrderState(),OrderEnum.State.WAIT_RECEIVE, SecurityUtils.getAccountId(),SecurityUtils.getIdentity());
    }

    @Override
    public ExcelErrorVO fullDeliver(List<FullDeliverExcelVO> lst) {
        ExcelErrorVO errorVO = new ExcelErrorVO(lst.size());
        for (int i = 0; i < lst.size(); i++) {
            FullDeliverExcelVO fullDeliverExcelVO = lst.get(i);
            try{
                DeliverCommand deliverCommand = TransferUtils.transfer(fullDeliverExcelVO, new Function<FullDeliverExcelVO, DeliverCommand>() {
                    @Override
                    public DeliverCommand apply(FullDeliverExcelVO fullDeliverExcelVO) {
                        DeliverCommand deliverCommand = new DeliverCommand();
                        deliverCommand.setSpuOrderId(Long.valueOf(fullDeliverExcelVO.getSpuOrderId()));
                        deliverCommand.setExpressCompanyName(fullDeliverExcelVO.getExpressCompanyName());
                        deliverCommand.setExpressNo(fullDeliverExcelVO.getExpressNo());
                        deliverCommand.setDeliverItemCommandList(null);
                        return deliverCommand;
                    }
                });
                deliverCreate(deliverCommand);
            }catch (PlatformException e){
                errorVO.addError(i + 2, e.getMessage());
            }catch (NumberFormatException e1){
                errorVO.addError(i + 2, "存在格式错误, 需为数字");
            }catch (Exception e2){
                errorVO.addError(i + 2, ExceptionUtil.getMessage(e2));
            }
        }
        return errorVO;
    }

    @Override
    public ExcelErrorVO splitDeliver(List<SplitDeliverExcelVO> lst) {
        ExcelErrorVO errorVO = new ExcelErrorVO(lst.size());
        for (int i = 0; i < lst.size(); i++) {
            SplitDeliverExcelVO splitDeliverExcelVO = lst.get(i);
            try{
                DeliverCommand deliverCommand = TransferUtils.transfer(splitDeliverExcelVO, new Function<SplitDeliverExcelVO, DeliverCommand>() {
                    @Override
                    public DeliverCommand apply(SplitDeliverExcelVO item) {
                        DeliverCommand deliverCommand = new DeliverCommand();
                        deliverCommand.setSpuOrderId(Long.valueOf(item.getSpuOrderId()));
                        deliverCommand.setExpressCompanyName(item.getExpressCompanyName());
                        deliverCommand.setExpressNo(item.getExpressNo());
                        DeliverItemCommand deliverItemCommand = new DeliverItemCommand();
                        deliverItemCommand.setSkuId(Long.valueOf(item.getSkuId()));
                        deliverCommand.setDeliverItemCommandList(Collections.singletonList(deliverItemCommand));
                        return deliverCommand;
                    }
                });
                deliverCreate(deliverCommand);
            }catch (PlatformException e){
                errorVO.addError(i + 2, e.getMessage());
            }catch (NumberFormatException e1){
                errorVO.addError(i + 2, "存在格式错误, 需为数字");
            }catch (Exception e2){
                errorVO.addError(i + 2, ExceptionUtil.getMessage(e2));
            }
        }
        return errorVO;
    }

    @Override
    public void closeOrder(String orderNo) {
        OrderAgg orderAgg = orderDomain.orderAgg(orderNo);
        OrderDTO order = orderAgg.getOrder();
        if (Objects.isNull(order)){
            log.error("超时关单，发现订单不存在:{}",orderNo);
            return;
        }
        Set<OrderEnum.State> allowModifyStates = OrderEnum.State.getMemberCancelOrderStates();
        if(!allowModifyStates.contains(order.getOrderState())){
            log.info("超时关单，订单状态已流转，不允许修改:{}",orderNo);
           return;
        }
        //关闭原因, SpuOrder 层折叠后直接落交易单扩展
        OrderExt orderExt = Optional.ofNullable(order.getOrderExt()).orElseGet(OrderExt::new);
        orderExt.setCancelReason("超时未支付关闭");
        orderExt.setCloseReason("买家超时未支付订单关闭");
        orderDomain.batchUpdateOrderState(Collections.singletonList(orderNo), order.getOrderState(), OrderEnum.State.CLOSE, orderExt);
        localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(order), order.getOrderState(), OrderEnum.State.CLOSE, AccountEnum.Identity.PLATFORM.getCode(), AccountEnum.Identity.PLATFORM);
    }

    public void doMemberPaySuccess(OrderAgg orderAgg) {
        if(orderAgg.getOrder().getGoodsAmount().greaterThanZero()){
            //如果选品金额大于0则进行渠道商支付
            OrderDTO order = orderAgg.getOrder();
            BalancePayReq balancePayReq = getBalancePayReq(order);
            BalancePayResult balancePayResult = payApi.balancePay(balancePayReq);
            if(BooleanUtil.isTrue(balancePayResult.isPayState())) {
                // 迁移: 原聚合根方法 orderAgg.channelPaySuccess(localMessageFacade, orderRepository) 平展到 application(去聚合根)
                channelPaySuccess(orderAgg);
            }else {
                ThrowsException.exception(BaseErrorCode.CUSTOM, "支付失败");
            }
        }else {
            //支付成功
            allPaySuccess(orderAgg);
        }
    }

    @Override
    public void orderBalancePay(String... orderNoList) {
        for (String orderNo : orderNoList) {
            OrderAgg orderAgg = orderDomain.orderAgg(orderNo);
            //检查状态
            BizUtil.checkInState(Collections.singletonList(OrderEnum.State.CHANNEL_WAIT_PAY.getCode()), orderAgg.getOrder().getOrderState().getCode(), OrderErrorCode.STATE_ERROR);
            recalculateServiceAmount(orderAgg);
            //扣减余额
            BalancePayReq balancePayReq = getBalancePayReq(orderAgg.getOrder());
            BalancePayResult balancePayResult = payApi.balancePay(balancePayReq);
            if (BooleanUtil.isTrue(balancePayResult.isPayState())) {
                channelPaySuccess(orderAgg);
            } else {
                ThrowsException.exception(OrderErrorCode.AMOUNT_LESS);
            }
        }
    }

    @Override
    public void orderBalancePay(Long... orderIdList) {
        for (Long orderId : orderIdList) {
            OrderAgg orderAgg = orderDomain.orderAgg(orderId);
            //检查状态
            BizUtil.checkInState(Collections.singletonList(OrderEnum.State.CHANNEL_WAIT_PAY.getCode()), orderAgg.getOrder().getOrderState().getCode(), OrderErrorCode.STATE_ERROR);
            recalculateServiceAmount(orderAgg);
            //扣减余额
            BalancePayReq balancePayReq = getBalancePayReq(orderAgg.getOrder());
            BalancePayResult balancePayResult = payApi.balancePay(balancePayReq);
            if (BooleanUtil.isTrue(balancePayResult.isPayState())) {
                channelPaySuccess(orderAgg);
            } else {
                ThrowsException.exception(OrderErrorCode.AMOUNT_LESS);
            }
        }
    }

    public void recalculateServiceAmount(OrderAgg orderAgg) {
        // 重新计算服务费
        OrderDTO order = orderAgg.getOrder();
        List<SkuOrderDTO> skuOrderList = orderAgg.getSkuOrderList();

        ChannelNowServiceFeeRes channelNowServiceFee = channelApi.queryNowServiceFee(order.getChannelId());
        skuOrderList.forEach(skuOrder -> skuOrder.buildServiceChange(channelNowServiceFee));

        Money newServiceAmount = skuOrderList.stream().map(SkuOrderDTO::getTotalServiceChange).reduce(Money.ZERO,Money::add);
        Money diffAmount = order.getServiceAmount().subtract(newServiceAmount);
        order.setServiceAmount(newServiceAmount);
        order.setTotalAmount(Money.of(Math.max(0, order.getTotalAmount().subtract(diffAmount).getCent())));

        orderDomain.orderAggSave(orderAgg);
    }

    /**
     * 全部支付成功
     */
    public void allPaySuccess(OrderAgg orderAgg) {
        // 状态修改
        OrderDTO order = orderAgg.getOrder();
        String orderNo = order.getOrderNo();
        if(orderAgg.isInit()){
            order.setOrderState(OrderEnum.State.SENDING);
            order.setOrderStateLog(OrderEnum.State.SENDING.toString());
            order.setPayTime(LocalDateTime.now());
            OrderSnapVO orderSnapVO = new OrderSnapVO();
            orderSnapVO.setOrderId(order.getId());
            order.setOrderSnapVO(orderSnapVO);
            for (SkuOrderDTO skuOrder : orderAgg.getSkuOrderList()) {
                skuOrder.setOrderState(OrderEnum.State.SENDING);
                skuOrder.setOrderStateLog(OrderEnum.State.SENDING.toString());
            }
        }else {
            OrderDTO orderEdit = new OrderDTO();
            orderEdit.setId(order.getId());
            orderEdit.setPayTime(LocalDateTime.now());
            orderRepository.orderSave(orderEdit);
            int count = orderRepository.batchUpdateOrderState(Arrays.asList(orderNo), OrderEnum.State.CHANNEL_WAIT_PAY, OrderEnum.State.SENDING, null);
            if (count < 1){
                ThrowsException.exception(BaseErrorCode.REPEAT);
            }

            //门店用户支付 FIXME
//            localMessageApi.storeAccountPay(order.getStoreId(), order.getAccountId(), order.getMemberAmount());

            orderRepository.batchUpdateSkuOrderState(Arrays.asList(orderNo), null, OrderEnum.State.SENDING);
            // 1、扣减库存
            OrderSnapVO orderSnapVO = order.getOrderSnapVO();

            // 2、请求第三方下单
            List<OrderSkuVO> outGoods = orderSnapVO.getOutGoods();
            if (!CollUtil.isEmpty(outGoods)) {
                // 获取供应商ID
                Long supplierId = outGoods.stream().findAny().get().getSupplierId();
                // 通过工厂获取策略
                // 执行下单
                ThirdPartyOrderResult result = ThirdPartyOrderProcessor.find().create(outGoods, order);
                // FIXME[outorder-removed]: 三方履约订单(OutOrder)持久化已删, 构建/落库/日志待后期以新履约模型替换
                // 构建外部订单（传入outGoods，供策略处理特有逻辑如outIds拼接）
                // List<OutOrder> outOrderList = strategy.buildOutOrders(result, order.getId(), outGoods);
                // strategy.saveOutOrdersLog(result, thirdPartyOrderService);
                // orderRepository.outOrderSave(outOrderList);
            }
        }
        localMessageApi.paySuccessNotify(orderAgg);
    }

    /**
     * 渠道商支付成功
     * 迁移: 原 OrderAgg.channelPaySuccess 聚合根方法平展到 application(去聚合根, 贫血模型)
     */
    public void channelPaySuccess(OrderAgg orderAgg) {
        OrderDTO order = orderAgg.getOrder();
        // 状态修改
        if(orderAgg.isInit()){
            order.setOrderState(OrderEnum.State.OPERATOR_WAIT_PAY);
            order.setOrderStateLog(OrderEnum.State.OPERATOR_WAIT_PAY.toString());
            OrderSnapVO orderSnapVO = new OrderSnapVO();
            orderSnapVO.setOrderId(order.getId());
            order.setOrderSnapVO(orderSnapVO);
            for (SkuOrderDTO skuOrder : orderAgg.getSkuOrderList()) {
                skuOrder.setOrderState(OrderEnum.State.OPERATOR_WAIT_PAY);
                skuOrder.setOrderStateLog(OrderEnum.State.OPERATOR_WAIT_PAY.toString());
            }
        } else {
            int count = orderRepository.batchUpdateOrderState(Collections.singletonList(order.getOrderNo()), OrderEnum.State.CHANNEL_WAIT_PAY, OrderEnum.State.OPERATOR_WAIT_PAY, null);
            if (count < 1){
                ThrowsException.exception(BaseErrorCode.REPEAT);
            }
            orderRepository.batchUpdateSkuOrderState(Collections.singletonList(order.getOrderNo()), null, OrderEnum.State.OPERATOR_WAIT_PAY);
        }
    }

    /**
     * 支付成功通知
     * 1. 仅通知选品的sku订单
     */
    private void paySuccessNotify(OrderAgg orderAgg) {
//        GoodsPaySuccessEvent goodsPaySuccessEvent = new GoodsPaySuccessEvent();
//        goodsPaySuccessEvent.setOrderId(orderAgg.getOrder().getId());
//        List<SkuOrderMessageVO> skuOrderMessageVOList = new ArrayList<>();
//        Map<Long, SpuOrderDTO> spuOrderMap = orderAgg.getSpuOrderList().stream().collect(Collectors.toMap(SpuOrderDTO::getSpuId, Function.identity()));
//        for (SkuOrderDTO skuOrder : orderAgg.getSkuOrderList()) {
//            SkuOrderMessageVO skuOrderMessageVO = TransferUtils.transfer(skuOrder,SkuOrderMessageVO.class);
//            skuOrderMessageVO.setChannelId(orderAgg.getOrder().getChannelId());
//            SpuOrderDTO spuOrder = spuOrderMap.get(skuOrder.getSpuId());
//            skuOrderMessageVO.setSpuChannelType(spuOrder.getSpuChannelType());
//            skuOrderMessageVOList.add(skuOrderMessageVO);
//        }
//        List<SpuOrderMessageVO> spuOrderMessageVOList = new ArrayList<>();
//        for (SpuOrderDTO spuOrder : orderAgg.getSpuOrderList()) {
//            SpuOrderMessageVO spuOrderMessageVO = TransferUtils.transfer(spuOrder,SpuOrderMessageVO.class);
//            spuOrderMessageVOList.add(spuOrderMessageVO);
//        }
//        goodsPaySuccessEvent.setSkuOrderList(skuOrderMessageVOList);
//        goodsPaySuccessEvent.setSpuOrderList(spuOrderMessageVOList);
//        //支付成功通知
//        orderRepository.goodsOrderPaySuccess(goodsPaySuccessEvent);
    }

    private BalancePayReq getBalancePayReq(OrderDTO order) {
        BalancePayReq balancePayReq = new BalancePayReq();
        balancePayReq.setAccountId(order.getChannelId());
        balancePayReq.setMemberId(order.getMemberId());
        balancePayReq.setAccountType(PurseEnum.User.CHANNEL);
        // 渠道商下单扣采购金, 科目必须显式传(BalancePayReq 已撤销默认值)
        balancePayReq.setPurseType(PurseEnum.Type.PURCHASE);
        balancePayReq.setPayAmount(order.getTotalAmount());
        balancePayReq.setGoodsAmount(order.getGoodsAmount());
        balancePayReq.setOrderNo(order.getId());
        balancePayReq.setOperatorId(order.getOperatorId());
        balancePayReq.setOrderInfo("交易单信息:" + JSONObject.toJSONString(order));
        return balancePayReq;
    }

    @Override
    public List<OrderStateCountVO> countOrderState(OrderQuery orderQuery) {
        Map<OrderEnum.State, Integer> countMap = orderRepository.stateCountMap(orderQuery);
        List<OrderStateCountVO> orderStateCountList = new ArrayList<>();
        countMap.forEach((state, count) -> {
            OrderStateCountVO orderStateCountVO = new OrderStateCountVO();
            orderStateCountVO.setOrderState(state);
            orderStateCountVO.setCount(count);
        });

        Integer refundingCount = refundRepository.countTotalRefunding(orderQuery);

        OrderStateCountVO refundingStateVO = new OrderStateCountVO();
        refundingStateVO.setOrderState(OrderEnum.State.REFUNDING);
        refundingStateVO.setCount(refundingCount);

        orderStateCountList.add(refundingStateVO);
        return orderStateCountList;
    }
}
