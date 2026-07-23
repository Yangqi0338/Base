package com.newzkl.platform.base.biz.order.application.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkl.scm.finance.model.account.res.ChannelNowServiceFeeRes;
import com.zkl.scm.finance.model.purse.req.BalancePayReq;
import com.zkl.scm.finance.model.purse.res.BalancePayRes;
import com.zkl.scm.finance.rpc.facade.account.IAccountPurseConfigFacade;
import com.zkl.scm.finance.rpc.facade.pay.IBalancePayFacade;
import com.zkl.scm.goods.rpc.facade.ISpuFacade;
import com.zkl.scm.goods.rpc.facade.IStoreFacade;
import com.zkl.scm.goods.rpc.model.order.OrderSkuVO;
import com.zkl.scm.goods.rpc.model.spu.InventoryExecuteReq;
import com.zkl.scm.goods.rpc.model.store.StoreRPCVO;
import com.zkl.scm.goods.rpc.model.terminal.StoreAccountPayMsg;
import com.newzkl.platform.base.common.core.redis.utils.RedisUtil;
import com.newzkl.platform.base.biz.order.model.enums.finance.PurseEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.order.model.enums.user.identity.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderErrorCode;
import com.zkl.scm.openapi.facade.ThirdPartyOrderFacade;
import com.newzkl.platform.base.biz.order.application.service.IQueryOrderService;
import com.newzkl.platform.base.biz.order.domain.factory.ThirdPartyOrderStrategyFactory;
import com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.ThirdPartyOrderResult;
import com.newzkl.platform.base.biz.order.domain.factory.freightStrategy.ThirdPartyOrderStrategy;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.*;
import com.newzkl.platform.base.biz.order.infrastructure.adapt.repository.OrderOperationRecordUtils;
import com.newzkl.platform.base.biz.order.model.order.dto.*;
import com.newzkl.platform.base.biz.order.model.order.req.CommitOrderPreReq;
import com.newzkl.platform.base.biz.order.model.order.req.SpuOrderPageReq;
import com.newzkl.platform.base.biz.order.model.order.res.CreateOrderRes;
import com.newzkl.platform.base.biz.order.model.order.util.OrderRedisKeyUtils;
import com.newzkl.platform.base.biz.order.model.order.util.RecalculateServiceAmountUtils;
import com.newzkl.platform.base.biz.order.model.order.vo.OrderStateCountVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SkuOrderVO;
import com.newzkl.platform.base.biz.order.model.order.vo.SpuOrderVO;
import com.zkl.scm.user.model.account.req.ChannelQueryRpcReq;
import com.zkl.scm.user.model.account.res.ChannelRes;
import com.zkl.scm.user.rpc.facade.IAccountFacade;
import com.zkl.scm.user.rpc.facade.IChannelFacade;
import com.zkl.scm.user.rpc.model.AccountGroupVO;
import com.newzkl.platform.base.common.core.utils.biz.ScmUtil;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.PatternUtil;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.spring.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sijiwang
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QueryOrderServiceImpl implements IQueryOrderService {

    @DubboReference
    private IChannelFacade channelFacade;

    @DubboReference
    private IAccountFacade accountFacade;

    @DubboReference
    private IStoreFacade storeFacade;

    @DubboReference
    private IAccountPurseConfigFacade purseConfigDomain;

    @DubboReference
    private IBalancePayFacade balancePayFacade;

    @DubboReference
    private ThirdPartyOrderFacade thirdPartyOrderService;

    @DubboReference
    private ISpuFacade spuFacade;


    private final OrderOperationRecordUtils orderOperationRecordUtil;

    private final IOrderRepository orderRepository;

    private final ISpuOrderRepository spuOrderRepository;

    private final ISkuOrderRepository skuOrderRepository;

    private final IOutOrderRepository outOrderRepository;

    private final IOrderDeliveryItemRepository orderDeliveryItemRepository;

    @Override
    public CreateOrderRes selectOrderPre(CommitOrderPreReq req) {
        String redisKey = OrderRedisKeyUtils.getPrePayOrderKey(req.getOrderNo(), req.getAccountId());
        return RedisUtil.get(redisKey);
    }

    @Override
    public Page<SpuOrderVO> spuPage(SpuOrderPageReq req) {
        // 处理渠道名称查询
        processChannelNameQuery(req);

        // 处理昵称查询
        processNicknameQuery(req);

        // 处理门店账号查询
        processStoreAccountQuery(req);

        // 执行分页查询
        Page<SpuOrder> spuOrderIPage = spuOrderRepository.pageQuery(req);
        List<SpuOrder> records = spuOrderIPage.getRecords();

        if (CollectionUtils.isEmpty(records)) {
            return createEmptyPage(spuOrderIPage);
        }

        // 查询相关数据
        QueryResult queryResult = fetchRelatedData(records);

        // 构建返回结果
        List<SpuOrderVO> spuOrderVOList = buildSpuOrderVOList(records, queryResult);

        // 构建返回的分页对象
        return buildResultPage(spuOrderIPage, spuOrderVOList);
    }

    /**
     * 根据spuOrderNo查询订单详情
     * @param spuOrderNo spu订单号
     * @return SpuOrderVO
     */
    @Override
    public SpuOrderVO getSpuOrderDetail(String spuOrderNo) {
        if (StrUtil.isBlank(spuOrderNo)) {
            return null;
        }

        // 查询SPU订单
        SpuOrder spuOrder = spuOrderRepository.getBySpuOrderNo(spuOrderNo);
        if (spuOrder == null) {
            return null;
        }

        // 查询相关数据
        QueryResult queryResult = fetchRelatedData(Collections.singletonList(spuOrder));

        // 构建并返回SpuOrderVO
        List<SpuOrderVO> spuOrderVOList = buildSpuOrderVOList(Collections.singletonList(spuOrder), queryResult);

        return spuOrderVOList.isEmpty() ? null : spuOrderVOList.get(0);
    }

    @Override
    public List<OrderStateCountVO> countOrderStateByChannel(Long channelId) {
        return spuOrderRepository.countOrderStateByChannel(channelId);
    }

    @Override
    public List<OrderStateCountVO> countOrderStateByAccount(Long accountId) {
        return spuOrderRepository.countOrderStateByAccount(accountId);
    }

    @Override
    public void orderBalancePay(String... orderNos) {
        for (String orderNo : orderNos) {
            Order order = orderRepository.getByOrderNo(orderNo);
            List<SpuOrder> spuOrders = spuOrderRepository.listByOrderNo(orderNo);
            List<SkuOrder> skuOrders = skuOrderRepository.listByOrderNo(orderNo);
            //检查状态
            ScmUtil.checkInState(Collections.singletonList(OrderEnum.State.CHANNEL_WAIT_PAY.getCode()), order.getOrderState(), OrderErrorCode.STATE_ERROR);
            //可能会发生服务费变化 MQ TODO
            recalculateServiceAmount(order, spuOrders, skuOrders);
            //扣减余额
            BalancePayReq balancePayReq = getBalancePayReq(order);
            BalancePayRes balancePayResult = balancePayFacade.balancePay(balancePayReq);
            if (BooleanUtil.isTrue(balancePayResult.isOperatorPayState())) {
                allPaySuccess(order, spuOrders, skuOrders);
                if (Objects.isNull(SecurityUtils.getAccountId())){
                    orderOperationRecordUtil.sendOrderNewRecordEvent(spuOrders, OrderEnum.State.CHANNEL_WAIT_PAY.getCode(),OrderEnum.State.SENDING.getCode(),0L,RoleEnum.CompanyRole.PLATFORM);
                }else {
                    orderOperationRecordUtil.sendOrderNewRecordEvent(spuOrders, OrderEnum.State.CHANNEL_WAIT_PAY.getCode(),OrderEnum.State.SENDING.getCode(),SecurityUtils.getAccountId(),SecurityUtils.getRole());
                }
            } else {
                if (RoleEnum.CompanyRole.OPERATOR.equals(SecurityUtils.getRole())) {
                    throw new ScmException(OrderErrorCode.AMOUNT_LESS);
                }
                if (BooleanUtil.isTrue(balancePayResult.isPayState())) {
                    channelPaySuccess(order);
                } else {
                    throw new ScmException(OrderErrorCode.AMOUNT_LESS);
                }
            }
        }
    }

    @Override
    public void operatorOrderBalancePay(String orderNo) {
        Order order = orderRepository.getByOrderNo(orderNo);
        List<SpuOrder> spuOrders = spuOrderRepository.listByOrderNo(orderNo);
        List<SkuOrder> skuOrders = skuOrderRepository.listByOrderNo(orderNo);
        //检查状态
        ScmUtil.checkInState(Collections.singletonList(OrderEnum.State.OPERATOR_WAIT_PAY.getCode()), order.getOrderState(), OrderErrorCode.STATE_ERROR);
        //扣减余额
        BalancePayReq balancePayReq = getOperatorBalancePayReq(order);
        BalancePayRes balancePayResult = balancePayFacade.balancePay(balancePayReq);
        if(BooleanUtil.isTrue(balancePayResult.isPayState())){
            allPaySuccess(order, spuOrders, skuOrders);
            orderOperationRecordUtil.sendOrderNewRecordEvent(spuOrders, OrderEnum.State.CHANNEL_WAIT_PAY.getCode(),OrderEnum.State.SENDING.getCode(),SecurityUtils.getAccountId(),SecurityUtils.getRole());
        }
    }

    @Override
    public void orderDirectPay(String orderNo) {
        Order order = orderRepository.getByOrderNo(orderNo);
        //检查状态
        ScmUtil.checkInState(Collections.singletonList(OrderEnum.State.MEMBER_WAIT_PAY.getCode()), order.getOrderState(), OrderErrorCode.STATE_ERROR);
        if(!OrderEnum.OrderType.MEMBER.getCode().equals(order.getOrderType())){
            throw new ScmException(BaseErrorCode.PARAM, "只有C端订单才能直接支付");
        }
        //支付成功处理
        doMemberPaySuccess(order);
    }

    @Override
    public SpuOrder selectSpuOrder(String spuOrderNo) {
        return spuOrderRepository.getBySpuOrderNo(spuOrderNo);
    }

    @Override
    public List<SkuOrder> getBySpuOrderNo(String spuOrderNo) {
        return skuOrderRepository.getBySpuOrderNo(spuOrderNo);
    }

    public void doMemberPaySuccess(Order order) {
        if(order.getChannelPurchaseAmount() > 0){
            //如果选品金额大于0则进行渠道商支付
            BalancePayReq balancePayReq = getBalancePayReq(order);
            BalancePayRes balancePayResult = balancePayFacade.balancePay(balancePayReq);
            if(BooleanUtil.isTrue(balancePayResult.isOperatorPayState())){
                //orderAgg.allPaySuccess(localMessageFacade, orderRepository);
            }else {
                if(BooleanUtil.isTrue(balancePayResult.isPayState())) {
                    channelPaySuccess(order);
                }else {
                    throw new ScmException(BaseErrorCode.CUSTOM, "支付失败");
                }
            }
        }else {
            //支付成功
            //orderAgg.allPaySuccess(localMessageFacade, orderRepository);
        }
    }
    private void allPaySuccess(Order order,List<SpuOrder> spuOrders,List<SkuOrder> skuOrders){


        orderRepository.updateOrderChainStateByOrderNo(order.getOrderNo(), OrderEnum.State.CHANNEL_WAIT_PAY.getCode(), OrderEnum.State.SENDING.getCode(), null);


        //门店用户支付
        StoreAccountPayMsg storeAccountPayMsg = new StoreAccountPayMsg();
        storeAccountPayMsg.setStoreId(order.getStoreId());
        storeAccountPayMsg.setAccountId(order.getUserId());
        storeAccountPayMsg.setChannelId(order.getChannelId());
        storeAccountPayMsg.setPayAmount(order.getUserPayAmount().intValue());
        storeFacade.storeAccountPayEvent(storeAccountPayMsg);

        // 1、扣减库存
        List<SkuOrder> localSkuOrders = skuOrders.stream().filter(vo -> Objects.isNull(vo.getOutSkuId())).collect(Collectors.toList());
        spuFacade.inventoryExecute(getCutInventoryExecuteReq(localSkuOrders));
        // 2、请求第三方下单
        List<SkuOrder> outSkuOrders = skuOrders.stream().filter(vo -> Objects.nonNull(vo.getOutSkuId())).collect(Collectors.toList());
        if (!org.springframework.util.CollectionUtils.isEmpty(outSkuOrders)) {
            // 获取供应商ID
            Long supplierId = outSkuOrders.stream().findAny().get().getSupplierId();
            // 通过工厂获取策略
            ThirdPartyOrderStrategy strategy = ThirdPartyOrderStrategyFactory.getStrategy(supplierId);
            if (strategy == null) {
                throw new ScmException(BaseErrorCode.BUSY, "不支持的供应商ID：" + supplierId);
            }
            SpuOrder first = spuOrders.stream().findFirst().orElse(null);
            List<OrderSkuVO> outGoods = new ArrayList<>();
            skuOrders.forEach(sku -> {
                OrderSkuVO orderSkuVO = new OrderSkuVO();
                orderSkuVO.setLocalId(sku.getId());
                orderSkuVO.setLocalSpuId(sku.getSpuId());
                orderSkuVO.setOutSpuId(first.getOutSpuId().toString());
                orderSkuVO.setOutId(sku.getOutSkuId().toString());
                orderSkuVO.setCount(sku.getBuyNum());
                outGoods.add(orderSkuVO);
            });
            // 执行下单
            ThirdPartyOrderResult result = strategy.createOrder(outGoods, order);
            // 构建外部订单（传入outGoods，供策略处理特有逻辑如outIds拼接）
            List<OutOrder> outOrderList = strategy.buildOutOrders(result, order.getId(), outGoods);
            strategy.saveOutOrdersLog(result, thirdPartyOrderService);
            outOrderRepository.batchSave(outOrderList);
        }
    }

    private void channelPaySuccess(Order order){
        orderRepository.updateOrderChainStateByOrderNo(order.getOrderNo(), OrderEnum.State.CHANNEL_WAIT_PAY.getCode(), OrderEnum.State.OPERATOR_WAIT_PAY.getCode(), null);
    }
    private InventoryExecuteReq getCutInventoryExecuteReq(List<SkuOrder> localSkuOrders) {
        InventoryExecuteReq inventoryExecuteReq = new InventoryExecuteReq();
        inventoryExecuteReq.setType(0);
        List<InventoryExecuteReq.Sku> skuList = new ArrayList<>();
        localSkuOrders.forEach(x->{
            InventoryExecuteReq.Sku sku = new InventoryExecuteReq.Sku();
            sku.setId(x.getSkuId());
            sku.setCount(x.getBuyNum());
            skuList.add(sku);
        });
        inventoryExecuteReq.setSkuList(skuList);
        return inventoryExecuteReq;
    }

    public void recalculateServiceAmount(Order order,List<SpuOrder> spuOrders,List<SkuOrder> skuOrders) {
        // 重新计算服务费
        ChannelNowServiceFeeRes channelNowServiceFee = purseConfigDomain.queryChannelNowServiceFee(order.getChannelId());
        skuOrders.forEach(skuOrder -> RecalculateServiceAmountUtils.recalculateServiceAmount(skuOrder,channelNowServiceFee));
        spuOrders.forEach(spuOrder ->
                spuOrder.setTotalServiceFee(skuOrders.stream()
                        .filter(it -> it.getSpuOrderNo().equals(spuOrder.getSpuOrderNo()))
                        .mapToLong(SkuOrder::getTotalServiceFee).sum())
        );

        Long newServiceAmount = spuOrders.stream().mapToLong(SpuOrder::getTotalServiceFee).sum();
        Long diffAmount = order.getTotalServiceFee() - newServiceAmount;
        order.setTotalServiceFee(newServiceAmount);
        order.setTotalServiceFeePending(Math.max(0, order.getTotalServiceFee() - diffAmount));

       orderRepository.updateById(order);
       spuOrderRepository.batchUpdateSpu(spuOrders);
       skuOrderRepository.batchUpdateSku(skuOrders);
    }

    private BalancePayReq getBalancePayReq(Order order) {
        BalancePayReq balancePayReq = new BalancePayReq();
        balancePayReq.setAccountId(order.getChannelId());
        balancePayReq.setMemberId(order.getUserId());
        balancePayReq.setAccountType(PurseEnum.FinanceUser.CHANNEL);
        balancePayReq.setPayAmount(order.getTotalServiceFeePending().intValue());
        balancePayReq.setGoodsAmount(order.getChannelPurchaseAmount().intValue());
        balancePayReq.setOrderNo(order.getId());
        balancePayReq.setOperatorId(order.getOperatorId());
        balancePayReq.setOrderInfo("交易单信息:" + JSONObject.toJSONString(order));
        return balancePayReq;
    }

    private BalancePayReq getOperatorBalancePayReq(Order order) {
        BalancePayReq balancePayReq = new BalancePayReq();
        balancePayReq.setAccountId(order.getOperatorId());
        balancePayReq.setOperatorId(order.getOperatorId());
        balancePayReq.setMemberId(order.getUserId());
        balancePayReq.setAccountType(PurseEnum.FinanceUser.OPERATOR);
        balancePayReq.setPayAmount(order.getTotalServiceFeePending().intValue());
        balancePayReq.setGoodsAmount(order.getChannelPurchaseAmount().intValue());
        balancePayReq.setOrderNo(order.getId());
        balancePayReq.setPurseType(PurseEnum.PurseType.PURCHASE);
        balancePayReq.setOrderInfo("交易单信息:" + JSONObject.toJSONString(order));
        return balancePayReq;
    }
    /**
     * 处理渠道名称查询
     */
    private void processChannelNameQuery(SpuOrderPageReq req) {
        String channelName = req.getChannelName();
        if (StrUtil.isNotBlank(channelName)) {
            ChannelQueryRpcReq channelQuery = new ChannelQueryRpcReq();
            channelQuery.setChannelName(channelName);
            channelQuery.resetQueryList();
            List<ChannelRes> channelVOs = channelFacade.channelList(channelQuery);

            // 渠道不存在则直接返回空分页
            if (CollUtil.isEmpty(channelVOs)) {
                return;
            }
            req.setChannelIdList(channelVOs.stream().map(ChannelRes::getId).collect(Collectors.toList()));
        }
    }

    /**
     * 处理昵称查询
     */
    private void processNicknameQuery(SpuOrderPageReq req) {
        String nickname = req.getNickname();
        if (StrUtil.isNotBlank(nickname)) {
            List<Long> userIds = accountFacade.queryMember(nickname);
            if (CollUtil.isEmpty(userIds)) {
                return;
            }
            req.setMemberIdList(userIds);
        }
    }

    /**
     * 处理门店账号查询
     */
    private void processStoreAccountQuery(SpuOrderPageReq req) {
        String storeAccount = req.getStoreAccount();
        if (StrUtil.isNotBlank(storeAccount)) {
            AccountGroupVO accountGroup = accountFacade.selectByUserAccount(storeAccount);
            RoleEnum.CompanyRole role = SecurityUtils.getRole();
            if (role == null) {
                return;
            }
            if (RoleEnum.CompanyRole.CHANNEL == role) {
                Optional.ofNullable(accountGroup)
                        .ifPresent(ag -> req.setMemberIdList(Collections.singletonList(ag.getId())));
            } else if (RoleEnum.CompanyRole.MEMBER == role) {
                Optional.ofNullable(accountGroup)
                        .ifPresent(ag -> req.setChannelIdList(Collections.singletonList(ag.getId())));
            }
        }
    }

    /**
     * 创建空分页对象
     */
    private Page<SpuOrderVO> createEmptyPage(Page<SpuOrder> sourcePage) {
        Page<SpuOrderVO> emptyPage = new Page<>();
        emptyPage.setCurrent(sourcePage.getCurrent());
        emptyPage.setSize(sourcePage.getSize());
        emptyPage.setTotal(sourcePage.getTotal());
        return emptyPage;
    }

    /**
     * 查询相关数据
     */
    private QueryResult fetchRelatedData(List<SpuOrder> records) {
        // 提取spu订单号，查询对应的sku订单
        List<String> spuOrderNos = records.stream().map(SpuOrder::getSpuOrderNo).collect(Collectors.toList());
        List<SkuOrder> skuOrders = skuOrderRepository.listBySpuOrderNo(spuOrderNos);

        // 提取用户ID和渠道ID，用于查询账号信息
        Set<Long> userIds = records.stream().map(SpuOrder::getUserId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> channelIds = records.stream().map(SpuOrder::getChannelId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Long> storeIds = records.stream().map(SpuOrder::getStoreId).filter(Objects::nonNull).collect(Collectors.toSet());

        // 合并所有需要查询的ID
        Set<Long> allAccountIds = new HashSet<>();
        allAccountIds.addAll(userIds);
        allAccountIds.addAll(channelIds);

        // 查询账号信息
        List<AccountGroupVO> accountGroupVOS = CollectionUtils.isNotEmpty(allAccountIds)
                ? accountFacade.listAccountByIds(new ArrayList<>(allAccountIds))
                : Collections.emptyList();

        // 构建账号信息映射
        Map<Long, AccountGroupVO> accountMap = accountGroupVOS.stream()
                .collect(Collectors.toMap(AccountGroupVO::getId, v -> v, (v1, v2) -> v1));

        // 查询门店信息
        List<StoreRPCVO> storeRPCVOS = CollectionUtils.isNotEmpty(storeIds)
                ? storeFacade.batchQueryStoreInfo(new ArrayList<>(storeIds))
                : Collections.emptyList();

        // 构建门店信息映射
        Map<Long, StoreRPCVO> storeMap = storeRPCVOS.stream()
                .collect(Collectors.toMap(StoreRPCVO::getId, v -> v, (v1, v2) -> v1));

        // 查询渠道商信息
        ChannelQueryRpcReq channelQuery = new ChannelQueryRpcReq();
        channelQuery.setIdList(new ArrayList<>(channelIds));
        channelQuery.setState(1);
        List<ChannelRes> channelVOs = channelFacade.channelList(channelQuery);
        Map<Long, ChannelRes> channelOutVOMap = channelVOs.stream()
                .collect(Collectors.toMap(ChannelRes::getId, v -> v, (v1, v2) -> v1));

        // 查询发货明细
        List<String> skuOrderNos = skuOrders.stream().map(SkuOrder::getSkuOrderNo).distinct().collect(Collectors.toList());
        List<OrderDeliveryItem> orderDeliveryItems = orderDeliveryItemRepository.selectBySkuOrderNos(skuOrderNos);
        Map<String, List<OrderDeliveryItem>> deliveryMap = orderDeliveryItems.stream()
                .collect(Collectors.groupingBy(OrderDeliveryItem::getSkuOrderNo));

        // 构建sku订单VO列表
        List<SkuOrderVO> skuOrderVOList = new ArrayList<>();
        skuOrders.forEach(skuOrder -> {
            SkuOrderVO skuOrderVO = TransferUtils.transfer(skuOrder, SkuOrderVO::new);
            skuOrderVO.setDeliveryItemList(deliveryMap.getOrDefault(skuOrder.getSkuOrderNo(), Collections.emptyList()));
            skuOrderVOList.add(skuOrderVO);
        });
        Map<String, List<SkuOrderVO>> skuOrderVOMap = skuOrderVOList.stream()
                .collect(Collectors.groupingBy(SkuOrderVO::getSpuOrderNo));

        return new QueryResult(
                skuOrderVOMap,
                accountMap,
                storeMap,
                channelOutVOMap
        );
    }

    /**
     * 构建SpuOrderVO列表
     */
    private List<SpuOrderVO> buildSpuOrderVOList(List<SpuOrder> spuOrders, QueryResult queryResult) {
        return spuOrders.stream().map(spuOrder -> {
            // 复制基础属性
            SpuOrderVO spuOrderVO = TransferUtils.transfer(spuOrder, SpuOrderVO::new);

            // 设置子订单列表
            List<SkuOrderVO> skuOrderList = queryResult.skuOrderVOMap.getOrDefault(spuOrder.getSpuOrderNo(), Collections.emptyList());
            spuOrderVO.setSkuOrderList(skuOrderList);

            // 设置用户账号信息
            if (spuOrder.getUserId() != null) {
                spuOrderVO.setMemberAccount(queryResult.accountMap.get(spuOrder.getUserId()));
            }

            // 设置渠道商账号信息
            if (spuOrder.getChannelId() != null) {
                spuOrderVO.setChannelAccount(queryResult.accountMap.get(spuOrder.getChannelId()));
            }

            // 设置门店信息
            if (spuOrder.getStoreId() != null) {
                spuOrderVO.setStore(queryResult.storeMap.get(spuOrder.getStoreId()));
            }

            // 设置渠道商信息并处理敏感数据脱敏
            if (spuOrder.getChannelId() != null) {
                ChannelRes channelOutVO = queryResult.channelOutVOMap.get(spuOrder.getChannelId());
                if (RoleEnum.CompanyRole.OPERATOR.equals(SecurityUtils.getRole()) && Objects.nonNull(channelOutVO)) {
                    if (!channelOutVO.getUpOperatorId().equals(SecurityUtils.getAccountId())) {
                        String receiptInfo = spuOrderVO.getReceiptInfo();
                        ChannelRes bean = JSONUtil.toBean(receiptInfo, ChannelRes.class);
                        String shipPhone = bean.getUsername();
                        bean.setUsername(DesensitizedUtil.mobilePhone(shipPhone));
                        spuOrderVO.setReceiptInfo(JSONUtil.toJsonStr(bean));
                        channelOutVO.setName(PatternUtil.desensitized(channelOutVO.getName(), 3, 2));
                    }
                }
                spuOrderVO.setChannel(channelOutVO);
            }

            // 设置剩余时间
            setRemainTime(spuOrderVO);

            return spuOrderVO;
        }).collect(Collectors.toList());
    }

    /**
     * 设置剩余时间
     */
    private void setRemainTime(SpuOrderVO spuOrderVO) {
        if (Objects.equals(spuOrderVO.getOrderState(), OrderEnum.State.MEMBER_WAIT_PAY.getCode())) {
            long remainTime = spuOrderVO.getCreateTime().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            spuOrderVO.setRemainTime(remainTime);
        } else if (Objects.equals(spuOrderVO.getOrderState(), OrderEnum.State.CHANNEL_WAIT_PAY.getCode())) {
            long remainTime;
            if (BooleanUtil.isTrue(SecurityContextHolder.isDev())) {
                // 测试环境7分钟
                remainTime = spuOrderVO.getUpdateTime().plusMinutes(7).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            } else {
                // 生产环境5天
                remainTime = spuOrderVO.getUpdateTime().plusDays(5).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
            spuOrderVO.setRemainTime(remainTime);
        }
    }

    /**
     * 构建返回的分页对象
     */
    private Page<SpuOrderVO> buildResultPage(Page<SpuOrder> sourcePage, List<SpuOrderVO> records) {
        Page<SpuOrderVO> resultPage = new Page<>();
        resultPage.setCurrent(sourcePage.getCurrent());
        resultPage.setSize(sourcePage.getSize());
        resultPage.setTotal(sourcePage.getTotal());
        resultPage.setRecords(records);
        return resultPage;
    }

    /**
     * 查询结果封装类
     */
    private record QueryResult(Map<String, List<SkuOrderVO>> skuOrderVOMap, Map<Long, AccountGroupVO> accountMap,
                               Map<Long, StoreRPCVO> storeMap, Map<Long, ChannelRes> channelOutVOMap) {
    }
}