package com.newzkl.platform.base.biz.order.domain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsStoreApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.IOrderDomainNew;
import com.newzkl.platform.base.biz.order.model.support.api.AccountGroupVO;
import com.newzkl.platform.base.biz.order.model.dto.Order;
import com.newzkl.platform.base.biz.order.model.dto.OrderAgg;
import com.newzkl.platform.base.biz.order.model.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.req.MemberOrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.OrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.biz.order.model.support.api.ChannelNowServiceFeeRes;
import com.newzkl.platform.base.biz.order.model.support.api.EarningsConfigRpcVO;
import com.newzkl.platform.base.biz.order.model.support.api.StoreRPCVO;
import com.newzkl.platform.base.biz.order.model.support.api.order.OrderGoodsCheckV2Res;
import com.newzkl.platform.base.biz.order.model.support.api.order.StoreDistributionDetailRpcVO;
import com.newzkl.platform.base.biz.order.model.vo.OrderSnapVO;
import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderExt;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.generator.SnowflakeIdAble;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 订单领域服务实现
 * @author sijiwang
 */
@Service
public class OrderDomainNewImpl implements IOrderDomainNew {

    @Autowired
    private IOrderRepository orderRepository;

    // 迁移: 跨域直连 facade 违 domain 依赖硬线, 改走 adapt/api 出站端口(infra 实现里 DubboReference)
    @Autowired
    private AccountApi accountApi;
    @Autowired
    private GoodsStoreApi goodsStoreApi;


    @Override
    public OrderCreateRes createOrder(OrderGoodsCheckV2Res data, OrderCreateCommand orderCreateCommand) {
        List<StoreDistributionDetailRpcVO> goodsInfo = data.getGoodsInfo();
        Map<Long, Integer> goodsFreight = data.getGoodsFreight();

        // 获取渠道商当前服务费比例
        // 迁移(Q2): 缓存移交 infra(@Cacheable), domain 直调 repository, 不碰 redisClient
        ChannelNowServiceFeeRes channelNowServiceFee = orderRepository.queryChannelNowServiceFee(goodsInfo.get(0).getChannelId());
        // 获取渠道商分润配置
        EarningsConfigRpcVO earningsConfigRpcVO = orderRepository.channelEarningsConfig(goodsInfo.get(0).getChannelId());
        Long orderId = SnowflakeIdAble.getSnowflakeId();
        Map<Long, Long> spuOrderIdMap = new HashMap<>();
        List<SkuOrder> skuOrderList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        // 构建订单数据
        if(StrUtil.isEmpty(orderCreateCommand.getOutOrderNo())){
            orderCreateCommand.setOutOrderNo(orderId.toString());
        }
        for (StoreDistributionDetailRpcVO orderGoodsInfoVO : goodsInfo) {
            Long spuId = orderGoodsInfoVO.getGoodsId();
            if(!spuOrderIdMap.containsKey(spuId)){
                Long spuOrderId = SnowflakeIdAble.getSnowflakeId();
                spuOrderIdMap.put(spuId, spuOrderId);
            }
            // 迁移(Q2): settleOrderType 缓存移交 infra(@Cacheable), domain 直调 repository
            RoleEnum.OrderType settleOrderType = orderRepository.settleOrderType(orderGoodsInfoVO.getSupplierId());
            skuOrderList.add(buildSkuOrder(orderGoodsInfoVO,spuOrderIdMap.get(spuId),orderId,channelNowServiceFee,earningsConfigRpcVO,settleOrderType.getCode(),now));
        }
        //根据sku订单
        Map<Long, SpuOrder> spuOrderMap = skuOrderList.parallelStream().collect(Collectors.groupingBy(SkuOrder::getSpuOrderId,
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
                    spuOrder.setCreateTime(now);
                    spuOrder.setStoreId(skuOrder.getStoreId());
                    // 填充数据
                    fillIn(spuOrder,orderCreateCommand,skuOrder);
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
        order.setOrderState(OrderEnum.State.NEW);
        order.setStoreId(goodsInfo.get(0).getStoreId());
        order.setAccountId(SecurityUtils.getAccountId());
        order.setUserName(SecurityUtils.getUsername());
        order.setNickname(SecurityUtils.getNickName());
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

    @Override
    public void orderAggSave(OrderAgg orderAgg) {
        orderRepository.orderAggSave(orderAgg);
    }
    private SkuOrder buildSkuOrder(StoreDistributionDetailRpcVO orderGoodsInfoVO,Long spuOrderId,Long orderId,
                                   ChannelNowServiceFeeRes channelNowServiceFee,
                                   EarningsConfigRpcVO earningsConfigRpcVO, Integer settleOrderType,LocalDateTime time){

        SkuOrder skuOrder = new SkuOrder();
        skuOrder.setFreightAmount(0);
        skuOrder.setDiscountAmount(0);
        skuOrder.setId(SnowflakeIdAble.getSnowflakeId());
        skuOrder.setOrderId(orderId);
        skuOrder.setSpuId(orderGoodsInfoVO.getGoodsId());
        skuOrder.setSkuImg(orderGoodsInfoVO.getSkuImg());
        skuOrder.setSpuOrderId(spuOrderId);
        skuOrder.setSpuChannelType(orderGoodsInfoVO.getChannelType());
        skuOrder.setStoreId(orderGoodsInfoVO.getStoreId());
        //计算订单金额
        if(SpuEnum.ChannelType.CUSTOM == orderGoodsInfoVO.getChannelType()){
            skuOrder.setStoreAmount(orderGoodsInfoVO.getSellPrice() * orderGoodsInfoVO.getBugNum());
        }else if (SpuEnum.ChannelType.SELECTION == orderGoodsInfoVO.getChannelType() ||
                SpuEnum.ChannelType.OUT == orderGoodsInfoVO.getChannelType()){
            skuOrder.setGoodsAmount(orderGoodsInfoVO.getSupplierPrice() * orderGoodsInfoVO.getBugNum());
            skuOrder.setSupplierAmount(orderGoodsInfoVO.getSpuSupplyPrice() * orderGoodsInfoVO.getBugNum());
            Integer storePrice = orderGoodsInfoVO.getSellPrice() == null ? 0 : orderGoodsInfoVO.getSellPrice();
            skuOrder.setStoreAmount(storePrice * orderGoodsInfoVO.getBugNum());
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

    private void fillIn(SpuOrder spuOrder, OrderCreateCommand orderCreateCommand, SkuOrder skuOrder){
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
        spuOrder.setDiscountAmount(0);
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
        List<StoreRPCVO> storeRPCVOS = goodsStoreApi.batchQueryStoreInfo(Collections.singletonList(spuOrder.getStoreId()));
        if (CollUtil.isNotEmpty(storeRPCVOS)){
            StoreRPCVO storeRPCVO = storeRPCVOS.get(0);
            spuOrderExt.setStoreHead(storeRPCVO.getLogo());
            spuOrderExt.setStoreName(storeRPCVO.getName());
        }
        //查门店im账号
        AccountGroupVO accountInfo = accountApi.accountInfo(spuOrder.getStoreId());
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
            spuOrder.setMemberAmount(spuOrder.getStoreAmount()+spuOrder.getFreightAmount()-spuOrder.getDiscountAmount());
        }
    }


}
