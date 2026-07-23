package com.newzkl.platform.base.biz.order.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.zkl.scm.finance.model.account.res.ChannelNowServiceFeeRes;
import com.zkl.scm.finance.model.earnings.vo.EarningsConfigRpcVO;
import com.zkl.scm.finance.model.pay.req.OrderPayReq;
import com.zkl.scm.finance.model.pay.res.TradeBaseRes;
import com.zkl.scm.goods.rpc.model.distribution.DistributionDetailVO;
import com.zkl.scm.goods.rpc.model.store.ModelShopDataDTO;
import com.newzkl.platform.base.biz.order.model.enums.RedisEnum;
import com.newzkl.platform.base.biz.order.model.enums.finance.EarningsEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.order.domain.factory.FreightStrategyFactory;
import com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.FreightCalculateStrategy;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ISkuOrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ISpuOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.ICreateOrderDomain;
import com.newzkl.platform.base.biz.order.model.order.dto.Order;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.CommitOrderPreReq;
import com.newzkl.platform.base.biz.order.model.order.req.CreateOrderReq;
import com.newzkl.platform.base.biz.order.model.order.req.FreightCalculateReq;
import com.newzkl.platform.base.biz.order.model.order.req.PayOrderReq;
import com.newzkl.platform.base.biz.order.model.order.res.CreateOrderRes;
import com.newzkl.platform.base.biz.order.model.order.res.FreightCalculateRes;
import com.newzkl.platform.base.biz.order.model.order.util.OrderRedisKeyUtils;
import com.newzkl.platform.base.biz.order.model.order.vo.GoodsFreightAggVO;
import com.zkl.scm.user.model.relation.vo.ShipAddressRpcVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sijiwang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderDomainImpl implements ICreateOrderDomain {

//    @DubboReference
//    private IDistributionRpcFacade distributionRpcFacade;
//
//    @DubboReference
//    private IShipAddressFacade shipAddressFacade;
//
//    @DubboReference
//    private IAccountPurseConfigFacade accountPurseConfigFacade;
//
//    @DubboReference
//    private ISupplierFacade supplierFacade;
//
//    @DubboReference
//    private IOrderPayFacade orderPayApi;
//
//    private final OrderOperationRecordUtils orderOperationRecordUtil;

    private final FreightStrategyFactory freightStrategyFactory;

    private final IOrderRepository orderRepository;

    private final ISpuOrderRepository spuOrderRepository;

    private final ISkuOrderRepository skuOrderRepository;

    @Override
    public CreateOrderRes createOrderPre(CreateOrderReq req) {

        List<Long> distributionIds = req.getGoodsItems().stream().map(CreateOrderReq.GoodsItem::getDistributionId).collect(Collectors.toList());

//        List<DistributionDetailVO> distributionDetailVOS = distributionRpcFacade.queryDistributionDetailByIds(distributionIds);
        List<DistributionDetailVO> distributionDetailVOS = new ArrayList<>();

        if (distributionIds.size() != distributionDetailVOS.size()) {
            log.info("goods信息:{},orderGoodsInfos信息:{}", distributionIds, distributionDetailVOS);
            throw new ScmException(BaseErrorCode.PARAM, "SKU异常");
        }
        // 2. 基础校验
        // 商品上架状态校验（spuState != 2 为未上架）
        Optional<DistributionDetailVO> offlineGoods = distributionDetailVOS.stream()
                .filter(vo -> !Objects.equals(vo.getSpuState(), 2))
                .findFirst();
        if (offlineGoods.isPresent()) {
            throw new ScmException(BaseErrorCode.PARAM,
                    "商品已下架，SPU ID：" + offlineGoods.get().getSpuId());
        }

        // 3. 库存校验 + 设置购买数量
        Map<Long, Integer> skuNumMap = req.getGoodsItems().stream()
                .collect(Collectors.toMap(
                        CreateOrderReq.GoodsItem::getDistributionId,
                        CreateOrderReq.GoodsItem::getBuyNum,
                        (k1, k2) -> k2 // 重复ID取最新值
                ));
        for (DistributionDetailVO vo : distributionDetailVOS) {
            Integer buyNum = skuNumMap.get(vo.getDistributionId());
            if (Objects.isNull(buyNum)) {
                throw new ScmException(BaseErrorCode.PARAM, "商品购买数量为空，铺货ID：" + vo.getDistributionId());
            }
            // 库存校验（skuInventory为SKU库存）
            if (vo.getSkuInventory() < buyNum) {
                throw new ScmException(BaseErrorCode.PARAM, vo.getSpuName() + "：库存不足");
            }
            // 设置购买数量（扩展字段，需在DistributionDetailVO中新增buyNum字段）
            vo.setBuyNum(buyNum);
        }


        // 5. 聚合SPU维度运费数据
        Map<Long, GoodsFreightAggVO> goodsFreightAggMap = aggregateGoodsFreightData(distributionDetailVOS);

        // 6. 获取收货地址
        Long accountId = req.getAccountId();
//        ShipAddressRpcVO addressDetail = shipAddressFacade.getAddressDetail(req.getShipId(), accountId);
        ShipAddressRpcVO addressDetail = null;
        if (Objects.isNull(addressDetail)) {
            throw new ScmException(BaseErrorCode.PARAM, "收货地址不存在");
        }

        // 7. 计算运费
        Map<Long, Integer> goodsFreightMap = calculateFreight(goodsFreightAggMap, addressDetail);

        List<Long> channelIds = distributionDetailVOS.stream().map(DistributionDetailVO::getChannelId).distinct().collect(Collectors.toList());
//        List<ChannelNowServiceFeeRes> channelNowServiceFeeRes = accountPurseConfigFacade.queryChannelNowServiceFees(channelIds);
        List<ChannelNowServiceFeeRes> channelNowServiceFeeRes = new ArrayList<>();
        Map<Long, ChannelNowServiceFeeRes> channelMap = channelNowServiceFeeRes.stream()
                .collect(Collectors.toMap(ChannelNowServiceFeeRes::getChannelId, Function.identity()));
        Map<Long, EarningsConfigRpcVO> earningsConfigRpcVOMap = new HashMap<>();
        channelNowServiceFeeRes.forEach(v -> {
            String shareRedisKey = RedisEnum.Key.SHARE_CONFIG.getCode(v.getChannelId().toString());
            EarningsConfigRpcVO earningsConfigRpcVO = null;
//            EarningsConfigRpcVO earningsConfigRpcVO = RedisUtil.get(shareRedisKey);
//            if (earningsConfigRpcVO == null) {
//                earningsConfigRpcVO = accountPurseConfigFacade.channelEarningsConfig(v.getChannelId());
//                RedisUtil.set(shareRedisKey, earningsConfigRpcVO, 24, TimeUnit.HOURS);
//            }
            earningsConfigRpcVOMap.put(v.getChannelId(), earningsConfigRpcVO);
        });
        List<CreateOrderRes.OrderItem> orderItemList = new ArrayList<>();
        List<SpuOrder> spuOrderList = new ArrayList<>();
        LocalDateTime createTime = LocalDateTime.now();
        Map<Long, List<DistributionDetailVO>> spuGroupedMap = distributionDetailVOS.stream().collect(Collectors.groupingBy(DistributionDetailVO::getSpuId));
        String orderNo = BusinessCodeUtil.generate(BusinessType.ORDER);
        spuGroupedMap.forEach((spuId, spuDetailVOS) -> {
            List<SkuOrder> skuOrderList = new ArrayList<>();
            CreateOrderRes.OrderItem spuItem = new CreateOrderRes.OrderItem();
            String spuOrderNo = BusinessCodeUtil.generate(BusinessType.ORDER_SPU);
            spuDetailVOS.forEach(v -> {
//                Integer settleOrderType = RedisUtil.get(RedisEnum.Key.SETTLE_ORDER_TYPE.getCode(v.getSupplierId()));
//                if (settleOrderType == null) {
//                    settleOrderType = supplierFacade.settleOrderType(v.getSupplierId());
//                    RedisUtil.set(RedisEnum.Key.SETTLE_ORDER_TYPE.getCode(v.getSupplierId()), settleOrderType);
//                }
                String skuOrderNo = BusinessCodeUtil.generate(BusinessType.ORDER_SKU);
                SkuOrder skuOrder = new SkuOrder();
                skuOrder.setFreightAmount(0L);
                skuOrder.setDiscountAmount(0L);
                skuOrder.setId(SnowflakeIdAble.getSnowflakeId());
                skuOrder.setSpuOrderNo(spuOrderNo);
                skuOrder.setSkuOrderNo(skuOrderNo);
                skuOrder.setOrderNo(orderNo);
                skuOrder.setOrderState(OrderEnum.State.NEW.getCode());
                skuOrder.setOrderStateLog(OrderEnum.State.NEW.getInfo());
//                skuOrder.setSettleOrderType(settleOrderType);
                skuOrder.setSettlementConfig(JSONObject.toJSONString(earningsConfigRpcVOMap.get(v.getChannelId())));
                skuOrder.setChannelId(v.getChannelId());
                skuOrder.setStoreId(v.getStoreId());
                skuOrder.setUserId(accountId);
                skuOrder.setSkuId(v.getSkuId());
                skuOrder.setSpuId(spuId);
                skuOrder.setDistributionId(v.getDistributionId());
                skuOrder.setSkuSnapshot(JSONObject.toJSONString(v));
                skuOrder.setBuyNum(v.getBuyNum());
                skuOrder.setDeliveryQuantity(0);
                skuOrder.setRefundingQuantity(0);
                skuOrder.setRefundedQuantity(0);
                skuOrder.setPlatformPurchaseAmount(v.getSkuSupplyPrice() * v.getBuyNum());
                skuOrder.setPlatformDistributionAmount(v.getSupplierPrice() * v.getBuyNum());
                skuOrder.setChannelPurchaseAmount(v.getSupplierPrice() * v.getBuyNum());
                skuOrder.setChannelDistributionAmount(v.getSellPrice() * v.getBuyNum());
                skuOrder.setStoreSalesAmount(v.getSellPrice() * v.getBuyNum());
                skuOrder.setOrderPayableAmount(v.getSellPrice() * v.getBuyNum());
                skuOrder.setFreightAmount(goodsFreightMap.get(v.getSkuId()).longValue());
                skuOrder.setDiscountAmount(0L);
                skuOrder.setOrderActualAmount(skuOrder.getOrderPayableAmount() + skuOrder.getFreightAmount() - skuOrder.getDiscountAmount());
                skuOrder.setSupplierId(v.getSupplierId());
                skuOrder.setOutSkuId(v.getOutSkuId());
                skuOrder.setCreateTime(createTime);
                skuOrderList.add(skuOrder);
            });
            spuItem.setSkuOrders(skuOrderList);
            DistributionDetailVO first = CollUtil.getFirst(spuDetailVOS);
            SpuOrder spuOrder = new SpuOrder();
            spuOrder.setId(SnowflakeIdAble.getSnowflakeId());
            spuOrder.setSpuOrderNo(spuOrderNo);
            spuOrder.setOrderNo(orderNo);
            spuOrder.setOrderType(OrderEnum.OrderType.getByCode(first.getChannelType().getCode()));
            spuOrder.setOrderState(OrderEnum.State.NEW.getCode());
            spuOrder.setOrderStateLog(OrderEnum.State.NEW.getInfo());
            spuOrder.setSettleFreightState(0);
            spuOrder.setSpuId(spuId);
            spuOrder.setOutSpuId(first.getOutSpuId());
            spuOrder.setGoodsQuantity(spuDetailVOS.stream()
                    .mapToInt(detail -> Optional.ofNullable(detail.getBuyNum()).orElse(0))
                    .sum());
            spuOrder.setGoodsSnapshot(JSONObject.toJSONString(first));
            spuOrder.setShipAddressId(addressDetail.getId());
            spuOrder.setReceiptInfo(JSONObject.toJSONString(addressDetail));
            spuOrder.setChannelId(first.getChannelId());
            spuOrder.setStoreId(first.getStoreId());
            spuOrder.setUserId(accountId);
            spuOrder.setPlatformPurchaseAmount(skuOrderList.stream().mapToLong(SkuOrder::getPlatformPurchaseAmount).sum());
            spuOrder.setPlatformDistributionAmount(skuOrderList.stream().mapToLong(SkuOrder::getPlatformDistributionAmount).sum());
            spuOrder.setChannelPurchaseAmount(skuOrderList.stream().mapToLong(SkuOrder::getChannelPurchaseAmount).sum());
            spuOrder.setChannelDistributionAmount(skuOrderList.stream().mapToLong(SkuOrder::getChannelDistributionAmount).sum());
            spuOrder.setStoreSalesAmount(skuOrderList.stream().mapToLong(SkuOrder::getStoreSalesAmount).sum());
            spuOrder.setOrderPayableAmount(skuOrderList.stream().mapToLong(SkuOrder::getOrderPayableAmount).sum());
            spuOrder.setOrderActualAmount(skuOrderList.stream().mapToLong(SkuOrder::getOrderActualAmount).sum());
            spuOrder.setFreightAmount(skuOrderList.stream().mapToLong(SkuOrder::getFreightAmount).sum());
            spuOrder.setDiscountAmount(skuOrderList.stream().mapToLong(SkuOrder::getDiscountAmount).sum());
            spuOrder.setOrderActualAmount(spuOrder.getOrderActualAmount() + spuOrder.getFreightAmount() - spuOrder.getDiscountAmount());
            spuOrder.setPlatformServiceFee(skuOrderList.stream().mapToLong(SkuOrder::getPlatformServiceFee).sum());
            spuOrder.setUserPayAmount(skuOrderList.stream().mapToLong(SkuOrder::getUserPayAmount).sum());
            spuOrder.setRemark(req.getRemark());
            spuOrderList.add(spuOrder);
            spuItem.setSpuOrder(spuOrder);
            orderItemList.add(spuItem);
        });
        Order order = new Order();
        order.setId(SnowflakeIdAble.getSnowflakeId());
        order.setOrderNo(orderNo);
        order.setOrderState(OrderEnum.State.NEW.getCode());
        order.setOrderStateLog(OrderEnum.State.NEW.getInfo());
        order.setOrderType(OrderEnum.OrderType.MEMBER.getCode());
        order.setOutOrderNo(req.getOutOrderNo());
        order.setIsExternalOrder(StrUtil.isNotBlank(req.getOutOrderNo()) ? 1 : 0);
        order.setSourceType(OrderEnum.SourceType.MMT.getCode());
        order.setShipAddressId(addressDetail.getId());
        order.setReceiptInfo(JSONObject.toJSONString(addressDetail));
        order.setUserId(accountId);
        order.setPlatformPurchaseAmount(spuOrderList.stream().mapToLong(SpuOrder::getPlatformPurchaseAmount).sum());
        order.setPlatformDistributionAmount(spuOrderList.stream().mapToLong(SpuOrder::getPlatformDistributionAmount).sum());
        order.setChannelPurchaseAmount(spuOrderList.stream().mapToLong(SpuOrder::getChannelPurchaseAmount).sum());
        order.setChannelDistributionAmount(spuOrderList.stream().mapToLong(SpuOrder::getChannelDistributionAmount).sum());
        order.setStoreSalesAmount(spuOrderList.stream().mapToLong(SpuOrder::getStoreSalesAmount).sum());
        order.setOrderPayableAmount(spuOrderList.stream().mapToLong(SpuOrder::getOrderPayableAmount).sum());
        order.setOrderActualAmount(spuOrderList.stream().mapToLong(SpuOrder::getOrderActualAmount).sum());
        order.setFreightAmount(spuOrderList.stream().mapToLong(SpuOrder::getFreightAmount).sum());
        order.setDiscountAmount(spuOrderList.stream().mapToLong(SpuOrder::getDiscountAmount).sum());
        order.setOrderActualAmount(order.getOrderActualAmount() + order.getFreightAmount() - order.getDiscountAmount());
        order.setPlatformServiceFee(spuOrderList.stream().mapToLong(SpuOrder::getPlatformServiceFee).sum());
        order.setUserPayAmount(spuOrderList.stream().mapToLong(SpuOrder::getUserPayAmount).sum());
        order.setRemark(req.getRemark());
        // 8. 封装返回结果
        CreateOrderRes res = new CreateOrderRes();
        res.setOrder(order);
        res.setOrderItems(orderItemList);
        res.setRemainTime(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        log.info("订单商品校验完成，运费计算结果：{}", goodsFreightMap);
//        RedisUtil.set(OrderRedisKeyUtils.getPrePayOrderKey(orderNo, accountId),res, OrderRedisKeyUtils.TIME_OUT);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateOrderRes commitOrderPre(CommitOrderPreReq req) {
        String lockKey = "MEMBER_PAY_LOCK:" + req.getAccountId() + ":" + req.getOrderNo();
//        RLock lock = RedissonLockUtil.lock(lockKey);
        String prePayOrderKey = OrderRedisKeyUtils.getPrePayOrderKey(req.getOrderNo(), req.getAccountId());
//            CreateOrderRes res = RedisUtil.get(prePayOrderKey);
        CreateOrderRes res = null;
        if (Objects.isNull(res)) {
            throw new ScmException(BaseErrorCode.NODATA);
        }
        orderRepository.orderAggSave(res);
        Order order = res.getOrder();
//            RedisUtil.del(prePayOrderKey);
        ModelShopDataDTO modelShopDataDTO = new ModelShopDataDTO();
        modelShopDataDTO.setStoreId(order.getStoreId());
        modelShopDataDTO.setAmount(order.getStoreSalesAmount().intValue());
//            modelShopDataDTO.setType(ModeShopOrderType.ORDER.name());
//            localMessageService.sendMessage(MQ.SCM_ORDER,MQ.Tag.COUNT_MODEL_SHOP_AMOUNT_EVENT,modelShopDataDTO);
//            localMessageService.sendMessage(MQ.SCM_ORDER,MQ.Tag.TIME_OUT_CLOSE_ORDER_EVENT, order.getOrderNo(), DelayTimeLevel.MINUTE_30.getLevel());
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TradeBaseRes payOrder(PayOrderReq req) {
        Order order = orderRepository.getByOrderNo(req.getOrderNo());
        if (Objects.isNull(order)){
            throw new ScmException(BaseErrorCode.NODATA);
        }
        if (!order.getOrderState().equals(OrderEnum.State.NEW.getCode())) {
            throw new ScmException(BaseErrorCode.CUSTOM, "订单状态异常，非待支付订单！");
        }
        // 组装支付对象
        OrderPayReq orderPayReq = buildOrderPayReq(order, OrderEnum.PayType.getByCode(req.getPaymentType()));
        //拉起支付，发支付消息
        ModelShopDataDTO modelShopDataDTO = new ModelShopDataDTO();
        modelShopDataDTO.setStoreId(order.getStoreId());
        modelShopDataDTO.setAmount(order.getOrderActualAmount().intValue());
//        modelShopDataDTO.setType(ModeShopOrderType.PAY.name());
//        localMessageService.sendMessage(MQ.SCM_ORDER,MQ.Tag.COUNT_MODEL_SHOP_AMOUNT_EVENT,modelShopDataDTO);
        // 调用支付API
//        TradeBaseRes payBaseResult = orderPayApi.orderPay(orderPayReq);
        TradeBaseRes payBaseResult = null;
        orderRepository.updateOrderChainStateByOrderNo(order.getOrderNo(), OrderEnum.State.NEW.getCode(), OrderEnum.State.MEMBER_WAIT_PAY.getCode(), null);
        Order orderUpdate = new Order();
        orderUpdate.setId(order.getId());
        orderUpdate.setPayTime(LocalDateTime.now());
        orderUpdate.setPayType(req.getPaymentType());
        orderUpdate.setPayFlowNo(payBaseResult.getTripartiteNo());
        orderRepository.updateById(orderUpdate);
        List<SpuOrder> spuOrders = spuOrderRepository.listByOrderNo(order.getOrderNo());
//        orderOperationRecordUtil.sendOrderNewRecordEvent(spuOrders, OrderEnum.State.NEW.getCode(),OrderEnum.State.MEMBER_WAIT_PAY.getCode(),SecurityUtils.getAccountId(),SecurityUtils.getRole());
        return payBaseResult;
    }

    @Override
    public CreateOrderRes createOrderAgain(String spuOrderNo) {
        SpuOrder spuOrder = spuOrderRepository.getBySpuOrderNo(spuOrderNo);
        List<SkuOrder> skuOrders = skuOrderRepository.getBySpuOrderNo(spuOrderNo);
        if (CollectionUtil.isEmpty(skuOrders) || Objects.isNull(spuOrder)){
            throw new ScmException(BaseErrorCode.NODATA);
        }
        CreateOrderReq req = new CreateOrderReq();
        req.setOrderType(OrderEnum.OrderType.MEMBER.getCode());
        req.setShipId(spuOrder.getShipAddressId());
        List<CreateOrderReq.GoodsItem> goodsItems = new ArrayList<>();
        for (SkuOrder skuOrder : skuOrders) {
            CreateOrderReq.GoodsItem goodsItem = new CreateOrderReq.GoodsItem();
            goodsItem.setDistributionId(skuOrder.getDistributionId());
            goodsItem.setBuyNum(skuOrder.getBuyNum());
            goodsItems.add(goodsItem);
        }
        req.setGoodsItems(goodsItems);
        return  createOrderPre(req);
    }

    /**
     * 聚合SPU维度运费数据
     */
    @Override
    public Map<Long, GoodsFreightAggVO> aggregateGoodsFreightData(List<DistributionDetailVO> distributionDetailVOS) {
        // 按SPU ID分组
        Map<Long, List<DistributionDetailVO>> spuGroupMap = distributionDetailVOS.stream()
                .collect(Collectors.groupingBy(DistributionDetailVO::getSpuId));

        Map<Long, GoodsFreightAggVO> freightAggMap = new HashMap<>();
        for (Map.Entry<Long, List<DistributionDetailVO>> entry : spuGroupMap.entrySet()) {
            Long spuId = entry.getKey();
            List<DistributionDetailVO> spuGoodsList = entry.getValue();

            // 取第一个商品作为基础信息
            DistributionDetailVO baseVO = spuGoodsList.stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("SPU分组下无商品数据，SPU ID：" + spuId));

            // 聚合重量/体积/数量
            BigDecimal totalWeight = spuGoodsList.stream()
                    .map(vo -> Objects.nonNull(vo.getWeight()) ? BigDecimal.valueOf(vo.getWeight()) : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalVolume = spuGoodsList.stream()
                    .map(vo -> Objects.nonNull(vo.getVolume()) ? BigDecimal.valueOf(vo.getVolume()) : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            Integer totalNum = spuGoodsList.stream()
                    .map(DistributionDetailVO::getBuyNum)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .sum();


            // 封装聚合VO
            GoodsFreightAggVO aggVO = new GoodsFreightAggVO();
            aggVO.setGoodsId(spuId);
            aggVO.setNum(totalNum);
            aggVO.setTotalWeight(totalWeight);
            aggVO.setTotalVolume(totalVolume);
            aggVO.setFreightTemplateId(baseVO.getFreightTemplateId());
            aggVO.setChannelType(baseVO.getChannelType());
            freightAggMap.put(spuId, aggVO);
        }
        return freightAggMap;
    }

    /**
     * 计算运费
     */
    @Override
    public Map<Long, Integer> calculateFreight(Map<Long, GoodsFreightAggVO> goodsFreightAggMap, ShipAddressRpcVO addressDetail) {
        Map<Long, Integer> freightMap = new HashMap<>();
        for (Map.Entry<Long, GoodsFreightAggVO> entry : goodsFreightAggMap.entrySet()) {
            Long goodsId = entry.getKey();
            GoodsFreightAggVO aggVO = entry.getValue();

            // 构建运费计算请求
            FreightCalculateReq calculateReq = new FreightCalculateReq();
            calculateReq.setGoodsId(goodsId);
            calculateReq.setNum(aggVO.getNum());
            calculateReq.setTotalWeight(aggVO.getTotalWeight());
            calculateReq.setTotalVolume(aggVO.getTotalVolume());
            calculateReq.setOutSpuId(aggVO.getOutSpuId());
            calculateReq.setOutSkuId(aggVO.getOutSkuId());
            calculateReq.setChannelType(aggVO.getChannelType());
            calculateReq.setItemCode(aggVO.getItemCode());
            calculateReq.setSupplierId(aggVO.getSupplierId());
            calculateReq.setFreightTemplateId(aggVO.getFreightTemplateId());
            // 补充地址编码（需从addressDetail解析省/市/区编码）
            calculateReq.setShipProvinceCode(addressDetail.getShipProvinceCode());
            calculateReq.setShipProvinceName(addressDetail.getShipProvinceName());
            calculateReq.setShipCityCode(addressDetail.getShipCityCode());
            calculateReq.setShipCityName(addressDetail.getShipCityName());
            calculateReq.setShipAreaCode(addressDetail.getShipAreaCode());
            calculateReq.setShipAreaName(addressDetail.getShipAreaName());
            calculateReq.setShipDetailAddress(addressDetail.getShipDetailAddress());

            // 获取策略并计算运费
            FreightCalculateStrategy strategy = freightStrategyFactory.getStrategy(aggVO.getFreightTemplateId());
            FreightCalculateRes calculateRes = strategy.calculate(calculateReq);
            freightMap.put(goodsId, calculateRes.getFreightAmount());
        }
        return freightMap;
    }


    // 订单支付请求构建方法
    private OrderPayReq buildOrderPayReq(Order order, OrderEnum.PayType payType) {
        OrderPayReq orderPayReq = new OrderPayReq();
        orderPayReq.setOrderNo(order.getId());
        orderPayReq.setConsumeType(EarningsEnum.ConsumeType.GOODS);
        orderPayReq.setOrderAmount(order.getOrderActualAmount().intValue());
        orderPayReq.setPayAmount(order.getUserPayAmount().intValue());
        orderPayReq.setOrderInfo(order.getRemark());
        orderPayReq.setGoodsInfo("订单号:" + order.getOrderNo());
        orderPayReq.setAccountId(SecurityUtils.getAccountId());
        orderPayReq.setAccountName(SecurityUtils.getUsername());
        orderPayReq.setPayType(payType);

        return orderPayReq;
    }
}
