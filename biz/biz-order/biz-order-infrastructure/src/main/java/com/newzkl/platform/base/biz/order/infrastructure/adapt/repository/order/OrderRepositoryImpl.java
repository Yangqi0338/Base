package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.api.ChannelApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.SupplierApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderRepository;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateVO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.order.*;
import com.newzkl.platform.base.biz.order.infrastructure.entity.*;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.MemberOrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.SkuOrderCommand;
import com.newzkl.platform.base.biz.order.model.req.query.*;
import com.newzkl.platform.base.biz.order.model.res.AlreadyDeliverRes;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.biz.order.model.support.api.EarningsConfigRpcVO;

import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.redis.RedisEnum;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.SettlementConfigOutVO;
import com.newzkl.platform.base.common.ddd.infrastructure.mybatis.model.BizCountMap;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.ddd.model.constant.OrderErrorCode;
import com.newzkl.platform.base.common.ddd.model.enums.finance.EarningsEnum;
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
    private final SkuOrderDAO skuOrderDAO;
    private final ChannelApi channelApi;
    private final DeliverDAO deliverDAO;
    private final OrderStateRecordDAO orderStateRecordDAO;

    /**
     * SkuOrderDTO 转 DO
     *
     * <p>SpuOrder 层折叠: spu/sku 快照字段收进 orderSkuInfo JSON 列, 3 个价格列去 sku 前缀,
     * TransferUtils 按同名映射覆盖不到, 需显式赋值</p>
     */
    private static SkuOrderDO toDO(SkuOrderDTO skuOrderDTO) {
        if (skuOrderDTO == null) {
            return null;
        }
        SkuOrderDO skuOrderDO = TransferUtils.transfer(skuOrderDTO, SkuOrderDO.class);
        skuOrderDO.setSupplierPrice(skuOrderDTO.getSkuSupplierPrice());
        skuOrderDO.setSalePrice(skuOrderDTO.getSkuSalePrice());
        skuOrderDO.setStorePrice(skuOrderDTO.getSkuStorePrice());
        // 部分更新场景(如只改 settleSendState)快照字段全 null, 此时不可 set 空对象, 否则 JSON 列被刷成 {}
        if (skuOrderDTO.getSpuId() != null || skuOrderDTO.getSkuId() != null
                || skuOrderDTO.getSpuName() != null || skuOrderDTO.getSkuName() != null) {
            OrderSkuInfo orderSkuInfo = new OrderSkuInfo();
            orderSkuInfo.setSpuId(skuOrderDTO.getSpuId());
            orderSkuInfo.setSpuName(skuOrderDTO.getSpuName());
            orderSkuInfo.setSpuImg(skuOrderDTO.getSpuImg());
            orderSkuInfo.setSkuId(skuOrderDTO.getSkuId());
            orderSkuInfo.setSkuName(skuOrderDTO.getSkuName());
            orderSkuInfo.setSkuWeight(skuOrderDTO.getSkuWeight());
            orderSkuInfo.setSkuVolume(skuOrderDTO.getSkuVolume());
            skuOrderDO.setOrderSkuInfo(orderSkuInfo);
        }
        return skuOrderDO;
    }

    /**
     * SkuOrderDO 转 DTO
     *
     * <p>SpuOrder 层折叠: orderSkuInfo JSON 列拆平回 DTO 平铺字段, 3 个价格列补回 sku 前缀</p>
     */
    private static SkuOrderDTO toDTO(SkuOrderDO skuOrderDO) {
        if (skuOrderDO == null) {
            return null;
        }
        SkuOrderDTO skuOrderDTO = TransferUtils.transfer(skuOrderDO, SkuOrderDTO.class);
        skuOrderDTO.setSkuSupplierPrice(skuOrderDO.getSupplierPrice());
        skuOrderDTO.setSkuSalePrice(skuOrderDO.getSalePrice());
        skuOrderDTO.setSkuStorePrice(skuOrderDO.getStorePrice());
        OrderSkuInfo orderSkuInfo = skuOrderDO.getOrderSkuInfo();
        if (orderSkuInfo != null) {
            skuOrderDTO.setSpuName(orderSkuInfo.getSpuName());
            skuOrderDTO.setSpuImg(orderSkuInfo.getSpuImg());
            skuOrderDTO.setSkuName(orderSkuInfo.getSkuName());
            skuOrderDTO.setSkuWeight(orderSkuInfo.getSkuWeight());
            skuOrderDTO.setSkuVolume(orderSkuInfo.getSkuVolume());
        }
        return skuOrderDTO;
    }

    /**
     * SkuOrderDO 转 VO
     *
     * <p>SpuOrder 层折叠: 与 {@link #toDTO} 同口径, 前端契约字段名保持 skuXxxPrice 不变</p>
     */
    private static SkuOrderVO toVO(SkuOrderDO skuOrderDO) {
        if (skuOrderDO == null) {
            return null;
        }
        SkuOrderVO skuOrderVO = TransferUtils.transfer(skuOrderDO, SkuOrderVO.class);
        skuOrderVO.setSkuSupplierPrice(skuOrderDO.getSupplierPrice());
        skuOrderVO.setSkuSalePrice(skuOrderDO.getSalePrice());
        skuOrderVO.setSkuStorePrice(skuOrderDO.getStorePrice());
        OrderSkuInfo orderSkuInfo = skuOrderDO.getOrderSkuInfo();
        if (orderSkuInfo != null) {
            skuOrderVO.setSpuName(orderSkuInfo.getSpuName());
            skuOrderVO.setSkuName(orderSkuInfo.getSkuName());
            skuOrderVO.setSkuWeight(orderSkuInfo.getSkuWeight());
            skuOrderVO.setSkuVolume(orderSkuInfo.getSkuVolume());
        }
        return skuOrderVO;
    }

    @Override
    public OrderDTO order(Long orderId) {
        OrderDO orderDO = orderDAO.selectById(orderId);
        return TransferUtils.transfer(orderDO, OrderDTO.class);
    }

    @Override
    public OrderDTO order(String orderNo) {
        OrderDO orderDO = getOne(orderDAO,orderDAO.getKeyLw(orderNo));
        return TransferUtils.transfer(orderDO, OrderDTO.class);
    }

    @Override
    public Page<OrderDTO> orderList(OrderQuery orderQuery) {
        Page<OrderDO> page = orderDAO.selectPage(RepositorySupport.page(orderQuery), orderDAO.getLw(orderQuery));
        return TransferUtils.transferPage(page,OrderDTO.class);
    }

    @Override
    public Map<OrderEnum.State, Integer> stateCountMap(OrderQuery orderQuery) {
        orderQuery.addGroupField(OrderDO::getOrderState);
        BizCountMap countMap = orderDAO.countMapWithOrderByQuery(orderDAO.getLw(orderQuery).unwrapAlias(), orderQuery);
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
        Page<SkuOrderDTO> result = TransferUtils.transferPage(page,SkuOrderDTO.class);
        result.setRecords(page.getRecords().stream().map(OrderRepositoryImpl::toDTO).collect(Collectors.toList()));
        return result;
    }

    @Override
    public int updateSkuRefundingCount(String orderNo, Long skuId, Integer count) {
        return skuOrderDAO.updateSkuRefundingCount(orderNo, skuId, count);
    }

    @Override
    public List<SkuRefundDTO> skuRefundResList(String orderNo, List<Long> skuIds) {
        SkuOrderQuery query = new SkuOrderQuery();
        query.setOrderNo(orderNo);
        query.setSkuIdList(skuIds);
        return skuOrderDAO.skuRefundResList(skuOrderDAO.getLw(query).unwrap("t"));
    }

    @Override
    public OrderAggVO orderAggVO(String orderNo) {
        OrderAggVO orderAggVO = new OrderAggVO();
        OrderDO orderDO = getOne(orderDAO, orderDAO.getKeyLw(orderNo));
        orderAggVO.setOrderVO(TransferUtils.transfer(orderDO, OrderVO.class));
        // 子表关联键为 order_no, 交易单不存在时不可放行空条件查询(否则捞全表 sku_order)
        if (orderDO == null) {
            orderAggVO.setSkuOrderList(Collections.emptyList());
            return orderAggVO;
        }
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderNo(orderDO.getOrderNo());
        List<SkuOrderDO> skuOrderDOS = skuOrderDAO.selectList(skuOrderDAO.getLw(skuOrderQuery));
        orderAggVO.setSkuOrderList(skuOrderDOS.stream().map(OrderRepositoryImpl::toVO).collect(Collectors.toList()));
        return orderAggVO;
    }

    @Override
    public int batchUpdateOrderState(List<String> orderNoList,  OrderEnum.State sourceState,  OrderEnum.State toState, OrderExt orderExt) {
        OrderDO orderDO = new OrderDO();
        if (toState == OrderEnum.State.CLOSE) {
            orderDO.setCloseTime(LocalDateTime.now());
        }
        OrderQuery query = new OrderQuery();
        query.setOrderNoList(orderNoList);
        query.setOrderState(sourceState);
        return orderDAO.update(orderDAO.getLw(query).toUpdate()
                .notEmptySet(OrderDO::getCloseTime, orderDO.getCloseTime())
                .notEmptySet(OrderDO::getOrderExt, orderExt)
                .append(OrderDO::getOrderStateLog, toState)
                .set(OrderDO::getOrderState,toState)
        );
    }

    @Override
    public List<Long> orderIdList(OrderQuery orderQuery) {
        return orderDAO.selectList(orderDAO.getLw(orderQuery)).stream()
                .map(OrderDO::getId)
                .collect(Collectors.toList());
    }

    @Override
    public int batchUpdateSkuOrderState(List<String> skuOrderNoList, OrderEnum.State from, OrderEnum.State to, SkuOrderCommand skuOrderCommand) {
        SkuOrderQuery query = new SkuOrderQuery();
        query.setSkuOrderNoList(skuOrderNoList);
        query.setOrderState(from);
        return skuOrderDAO.update(skuOrderDAO.getLw(query).toUpdate()
                .notEmptySet(SkuOrderDO::getDeliveredTime, skuOrderCommand.getDeliveredTime())
                .notEmptySet(SkuOrderDO::getReceiveTime,skuOrderCommand.getReceiveTime())
                .append(SkuOrderDO::getOrderStateLog, to)
                .set(SkuOrderDO::getOrderState,to)
        );
    }

    @Override
    public int batchUpdateSkuOrderState(List<String> orderNoList, OrderEnum.State sourceState, OrderEnum.State toState) {
        SkuOrderQuery query = new SkuOrderQuery();
        query.setOrderNoList(orderNoList);
        query.setOrderState(sourceState);
        return skuOrderDAO.update(skuOrderDAO.getLw(query).toUpdate()
                .append(SkuOrderDO::getOrderStateLog, toState)
                .set(SkuOrderDO::getOrderState,toState)
        );
    }


    @Override
    public List<OrderStateVO> accountOrderState(Long accountId, List<Long> orderIdList) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setChannelId(accountId);
        orderQuery.setIdList(orderIdList);
        return list(orderDAO, orderDAO.getLw(orderQuery), OrderStateVO.class);
    }

    @Override
    public OrderRelationVO orderRelation(Long orderId, Long spuId) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setIdList(Collections.singletonList(orderId));
        return getOne(orderDAO, orderDAO.getLw(orderQuery), OrderRelationVO.class);
    }

    @Override
    public List<OrderStateCheckDTO> checkOrderState(List<String> orderNoList) {
        return orderDAO.checkOrderState(orderNoList);
    }

    @Override
    public List<String> orderNoBySkuOrderNo(List<String> skuOrderNoList) {
        SkuOrderQuery query = new SkuOrderQuery();
        query.setSkuOrderNoList(skuOrderNoList);
        // 子表关联键为 order_no, 需再回查交易单主键; 单号为空时不可放行空条件查询(否则捞全表 order)
        return CollUtil.distinct(
                listOneField(skuOrderDAO, skuOrderDAO.getLw(query), SkuOrderDO::getOrderNo)
        );
    }

    @Override
    public int skuOrderEditForRefundPass(List<String> skuOrderNoList) {
        if (CollUtil.isEmpty(skuOrderNoList)) {
            return 0;
        }
        return skuOrderDAO.skuOrderEditForRefundPass(skuOrderNoList, OrderEnum.State.CLOSE);
    }

    @Override
    public int skuOrderEditForRefundClose(List<String> skuOrderNoList) {
        if (CollUtil.isEmpty(skuOrderNoList)) {
            return 0;
        }
        SkuOrderQuery query = new SkuOrderQuery();
        query.setSkuOrderNoList(skuOrderNoList);
        return skuOrderDAO.update(skuOrderDAO.getLw(query).toUpdate()
                .set(SkuOrderDO::getRefundingCount,0)
        );
    }

    @Override
    public int skuOrderSave(SkuOrderDTO skuOrderDTO, SkuOrderQuery skuOrderQuery) {
        SkuOrderDO skuOrderDO = toDO(skuOrderDTO);
        if (skuOrderQuery != null) {
            return skuOrderDAO.update(skuOrderDO, skuOrderDAO.getLw(skuOrderQuery));
        }else {
            return skuOrderDAO.insert(skuOrderDO);
        }
    }

    @Override
    public int skuOrderSave(List<SkuOrderDTO> skuList) {
        List<SkuOrderDO> skuOrderList = skuList.stream().map(OrderRepositoryImpl::toDO).collect(Collectors.toList());
        List<BatchResult> result = skuOrderDAO.insertOrUpdate(skuOrderList);
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
    public List<AlreadyDeliverRes> getAlreadyDeliverResList(String orderNo, List<Long> skuIds) {
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderNo(orderNo);
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
    public int updateSkuDeliverCount(String orderNo, DeliverItemVO deliverItem) {
        return skuOrderDAO.updateSkuDeliverCount(orderNo, deliverItem.getSkuId(), deliverItem.getCount());
    }

    @Override
    public List<Long> querySkuOrderIdList(String orderNo, List<Long> skuIdList) {
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderNo(orderNo);
        skuOrderQuery.setSkuIdList(skuIdList);
        return listOneField(skuOrderDAO, skuOrderDAO.getLw(skuOrderQuery), SkuOrderDO::getId);
    }

    @Override
    public List<String> querySkuOrderNoList(String orderNo, List<Long> skuIdList) {
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderNo(orderNo);
        skuOrderQuery.setSkuIdList(skuIdList);
        return listOneField(skuOrderDAO, skuOrderDAO.getLw(skuOrderQuery), SkuOrderDO::getOrderNo);
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
        return list.stream().map(OrderRepositoryImpl::toVO).collect(Collectors.toList());
    }

    @Override
    public EarningsEnum.SettleType settleOrderType(Long supplierId) {
        return supplierApi.settleOrderType(supplierId);
    }

    @Override
    public Integer orderCount(OrderQuery orderQuery) {
        return Math.toIntExact(orderDAO.selectCount(orderDAO.getLw(orderQuery)));
    }

    @Override
    public Money orderSumAmount(OrderQuery orderQuery) {
        return listOneField(orderDAO, orderDAO.getLw(orderQuery), OrderDO::getTotalAmount).stream()
                .reduce(Money.ZERO, Money::add);
    }

    @Override
    public List<GroupCountRes> orderCountComplete(TimeQuery timeQuery) {
        // 迁移: 原 ScmUtil.groupCountRes2Complete(依赖 new-scm DateSplitUtils) 内联至 infra
        // 按 groupType(0 小时/1 天) + groupCount 切片, 用已有统计填桶, 空桶补 0
        List<GroupCountRes> groupCountRes = orderDAO.orderCount(timeQuery);
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
    public void updateOrderShip(String orderNo, com.newzkl.platform.base.common.ddd.model.vo.ShipVO shipVo) {
        OrderDO orderDO = new OrderDO();
        // FIXME
        orderDO.setOrderNo(orderNo);
        orderDO.setShipVO(shipVo);
        orderDAO.updateById(orderDO);
    }

    @Override
    public List<OrderDTO> listDOByOrderStateAndUpdateTimeLessThan(OrderEnum.State orderState, LocalDateTime updateTime) {
        // 需要新增待支付时间，禁止使用updateTime不明动作更新时间来筛选 TODO
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setOrderState(orderState);
        orderQuery.initSortField("updateTime", true);
//        orderQuery.setUpdateTime(updateTime);
        List<OrderDO> orderDOS = orderDAO.selectList(orderDAO.getLw(orderQuery));
        return TransferUtils.transfers(orderDOS, OrderDTO.class);
    }

    @Override
    public List<DeliverVO> deliverListByQuery(DeliverQuery deliverQuery) {
        List<DeliverDO> list = deliverDAO.selectList(deliverDAO.getLw(deliverQuery));
        return TransferUtils.transfers(list,DeliverVO.class);
    }

    @Override
    public List<DeliverVO> deliverListByOrderNo(String orderNo) {
        DeliverQuery deliverQuery = new DeliverQuery();
        deliverQuery.setOrderNo(orderNo);
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

    @Override
    public List<String> orderNoList(OrderQuery orderQuery) {
        return orderDAO.selectList(orderDAO.getLw(orderQuery)).stream()
                .map(OrderDO::getOrderNo)
                .collect(Collectors.toList());
    }
}
