package com.newzkl.platform.base.biz.order.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.newzkl.platform.base.biz.order.application.service.ICommitOrder;
import com.newzkl.platform.base.biz.order.application.service.IOrderService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsSpuApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.OperatorApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.*;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.res.CompleteSkuOrderRes;
import com.newzkl.platform.base.biz.order.model.res.DeliverRes;
import com.newzkl.platform.base.biz.order.model.res.ReceiveSkuOrderRes;
import com.newzkl.platform.base.biz.order.model.support.api.ChannelNowServiceFeeRes;
import com.newzkl.platform.base.biz.order.model.support.api.SkuSaleInfo;
import com.newzkl.platform.base.biz.order.model.support.api.order.GoodsVO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckRes;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderSkuVO;
import com.newzkl.platform.base.biz.order.model.support.api.spu.InventoryExecuteReq;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.constant.OrderErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/129:45
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OperatorApi operatorFacade;

    private final IOrderDomain orderDomain;

    private final GoodsSpuApi spuFacade;

    private final IOrderRepository orderRepository;

    private final IBalancePayApi balancePayApi;

    private final ISettleDomain settleDomain;

    private final ICommitOrder commitOrder;

    private final IOrderGoodsFacade orderGoodsFacade;

    private final ThirdPartyOrderService thirdPartyOrderService;

    @Override
    public void receiveSkuOrder(Long spuOrderId, List<Long> skuOrderIdList) {
        // 收货
        ReceiveSkuOrderRes receiveSkuOrderRes = orderDomain.receiveSkuOrder(spuOrderId, skuOrderIdList);
        // 结算
        if(ObjectUtil.isNotEmpty(receiveSkuOrderRes.getWaitSettlementOrder())){
            List<SettleOrderWaitCommand> commandList = TransferUtils.transfers(receiveSkuOrderRes.getWaitSettlementOrder(), SettleOrderWaitCommand::new, (c, v)->{
                v.setSkuOrderId(c.getId());
                v.setOrderMoney(c.getSupplierAmount());
                v.setSkuCount(c.getCount());
                v.setType(0);
            });
            settleDomain.settleOrderWaitSave(commandList,0);
        }
    }
    @Override
    public void completeSkuOrder(Long spuOrderId, List<Long> skuOrderIdList) {
        // 完成
        CompleteSkuOrderRes completeSkuOrderRes = orderDomain.completeSkuOrder(spuOrderId, skuOrderIdList);
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
    public Integer channelRealOrderFreight(OrderCreateCommand orderCreateCommand){
        // 1、获取用户信息 包含渠道商id、运营商id
        // 2、获取商品信息、校验商品状态、库存并返回运费
        List<OrderItemCommand> orderGoodsList = orderCreateCommand.getOrderGoodsList();
        List<GoodsVO> goodsList = commitOrder.buildGoodsVO(orderGoodsList);
        PlatformResult<OrderGoodsCheckRes> result = orderGoodsFacade.orderGoodsCheck(commitOrder.buildOrderGoodsCheckReq(orderCreateCommand, new MemberOrderCreateCommand()), goodsList);
        Map<Long, Integer> goodsFreight = result.getData().getGoodsFreight();
        Integer amount = goodsFreight.values().stream().mapToInt(Integer::intValue).sum();
        return amount;
    }

    @Override
    public void memberPaySuccess(Long orderId) {
        OrderAgg orderAgg = orderRepository.orderAgg(orderId);
        doMemberPaySuccess(orderAgg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deliverCreate(DeliverCommand deliverCommand) {
        //发货
        DeliverRes deliverRes = orderDomain.deliverCreate(deliverCommand);
        SpuOrderVO spuOrderVO = deliverRes.getSpuOrderVO();
        //发货开发者通知
        List<SkuCountDTO> skuCountDTOList = OrderUtil.deliverItemCommand2SkuCountDTO(deliverCommand.getDeliverItemCommandList());
        orderRepository.deliverNotify(spuOrderVO.getOutOrderNo(), skuCountDTOList, deliverCommand.getExpressCompanyName(), deliverCommand.getExpressNo(), spuOrderVO.getChannelId());
        //结算运费
        if(CommonEnum.YesOrNo.NO == spuOrderVO.getSettleSendState() && spuOrderVO.getSupplierId() > 100L){
            FreightSettleOrderWaitCommand freightSettleOrderWaitCommand = new FreightSettleOrderWaitCommand();
            freightSettleOrderWaitCommand.setSpuOrderId(spuOrderVO.getId());
            freightSettleOrderWaitCommand.setSpuId(spuOrderVO.getSpuId());
            freightSettleOrderWaitCommand.setSupplierId(spuOrderVO.getSupplierId());
            freightSettleOrderWaitCommand.setAmount(spuOrderVO.getFreightAmount());
            if(!freightSettleOrderWaitCommand.getAmount().equals(0)){
                settleDomain.freightSettleOrderWaitSave(Collections.singletonList(freightSettleOrderWaitCommand),
                        orderRepository.settleOrderType(spuOrderVO.getSupplierId()));
            }
            orderDomain.freightSettleSuccessNotify(Collections.singletonList(spuOrderVO.getId()));
        }
        SpuOrder spuOrder = new SpuOrder();
        BeanUtils.copyProperties(spuOrderVO, spuOrder);
        orderRepository.sendOrderNewRecordEvent(Collections.singletonList(spuOrder), spuOrder.getOrderState(),OrderEnum.State.WAIT_RECEIVE, SecurityUtils.getAccountId(),SecurityUtils.getRole());
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
    public Map<Integer, Integer> spuOrderStateCountMap(SpuOrderQuery spuOrderQuery) {
        List<Integer> orderStateList = Opt.ofEmptyAble(spuOrderQuery.getOrderStateList()).orElse(ArrayUtil.map(OrderEnum.State.values(), OrderEnum.State::getCode));
        CollUtil.addIfAbsent(orderStateList, spuOrderQuery.getOrderState());
        spuOrderQuery.setOrderState(null);

        Integer type = spuOrderQuery.getType();
        // Q3 越权收敛: 原口径 searchSupplierIdList = 请求 ∪ 可见(UNION), 运营商传任意 supplierId 即可越权查非可见范围。
        // 收敛为 可见 ∩ 请求(INTERSECT), 与 OrderController.appendSpuOrderQuery OPERATOR 分支口径一致:
        // 有请求则可见范围按请求过滤; 无请求则用全部可见范围; 交集为空则不查(返回全 0)。
        List<Long> supplierIdList = operatorFacade.supplierIdListByType(SecurityUtils.getAccountId(), type);
        List<Long> searchSupplierIdList = Opt.ofNullable(spuOrderQuery.getSupplierIdList()).orElse(new ArrayList<>());
        Collection<Long> reqIdList = CollUtil.addAll(searchSupplierIdList, spuOrderQuery.getSupplierId());
        if (CollUtil.isNotEmpty(reqIdList)) {
            supplierIdList = supplierIdList.stream().filter(reqIdList::contains).collect(Collectors.toList());
        }
        Map<Integer, Integer> countMap = new HashMap<>();

        if (CollUtil.isNotEmpty(supplierIdList)) {
            spuOrderQuery.setSupplierIdList(supplierIdList);
            countMap.putAll(orderRepository.stateCountMap(spuOrderQuery));
        }

        orderStateList.forEach(orderState -> {
            if (!countMap.containsKey(orderState)) {
                countMap.put(orderState, 0);
            }
        });
        return countMap;
    }

    @Override
    public void closeOrder(Long orderId) {
        OrderAgg orderAgg = orderDomain.orderAgg(orderId);
        Order order = orderAgg.getOrder();
        if (Objects.isNull(order)){
            log.error("超时关单，发现订单不存在:{}",orderId);
            return;
        }
        Set<OrderEnum.State> allowModifyStates = OrderEnum.State.getMemberCancelOrderStates();
        if(!allowModifyStates.contains(order.getOrderState())){
            log.info("超时关单，订单状态已流转，不允许修改:{}",orderId);
           return;
        }
        //关闭原因，现在仅有一个spu是可以这么写的
        SpuOrderExt spuOrderExt;
        List<SpuOrder> spuOrderList = orderAgg.getSpuOrderList();
        if (CollUtil.isNotEmpty(spuOrderList)){
             spuOrderExt = spuOrderList.get(0).getSpuOrderExt();
        }else {
            spuOrderExt = new SpuOrderExt();
        }
        if (Objects.isNull(spuOrderExt)){
            spuOrderExt = new SpuOrderExt();
        }
        spuOrderExt.setCancelReason("超时未支付关闭");
        spuOrderExt.setCloseReason("买家超时未支付订单关闭");
        orderDomain.batchUpdateOrderState(Collections.singletonList(order.getId()), order.getOrderState(), OrderEnum.State.CLOSE, JSONObject.toJSONString(spuOrderExt));
        orderRepository.sendOrderNewRecordEvent(orderAgg.getSpuOrderList(), order.getOrderState(), OrderEnum.State.CLOSE, RoleEnum.CompanyRole.PLATFORM,RoleEnum.CompanyRole.PLATFORM);
    }

    public void doMemberPaySuccess(OrderAgg orderAgg) {
        if(orderAgg.getOrder().getGoodsAmount() > 0){
            //如果选品金额大于0则进行渠道商支付
            Order order = orderAgg.getOrder();
            BalancePayReq balancePayReq = getBalancePayReq(order);
            BalancePayResult balancePayResult = balancePayApi.balancePay(balancePayReq);
            if(BooleanUtil.isTrue(balancePayResult.isOperatorPayState())){
                //orderAgg.allPaySuccess(localMessageFacade, orderRepository);
            }else {
                if(BooleanUtil.isTrue(balancePayResult.isPayState())) {
                    // 迁移: 原聚合根方法 orderAgg.channelPaySuccess(localMessageFacade, orderRepository) 平展到 application(去聚合根)
                    channelPaySuccess(orderAgg);
                }else {
                    ThrowsException.exception(BaseErrorCode.CUSTOM, "支付失败");
                }
            }
        }else {
            //支付成功
            //orderAgg.allPaySuccess(localMessageFacade, orderRepository);
        }
    }
    @Override

    public void orderBalancePay(Long... idList) {
        for (Long id : idList) {
            OrderAgg orderAgg = orderRepository.orderAgg(id);
            //检查状态
            ScmUtil.checkInState(Collections.singletonList(OrderEnum.State.CHANNEL_WAIT_PAY), orderAgg.getOrder().getOrderState(), OrderErrorCode.STATE_ERROR);
            //可能会发生服务费变化 MQ TODO
            recalculateServiceAmount(orderAgg);
            //扣减余额
            BalancePayReq balancePayReq = getBalancePayReq(orderAgg.getOrder());
            BalancePayResult balancePayResult = balancePayApi.balancePay(balancePayReq);
            if (BooleanUtil.isTrue(balancePayResult.isOperatorPayState())) {
                allPaySuccess(orderAgg);
                if (Objects.isNull(SecurityUtils.getAccountId())){
                    orderRepository.sendOrderNewRecordEvent(orderAgg.getSpuOrderList(), OrderEnum.State.CHANNEL_WAIT_PAY,OrderEnum.State.SENDING,RoleEnum.CompanyRole.PLATFORM,RoleEnum.CompanyRole.PLATFORM);
                }else {
                    orderRepository.sendOrderNewRecordEvent(orderAgg.getSpuOrderList(), OrderEnum.State.CHANNEL_WAIT_PAY,OrderEnum.State.SENDING,SecurityUtils.getAccountId(),SecurityUtils.getRole());
                }
            } else {
                if (RoleEnum.CompanyRole.OPERATOR == SecurityUtils.getRole()) {
                    ThrowsException.exception(OrderErrorCode.AMOUNT_LESS);
                }
                if (BooleanUtil.isTrue(balancePayResult.isPayState())) {
                    // 迁移: 原聚合根方法 orderAgg.channelPaySuccess(orderRepository) 平展到 application(去聚合根)
                    channelPaySuccess(orderAgg);
                } else {
                    ThrowsException.exception(OrderErrorCode.AMOUNT_LESS);
                }
            }
        }
    }

    public void recalculateServiceAmount(OrderAgg orderAgg) {
        // 重新计算服务费
        Order order = orderAgg.getOrder();
        List<SkuOrder> skuOrderList = orderAgg.getSkuOrderList();
        List<SpuOrder> spuOrderList = orderAgg.getSpuOrderList();

        ChannelNowServiceFeeRes channelNowServiceFee = orderRepository.queryChannelNowServiceFee(order.getChannelId());
        skuOrderList.forEach(skuOrder -> skuOrder.buildServiceChange(channelNowServiceFee));
        spuOrderList.forEach(spuOrder ->
                spuOrder.setServiceAmount(skuOrderList.stream()
                        .filter(it -> it.getSpuOrderId().equals(spuOrder.getId()))
                        .mapToInt(SkuOrder::getTotalServiceChange).sum())
        );

        int newServiceAmount = spuOrderList.stream().mapToInt(SpuOrder::getServiceAmount).sum();
        int diffAmount = order.getServiceAmount() - newServiceAmount;
        order.setServiceAmount(newServiceAmount);
        order.setTotalAmount(Math.max(0, order.getTotalAmount() - diffAmount));

        orderRepository.orderAggUpdate(orderAgg);
    }

    /**
     * 全部支付成功
     */
    public void allPaySuccess(OrderAgg orderAgg) {
        // 状态修改
        Order order = orderAgg.getOrder();
        if(orderAgg.isInit()){
            order.setOrderState(OrderEnum.State.SENDING);
            order.setOrderStateLog(OrderEnum.State.SENDING.toString());
            order.setPayTime(LocalDateTime.now());
            OrderSnapVO orderSnapVO = new OrderSnapVO();
            orderSnapVO.setOrderId(order.getId());
            order.setOrderSnapVO(orderSnapVO);
            for (SpuOrder spuOrder : orderAgg.getSpuOrderList()) {
                spuOrder.setOrderState(OrderEnum.State.SENDING);
                spuOrder.setOrderStateLog(OrderEnum.State.SENDING.toString());
            }
            for (SkuOrder skuOrder : orderAgg.getSkuOrderList()) {
                skuOrder.setOrderState(OrderEnum.State.SENDING);
                skuOrder.setOrderStateLog(OrderEnum.State.SENDING.toString());
            }
        }else {
            Order orderEdit = new Order();
            orderEdit.setId(order.getId());
            orderEdit.setPayTime(LocalDateTime.now());
            orderRepository.orderEdit(orderEdit);
            int count = orderRepository.batchUpdateOrderState(Arrays.asList(order.getId()), OrderEnum.State.CHANNEL_WAIT_PAY, OrderEnum.State.SENDING);
            if (count < 1){
                ThrowsException.exception(BaseErrorCode.REPEAT);
            }

            //门店用户支付
            orderRepository.storeAccountPay(order.getStoreId(), order.getAccountId(), order.getMemberAmount());

            orderRepository.batchUpdateSpuOrderStateByOrderId(Arrays.asList(order.getId()), null, OrderEnum.State.SENDING, null);
            orderRepository.batchUpdateSkuOrderStateByOrderId(Arrays.asList(order.getId()), null, OrderEnum.State.SENDING);
            // 1、扣减库存
            OrderSnapVO orderSnapVO = order.getOrderSnapVO();
            spuFacade.inventoryExecute(getCutInventoryExecuteReq(orderSnapVO.getLocalGoods()));
            // 2、请求第三方下单
            List<OrderSkuVO> outGoods = orderSnapVO.getOutGoods();
            if (!CollUtil.isEmpty(outGoods)) {
                // 获取供应商ID
                Long supplierId = outGoods.stream().findAny().get().getSupplierId();
                // 通过工厂获取策略
                ThirdPartyOrderStrategy strategy = ThirdPartyOrderStrategyFactory.getStrategy(supplierId);
                if (strategy == null) {
                    ThrowsException.exception(BaseErrorCode.BUSY, "不支持的供应商ID：" + supplierId);
                }
                // 执行下单
                ThirdPartyOrderResult result = strategy.createOrder(outGoods, order);
                // 构建外部订单（传入outGoods，供策略处理特有逻辑如outIds拼接）
                List<OutOrder> outOrderList = strategy.buildOutOrders(result, order.getId(), outGoods);
                strategy.saveOutOrdersLog(result, thirdPartyOrderService);
                orderRepository.outOrderSave(outOrderList);
            }
        }
        paySuccessNotify(orderAgg);
    }

    /**
     * 渠道商支付成功
     * 迁移: 原 OrderAgg.channelPaySuccess 聚合根方法平展到 application(去聚合根, 贫血模型)
     */
    public void channelPaySuccess(OrderAgg orderAgg) {
        Order order = orderAgg.getOrder();
        // 状态修改
        if(orderAgg.isInit()){
            order.setOrderState(OrderEnum.State.OPERATOR_WAIT_PAY);
            order.setOrderStateLog(OrderEnum.State.OPERATOR_WAIT_PAY.toString());
            OrderSnapVO orderSnapVO = new OrderSnapVO();
            orderSnapVO.setOrderId(order.getId());
            order.setOrderSnapVO(orderSnapVO);
            for (SpuOrder spuOrder : orderAgg.getSpuOrderList()) {
                spuOrder.setOrderState(OrderEnum.State.OPERATOR_WAIT_PAY);
                spuOrder.setOrderStateLog(OrderEnum.State.OPERATOR_WAIT_PAY.toString());
            }
            for (SkuOrder skuOrder : orderAgg.getSkuOrderList()) {
                skuOrder.setOrderState(OrderEnum.State.OPERATOR_WAIT_PAY);
                skuOrder.setOrderStateLog(OrderEnum.State.OPERATOR_WAIT_PAY.toString());
            }
        } else {
            int count = orderRepository.batchUpdateOrderState(Arrays.asList(order.getId()), OrderEnum.State.CHANNEL_WAIT_PAY, OrderEnum.State.OPERATOR_WAIT_PAY);
            if (count < 1){
                ThrowsException.exception(BaseErrorCode.REPEAT);
            }
            orderRepository.batchUpdateSpuOrderStateByOrderId(Arrays.asList(order.getId()), null, OrderEnum.State.OPERATOR_WAIT_PAY, null);
            orderRepository.batchUpdateSkuOrderStateByOrderId(Arrays.asList(order.getId()), null, OrderEnum.State.OPERATOR_WAIT_PAY);
        }
    }

    /**
     * 支付成功通知
     * 1. 仅通知选品的sku订单
     */
    private void paySuccessNotify(OrderAgg orderAgg) {
        GoodsPaySuccessEvent goodsPaySuccessEvent = new GoodsPaySuccessEvent();
        goodsPaySuccessEvent.setOrderId(orderAgg.getOrder().getId());
        List<SkuOrderMessageVO> skuOrderMessageVOList = new ArrayList<>();
        Map<Long, SpuOrder> spuOrderMap = orderAgg.getSpuOrderList().stream().collect(Collectors.toMap(SpuOrder::getSpuId, Function.identity()));
        for (SkuOrder skuOrder : orderAgg.getSkuOrderList()) {
            SkuOrderMessageVO skuOrderMessageVO = OrderUtil.skuOrder2SkuOrderEarningsVO(skuOrder);
            skuOrderMessageVO.setChannelId(orderAgg.getOrder().getChannelId());
            SpuOrder spuOrder = spuOrderMap.get(skuOrder.getSpuId());
            skuOrderMessageVO.setSpuChannelType(spuOrder.getSpuChannelType());
            skuOrderMessageVOList.add(skuOrderMessageVO);
        }
        List<SpuOrderMessageVO> spuOrderMessageVOList = new ArrayList<>();
        for (SpuOrder spuOrder : orderAgg.getSpuOrderList()) {
            SpuOrderMessageVO spuOrderMessageVO = OrderUtil.spuOrderPaySuccess(spuOrder);
            spuOrderMessageVOList.add(spuOrderMessageVO);
        }
        goodsPaySuccessEvent.setSkuOrderList(skuOrderMessageVOList);
        goodsPaySuccessEvent.setSpuOrderList(spuOrderMessageVOList);
        //支付成功通知
        orderRepository.goodsOrderPaySuccess(goodsPaySuccessEvent);
    }

    private InventoryExecuteReq getCutInventoryExecuteReq(OrderCreateCommand orderCreateCommand, Map<Long, SkuSaleInfo> skuInfoMap) {
        InventoryExecuteReq inventoryExecuteReq = new InventoryExecuteReq();
        inventoryExecuteReq.setType(0);
        List<InventoryExecuteReq.Sku> skuList = new ArrayList<>();
        //约束:只计算实物的库存
        for (OrderItemCommand orderItemCommand : orderCreateCommand.getOrderGoodsList()) {
            SkuSaleInfo skuSaleInfo = skuInfoMap.get(orderItemCommand.getSkuId());
            if(skuSaleInfo != null && SpuEnum.SaleType.REAL == skuSaleInfo.getSpuSaleType()){
                InventoryExecuteReq.Sku sku = new InventoryExecuteReq.Sku();
                sku.setId(orderItemCommand.getSkuId());
                sku.setCount(orderItemCommand.getCount());
                skuList.add(sku);
            }
        }
        inventoryExecuteReq.setSkuList(skuList);
        return inventoryExecuteReq;
    }


    private BalancePayReq getBalancePayReq(Order order) {
        BalancePayReq balancePayReq = new BalancePayReq();
        balancePayReq.setAccountId(order.getChannelId());
        balancePayReq.setMemberId(order.getMemberId());
        balancePayReq.setAccountType(FinanceEnum.FinanceUser.CHANNEL);
        balancePayReq.setPayAmount(order.getTotalAmount());
        balancePayReq.setGoodsAmount(order.getGoodsAmount());
        balancePayReq.setOrderNo(order.getId());
        balancePayReq.setOperatorId(order.getOperatorId());
        balancePayReq.setOrderInfo("交易单信息:" + JSONObject.toJSONString(order));
        return balancePayReq;
    }

    private BalancePayReq getOperatorBalancePayReq(Order order) {
        BalancePayReq balancePayReq = new BalancePayReq();
        balancePayReq.setAccountId(order.getOperatorId());
        balancePayReq.setOperatorId(order.getOperatorId());
        balancePayReq.setMemberId(order.getMemberId());
        balancePayReq.setAccountType(FinanceEnum.FinanceUser.OPERATOR);
        balancePayReq.setPayAmount(order.getTotalAmount());
        balancePayReq.setGoodsAmount(order.getGoodsAmount());
        balancePayReq.setOrderNo(order.getId());
        balancePayReq.setPurseType(FinanceEnum.PurseType.PURCHASE.getType());
        balancePayReq.setOrderInfo("交易单信息:" + JSONObject.toJSONString(order));
        return balancePayReq;
    }
}
