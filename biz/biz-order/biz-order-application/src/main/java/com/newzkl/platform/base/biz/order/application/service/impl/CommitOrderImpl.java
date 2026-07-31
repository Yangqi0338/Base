package com.newzkl.platform.base.biz.order.application.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;

import com.newzkl.platform.base.biz.order.application.service.ICommitOrder;
import com.newzkl.platform.base.biz.order.application.service.IQueryService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountShipAddressApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsDistributionApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.CreateMemberPrePayOrderDomain;
import com.newzkl.platform.base.biz.order.domain.service.IOrderDomain;
import com.newzkl.platform.base.biz.order.domain.service.IOrderDomainNew;
import com.newzkl.platform.base.biz.order.model.dto.Order;
import com.newzkl.platform.base.biz.order.model.dto.OrderAgg;
import com.newzkl.platform.base.biz.order.model.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.biz.order.model.support.api.PayBaseResult;
import com.newzkl.platform.base.biz.order.model.support.api.order.GoodsVO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckReq;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckRes;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsInfoVO;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.core.mq.model.enums.MQEnum;
import com.newzkl.platform.base.common.core.redis.aspect.DistributedLock;
import com.newzkl.platform.base.common.core.redis.lock.impl.RedissonLockUtil;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CommitOrderImpl implements ICommitOrder {


    private final IOrderDomain orderDomain;

    @DubboReference
    private IOrderGoodsFacade orderGoodsFacade;

    @DubboReference
    private IOrderPayApi orderPayApi;

    @DubboReference
    private GoodsDistributionApi distributionRpcFacade;

    @Autowired
    private IQueryService queryService;
    @Autowired
    private IOrderDomainNew orderDomainNew;

    @DubboReference
    private IAccountPurseApi accountPurseApi;

    private final IOrderRepository orderRepository;

    @DubboReference
    private AccountShipAddressApi shipAddressFacade;

    @Override

    public OrderCreateRes commitOrder(OrderCreateCommand orderCreateCommand,
                                      MemberOrderCreateCommand memberOrderCreateCommand) {
        // 1、获取用户信息 包含渠道商id、运营商id
        // 2、获取商品信息、校验商品状态、库存并返回运费
        List<OrderItemCommand> orderGoodsList = orderCreateCommand.getOrderGoodsList();
        List<GoodsVO> goodsList = buildGoodsVO(orderGoodsList);
        PlatformResult<OrderGoodsCheckRes> result = orderGoodsFacade.orderGoodsCheck(buildOrderGoodsCheckReq(orderCreateCommand, memberOrderCreateCommand), goodsList);
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
            orderDomain.savePrePayOrder(order, memberOrderCreateCommand);
            return order;
        }else if (OrderEnum.OrderType.Channel == orderCreateCommand.getOrderType()){
            orderDomain.orderAggSave(order.getOrderAgg());
            // 7、后续扣减库存、扣减采购金、外部供应链订单请求创建订单放在支付后的异步处理中
            localMessageFacade.orderChannelNodeHandle(new OrderSyncHandleVO(order.getOrderId()));
        }
        return order;
    }

    @Override

    public OrderCreateRes createMemberPrePayOrder(OrderCreateCommand orderCreateCommand,
                                                  MemberOrderCreateCommand memberOrderCreateCommand) {
        return new CreateMemberPrePayOrderDomain(orderGoodsFacade,accountPurseApi, orderDomainNew,shipAddressFacade, orderCreateCommand, memberOrderCreateCommand).execute();
    }

    /**
     * 构建SKU订单（保留核心逻辑，简化冗余setter，优化空值处理）
     */
    private SkuOrder buildSkuOrder(OrderGoodsInfoVO goodsInfo, Long spuOrderId, Long orderId, LocalDateTime createTime) {
        SkuOrder skuOrder = new SkuOrder();
        // 基础字段批量赋值（减少冗余行）
        skuOrder.setId(SnowflakeIdAble.getSnowflakeId());
        skuOrder.setOrderId(orderId);
        skuOrder.setSpuOrderId(spuOrderId);
        skuOrder.setSpuId(goodsInfo.getSpuId());
        skuOrder.setSkuId(goodsInfo.getSkuId());
        skuOrder.setCount(goodsInfo.getNum());
        skuOrder.setCreateTime(createTime);
        // 空值兜底简化
        skuOrder.setOutSkuId(StrUtil.isEmpty(goodsInfo.getOutSkuId()) ? "0" : goodsInfo.getOutSkuId());

        // 金额计算：简化条件判断，语义化常量
        SpuEnum.ChannelType channelType = goodsInfo.getSpuChannelType();
        if (SpuEnum.ChannelType.CUSTOM == channelType) {
            skuOrder.setStoreAmount(goodsInfo.getStorePrice() * goodsInfo.getNum());
        } else if (SpuEnum.ChannelType.SELECTION == channelType
                || SpuEnum.ChannelType.OUT == channelType) {
            skuOrder.setGoodsAmount(goodsInfo.getSalePrice() * goodsInfo.getNum());
            skuOrder.setSupplierAmount(goodsInfo.getSupplyPrice() * goodsInfo.getNum());
            Integer storePrice = Optional.ofNullable(goodsInfo.getStorePrice()).orElse(0);
            skuOrder.setStoreAmount(storePrice * goodsInfo.getNum());
        } else {
            ThrowsException.exception(BaseErrorCode.PARAM);
        }

        // 固定值字段集中赋值
        skuOrder.setFreightAmount(0);
        skuOrder.setDiscountAmount(0);
        skuOrder.setOrderState(OrderEnum.State.NEW);
        skuOrder.setOrderStateLog(OrderEnum.State.NEW.toString());
        skuOrder.setSupplierId(goodsInfo.getSupplierId());
        skuOrder.setTwoMarketId(goodsInfo.getTwoMarketId());
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
        skuOrder.setSkuStorePrice(Optional.ofNullable(goodsInfo.getStorePrice()).orElse(0));
        return skuOrder;
    }

    /**
     * 填充SPU订单数据（原fillIn方法，简化命名+冗余setter）
     */
    private void fillInSpuOrder(SpuOrder spuOrder, OrderCreateCommand orderCmd,
                                MemberOrderCreateCommand memberOrderCmd, SkuOrder skuOrder) {
        // 核心字段赋值（按业务逻辑分组，增强可读性）
        spuOrder.setOrderType(orderCmd.getOrderType());
        spuOrder.setOutOrderNo(orderCmd.getOutOrderNo());
        spuOrder.setOrderId(skuOrder.getOrderId());
        spuOrder.setSpuChannelType(skuOrder.getSpuChannelType());
        spuOrder.setSpuSaleType(skuOrder.getSpuSaleType());
        spuOrder.setChannelId(orderCmd.getChannelId());
        spuOrder.setMerchantId(memberOrderCmd.getMerchantId());
        spuOrder.setSupplierId(skuOrder.getSupplierId());
        spuOrder.setDealerId(skuOrder.getDealerId());
        spuOrder.setOperatorId(skuOrder.getOperatorId());
        spuOrder.setSpuId(skuOrder.getSpuId());
        spuOrder.setSpuName(skuOrder.getSpuName());
        spuOrder.setSpuImg(skuOrder.getSpuImg());

        // 订单状态&物流
        spuOrder.setDiscountAmount(0);
        spuOrder.setOrderState(OrderEnum.State.NEW);
        ShipVO shipVO = orderCmd.getShipVO();
        spuOrder.setShipVO(shipVO);
        spuOrder.setShipPhone(shipVO.getShipPhone());
        spuOrder.setRemark(orderCmd.getRemark());
        spuOrder.setOrderStateLog(OrderEnum.State.NEW.toString());

        // 门店/用户信息
        spuOrder.setSettleSendState(0);
        spuOrder.setStoreId(memberOrderCmd.getStoreId());
        spuOrder.setAccountId(memberOrderCmd.getAccountId());
        spuOrder.setMemberId(memberOrderCmd.getAccountId());
        spuOrder.setStoreName(memberOrderCmd.getStoreName());
        spuOrder.setStoreHead(memberOrderCmd.getStoreHead());

        // 扩展字段
        SpuOrderExt spuOrderExt = new SpuOrderExt();
        spuOrderExt.setStoreAccount(memberOrderCmd.getStoreAccount());
        spuOrderExt.setStoreName(memberOrderCmd.getStoreName());
        spuOrderExt.setStoreHead(memberOrderCmd.getStoreHead());
        spuOrderExt.setUserAccount(memberOrderCmd.getUserAccount());
        spuOrderExt.setUserName(memberOrderCmd.getUserName());
        spuOrderExt.setNickName(memberOrderCmd.getNickName());
        spuOrderExt.setMemberId(memberOrderCmd.getAccountId());
        spuOrder.setSpuOrderExt(spuOrderExt);

        // 会员金额计算（自定义渠道专属）
        if (spuOrder.getSpuChannelType() == SpuEnum.ChannelType.CUSTOM) {
            spuOrder.setMemberAmount(spuOrder.getStoreAmount() + spuOrder.getFreightAmount() - spuOrder.getDiscountAmount());
        }
    }
    @Override
    public OrderCreateRes getOrderCreateResByRedis(MemberOrderCreateCommand memberOrderCreateCommand) {
        return orderDomain.getPrePayOrder(
                new MemberOrderCreateCommand(memberOrderCreateCommand.getStoreId(), memberOrderCreateCommand.getAccountId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderCreateRes createOrderAgain(List<Long> spuOrderIdList) {
        for (Long spuOrderId : spuOrderIdList) {
            SpuOrderAggVO spuOrderAggVO = queryService.spuOrderAggVO(spuOrderId);
            SpuOrderVO spuOrderVO = spuOrderAggVO.getSpuOrderVO();
            List<SkuOrderVO> skuOrderList = spuOrderAggVO.getSkuOrderList();
            if (spuOrderVO == null){
                ThrowsException.exception(BaseErrorCode.NODATA);
            }
            OrderCreateCommand orderCreateCommand = new OrderCreateCommand();
            orderCreateCommand.setOrderType(spuOrderVO.getOrderType());
            orderCreateCommand.setShipVO(JSON.parseObject(spuOrderVO.getShipVO(), ShipVO.class));
            orderCreateCommand.setChannelId(spuOrderVO.getChannelId());
            List<OrderItemCommand> orderGoodsList = new ArrayList<>();
            skuOrderList.forEach(skuOrder -> {
                StoreDistributionRpcVO storeDistributionRpcVO = distributionRpcFacade.selectBySkuId(spuOrderVO.getChannelId(), spuOrderVO.getStoreId(), skuOrder.getSkuId());
                orderGoodsList.add(new OrderItemCommand(storeDistributionRpcVO.getId(), skuOrder.getSkuId(), skuOrder.getCount()));
            });
            orderCreateCommand.setOrderGoodsList(orderGoodsList);
            orderCreateCommand.setOperatorId(SecurityUtils.getAccountId());
            MemberOrderCreateCommand memberOrderCreateCommand = new MemberOrderCreateCommand();
            memberOrderCreateCommand.setStoreId(spuOrderVO.getStoreId());
            memberOrderCreateCommand.setAccountId(SecurityUtils.getAccountId());
            memberOrderCreateCommand.setUserName(SecurityUtils.getUsername());
            memberOrderCreateCommand.setNickName(SecurityUtils.getNickName());
            return new CreateMemberPrePayOrderDomain(orderGoodsFacade,accountPurseApi, orderDomainNew,shipAddressFacade, orderCreateCommand, memberOrderCreateCommand).execute();
        }
        return null;
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
        orderDomain.delPrePayOrder(
                new MemberOrderCreateCommand(consumerPaymentCommand.getStoreId(), consumerPaymentCommand.getAccountId()));
        // 落库发下单消息
        ModelShopDataDTO modelShopDataDTO = new ModelShopDataDTO();
        modelShopDataDTO.setStoreId(consumerPaymentCommand.getStoreId());
        modelShopDataDTO.setAmount(order.getOrderAgg().getOrder().getGoodsAmount());
        modelShopDataDTO.setType(ModeShopOrderType.ORDER.name());
        localMessageFacade.sendMessage(MQ.Tag.COUNT_MODEL_SHOP_AMOUNT_EVENT,modelShopDataDTO, ModelShopDataDTO.class.getCanonicalName());
        localMessageFacade.sendDelayMessage(MQ.Tag.TIME_OUT_CLOSE_ORDER_EVENT, order.getOrderAgg().getOrder().getId(), Long.class.getCanonicalName(), MQEnum.DelayTimeLevel.MINUTE_30.getLevel());
//            orderOperationRecordUtil.sendOrderNewRecordEvent(order.getOrderAgg().getSpuOrderList(), OrderEnum.State.NEW,OrderEnum.State.NEW,SecurityUtils.getAccountId(),SecurityUtils.getRole());
        return order.getOrderAgg();
    }

    @Override
    public PayBaseResult memberPayOrder(PayMemberOrderCommand command) {
        OrderAgg orderAgg = orderDomain.orderAgg(command.getOrderId());
        Order order = orderAgg.getOrder();
        if (Objects.isNull(order)) {
            throw new PlatformException(BaseErrorCode.NODATA);
        }

        // 组装支付对象
        OrderPayReq orderPayReq = buildOrderPayReq(order, command.getPaymentType());
        //拉起支付，发支付消息
        ModelShopDataDTO modelShopDataDTO = new ModelShopDataDTO();
        modelShopDataDTO.setStoreId(order.getStoreId());
        modelShopDataDTO.setAmount(order.getGoodsAmount());
        modelShopDataDTO.setType(ModeShopOrderType.PAY.name());
        localMessageFacade.sendMessage(MQ.Tag.COUNT_MODEL_SHOP_AMOUNT_EVENT,modelShopDataDTO, ModelShopDataDTO.class.getCanonicalName());
        // 调用支付API
        PayBaseResult payBaseResult = orderPayApi.orderPay(orderPayReq);
        orderDomain.batchUpdateOrderState(Collections.singletonList(order.getId()), OrderEnum.State.NEW, OrderEnum.State.MEMBER_WAIT_PAY, null);
        Order orderUpdate = new Order();
        orderUpdate.setId(order.getId());
        orderUpdate.setPayTime(LocalDateTime.now());
        orderUpdate.setPayType(command.getPaymentType());
        orderDomain.orderEdit(orderUpdate);
        orderRepository.sendOrderNewRecordEvent(orderAgg.getSpuOrderList(), OrderEnum.State.NEW,OrderEnum.State.MEMBER_WAIT_PAY,SecurityUtils.getAccountId(),SecurityUtils.getRole());

        return payBaseResult;
    }

    @Override
    public Boolean changeOrderShip(OrderShipCommand command) {
        MemberOrderCreateCommand cacheCommand = new MemberOrderCreateCommand(command.getStoreId(), command.getAccountId());
        ShipVO shipVO = command.getShipVO();
        if (shipVO.getId() != null){
            ShipAddressOutVO shipAddressOutVO = shipAddressFacade.shipAddress(shipVO.getId());
            if (Objects.isNull(shipAddressOutVO)){
                ThrowsException.exception(BaseErrorCode.PARAM, "收货地址不存在");
            }
            BeanUtils.copyProperties(shipAddressOutVO, shipVO);
            shipVO.setShipPhone(shipAddressOutVO.getShipPhone() == null ? StrUtil.EMPTY : shipAddressOutVO.getShipPhone().toString());
        }
        try {
            // 1. 查询缓存中的订单
            OrderCreateRes orderCreateRes = orderDomain.getPrePayOrder(cacheCommand);

            if (Objects.nonNull(orderCreateRes) && Objects.nonNull(orderCreateRes.getOrderAgg())) {
                // 缓存逻辑：复用领域层核心校验
                OrderAgg orderAgg = orderCreateRes.getOrderAgg();
                Map<Long, Integer> goodsFreight = orderDomain.validateOrderShipChange(orderAgg, shipVO);
                orderDomain.validateFreightUnchanged(orderAgg.getSpuOrderList(), goodsFreight);

                // 线程安全更新缓存地址
                OrderCreateRes newOrder = copyAndUpdateShipInfo(orderCreateRes,shipVO);
                orderDomain.savePrePayOrder(newOrder, cacheCommand);

                log.info("修改预支付订单地址成功(缓存), orderId={}", command.getOrderId());
                return true;
            } else {
                // 数据库逻辑：直接调用领域层方法（完全复用，无重复代码）
                Boolean result = orderDomain.changeOrderShip(command);
                log.info("修改订单地址成功(数据库), orderId={}, 结果={}", command.getOrderId(), result);
                return result;
            }
        } catch (Exception e) {
            log.error("修改订单地址失败, orderId={}", command.getOrderId(), e);
            throw new PlatformException(BaseErrorCode.UPDATE, "修改订单收货地址失败：" + e.getMessage());
        }
    }

    // 订单支付请求构建方法
    private OrderPayReq buildOrderPayReq(Order order, Integer payType) {
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
        Order order = orderAgg.getOrder();
        Order newOrder = new Order();
        BeanUtils.copyProperties(order, newOrder);
        newOrder.setShipVO(shipVO);
        orderAgg.setOrder(newOrder);

        // 深拷贝商品订单列表，修改收货地址
        List<SpuOrder> newSpuOrderList = orderAgg.getSpuOrderList().stream()
                .map(spuOrder -> {
                    SpuOrder newSpuOrder = new SpuOrder();
                    BeanUtils.copyProperties(spuOrder, newSpuOrder);
                    newSpuOrder.setShipVO(shipVO);
                    return newSpuOrder;
                }).collect(Collectors.toList());
        orderAgg.setSpuOrderList(newSpuOrderList);

        return copy;
    }
}
