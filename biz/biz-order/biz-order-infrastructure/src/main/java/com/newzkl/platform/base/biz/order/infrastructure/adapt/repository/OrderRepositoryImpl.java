package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountChannelApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IOrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IRefundRepository;
import com.newzkl.platform.base.biz.order.domain.service.StoreAccountPayUtil;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateRecordRPC;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderStateVO;
import com.newzkl.platform.base.biz.order.infrastructure.assembler.*;
import com.newzkl.platform.base.biz.order.infrastructure.dao.*;
import com.newzkl.platform.base.biz.order.infrastructure.entity.*;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.*;
import com.newzkl.platform.base.biz.order.model.res.AlreadyDeliverRes;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.biz.order.model.res.OrderStateCheckRes;
import com.newzkl.platform.base.biz.order.model.support.api.ChannelNowServiceFeeRes;
import com.newzkl.platform.base.biz.order.model.support.api.SkuSaleInfo;
import com.newzkl.platform.base.biz.order.model.support.api.freight.FreightCalculateGoodsVO;
import com.newzkl.platform.base.biz.order.model.support.api.freight.FreightCalculateReq;
import com.newzkl.platform.base.biz.order.model.support.api.freight.FreightCalculateRes;
import com.newzkl.platform.base.biz.order.model.support.api.freight.RegionEasyVO;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.mq.infrastructure.utils.MQUtil;
import com.newzkl.platform.base.common.core.mq.model.constant.MQ;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.constant.DeliverErrorCode;
import com.newzkl.platform.base.common.ddd.model.constant.OrderErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/1211:11
 */
@Repository
@Slf4j
public class OrderRepositoryImpl implements IOrderRepository {

    private static final String PRE_PAY_ORDER_KEY = "prePayOrder:";

    private static final Long TIME_OUT = 60L * 60;
    @Autowired
    private ISupplierFacade supplierFacade;
    @Autowired
    private INotifyFacade notifyFacade;
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private OrderAssembler orderAssembler;
    @Autowired
    private SpuOrderDAO spuOrderDAO;
    @Autowired
    private SpuOrderAssembler spuOrderAssembler;
    @Autowired
    private SkuOrderDAO skuOrderDAO;
    @Autowired
    private SkuOrderAssembler skuOrderAssembler;

    @Autowired
    private IAccountConfigApi accountConfigApi;
    @Autowired
    private AccountChannelApi channelFacade;
    @Autowired
    private IFreightFacade freightFacade;
    @Autowired
    private DeliverDAO deliverDAO;
    @Autowired
    private DeliverAssembler deliverAssembler;
    @Autowired
    private OutOrderDAO outOrderDAO;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private StoreAccountPayUtil storeAccountPayUtil;
    @Autowired
    private IRefundRepository refundRepository;

    @Override
    public OrderAgg orderAgg(Long orderId) {
        OrderAgg orderAgg = new OrderAgg();
        orderAgg.setInit(false);
        OrderDO orderDO = orderDAO.selectById(orderId);
        orderAgg.setOrder(orderAssembler.doToDomain(orderDO));

        SpuOrderQuery spuOrderQuery = new SpuOrderQuery();
        spuOrderQuery.setOrderId(orderId);
        List<SpuOrderDO> spuOrderDOList = spuOrderDAO.listDOByQuery(spuOrderQuery);
        List<SpuOrder> spuOrderList = TransferUtils.transfers(spuOrderDOList, item -> spuOrderAssembler.doToDomain(item));
        orderAgg.setSpuOrderList(spuOrderList);

        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderId(orderId);
        List<SkuOrderDO> skuOrderDOList = skuOrderDAO.listDOByQuery(skuOrderQuery);
        List<SkuOrder> skuOrderList = TransferUtils.transfers(skuOrderDOList, item -> skuOrderAssembler.doToDomain(item));
        orderAgg.setSkuOrderList(skuOrderList);
        return orderAgg;
    }

    @Override
    public Order order(Long orderId) {
        OrderDO orderDO = orderDAO.selectById(orderId);
        return orderAssembler.doToDomain(orderDO);
    }

    @Override
    public OrderVO orderVO(Long orderId) {
        return orderDAO.selectById(orderId);
    }

    @Override
    public Page<OrderVO> orderVOList(OrderQuery orderQuery) {

        List<OrderVO> orderVO = orderDAO.listByQuery(orderQuery);

        return page;
    }

    @Override
    public Page<SpuOrderVO> spuOrderVOList(SpuOrderQuery spuOrderQuery) {

        List<SpuOrderVO> orderVO = spuOrderDAO.listByQuery(spuOrderQuery);

        return page;
    }

    @Override
    public Map<Integer, Integer> stateCountMap(SpuOrderQuery spuOrderQuery) {
        spuOrderQuery.addGroupField("t.order_state");

        List<Map<String, Object>> countMap = spuOrderDAO.countMapByQuery(spuOrderQuery);
        Map<Integer, Integer> resultMap = new HashMap<>();
        countMap.forEach(map -> {
            Integer count = MapUtil.getInt(map, "count0");
            Integer state = MapUtil.getInt(map, "count1");
            resultMap.put(state, count);
        });
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderAggSave(OrderAgg orderAgg) {
        try {
            Order order = orderAgg.getOrder();
            OrderDO orderDO = orderAssembler.domainToDO(order);
            orderDAO.insertBatch(Collections.singletonList(orderDO));
            List<SpuOrderDO> spuOrderDOList = TransferUtils.transfers(orderAgg.getSpuOrderList(), item -> spuOrderAssembler.domainToDO(item));
            spuOrderDOList.forEach(item -> item.setShipVO(orderDO.getShipVO()));
            spuOrderDAO.insertBatch(spuOrderDOList);
            List<SkuOrderDO> skuOrderDOList = TransferUtils.transfers(orderAgg.getSkuOrderList(), item -> skuOrderAssembler.domainToDO(item));
            skuOrderDAO.insertBatch(skuOrderDOList);
        } catch (DuplicateKeyException e) {
            log.error("订单持久化异常:", e);
            ThrowsException.exception(BaseErrorCode.EXIST_DATA);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderAggUpdate(OrderAgg orderAgg) {
        Order order = orderAgg.getOrder();
        OrderDO orderDO = orderAssembler.domainToDO(order);
        orderDAO.updateById(orderDO);
        List<SpuOrderDO> spuOrderDOList = TransferUtils.transfers(orderAgg.getSpuOrderList(), item -> spuOrderAssembler.domainToDO(item));
        spuOrderDOList.forEach(spuOrder -> {
            spuOrderDAO.updateById(spuOrder);
        });
        List<SkuOrderDO> skuOrderDOList = TransferUtils.transfers(orderAgg.getSkuOrderList(), item -> skuOrderAssembler.domainToDO(item));
        skuOrderDOList.forEach(skuOrder -> {
            skuOrderDAO.updateById(skuOrder);
        });
    }

    @Override
    public Page<SkuOrderVO> skuOrderVOList(SkuOrderQuery orderQuery) {

        List<SkuOrderVO> orderVO = skuOrderDAO.listByQuery(orderQuery);

        return page;
    }

    @Override
    public SpuOrderVO spuOrderVO(Long spuOrderId) {
        return spuOrderDAO.selectById(spuOrderId);
    }

    @Override
    public SpuOrderAggVO spuOrderAggVO(Long spuOrderId) {
        SpuOrderAggVO spuOrderAggVO = new SpuOrderAggVO();
        SpuOrderVO spuOrderVO = spuOrderVO(spuOrderId);
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setSpuOrderId(spuOrderId);
        List<SkuOrderVO> skuOrderVOS = skuOrderDAO.listByQuery(skuOrderQuery);
        spuOrderAggVO.setSpuOrderVO(spuOrderVO);
        spuOrderAggVO.setSkuOrderList(skuOrderVOS);
        return spuOrderAggVO;
    }

    @Override
    public int batchUpdateOrderState(List<Long> orderIdList,  OrderEnum.State sourceState,  OrderEnum.State toState) {
        return orderDAO.batchUpdateOrderState(orderIdList, sourceState, toState);
    }

    @Override
    public void batchUpdateSpuOrderState(List<Long> spuOrderIdList,  OrderEnum.State sourceState,  OrderEnum.State toState) {
        SpuOrderDO spuOrderDO = new SpuOrderDO();
        if (toState == OrderEnum.State.WAIT_RECEIVE) {
            spuOrderDO.setDeliveredTime(LocalDateTime.now());
        } else if (toState == OrderEnum.State.DOWN_RECEIVE) {
            spuOrderDO.setReceiveTime(LocalDateTime.now());
        } else if (toState == OrderEnum.State.CLOSE) {
            spuOrderDO.setCloseTime(LocalDateTime.now());
        }
        spuOrderDAO.batchUpdateSpuOrderState(spuOrderIdList, sourceState, toState, spuOrderDO);
    }

    @Override
    public void batchUpdateSpuOrderStateByOrderId(List<Long> orderIdList,  OrderEnum.State sourceState,  OrderEnum.State toState, String spuOrderExt) {
        spuOrderDAO.batchUpdateOrderStateByOrderId(orderIdList, sourceState, toState, spuOrderExt);
    }

    @Override
    public List<Long> orderIdList(OrderQuery orderQuery) {
        return orderDAO.listPkByQuery(orderQuery);
    }

    @Override
    public int batchUpdateSkuOrderState(List<Long> skuOrderIdList, OrderEnum.State from, OrderEnum.State to, SkuOrderCommand skuOrderCommand) {
        return skuOrderDAO.batchUpdateSkuOrderState(skuOrderIdList, from, to, skuOrderCommand);
    }

    @Override
    public void batchUpdateSkuOrderStateByOrderId(List<Long> orderIdList, OrderEnum.State sourceState, OrderEnum.State toState) {
        skuOrderDAO.batchUpdateOrderStateByOrderId(orderIdList, sourceState, toState);
    }

    @Override
    public List<SpuOrderStateVO> accountOrderState(Long accountId, List<Long> spuOrderIdList) {
        return spuOrderDAO.accountOrderState(accountId, spuOrderIdList);
    }

    @Override
    public SpuOrderRelationVO spuOrderRelation(Long orderId, Long spuId) {
        return spuOrderDAO.spuOrderRelation(orderId, spuId);
    }

    @Override
    public List<OrderStateCheckRes> checkSpuOrderState(List<Long> orderId) {
        if (ObjectUtil.isEmpty(orderId)) {
            return new ArrayList<>();
        }
        return spuOrderDAO.checkSpuOrderState(orderId);
    }

    @Override
    public OrderAggVO orderAggVO(Long orderId) {
        OrderAggVO orderAggVO = new OrderAggVO();
        OrderVO orderVO = orderVO(orderId);
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderId(orderId);
        List<SkuOrderVO> skuOrderVOS = skuOrderDAO.listByQuery(skuOrderQuery);
        orderAggVO.setOrderVO(orderVO);
        orderAggVO.setSkuOrderList(skuOrderVOS);
        return orderAggVO;
    }

    @Override
    public List<OrderStateCheckRes> checkOrderState(List<Long> orderId) {
        return orderDAO.checkOrderState(orderId);
    }

    @Override
    public List<Long> orderIdBySpuSkuOrderId(List<Long> spuOrderId, List<Long> skuOrderId) {
        return skuOrderDAO.orderIdBySpuSkuOrderId(spuOrderId, skuOrderId);
    }

    @Override
    public void skuOrderEditForRefundPass(List<Long> skuIdList) {
        if (CollUtil.isEmpty(skuIdList)) {
            return;
        }
        skuOrderDAO.skuOrderEditForRefundPass(skuIdList);
    }

    @Override
    public void orderStateNotify(OrderEnum.OrderType orderType, Long channelId, String outOrderNo, OrderEnum.State currentState, OrderEnum.State toState) {
        //渠道商选品订单的开发者通知
        if (OrderEnum.OrderType.Channel == orderType) {
            NotifyEventContent notifyEventContent = new NotifyEventContent();
            notifyEventContent.setServiceType(NotifyEnums.ServiceType.ORDER.getCode());
            notifyEventContent.setBusinessType(NotifyEnums.OrderType.TRADE_STATE.getCode());
            notifyEventContent.setEventInfo(JSONObject.toJSONString(new ApiOrderStateEvent(outOrderNo, currentState, toState)));
            notifyFacade.batchSend(Collections.singletonList(channelId), notifyEventContent);
        }
    }

    @Override
    public void skuOrderEditForRefundClose(List<Long> skuIdList) {
        if (CollUtil.isEmpty(skuIdList)) {
            return;
        }
        skuOrderDAO.skuOrderEditForRefundClose(skuIdList);
    }

    @Override
    public ChannelNowServiceFeeRes queryChannelNowServiceFee(Long channelId) {
        return accountConfigApi.queryChannelNowServiceFee(channelId);
    }

    @Override
    public void skuOrderEditByQuery(SkuOrder sku, SkuOrderQuery skuQuery) {
        SkuOrderDO skuOrderDO = skuOrderAssembler.domainToDO(sku);
        skuOrderDAO.updateByQuery(skuOrderDO, skuQuery);
    }

    @Override
    public void spuOrderEditByQuery(SpuOrder spu, SpuOrderQuery spuOrderQuery) {
        SpuOrderDO spuOrderDO = spuOrderAssembler.domainToDO(spu);
        spuOrderDAO.updateByQuery(spuOrderDO, spuOrderQuery);
    }

    @Override
    public void wakeUpDelayMessage(String key) {
        localMessageFacade.wakeUpDelayMessage(key);
    }

    @Override
    public List<SettlementConfigOutVO> settlementConfigBatch(List<Long> supplierIdList) {
        return supplierFacade.settlementConfigBatch(supplierIdList);
    }

    @Override
    public EarningsConfigRpcVO channelEarningsConfig(Long channelId) {
        return channelFacade.channelEarningsConfig(channelId);
    }

    @Override
    public List<AlreadyDeliverRes> getAlreadyDeliverResList(Long spuOrderId, List<Long> skuIds) {
        return skuOrderDAO.getAlreadyDeliverResList(spuOrderId, skuIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deliverSave(Deliver deliver) {
        DeliverDO model = deliverAssembler.domainToDO(deliver);
        deliverDAO.insert(model);
        List<DeliverItemVO> deliverItemList = deliver.getItem();
        //修改订单商品发货数量
        for (DeliverItemVO deliverItem : deliverItemList) {
            int count = skuOrderDAO.updateSkuDeliverCount(deliverItem.getSpuOrderId(), deliverItem.getSkuId(), deliverItem.getCount());
            if (count != 1) {
                ThrowsException.exception(DeliverErrorCode.UPDATE_DELIVER_NUM);
            }
        }
    }

    @Override
    public List<Long> querySkuOrderIdList(Long spuOrderId, List<Long> skuIdList) {
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setSpuOrderId(spuOrderId);
        skuOrderQuery.setSkuIdList(skuIdList);
        return skuOrderDAO.idByQuery(skuOrderQuery);
    }

    @Override
    public SpuOrder spuOrder(Long spuOrderId) {
        SpuOrderDO spuOrderDO = spuOrderDAO.selectById(spuOrderId);
        return spuOrderAssembler.doToDomain(spuOrderDO);
    }

    @Override
    public void orderEdit(Order orderEdit) {
        OrderDO orderDO = orderAssembler.domainToDO(orderEdit);
        orderDAO.updateById(orderDO);
    }

    @Override
    public void deliverEdit(Deliver deliver) {
        DeliverDO deliverDO = deliverAssembler.domainToDO(deliver);
        deliverDAO.updateById(deliverDO);
    }

    @Override
    public void deliverDelete(Long id) {
        deliverDAO.deleteByPrimaryKey(id);
    }

    @Override
    public List<OrderStateCountVO> countOrderStateByChannel(Long channelId) {

        List<OrderStateCountVO> orderStateCountList = Optional.ofNullable(spuOrderDAO.countOrderStateByChannel(channelId))
                .orElse(Collections.emptyList());

        Integer refundingCount = refundRepository.countTotalRefundingByStoreId(channelId);

        OrderStateCountVO refundingStateVO = new OrderStateCountVO();
        refundingStateVO.setOrderState(OrderEnum.State.REFUNDING.getCode());
        refundingStateVO.setCount(refundingCount);

        orderStateCountList.add(refundingStateVO);
        return orderStateCountList;
    }

    @Override
    public List<OrderStateCountVO> countOrderStateByAccount(Long accountId) {

        List<OrderStateCountVO> orderStateCountList = Optional.ofNullable(spuOrderDAO.countOrderStateByAccount(accountId))
                .orElse(Collections.emptyList());

        Integer refundingCount = refundRepository.countTotalRefundingByMemberId(accountId);

        OrderStateCountVO refundingStateVO = new OrderStateCountVO();
        refundingStateVO.setOrderState(OrderEnum.State.REFUNDING.getCode());
        refundingStateVO.setCount(refundingCount);

        orderStateCountList.add(refundingStateVO);
        return orderStateCountList;
    }

    @Override
    public void outOrderSave(List<OutOrder> outOrderList) {
        for (OutOrder outOrder : outOrderList) {
            OutOrderDO outOrderDO = SaleConvertUtil.buildOutOrderDO(outOrder);
            outOrderDAO.insert(outOrderDO);
        }
    }

    @Override
    public List<SkuOrderVO> querySkuOrderByOrderId(Long orderId, List<Long> skuIdList) {
        return skuOrderDAO.querySkuOrderByOrderId(orderId, skuIdList);
    }

    @Override
    public void deliverNotify(String outOrderNo, List<SkuCountDTO> skuCountDTOList, String expressCompanyName, String expressNo, Long channelId) {
        log.info("发货通知开发者: outOrderNo: " + outOrderNo + " : " + JSONObject.toJSONString(skuCountDTOList));
        NotifyEventContent notifyEventContent = new NotifyEventContent();
        notifyEventContent.setServiceType(NotifyEnums.ServiceType.ORDER.getCode());
        notifyEventContent.setBusinessType(NotifyEnums.OrderType.DELIVERY.getCode());
        ApiDeliverEvent object = new ApiDeliverEvent(outOrderNo, skuCountDTOList, expressCompanyName, expressNo);
        notifyEventContent.setEventInfo(JSONObject.toJSONString(object));
        notifyFacade.batchSend(Collections.singletonList(channelId), notifyEventContent);
    }

    @Override
    public RoleEnum.OrderType settleOrderType(Long supplierId) {
        return supplierFacade.settleOrderType(supplierId);
    }

    @Override
    public void updateOrderShip(Long orderId, String shipVo) {
        orderDAO.updateOrderShip(orderId, shipVo);
        spuOrderDAO.updateOrderShip(orderId, shipVo);
    }

    @Override
    public List<SpuOrder> listDOByOrderStateAndUpdateTimeLessThan(OrderEnum.State orderState, LocalDateTime updateTime) {
        List<SpuOrderDO> spuOrderDOS = spuOrderDAO.listDOByOrderStateAndUpdateTimeLessThan(orderState, updateTime);
        if (ObjectUtil.isEmpty(spuOrderDOS)) {
            return Collections.emptyList();
        }
        return TransferUtils.transfers(spuOrderDOS, item -> spuOrderAssembler.doToDomain(item));
    }

    @Override
    public void storeAccountPay(Long storeId, Long accountId, Integer memberAmount) {
        storeAccountPayUtil.storeAccountPayEvent(storeId, accountId, memberAmount);
    }

    @Override
    public void sendOrderNewRecordEvent(List<SpuOrder> spuOrderList, OrderEnum.State beforeOrderState, OrderEnum.State afterOrderState, Long operatorId, RoleEnum.CompanyRole operatorRoleId) {
        spuOrderList.forEach(spuOrder -> {
            OrderStateRecordRPC orderStateRecordRPC = new OrderStateRecordRPC();
            orderStateRecordRPC.setOrderId(spuOrder.getOrderId());
            orderStateRecordRPC.setSpuOrderId(spuOrder.getId());
            orderStateRecordRPC.setBeforeOrderState(beforeOrderState);
            orderStateRecordRPC.setBeforeStateDesc(beforeOrderState.getInfo());
            orderStateRecordRPC.setAfterOrderState(afterOrderState);
            orderStateRecordRPC.setAfterStateDesc(afterOrderState.getInfo());
            orderStateRecordRPC.setOrdererId(spuOrder.getAccountId());
            orderStateRecordRPC.setOperatorId(operatorId);
            orderStateRecordRPC.setOperatorRoleId(operatorRoleId);
            orderStateRecordRPC.setRoleDesc(operatorRoleId.getValue());
            orderStateRecordRPC.setOperateTime(LocalDateTime.now());
            orderStateRecordRPC.setCreateTime(LocalDateTime.now());
            orderStateRecordRPC.setUpdateTime(LocalDateTime.now());
            MQUtil.send(MQ.Tag.ORDER_STATE_RECORD_EVENT,orderStateRecordRPC);
            log.info("订单状态记录消息发送成功，orderStateRecordRPC: {}", orderStateRecordRPC);
        });
    }

    @Override
    public void savePrePayOrder(OrderCreateRes order, MemberOrderCreateCommand memberOrderCreateCommand) {
        redisClient.setCacheObject(getPrePayOrderKey(memberOrderCreateCommand), order,TIME_OUT, TimeUnit.SECONDS);
    }

    @Override
    public OrderCreateRes getPrePayOrder(MemberOrderCreateCommand memberOrderCreateCommand) {
        String cacheKey = getPrePayOrderKey(memberOrderCreateCommand);
        try {
            return redisClient.getCacheObject(cacheKey);
        } catch (ClassCastException e) {
            // 1. 记录详细日志，便于定位问题
            log.error("读取预支付订单缓存类型错误，key={}，错误信息：{}", cacheKey, e.getMessage(), e);
            // 2. 删除错误的缓存数据（避免重复报错）
            redisClient.deleteObject(cacheKey);
            // 3. 抛业务异常，上层统一处理
            throw new PlatformException(OrderErrorCode.NOT_EXISTS,"预支付订单缓存数据异常，请重新生成订单");
        }
    }

    @Override
    public void delPrePayOrder(MemberOrderCreateCommand memberOrderCreateCommand) {
        redisClient.deleteObject(getPrePayOrderKey(memberOrderCreateCommand));
    }

    @Override
    public Long getPrePayOrderExpire(MemberOrderCreateCommand memberOrderCreateCommand) {
        String prePayOrderKey = getPrePayOrderKey(memberOrderCreateCommand);
        return redisClient.getExpire(prePayOrderKey,TimeUnit.SECONDS);
    }

    private String getPrePayOrderKey(MemberOrderCreateCommand memberOrderCreateCommand){
        String key = PRE_PAY_ORDER_KEY + memberOrderCreateCommand.getStoreId() + "-" + memberOrderCreateCommand.getAccountId();
        log.info("预订单的key:{}",key);
        return key;
    }

    /**
     * 获取运费计算商品参数
     * @param orderGoodsList
     * @param skuVOList
     * @return
     */
    private Map<Long, List<FreightCalculateGoodsVO>> getGoodsInfoList(List<OrderItemCommand> orderGoodsList, List<SkuSaleInfo> skuVOList) {
        Map<Long, SkuSaleInfo> skuVOMap = skuVOList.stream().collect(Collectors.toMap(SkuSaleInfo::getSkuId, Function.identity()));
        Map<Long, List<FreightCalculateGoodsVO>> map = new HashMap<>(10);
        for (OrderItemCommand orderItemCommand : orderGoodsList) {
            SkuSaleInfo skuVO = skuVOMap.get(orderItemCommand.getSkuId());
            List<FreightCalculateGoodsVO> freightCalculateGoodsVOS = map.get(skuVO.getSupplierId());
            if(freightCalculateGoodsVOS == null){
                freightCalculateGoodsVOS = new ArrayList<>();
                map.put(skuVO.getSupplierId(), freightCalculateGoodsVOS);
            }
            FreightCalculateGoodsVO freightCalculateGoodsVO = new FreightCalculateGoodsVO();
            freightCalculateGoodsVO.setSkuId(skuVO.getSkuId());
            freightCalculateGoodsVO.setSpuId(skuVO.getSpuId());
            freightCalculateGoodsVO.setSkuNum(orderItemCommand.getCount());
            freightCalculateGoodsVO.setWeight(new BigDecimal(skuVO.getWeight() == null?0:skuVO.getWeight()));
            freightCalculateGoodsVO.setVolume(new BigDecimal(skuVO.getVolume() == null?0:skuVO.getWeight()));
            freightCalculateGoodsVO.setTemplateId(skuVO.getFreightTemplateId());
            freightCalculateGoodsVOS.add(freightCalculateGoodsVO);
        }
        return map;
    }

    /**
     * 获取运费计算地区参数
     * @param shipVO
     * @return
     */
    private RegionEasyVO getRegionEasyVO(ShipVO shipVO) {
        RegionEasyVO regionEasyVO = new RegionEasyVO();
        regionEasyVO.setProvinceCode(shipVO.getShipProvinceCode());
        regionEasyVO.setCityCode(shipVO.getShipCityCode());
        regionEasyVO.setAreaCode(shipVO.getShipAreaCode());
        return regionEasyVO;
    }
}
