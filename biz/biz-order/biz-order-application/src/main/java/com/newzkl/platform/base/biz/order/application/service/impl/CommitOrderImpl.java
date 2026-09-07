package com.newzkl.platform.base.biz.order.application.service.impl;
import com.newzkl.platform.base.biz.order.application.service.OrderService;
import com.newzkl.platform.base.common.ddd.facade.HuiFuPurseInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.newzkl.platform.base.biz.order.application.service.CommitOrder;
import com.newzkl.platform.base.biz.order.application.service.QueryService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.*;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.OrderDomain;
import com.newzkl.platform.base.biz.order.domain.service.ShipAddressDomain;
import com.newzkl.platform.base.biz.order.model.dto.OrderAgg;
import com.newzkl.platform.base.biz.order.model.dto.OrderDTO;
import com.newzkl.platform.base.biz.order.model.dto.SkuOrderDTO;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.biz.order.model.res.ShipAddressRes;
import com.newzkl.platform.base.common.ddd.facade.OrderPayReq;
import com.newzkl.platform.base.common.ddd.facade.PayBaseResult;
import com.newzkl.platform.base.biz.order.model.support.api.order.*;
import com.newzkl.platform.base.common.ddd.facade.*;
import com.newzkl.platform.base.biz.order.model.vo.OrderAggVO;
import com.newzkl.platform.base.biz.order.model.vo.OrderVO;
import com.newzkl.platform.base.common.ddd.model.vo.ShipVO;
import com.newzkl.platform.base.biz.order.model.vo.SkuOrderVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.redis.aspect.DistributedLock;
import com.newzkl.platform.base.common.ddd.model.enums.finance.PaymentEnum;
import com.newzkl.platform.base.common.ddd.model.enums.store.StoreStyleEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeGenerator;
import com.newzkl.platform.base.common.ddd.facade.ModelShopOutVO;
import com.newzkl.platform.base.common.ddd.facade.StoreGoodsDetailOutVO;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author niu
 * @description:
 * @date 2024/5/7 10:25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommitOrderImpl implements CommitOrder {


    private final OrderDomain orderDomain;
    private final OrderService orderService;
    private final GoodsApi goodsApi;
    private final PayApi orderPayApi;
    private final QueryService queryService;
    private final PurseApi accountPurseApi;
    private final OrderRepository orderRepository;
    private final ShipAddressDomain shipAddressDomain;
    private final LocalMessageApi localMessageApi;

    @Override
    public OrderCreateRes commitOrder(OrderCreateCommand orderCreateCommand,
                                      MemberOrderCreateCommand memberOrderCreateCommand) {
        // 1、获取用户信息 包含渠道商id、运营商id
        // 2、获取商品信息、校验商品状态、库存并返回运费
        List<OrderItemCommand> orderGoodsList = orderCreateCommand.getOrderGoodsList();
        List<GoodsVO> goodsList = buildGoodsVO(orderGoodsList);
        PlatformResult<OrderGoodsCheckRes> result = goodsApi.orderCheck(buildOrderGoodsCheckReq(orderCreateCommand, memberOrderCreateCommand), goodsList);
        if (!result.isSuccess()) {
            throw new RuntimeException(result.getMessage());
        }
        // 3、根据货源拆分订单数据，若是外部供应链商品则异步请求运费
        OrderGoodsCheckRes data = result.getData();
        data.getGoodsInfo().forEach(goodsInfo -> {
            goodsInfo.setStorePrice(goodsInfo.getSalePrice());
            goodsInfo.setNum(goodsInfo.getNum() == null? 1:goodsInfo.getNum());
        });
        // 4、生成预支付订单
        OrderCreateRes order = orderDomain.createOrder(data, orderCreateCommand, memberOrderCreateCommand);
        // 若是C端用户下单则缓存并返回预支付单订单信息
        if(OrderEnum.OrderType.MEMBER == orderCreateCommand.getOrderType()){
            orderRepository.savePrePayOrder(order, memberOrderCreateCommand);
            return order;
        }else if (OrderEnum.OrderType.CHANNEL == orderCreateCommand.getOrderType()){
            orderDomain.orderAggSave(order.getOrderAgg());
            // 7、后续扣减库存、扣减采购金、外部供应链订单请求创建订单放在支付后的异步处理中
            orderService.orderBalancePay(order.getOrderNo());
        }
        return order;
    }

    @Override

    public OrderCreateRes createMemberPrePayOrder(OrderCreateCommand orderCreateCommand,
                                                  MemberOrderCreateCommand memberOrderCreateCommand) {
        return doMemberPrePayOrder(orderCreateCommand, memberOrderCreateCommand);
    }

    /**
     * 构建SKU订单（保留核心逻辑，简化冗余setter，优化空值处理）
     */
    private SkuOrderDTO buildSkuOrder(OrderGoodsInfoVO goodsInfo, Long spuOrderId, String orderNo, LocalDateTime createTime) {
        SkuOrderDTO skuOrder = new SkuOrderDTO();
        // 基础字段批量赋值（减少冗余行）
        skuOrder.setId(SnowflakeGenerator.getSnowflakeId());
        skuOrder.setOrderNo(orderNo);
        skuOrder.setSpuId(goodsInfo.getSpuId());
        skuOrder.setSkuId(goodsInfo.getSkuId());
        skuOrder.setCount(goodsInfo.getNum());
        skuOrder.setCreateTime(createTime);
        // 空值兜底简化
        skuOrder.setOutSkuId(StrUtil.isEmpty(goodsInfo.getOutSkuId()) ? "0" : goodsInfo.getOutSkuId());

        // 金额计算：简化条件判断，语义化常量
        SpuEnum.ChannelType channelType = goodsInfo.getSpuChannelType();
        if (SpuEnum.ChannelType.SELECTION == channelType
                || SpuEnum.ChannelType.OUT == channelType) {
            skuOrder.setGoodsAmount(goodsInfo.getSalePrice().multiply(goodsInfo.getNum()));
            skuOrder.setSupplierAmount(goodsInfo.getSupplyPrice().multiply(goodsInfo.getNum()));
            skuOrder.setStoreAmount(goodsInfo.getStorePrice().multiply(goodsInfo.getNum()));
        } else {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }

        // 固定值字段集中赋值
        skuOrder.setFreightAmount(Money.ZERO);
        skuOrder.setDiscountAmount(Money.ZERO);
        skuOrder.setOrderState(OrderEnum.State.NEW);
        skuOrder.setOrderStateLog(OrderEnum.State.NEW.toString());
        skuOrder.setSupplierId(goodsInfo.getSupplierId());
        skuOrder.setDeliverCount(0);
        skuOrder.setRefundedCount(0);
        skuOrder.setRefundingCount(0);

        // 商品信息字段赋值
        skuOrder.setSkuImg(goodsInfo.getImg());
        skuOrder.setSpuChannelType(channelType);
        skuOrder.setSpuName(goodsInfo.getSpuName());
        skuOrder.setSpuImg(goodsInfo.getSpuImg());
        skuOrder.setSkuSaleAttribute(goodsInfo.getSaleAttributeJson());
        skuOrder.setSkuWeight(goodsInfo.getWeight().doubleValue());
        skuOrder.setSkuVolume(goodsInfo.getVolume().doubleValue());
        skuOrder.setSkuSalePrice(goodsInfo.getSalePrice());
        skuOrder.setSkuSupplierPrice(goodsInfo.getSupplyPrice());
        skuOrder.setSkuStorePrice(goodsInfo.getStorePrice());
        return skuOrder;
    }

    @Override
    public OrderCreateRes getOrderCreateResByRedis(MemberOrderCreateCommand memberOrderCreateCommand) {
        return orderDomain.getPrePayOrder(
                new MemberOrderCreateCommand(memberOrderCreateCommand.getStoreId(), memberOrderCreateCommand.getAccountId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderCreateRes createOrderAgain(List<String> orderNoList) {
        for (String orderNo : orderNoList) {
            OrderAggVO orderAggVO = queryService.orderAggVO(orderNo);
            OrderVO orderVO = orderAggVO.getOrderVO();
            List<SkuOrderVO> skuOrderList = orderAggVO.getSkuOrderList();
            if (orderVO == null){
                ThrowsException.exception(BaseErrorCode.NODATA);
            }
            OrderCreateCommand orderCreateCommand = new OrderCreateCommand();
            orderCreateCommand.setOrderType(orderVO.getOrderType());
            orderCreateCommand.setShipVO(JSON.parseObject(orderVO.getShipVO(), ShipVO.class));
            orderCreateCommand.setChannelId(orderVO.getChannelId());
            List<OrderItemCommand> orderGoodsList = new ArrayList<>();
            skuOrderList.forEach(skuOrder -> {
                StoreGoodsDetailOutVO storeGoodsRpcVO = goodsApi.selectBySkuId(orderVO.getChannelId(), orderVO.getStoreId(), skuOrder.getSkuId());
                orderGoodsList.add(new OrderItemCommand(storeGoodsRpcVO.getId(), skuOrder.getSkuId(), skuOrder.getCount()));
            });
            orderCreateCommand.setOrderGoodsList(orderGoodsList);
            orderCreateCommand.setOperatorId(SecurityUtils.getAccountId());
            MemberOrderCreateCommand memberOrderCreateCommand = new MemberOrderCreateCommand();
            memberOrderCreateCommand.setStoreId(orderVO.getStoreId());
            memberOrderCreateCommand.setAccountId(SecurityUtils.getAccountId());
            memberOrderCreateCommand.setUserName(SecurityUtils.getUsername());
            memberOrderCreateCommand.setNickName(SecurityUtils.getNickName());
            return doMemberPrePayOrder(orderCreateCommand, memberOrderCreateCommand);
        }
        return null;
    }

    /**
     * 执行C端用户订单提交流程
     */
    public OrderCreateRes doMemberPrePayOrder(OrderCreateCommand orderCreateCommand, MemberOrderCreateCommand memberOrderCreateCommand) {
        List<OrderItemCommand> orderGoodsList = orderCreateCommand.getOrderGoodsList();
        if (CollUtil.isEmpty(orderGoodsList)) {
            ThrowsException.exception(BaseErrorCode.PARAM, "订单商品不能为空");
        }
        List<GoodsVO> goodsList = orderGoodsList.stream()
                .map(item -> {
                    GoodsVO goodsVO = new GoodsVO();
                    goodsVO.setStoreGoodsId(item.getStoreGoodsId());
                    goodsVO.setSkuId(item.getSkuId());
                    goodsVO.setNum(item.getCount());
                    return goodsVO;
                }).collect(Collectors.toList());

        // 可支付校验
        Long channelId = orderCreateCommand.getChannelId();
        HuiFuPurseInfo huiFuPurseInfo = accountPurseApi.queryHuiFuPurse(channelId);
        String huifuId = huiFuPurseInfo.getHuifuId();
        orderCreateCommand.setBenefitTripartiteId(huifuId);
        ShipVO shipVO = orderCreateCommand.getShipVO();
        if (shipVO.getId() != null){
            // 收货地址并入订单域后由 ShipAddressDomain 同域查询, 不再经账户域 facade 出站
            ShipAddressRes shipAddressRes = shipAddressDomain.detail(shipVO.getId());
            if (Objects.isNull(shipAddressRes)){
                ThrowsException.exception(BaseErrorCode.PARAM, "收货地址不存在");
            }
            BeanUtils.copyProperties(shipAddressRes, shipVO);
            shipVO.setShipPhone(shipAddressRes.getShipPhone() == null ? StrUtil.EMPTY : shipAddressRes.getShipPhone().toString());
        }
        // 商品校验

        OrderGoodsCheckReq checkReq = new OrderGoodsCheckReq();
        checkReq.setChannelId(orderCreateCommand.getChannelId())
                .setStoreId(memberOrderCreateCommand.getStoreId())
                .setShipProvinceCode(shipVO.getShipProvinceCode())
                .setShipCityCode(shipVO.getShipCityCode())
                .setShipAreaCode(shipVO.getShipAreaCode())
                .setShipArea(shipVO.getShipArea());

        PlatformResult<OrderGoodsCheckV2Res> checkResult = goodsApi.orderCheckV2(checkReq, goodsList);
        if (!checkResult.isSuccess()) {
            ThrowsException.exception(BaseErrorCode.PARAM, checkResult.getMessage());
        }

        // 生成订单
        OrderGoodsCheckV2Res checkData = checkResult.getData();
        StoreGoodsDetailRpcVO storeGoodsDetailRpcVO = checkData.getGoodsInfo().get(0);
        memberOrderCreateCommand.setStoreId(storeGoodsDetailRpcVO.getStoreId());
        memberOrderCreateCommand.setAccountId(SecurityUtils.getAccountId());
        orderCreateCommand.setChannelId(storeGoodsDetailRpcVO.getChannelId());
        //  创建订单 + 保存预支付订单
        OrderCreateRes order = orderDomain.createOrder(checkData, orderCreateCommand);
        orderDomain.savePrePayOrder(order, memberOrderCreateCommand);
        return order;
    }

    @Override
    @DistributedLock("'memberRefund:' + #consumerPaymentCommand.accountId + ':' + #consumerPaymentCommand.storeId")
    public OrderAgg commitMemberPrePayOrder(CommitMemberOrderCommand consumerPaymentCommand) {

        // 查缓存
        OrderCreateRes order = orderDomain.getPrePayOrder(
                new MemberOrderCreateCommand(consumerPaymentCommand.getStoreId(), consumerPaymentCommand.getAccountId()));

        if (Objects.isNull(order)) {
            throw new PlatformException(BaseErrorCode.NODATA);
        }

        // 落库
        orderDomain.orderAggSave(order.getOrderAgg());

        // 删除缓存
        orderRepository.delPrePayOrder(
                new MemberOrderCreateCommand(consumerPaymentCommand.getStoreId(), consumerPaymentCommand.getAccountId()));
        // 落库发下单消息
        ModelShopOutVO modelShopDataDTO = new ModelShopOutVO();
        modelShopDataDTO.setStoreId(consumerPaymentCommand.getStoreId());
        modelShopDataDTO.setAmount(order.getOrderAgg().getOrder().getGoodsAmount());
        modelShopDataDTO.setType(StoreStyleEnum.ModeShopOrderType.ORDER);
//        localMessageApi.sendModelShopMessage(modelShopDataDTO);
        localMessageApi.orderExpireClose(order.getOrderAgg().getOrder().getId());
        return order.getOrderAgg();
    }

    @Override
    public PayBaseResult memberPayOrder(PayMemberOrderCommand command) {
        OrderAgg orderAgg = orderDomain.orderAgg(command.getOrderNo());
        OrderDTO order = orderAgg.getOrder();
        if (Objects.isNull(order)) {
            throw new PlatformException(BaseErrorCode.NODATA);
        }

        // 组装支付对象
        OrderPayReq orderPayReq = buildOrderPayReq(order, command.getPaymentType());
        //拉起支付，发支付消息
        ModelShopOutVO modelShopDataDTO = new ModelShopOutVO();
        modelShopDataDTO.setStoreId(order.getStoreId());
        modelShopDataDTO.setAmount(order.getGoodsAmount());
        modelShopDataDTO.setType(StoreStyleEnum.ModeShopOrderType.PAY);
//        localMessageApi.sendModelShopMessage(modelShopDataDTO);
        // 调用支付API
        PayBaseResult payBaseResult = orderPayApi.orderPay(orderPayReq);
        orderDomain.batchUpdateOrderState(Collections.singletonList(order.getOrderNo()), OrderEnum.State.NEW, OrderEnum.State.MEMBER_WAIT_PAY, null);
        OrderDTO orderUpdate = new OrderDTO();
        orderUpdate.setId(order.getId());
        orderUpdate.setPayTime(LocalDateTime.now());
        orderUpdate.setPayType(command.getPaymentType());
        orderDomain.orderEdit(orderUpdate);
        localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(order), OrderEnum.State.NEW,OrderEnum.State.MEMBER_WAIT_PAY,SecurityUtils.getAccountId(),SecurityUtils.getIdentity());

        return payBaseResult;
    }

    @Override
    public Boolean changeOrderShip(OrderShipCommand command) {
        MemberOrderCreateCommand cacheCommand = new MemberOrderCreateCommand(command.getStoreId(), command.getAccountId());
        ShipVO shipVO = command.getShipVO();
        if (shipVO.getId() != null){
            ShipAddressRes shipAddressRes = shipAddressDomain.detail(shipVO.getId());
            if (Objects.isNull(shipAddressRes)){
                ThrowsException.exception(BaseErrorCode.PARAM, "收货地址不存在");
            }
            BeanUtils.copyProperties(shipAddressRes, shipVO);
            shipVO.setShipPhone(shipAddressRes.getShipPhone() == null ? StrUtil.EMPTY : shipAddressRes.getShipPhone().toString());
        }
        try {
            // 1. 查询缓存中的订单
            OrderCreateRes orderCreateRes = orderDomain.getPrePayOrder(cacheCommand);

            if (Objects.nonNull(orderCreateRes) && Objects.nonNull(orderCreateRes.getOrderAgg())) {
                // 缓存逻辑：复用领域层核心校验
                OrderAgg orderAgg = orderCreateRes.getOrderAgg();
                Map<Long, Money> goodsFreight = orderDomain.validateOrderShipChange(orderAgg, shipVO);
                orderDomain.validateFreightUnchanged(orderAgg, goodsFreight);

                // 线程安全更新缓存地址
                OrderCreateRes newOrder = copyAndUpdateShipInfo(orderCreateRes,shipVO);
                orderRepository.savePrePayOrder(newOrder, cacheCommand);

                log.info("修改预支付订单地址成功(缓存), orderId={}", command.getOrderNo());
                return true;
            } else {
                // 数据库逻辑：直接调用领域层方法（完全复用，无重复代码）
                Boolean result = orderDomain.changeOrderShip(command);
                log.info("修改订单地址成功(数据库), orderId={}, 结果={}", command.getOrderNo(), result);
                return result;
            }
        } catch (Exception e) {
            log.error("修改订单地址失败, orderId={}", command.getOrderNo(), e);
            throw new PlatformException(BaseErrorCode.UPDATE, "修改订单收货地址失败：" + e.getMessage());
        }
    }

    // 订单支付请求构建方法
    private OrderPayReq buildOrderPayReq(OrderDTO order, PaymentEnum.PayType payType) {
        OrderPayReq orderPayReq = new OrderPayReq();
        orderPayReq.setOrderNo(order.getId());
        orderPayReq.setConsumeType(EarningsEnum.ConsumeType.GOODS);
        orderPayReq.setOrderAmount(order.getStoreAmount());
        orderPayReq.setPayAmount(order.getMemberAmount());
        orderPayReq.setOrderInfo(order.getRemark());
        orderPayReq.setGoodsInfo("订单号:" + order.getId());
        orderPayReq.setAccountId(SecurityUtils.getAccountId());
        orderPayReq.setAccountName(SecurityUtils.getUsername());
        orderPayReq.setPayType(payType);
        orderPayReq.setChannelId(order.getChannelId());

        return orderPayReq;
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
    /**
     * 复制对象并更新收货地址（解决线程安全问题）
     */
    private OrderCreateRes copyAndUpdateShipInfo(OrderCreateRes original, ShipVO shipVO) {
        // 建议使用BeanUtils.copyProperties或自定义拷贝，避免浅拷贝
        OrderCreateRes copy = new OrderCreateRes();
        // 拷贝原有属性
        copy.setOrderAgg(original.getOrderAgg());

        // 深拷贝订单对象，修改收货地址
        OrderAgg orderAgg = copy.getOrderAgg();
        OrderDTO order = orderAgg.getOrder();
        OrderDTO newOrder = new OrderDTO();
        BeanUtils.copyProperties(order, newOrder);
        newOrder.setShipVO(shipVO);
        orderAgg.setOrder(newOrder);

        return copy;
    }
}
