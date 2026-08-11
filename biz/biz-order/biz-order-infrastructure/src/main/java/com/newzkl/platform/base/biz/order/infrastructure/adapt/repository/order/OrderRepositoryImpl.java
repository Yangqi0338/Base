package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.api.ChannelApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.SupplierApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderRepository;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderStateVO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.order.*;
import com.newzkl.platform.base.biz.order.infrastructure.entity.*;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.MemberOrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.SkuOrderCommand;
import com.newzkl.platform.base.biz.order.model.req.query.*;
import com.newzkl.platform.base.biz.order.model.res.AlreadyDeliverRes;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.biz.order.model.support.api.EarningsConfigRpcVO;

import com.newzkl.platform.base.biz.order.model.support.api.openapi.ApiOrderStateEvent;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.mq.infrastructure.utils.NotifyUtil;
import com.newzkl.platform.base.common.core.mq.model.notify.NotifyEnums;
import com.newzkl.platform.base.common.core.mq.model.notify.NotifyEventCommand;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigOutVO;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.ddd.model.constant.OrderErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.SettleType;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.query.TimeQuery;
import com.newzkl.platform.base.common.ddd.model.res.GroupCountRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
@RequiredArgsConstructor
public class OrderRepositoryImpl extends RepositorySupport implements OrderRepository {

    private static final Long TIME_OUT = 60L * 60;

    private final SupplierApi supplierApi;
    private final OrderDAO orderDAO;
    private final SpuOrderDAO spuOrderDAO;
    private final SkuOrderDAO skuOrderDAO;
    private final ChannelApi channelApi;
    private final DeliverDAO deliverDAO;
    private final OrderStateRecordDAO orderStateRecordDAO;

    @Override
    public OrderDTO order(Long orderId) {
        OrderDO orderDO = orderDAO.selectById(orderId);
        return TransferUtils.transfer(orderDO, OrderDTO.class);
    }

    @Override
    public Page<OrderDTO> orderList(OrderQuery orderQuery) {
        Page<OrderDO> page = orderDAO.selectPage(RepositorySupport.page(orderQuery), orderDAO.getLw(orderQuery));
        return TransferUtils.transferPage(page,OrderDTO.class);
    }

    @Override
    public Page<SpuOrderDTO> spuOrderList(SpuOrderQuery spuOrderQuery) {
        Page<SpuOrderDO> page = spuOrderDAO.selectPage(RepositorySupport.page(spuOrderQuery), spuOrderDAO.getLw(spuOrderQuery));
        return TransferUtils.transferPage(page, SpuOrderDTO.class);
    }

    @Override
    public Map<OrderEnum.State, Integer> stateCountMap(SpuOrderQuery spuOrderQuery) {
        spuOrderQuery.addGroupField(SpuOrderDO::getOrderState);
        BizCountMap countMap = spuOrderDAO.countMapWithOrderByQuery(spuOrderDAO.getLw(spuOrderQuery).unwrapAlias(), spuOrderQuery);
        Map<OrderEnum.State, Integer> resultMap = new HashMap<>();
        countMap.forEach(map -> {
            Integer count = BizCountMap.getIntCount(map,0);
            Integer state = BizCountMap.getIntCount(map,1);
            resultMap.put(OrderEnum.State.getByCode(state), count);
        });
        return resultMap;
    }

    @Override
    public Page<SkuOrderDTO> skuOrderList(SkuOrderQuery orderQuery) {
        Page<SkuOrderDO> page = skuOrderDAO.selectPage(RepositorySupport.page(orderQuery), skuOrderDAO.getLw(orderQuery));
        return TransferUtils.transferPage(page,SkuOrderDTO.class);
    }

    @Override
    public int updateSkuRefundingCount(Long orderId, Long skuId, Integer count) {
        return skuOrderDAO.updateSkuRefundingCount(orderId, skuId, count);
    }

    @Override
    public int updateSpuRefundingCount(Long spuOrderId, Integer count) {
        return spuOrderDAO.updateSpuRefundingCount(spuOrderId, count);
    }

    @Override
    public List<SkuRefundDTO> skuRefundResList(Long spuOrderId, List<Long> skuIds) {
        SkuOrderQuery query = new SkuOrderQuery();
        query.setSpuOrderId(spuOrderId);
        query.setSkuIdList(skuIds);
        return skuOrderDAO.skuRefundResList(skuOrderDAO.getLw(query).unwrap("t"));
    }

    @Override
    public SpuOrderVO spuOrderVO(Long spuOrderId) {
        SpuOrderDO spuOrderDO = spuOrderDAO.selectById(spuOrderId);
        return TransferUtils.transfer(spuOrderDO,SpuOrderVO.class);
    }

    @Override
    public SpuOrderAggVO spuOrderAggVO(Long spuOrderId) {
        SpuOrderAggVO spuOrderAggVO = new SpuOrderAggVO();
        SpuOrderVO spuOrderVO = spuOrderVO(spuOrderId);
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setSpuOrderId(spuOrderId);
        List<SkuOrderDO> skuOrderVOS = skuOrderDAO.selectList(skuOrderDAO.getLw(skuOrderQuery));
        spuOrderAggVO.setSpuOrderVO(spuOrderVO);
        spuOrderAggVO.setSkuOrderList(TransferUtils.transfers(skuOrderVOS, SkuOrderVO.class));
        return spuOrderAggVO;
    }

    @Override
    public int batchUpdateOrderState(List<Long> orderIdList,  OrderEnum.State sourceState,  OrderEnum.State toState) {
        OrderQuery query = new OrderQuery();
        query.setIdList(orderIdList);
        query.setOrderState(sourceState);
        return orderDAO.update(orderDAO.getLw(query).toUpdate()
                .append(OrderDO::getOrderStateLog, toState)
                .set(OrderDO::getOrderState,toState)
        );
    }

    @Override
    public int batchUpdateSpuOrderState(List<Long> spuOrderIdList,  OrderEnum.State sourceState,  OrderEnum.State toState) {
        SpuOrderDO spuOrderDO = new SpuOrderDO();
        LocalDateTime now = LocalDateTime.now();
        if (toState == OrderEnum.State.WAIT_RECEIVE) {
            spuOrderDO.setDeliveredTime(now);
        } else if (toState == OrderEnum.State.DOWN_RECEIVE) {
            spuOrderDO.setReceiveTime(now);
        } else if (toState == OrderEnum.State.CLOSE) {
            spuOrderDO.setCloseTime(now);
        }
        SpuOrderQuery query = new SpuOrderQuery();
        query.setIdList(spuOrderIdList);
        query.setOrderState(sourceState);
        return spuOrderDAO.update(spuOrderDAO.getLw(query).toUpdate()
                .notEmptySet(SpuOrderDO::getDeliveredTime, spuOrderDO.getDeliveredTime())
                .notEmptySet(SpuOrderDO::getReceiveTime, spuOrderDO.getReceiveTime())
                .notEmptySet(SpuOrderDO::getCloseTime, spuOrderDO.getReceiveTime())
                .append(SpuOrderDO::getOrderStateLog, toState)
                .set(SpuOrderDO::getOrderState,toState)
        );
    }

    @Override
    public int batchUpdateSpuOrderStateByOrderId(List<Long> orderIdList, OrderEnum.State sourceState, OrderEnum.State toState, String spuOrderExt) {
        SpuOrderQuery query = new SpuOrderQuery();
        query.setOrderIdList(orderIdList);
        query.setOrderState(sourceState);
        return spuOrderDAO.update(spuOrderDAO.getLw(query).toUpdate()
                .notEmptySet(SpuOrderDO::getSpuOrderExt,spuOrderExt)
                .append(SpuOrderDO::getOrderStateLog, toState)
                .set(SpuOrderDO::getOrderState,toState)
        );
    }

    @Override
    public List<Long> orderIdList(OrderQuery orderQuery) {
        return orderDAO.selectList(orderDAO.getLw(orderQuery)).stream()
                .map(OrderDO::getId)
                .collect(Collectors.toList());
    }

    @Override
    public int batchUpdateSkuOrderState(List<Long> skuOrderIdList, OrderEnum.State from, OrderEnum.State to, SkuOrderCommand skuOrderCommand) {
        SkuOrderQuery query = new SkuOrderQuery();
        query.setIdList(skuOrderIdList);
        query.setOrderState(from);
        return skuOrderDAO.update(skuOrderDAO.getLw(query).toUpdate()
                .notEmptySet(SkuOrderDO::getDeliveredTime, skuOrderCommand.getDeliveredTime())
                .notEmptySet(SkuOrderDO::getReceiveTime,skuOrderCommand.getReceiveTime())
                .append(SkuOrderDO::getOrderStateLog, to)
                .set(SkuOrderDO::getOrderState,to)
        );
    }

    @Override
    public int batchUpdateSkuOrderStateByOrderId(List<Long> orderIdList, OrderEnum.State sourceState, OrderEnum.State toState) {
        SkuOrderQuery query = new SkuOrderQuery();
        query.setOrderIdList(orderIdList);
        query.setOrderState(sourceState);
        return skuOrderDAO.update(skuOrderDAO.getLw(query).toUpdate()
                .append(SkuOrderDO::getOrderStateLog, toState)
                .set(SkuOrderDO::getOrderState,toState)
        );
    }

    @Override
    public List<SpuOrderStateVO> accountOrderState(Long accountId, List<Long> spuOrderIdList) {
        SpuOrderQuery spuOrderQuery = new SpuOrderQuery();
        spuOrderQuery.setChannelId(accountId);
        spuOrderQuery.setIdList(spuOrderIdList);
        return list(spuOrderDAO, spuOrderDAO.getLw(spuOrderQuery), SpuOrderStateVO.class);
    }

    @Override
    public SpuOrderRelationVO spuOrderRelation(Long orderId, Long spuId) {
        SpuOrderQuery spuOrderQuery = new SpuOrderQuery();
        spuOrderQuery.setOrderId(orderId);
        spuOrderQuery.setSpuId(spuId);
        return getOne(spuOrderDAO, spuOrderDAO.getLw(spuOrderQuery), SpuOrderRelationVO.class);
    }

    @Override
    public List<OrderStateCheckDTO> checkSpuOrderState(List<Long> orderId) {
        if (ObjectUtil.isEmpty(orderId)) {
            return new ArrayList<>();
        }
        return spuOrderDAO.checkSpuOrderState(orderId);
    }

    @Override
    public List<OrderStateCheckDTO> checkOrderState(List<Long> orderId) {
        return orderDAO.checkOrderState(orderId);
    }

    @Override
    public List<Long> orderIdBySpuSkuOrderId(List<Long> spuOrderId, List<Long> skuOrderId) {
        SkuOrderQuery query = new SkuOrderQuery();
        query.setIdList(skuOrderId);
        query.setSpuOrderIdList(spuOrderId);
        return CollUtil.distinct(
                listOneField(skuOrderDAO, skuOrderDAO.getLw(query), SkuOrderDO::getOrderId)
        );
    }

    @Override
    public int skuOrderEditForRefundPass(List<Long> skuIdList) {
        if (CollUtil.isEmpty(skuIdList)) {
            return 0;
        }
        return skuOrderDAO.skuOrderEditForRefundPass(skuIdList, OrderEnum.State.CLOSE);
    }

    @Override
    public void orderStateNotify(OrderEnum.OrderType orderType, Long channelId, String outOrderNo, OrderEnum.State currentState, OrderEnum.State toState) {
        //渠道商选品订单的开发者通知
        if (OrderEnum.OrderType.CHANNEL == orderType) {
            NotifyEventCommand notifyEventCommand = new NotifyEventCommand();
            notifyEventCommand.setServiceType(NotifyEnums.ServiceType.ORDER.getCode());
            notifyEventCommand.setBusinessType(NotifyEnums.OrderType.TRADE_STATE.getCode());
            notifyEventCommand.setEventInfo(JSONObject.toJSONString(new ApiOrderStateEvent(outOrderNo,
                    currentState == null ? null : currentState.getCode(),
                    toState == null ? null : toState.getCode())));
            NotifyUtil.batchSend(Collections.singletonList(channelId), notifyEventCommand);
        }
    }

    @Override
    public int skuOrderEditForRefundClose(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return 0;
        }
        SkuOrderQuery query = new SkuOrderQuery();
        query.setIdList(idList);
        return skuOrderDAO.update(skuOrderDAO.getLw(query).toUpdate()
                .set(SkuOrderDO::getRefundingCount,0)
        );
    }

    @Override
    public int cutSkuOrderRefundingNumber(Long spuOrderId, List<Long> skuIdList) {
        if (CollUtil.isEmpty(skuIdList)) {
            return 0;
        }
        return spuOrderDAO.cutSkuOrderRefundingNumber(spuOrderId, skuIdList);
    }

    @Override
    public int skuOrderSave(SkuOrderDTO skuOrderDTO, SkuOrderQuery skuOrderQuery) {
        SkuOrderDO skuOrderDO = TransferUtils.transfer(skuOrderDTO, SkuOrderDO.class);
        if (skuOrderQuery != null) {
            return skuOrderDAO.update(skuOrderDO, skuOrderDAO.getLw(skuOrderQuery));
        }else {
            return skuOrderDAO.insert(skuOrderDO);
        }
    }

    @Override
    public int skuOrderSave(List<SkuOrderDTO> skuList) {
        List<SkuOrderDO> skuOrderList = TransferUtils.transfers(skuList, SkuOrderDO.class);
        List<BatchResult> result = skuOrderDAO.insertOrUpdate(skuOrderList);
        return 1;
    }

    @Override
    public int spuOrderSave(SpuOrderDTO spuOrderDTO, SpuOrderQuery spuOrderQuery) {
        SpuOrderDO spuOrderDO = TransferUtils.transfer(spuOrderDTO, SpuOrderDO.class);
        if (spuOrderQuery != null) {
            return spuOrderDAO.update(spuOrderDO, spuOrderDAO.getLw(spuOrderQuery));
        }else {
            return spuOrderDAO.insert(spuOrderDO);
        }
    }

    @Override
    public int spuOrderSave(List<SpuOrderDTO> spuList) {
        List<SpuOrderDO> spuOrderList = TransferUtils.transfers(spuList, SpuOrderDO.class);
        List<BatchResult> result = spuOrderDAO.insertOrUpdate(spuOrderList);
        return 1;
    }

    @Override
    public List<SettlementConfigOutVO> settlementConfigBatch(List<Long> supplierIdList) {
        return supplierApi.settlementConfigBatch(supplierIdList);
    }

    @Override
    public EarningsConfigRpcVO channelEarningsConfig(Long channelId) {
        return channelApi.channelEarningsConfig(channelId);
    }

    @Override
    public List<AlreadyDeliverRes> getAlreadyDeliverResList(Long spuOrderId, List<Long> skuIds) {
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setSpuOrderId(spuOrderId);
        skuOrderQuery.setSkuIdList(skuIds);
        return list(skuOrderDAO, skuOrderDAO.getLw(skuOrderQuery), AlreadyDeliverRes.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deliverSave(Deliver deliver) {
        DeliverDO model = TransferUtils.transfer(deliver, DeliverDO.class);
        return deliverDAO.insertOrUpdate(model);
    }

    @Override
    public int updateSkuDeliverCount(DeliverItemVO deliverItem) {
        return skuOrderDAO.updateSkuDeliverCount(deliverItem.getSpuOrderId(), deliverItem.getSkuId(), deliverItem.getCount());
    }

    @Override
    public List<Long> querySkuOrderIdList(Long spuOrderId, List<Long> skuIdList) {
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setSpuOrderId(spuOrderId);
        skuOrderQuery.setSkuIdList(skuIdList);
        return skuOrderDAO.selectList(skuOrderDAO.getLw(skuOrderQuery)).stream()
                .map(SkuOrderDO::getId)
                .collect(Collectors.toList());
    }

    @Override
    public Long spuOrderIdByOrderSku(Long orderId, Long skuId) {
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderId(orderId);
        skuOrderQuery.setSkuId(skuId);
        return findOneField(skuOrderDAO, skuOrderDAO.getLw(skuOrderQuery), SkuOrderDO::getSpuOrderId);
    }

    @Override
    public SpuOrderDTO spuOrder(Long spuOrderId) {
        SpuOrderDO spuOrderDO = spuOrderDAO.selectById(spuOrderId);
        return TransferUtils.transfer(spuOrderDO, SpuOrderDTO.class);
    }

    @Override
    public boolean orderSave(OrderDTO orderEdit) {
        OrderDO orderDO = TransferUtils.transfer(orderEdit, OrderDO.class);
        return orderDAO.insertOrUpdate(orderDO);
    }

    @Override
    public void deliverDelete(Long id) {
        deliverDAO.deleteById(id);
    }

    @Override
    public List<SkuOrderVO> querySkuOrderByOrderId(Long orderId, List<Long> skuIdList) {
        SkuOrderQuery query = new SkuOrderQuery();
        List<SkuOrderDO> list = skuOrderDAO.selectList(skuOrderDAO.getLw(query));
        return TransferUtils.transfers(list, SkuOrderVO.class);
    }

    @Override
    public List<SpuOrderItemExcelVO> querySpuOrderItemExcelVO(SpuOrderQuery spuOrderQuery) {
        // 纯 Java 编排: 先按 SPU 条件取 spu_order, 再按 spu_order_id 取 sku_order, 内存组装
        // 不写联表 SQL —— Base sku_order/spu_order 手迁 XML 用 *_no 列, 与 MP 实体 *_id 映射矛盾, 物理列口径未定
        // 二次加工(orderState 转义 / attribute 解析 / ship 拆分 / price 分转元)由 domain 层完成
        List<SpuOrderDO> spuOrderDOList = spuOrderDAO.selectList(spuOrderDAO.getLw(spuOrderQuery));
        if (CollUtil.isEmpty(spuOrderDOList)) {
            return Collections.emptyList();
        }
        Map<Long, SpuOrderDO> spuOrderMap = spuOrderDOList.stream()
                .collect(Collectors.toMap(SpuOrderDO::getId, Function.identity()));
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setSpuOrderIdList(new ArrayList<>(spuOrderMap.keySet()));
        List<SkuOrderDO> skuOrderDOList = skuOrderDAO.selectList(skuOrderDAO.getLw(skuOrderQuery));
        List<SpuOrderItemExcelVO> result = new ArrayList<>();
        for (SkuOrderDO skuOrder : skuOrderDOList) {
            SpuOrderDO spuOrder = spuOrderMap.get(skuOrder.getSpuOrderId());
            SpuOrderItemExcelVO vo = new SpuOrderItemExcelVO();
            vo.setId(skuOrder.getSpuOrderId() == null ? null : String.valueOf(skuOrder.getSpuOrderId()));
            vo.setOrderState(skuOrder.getOrderState() == null ? null : String.valueOf(skuOrder.getOrderState()));
            vo.setSpuName(skuOrder.getSpuName());
            vo.setAttribute(skuOrder.getSkuSaleAttribute());
            vo.setSkuId(skuOrder.getSkuId() == null ? null : String.valueOf(skuOrder.getSkuId()));
            vo.setPrice(skuOrder.getSkuSupplierPrice() == null ? null : String.valueOf(skuOrder.getSkuSupplierPrice()));
            vo.setCount(skuOrder.getCount() == null ? null : String.valueOf(skuOrder.getCount()));
            vo.setRefundingCount(skuOrder.getRefundingCount());
            if (spuOrder != null) {
                vo.setOutOrderNo(spuOrder.getOutOrderNo());
                vo.setShipVO(spuOrder.getShipVO());
                vo.setRemark(spuOrder.getRemark());
            }
            result.add(vo);
        }
        return result;
    }

    @Override
    public SettleType settleOrderType(Long supplierId) {
        return supplierApi.settleOrderType(supplierId);
    }

    @Override
    public Integer spuOrderCount(SpuOrderQuery spuOrderQuery) {
        return Math.toIntExact(spuOrderDAO.selectCount(spuOrderDAO.getLw(spuOrderQuery)));
    }

    @Override
    public Money spuOrderSumAmount(SpuOrderQuery spuOrderQuery) {
        return listOneField(spuOrderDAO, spuOrderDAO.getLw(spuOrderQuery), SpuOrderDO::getTotalAmount).stream()
                .reduce(Money.ZERO, Money::add);
    }

    @Override
    public List<GroupCountRes> orderCountComplete(TimeQuery timeQuery) {
        // 迁移: 原 ScmUtil.groupCountRes2Complete(依赖 new-scm DateSplitUtils) 内联至 infra
        // 按 groupType(0 小时/1 天) + groupCount 切片, 用已有统计填桶, 空桶补 0
        List<GroupCountRes> groupCountRes = spuOrderDAO.orderCount(timeQuery);
        Map<String, GroupCountRes> groupCountResMap = groupCountRes.stream()
                .collect(Collectors.toMap(GroupCountRes::getTransDay, Function.identity(), (a, b) -> a));
        int groupCount = timeQuery.getGroupCount() == null ? 1 : timeQuery.getGroupCount();
        boolean byHour = timeQuery.getGroupType() != null && timeQuery.getGroupType() == 0;
        String pattern = byHour ? "yyyy-MM-dd-HH" : "yyyy-MM-dd";
        Date startTime = new Date(timeQuery.getCreateBeginTime());
        Date endTime = new Date(timeQuery.getCreateEndTime());
        List<GroupCountRes> result = new ArrayList<>();
        for (Date cursor = startTime; cursor.getTime() < endTime.getTime();
             cursor = byHour ? DateUtil.offsetHour(cursor, groupCount) : DateUtil.offsetDay(cursor, groupCount)) {
            String format = DateUtil.format(cursor, pattern);
            GroupCountRes group = new GroupCountRes();
            group.setTransDay(format);
            GroupCountRes hit = groupCountResMap.get(format);
            if (hit != null) {
                group.setTransNum(hit.getTransNum());
                group.setTransAmount(hit.getTransAmount());
            }
            result.add(group);
        }
        return result;
    }

    @Override
    public void updateOrderShip(Long orderId, ShipVO shipVo) {
        OrderDO orderDO = new OrderDO();
        orderDO.setId(orderId);
        orderDO.setShipVO(shipVo);
        orderDAO.updateById(orderDO);
    }

    @Override
    public void updateSpuOrderShip(Long orderId, ShipVO shipVo) {
        SpuOrderQuery spuOrderQuery = new SpuOrderQuery();
        spuOrderQuery.setOrderId(orderId);
        spuOrderDAO.update(spuOrderDAO.getLw(spuOrderQuery).toUpdate().set(SpuOrderDO::getShipVO, shipVo));
    }

    @Override
    public List<SpuOrderDTO> listDOByOrderStateAndUpdateTimeLessThan(OrderEnum.State orderState, LocalDateTime updateTime) {
        // 需要新增待支付时间，禁止使用updateTime不明动作更新时间来筛选 TODO
        SpuOrderQuery spuOrderQuery = new SpuOrderQuery();
        spuOrderQuery.setOrderState(orderState);
        spuOrderQuery.initSortField("updateTime", true);
//        spuOrderQuery.setUpdateTime(updateTime);
        List<SpuOrderDO> spuOrderDOS = spuOrderDAO.selectList(spuOrderDAO.getLw(spuOrderQuery));
        return TransferUtils.transfers(spuOrderDOS, SpuOrderDTO.class);
    }

    @Override
    public List<DeliverVO> deliverListByQuery(DeliverQuery deliverQuery) {
        List<DeliverDO> list = deliverDAO.selectList(deliverDAO.getLw(deliverQuery));
        return TransferUtils.transfers(list,DeliverVO.class);
    }

    @Override
    public List<DeliverVO> deliverListBySpuOrderId(Long spuOrderId) {
        DeliverQuery deliverQuery = new DeliverQuery();
        deliverQuery.setSpuOrderIdList(Collections.singletonList(spuOrderId));
        List<DeliverDO> list = deliverDAO.selectList(deliverDAO.getLw(deliverQuery));
        return TransferUtils.transfers(list, DeliverVO.class);
    }

    @Override
    public void savePrePayOrder(OrderCreateRes order, MemberOrderCreateCommand memberOrderCreateCommand) {
        RedisUtil.set(getPrePayOrderKey(memberOrderCreateCommand), order, TIME_OUT, TimeUnit.SECONDS);
    }

    @Override
    public OrderCreateRes getPrePayOrder(MemberOrderCreateCommand memberOrderCreateCommand) {
        String cacheKey = getPrePayOrderKey(memberOrderCreateCommand);
        try {
            return RedisUtil.get(cacheKey);
        } catch (ClassCastException e) {
            // 1. 记录详细日志，便于定位问题
            log.error("读取预支付订单缓存类型错误，key={}，错误信息：{}", cacheKey, e.getMessage(), e);
            // 2. 删除错误的缓存数据（避免重复报错）
            RedisUtil.del(cacheKey);
            // 3. 抛业务异常，上层统一处理
            throw new PlatformException(OrderErrorCode.NOT_EXISTS,"预支付订单缓存数据异常，请重新生成订单");
        }
    }

    @Override
    public void delPrePayOrder(MemberOrderCreateCommand memberOrderCreateCommand) {
        RedisUtil.del(getPrePayOrderKey(memberOrderCreateCommand));
    }

    @Override
    public Long getPrePayOrderExpire(MemberOrderCreateCommand memberOrderCreateCommand) {
        String prePayOrderKey = getPrePayOrderKey(memberOrderCreateCommand);
        return RedisUtil.getRemainingTime(prePayOrderKey, TimeUnit.SECONDS);
    }

    private String getPrePayOrderKey(MemberOrderCreateCommand memberOrderCreateCommand){
        String key = RedisEnum.Key.PRE_PAY_ORDER.getCode(memberOrderCreateCommand.getStoreId(), memberOrderCreateCommand.getAccountId());
        log.info("预订单的key:{}",key);
        return key;
    }

    @Override
    public OrderStateRecordEntity createStateRecord(OrderStateRecordEntity record) {
        OrderStateRecordDO orderStateRecordDO = TransferUtils.transfer(record, OrderStateRecordDO.class);
        if (record.getId() == null){
            orderStateRecordDAO.insert(orderStateRecordDO);
            record.setId(orderStateRecordDO.getId());
        }else {
            orderStateRecordDAO.updateById(orderStateRecordDO);
        }
        return record;
    }

    @Override
    public Page<OrderStateRecordEntity> recordPage(OrderStateRecordQuery query) {
        Page<OrderStateRecordDO> page = orderStateRecordDAO.selectPage(RepositorySupport.page(query),orderStateRecordDAO.getLw(query));
        return TransferUtils.transferPage(page,OrderStateRecordEntity.class);
    }
}
