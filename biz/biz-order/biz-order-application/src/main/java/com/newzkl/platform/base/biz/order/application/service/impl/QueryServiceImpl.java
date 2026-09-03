package com.newzkl.platform.base.biz.order.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.application.service.QueryService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.AccountApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.ChannelApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.OrderRepository;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundRepository;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateVO;
import com.newzkl.platform.base.biz.order.model.dto.*;
import com.newzkl.platform.base.biz.order.model.req.query.DeliverQuery;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.support.api.StoreRPCVO;
import com.newzkl.platform.base.biz.order.model.vo.*;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.facade.AccountGroupVO;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder.isDev;

/**
 * @author muc_fang
 * @Description:
 * @date 2024/3/129:43
 */
@Service
@RequiredArgsConstructor
public class QueryServiceImpl implements QueryService {

    private final OrderRepository orderRepository;
    private final ChannelApi channelApi;
    private final RefundRepository refundRepository;
    private final AccountApi accountApi;
    private final GoodsApi goodsApi;

    @Override
    public Page<OrderVO> orderVOList(OrderQuery orderQuery) {
        Page<OrderDTO> orderDTOPage = orderRepository.orderList(orderQuery);
        return TransferUtils.transferPage(orderDTOPage, OrderVO.class);
    }

    @Override
    public Page<SkuOrderVO> skuOrderVOList(SkuOrderQuery orderQuery) {
        Page<SkuOrderDTO> skuOrderDTOPage = orderRepository.skuOrderList(orderQuery);
        return TransferUtils.transferPage(skuOrderDTOPage, SkuOrderVO.class);
    }
    @Override
    public OrderAggVO orderAggVO(String orderNo) {
        OrderAggVO orderAggVO = orderRepository.orderAggVO(orderNo);
        //物流信息
        Map<Long, List<DeliverVO>> deliverMap = this.orderDeliverInfo(orderNo);
        this.buildDeliver(deliverMap, orderAggVO);
        OrderVO orderVO = orderAggVO.getOrderVO();
        OrderExt orderExt = Optional.ofNullable(orderVO.getOrderExt()).orElseGet(OrderExt::new);
        Long memberId = orderVO.getMemberId();
        Long channelId = orderVO.getChannelId();

        List<Long> storeIds = Collections.singletonList(channelId);
        List<StoreRPCVO> storeRPCVOS =CollUtil.isNotEmpty(storeIds)? goodsApi.batchQueryStoreInfo(storeIds):Collections.emptyList();
        if (CollUtil.isNotEmpty(storeRPCVOS)){
            StoreRPCVO storeRPCVO = storeRPCVOS.get(0);
            if (storeRPCVO != null){
                orderVO.setStoreName(storeRPCVO.getName());
                orderVO.setStoreHead(storeRPCVO.getLogo());
                orderExt.setStoreName(storeRPCVO.getName());
                orderExt.setStoreHead(storeRPCVO.getLogo());
            }
        }

        List<Long> memberIds = new ArrayList<>();
        memberIds.add(memberId);
        memberIds.add(channelId);
        memberIds = memberIds.stream().distinct().collect(Collectors.toList());
        List<AccountGroupVO> accountGroupVOS =CollUtil.isNotEmpty(memberIds)? accountApi.listAccountByIds(memberIds):Collections.emptyList();
        if (CollUtil.isNotEmpty(accountGroupVOS)){
            Map<Long, AccountGroupVO> accountMap = accountGroupVOS.stream().collect(Collectors.toMap(AccountGroupVO::getId, v -> v));
            AccountGroupVO storeAccountDto = accountMap.get(orderVO.getChannelId());
            if (storeAccountDto != null){
                orderExt.setStoreAccount(storeAccountDto.getUserAccount());
            }
            AccountGroupVO memberAccount = accountMap.get(orderVO.getAccountId());
            if (memberAccount != null){
                orderExt.setUserAccount(memberAccount.getUserAccount());
                orderExt.setUserName(memberAccount.getPhone());
                orderExt.setNickName(memberAccount.getNickname());
                orderExt.setMemberHead(memberAccount.getHead());
                orderVO.setUsername(memberAccount.getPhone());
                orderVO.setNickname(memberAccount.getNickname());
            }
        }

        orderVO.setOrderExt(orderExt);
        if (orderVO.getRefundingCount() != null && orderVO.getRefundingCount() > 0) {
            RefundDTO refundDTO = refundRepository.refundByOrderNo(orderNo);
            orderVO.setRefundState(refundDTO.getRefundState());
            orderVO.setRefundType(refundDTO.getRefundType());
        }
        if (orderVO.getOrderState() == OrderEnum.State.MEMBER_WAIT_PAY) {
            long remainTime = orderVO.getCreateTime().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            orderVO.setRemainTime(remainTime);
        }else if (orderVO.getOrderState() == OrderEnum.State.CHANNEL_WAIT_PAY){
            long remainTime;
            if(isDev()){//测试环境7分钟
                remainTime = orderVO.getUpdateTime().plusMinutes(7).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }else {//生产环境5天
                remainTime = orderVO.getUpdateTime().plusDays(5).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
            orderVO.setRemainTime(remainTime);
        }
        return orderAggVO;
    }

    @Override
    public Page<OrderAggVO> orderAggVOList(OrderQuery orderQuery) {
        // ========== 1. 处理渠道名称查询：转换为渠道ID列表 ==========
        String channelName = orderQuery.getChannelName();
        if (StrUtil.isNotBlank(channelName)) {
            List<ChannelDTO> channelVOs = channelApi.channelList(null, channelName);

            // 渠道不存在则直接返回空分页
            if (CollUtil.isEmpty(channelVOs)) {
                return new Page<>();
            }
            orderQuery.setChannelIdList(channelVOs.stream().map(ChannelDTO::getId).collect(Collectors.toList()));
        }
        String nickname = orderQuery.getNickname();
        if (StrUtil.isNotBlank(nickname)) {
            List<Long> longs = accountApi.queryMember(nickname);
            if (CollUtil.isEmpty(longs)){
                return new Page<>();
            }
            orderQuery.setMemberIdList(longs);
        }
        // ========== 2. 处理门店账号查询：转换为渠道ID列表 ==========
        String storeAccount = orderQuery.getStoreAccount();
        if (StrUtil.isNotBlank(storeAccount)) {
            AccountGroupVO accountGroup = accountApi.selectByUserAccount(storeAccount);
            AccountEnum.Identity identity = SecurityUtils.getIdentity();
            if (AccountEnum.Identity.CHANNEL == identity) {
                Optional.ofNullable(accountGroup)
                        .ifPresent(ag -> orderQuery.setMemberIdList(Collections.singletonList(ag.getId())));
            }else if (AccountEnum.Identity.MEMBER == identity){
                Optional.ofNullable(accountGroup)
                        .ifPresent(ag -> orderQuery.setChannelIdList(Collections.singletonList(ag.getId())));
            }

        }
        
        // ========== 3. 查询交易单分页数据 ==========
        Page<OrderDTO> orderPage = orderRepository.orderList(orderQuery);
        List<String> orderNoLis = orderPage.getRecords().stream().map(OrderDTO::getOrderNo).collect(Collectors.toList());

        // 交易单为空则返回空分页
        if (CollUtil.isEmpty(orderNoLis)) {
            return new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        }

        // ========== 4. 查询SKU订单+物流信息，并构建分组映射 ==========
        // 4.1 查询SKU订单并按交易单号分组(子表关联键为 order_no)
        List<String> orderNos = orderPage.getRecords().stream().map(OrderDTO::getOrderNo).collect(Collectors.toList());
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderNoList(orderNos);
        List<SkuOrderVO> skuOrderList = TransferUtils.transfers(orderRepository.skuOrderList(skuOrderQuery).getRecords(), SkuOrderVO.class);
        Map<String, List<SkuOrderVO>> skuOrderMapByOrderNo =
            skuOrderList.stream().collect(Collectors.groupingBy(SkuOrderVO::getOrderNo));

        // 4.2 查询物流信息并构建映射
        Map<String, Map<Long, List<DeliverVO>>> deliverMap = this.orderDeliverInfo(orderNoLis);
        List<OrderVO> orderVOList = TransferUtils.transfers(orderPage.getRecords(), OrderVO.class);
        List<Long> memberIds = orderVOList.stream().map(OrderVO::getAccountId).collect(Collectors.toList());
        List<Long> storeIds = orderVOList.stream().map(OrderVO::getChannelId).distinct().collect(Collectors.toList());
        memberIds.addAll(storeIds);
        memberIds = memberIds.stream().distinct().collect(Collectors.toList());
        List<AccountGroupVO> accountGroupVOS =CollUtil.isNotEmpty(memberIds)? accountApi.listAccountByIds(memberIds):Collections.emptyList();
        Map<Long, AccountGroupVO> accountMap = accountGroupVOS.stream().collect(Collectors.toMap(AccountGroupVO::getId, v -> v));
        List<StoreRPCVO> storeRPCVOS =CollUtil.isNotEmpty(storeIds)? goodsApi.batchQueryStoreInfo(storeIds):Collections.emptyList();
        Map<Long, StoreRPCVO> storeMap = storeRPCVOS.stream().collect(Collectors.toMap(StoreRPCVO::getId, v -> v));
        // ========== 5. 组装交易单聚合VO列表 ==========
        List<OrderAggVO> aggVOList = new ArrayList<>();
        for (OrderVO orderVO : orderVOList) {
            String orderNo = orderVO.getOrderNo();
            OrderAggVO aggVO = new OrderAggVO();

            // 5.1 基础信息填充
            aggVO.setOrderVO(orderVO);
            aggVO.setSkuOrderList(skuOrderMapByOrderNo.getOrDefault(orderVO.getOrderNo(), new ArrayList<>()));

            // 5.2 处理交易单扩展信息
            OrderExt orderExt = Optional.ofNullable(orderVO.getOrderExt()).orElseGet(OrderExt::new);
            StoreRPCVO storeRPCVO = storeMap.get(orderVO.getChannelId());
            if (storeRPCVO != null){
                orderVO.setStoreName(storeRPCVO.getName());
                orderVO.setStoreHead(storeRPCVO.getLogo());
                orderExt.setStoreName(storeRPCVO.getName());
                orderExt.setStoreHead(storeRPCVO.getLogo());
            }
            AccountGroupVO storeAccountDto = accountMap.get(orderVO.getChannelId());
            if (storeAccountDto != null){
                orderExt.setStoreAccount(storeAccountDto.getUserAccount());
            }
            AccountGroupVO memberAccount = accountMap.get(orderVO.getAccountId());
            if (memberAccount != null){
                orderExt.setUserAccount(memberAccount.getUserAccount());
                orderExt.setUserName(memberAccount.getPhone());
                orderExt.setNickName(memberAccount.getNickname());
                orderExt.setMemberHead(memberAccount.getHead());
                orderVO.setUsername(memberAccount.getPhone());
                orderVO.setNickname(memberAccount.getNickname());
            }
            orderVO.setOrderExt(orderExt);
            // 5.3 处理退款状态
            if (orderVO.getRefundingCount() != null && orderVO.getRefundingCount() > 0) {
                RefundDTO refundDTO = refundRepository.refundByOrderNo(orderNo);
                orderVO.setRefundState(refundDTO.getRefundState());
                orderVO.setRefundType(refundDTO.getRefundType());
            }
            
            // 5.4 填充物流信息
            buildDeliver(deliverMap.get(orderNo), aggVO);

            // 5.5 处理待支付订单剩余时间
            if (Objects.equals(orderVO.getOrderState(), OrderEnum.State.MEMBER_WAIT_PAY)) {
                long remainTime = orderVO.getCreateTime().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                orderVO.setRemainTime(remainTime);
            }else if (Objects.equals(orderVO.getOrderState(), OrderEnum.State.CHANNEL_WAIT_PAY)){
                long remainTime;
                if(isDev()){//测试环境7分钟
                    remainTime = orderVO.getUpdateTime().plusMinutes(7).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                }else {//生产环境5天
                    remainTime = orderVO.getUpdateTime().plusDays(5).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                }
                orderVO.setRemainTime(remainTime);
            }

            aggVOList.add(aggVO);
        }

        // ========== 6. 转换分页返回 ==========
        Page<OrderAggVO> resultPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        resultPage.setRecords(aggVOList);
        return resultPage;
    }
    @Override
    public List<Long> orderIdList(OrderQuery orderQuery) {
        return orderRepository.orderIdList(orderQuery);
    }
    @Override
    public List<String> orderNoList(OrderQuery orderQuery) {
        return orderRepository.orderNoList(orderQuery);
    }
    @Override
    public List<OrderStateVO> accountOrderState(Long accountId, List<Long> orderIdList) {
        return orderRepository.accountOrderState(accountId, orderIdList);
    }

    @Override
    public OrderRelationVO orderRelation(Long orderId, Long spuId) {
        return orderRepository.orderRelation(orderId, spuId);
    }

    @Override
    public Map<Long, List<DeliverVO>> orderDeliverInfo(String orderNo) {
        DeliverQuery deliverQuery = new DeliverQuery();
        deliverQuery.setOrderNo(orderNo);
        List<DeliverVO> deliverVOS = orderRepository.deliverListByQuery(deliverQuery);
        //组装数据
        Map<Long, List<DeliverVO>> deliverVOList = new HashMap<>();
        for (DeliverVO deliverVO : deliverVOS) {
            List<DeliverItemVO> deliverItemVOS = JSONObject.parseArray(deliverVO.getItem(), DeliverItemVO.class);
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

    /**
     * 批量查询物流信息
     *
     * @param orderNoList
     * @return
     */
    private Map<String, Map<Long, List<DeliverVO>>> orderDeliverInfo(List<String> orderNoList) {
        DeliverQuery deliverQuery = new DeliverQuery();
        deliverQuery.setOrderNoList(orderNoList);
        List<DeliverVO> deliverVOS = orderRepository.deliverListByQuery(deliverQuery);
        //组装数据 - 先按spuOrderId分组，再按skuId分组
        Map<String, Map<Long, List<DeliverVO>>> result = new HashMap<>();
        for (DeliverVO deliverVO : deliverVOS) {
            String currentOrderNo = deliverVO.getOrderNo();
            // 确保外层Map中存在该spuOrderId的分组
            if (!result.containsKey(currentOrderNo)) {
                result.put(currentOrderNo, new HashMap<>());
            }
            Map<Long, List<DeliverVO>> skuDeliverMap = result.get(currentOrderNo);

            // 内部按skuId分组（保持原有逻辑）
            List<DeliverItemVO> deliverItemVOS = JSONObject.parseArray(deliverVO.getItem(), DeliverItemVO.class);
            for (DeliverItemVO deliverItemVO : deliverItemVOS) {
                if (!skuDeliverMap.containsKey(deliverItemVO.getSkuId())) {
                    skuDeliverMap.put(deliverItemVO.getSkuId(), new ArrayList<>());
                    skuDeliverMap.get(deliverItemVO.getSkuId()).add(deliverVO);
                } else {
                    skuDeliverMap.get(deliverItemVO.getSkuId()).add(deliverVO);
                }
            }
        }
        return result;
    }



    /**
     * 填充物流信息
     */
    private void buildDeliver(Map<Long, List<DeliverVO>> map, OrderAggVO orderAggVO) {
        if(map != null) {
            for (SkuOrderVO skuOrderVO : orderAggVO.getSkuOrderList()) {
                List<DeliverVO> deliverList = map.get(skuOrderVO.getSkuId());
                if (deliverList != null) {
                    skuOrderVO.setDeliverVOList(deliverList);
                }
            }
        }
    }
}
