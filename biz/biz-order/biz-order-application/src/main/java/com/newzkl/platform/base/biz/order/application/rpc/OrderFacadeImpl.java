package com.newzkl.platform.base.biz.order.application.rpc;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.application.service.CommitOrder;
import com.newzkl.platform.base.biz.order.application.service.OrderService;
import com.newzkl.platform.base.biz.order.application.service.QueryService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.GoodsApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.LocalMessageApi;
import com.newzkl.platform.base.biz.order.domain.service.OrderDomain;
import com.newzkl.platform.base.biz.order.facade.OrderFacade;
import com.newzkl.platform.base.biz.order.facade.model.api.order.*;
import com.newzkl.platform.base.biz.order.facade.model.hdh.ItemInfo;
import com.newzkl.platform.base.biz.order.facade.model.hdh.OrderCallbackRequest;
import com.newzkl.platform.base.biz.order.facade.model.hdh.PkgInfo;
import com.newzkl.platform.base.biz.order.facade.model.order.*;
import com.newzkl.platform.base.biz.order.model.dto.OrderAgg;
import com.newzkl.platform.base.biz.order.model.dto.OrderDTO;
import com.newzkl.platform.base.biz.order.model.dto.OrderStateRecordEntity;
import com.newzkl.platform.base.biz.order.model.req.DeliverCommand;
import com.newzkl.platform.base.biz.order.model.req.DeliverItemCommand;
import com.newzkl.platform.base.biz.order.model.req.MemberOrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.OrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.OrderItemCommand;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.biz.order.model.vo.OrderVO;
import com.newzkl.platform.base.common.ddd.model.vo.ShipVO;
import com.newzkl.platform.base.biz.order.model.vo.SkuOrderVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.domain.utils.COrderStateMachine;
import com.newzkl.platform.base.common.ddd.facade.ApiSkuVO;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.ThirdPartyOrderEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author fang
 */
@Slf4j
@DubboService
@Component
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {

    private final OrderService orderService;
    private final OrderDomain orderDomain;
    private final LocalMessageApi localMessageApi;
    private final QueryService queryService;
    private final CommitOrder commitOrder;
    private final GoodsApi goodsApi;

    @Override
    public ApiOrderRes apiSubmitOrder(Long accountId, ApiOrderSubmitReq orderReq) {
        OrderCreateCommand orderCreateCommand = new OrderCreateCommand();
        orderCreateCommand.setChannelId(accountId);
        orderCreateCommand.setOrderType(OrderEnum.OrderType.CHANNEL);
        // openapi 入口的单一律标乐态来源(轴A 订单来源) 主体恒为渠道商(SignatureFilter 已置 Identity.CHANNEL)
        orderCreateCommand.setPlatformType(ThirdPartyOrderEnum.PlatformTypeEnum.LE_TAI);
        orderCreateCommand.setShipVO(TransferUtils.transfer(orderReq, ShipVO.class));
        orderCreateCommand.setOrderGoodsList(TransferUtils.transfers(orderReq.getOrderGoodsList(), apiOrderSubmitItemReq -> {
            OrderItemCommand orderItemCommand = new OrderItemCommand();
            orderItemCommand.setSkuId(apiOrderSubmitItemReq.getSkuId());
            orderItemCommand.setCount(apiOrderSubmitItemReq.getCount());
            return orderItemCommand;
        }));
        orderCreateCommand.setRemark(orderReq.getRemark());
        orderCreateCommand.setOutOrderNo(orderReq.getOutOrderNo());
        OrderCreateRes orderCreateRes = commitOrder.commitOrder(orderCreateCommand, new MemberOrderCreateCommand());
        return TransferUtils.transfer(orderCreateRes, orderCreateRes1 -> {
            ApiOrderRes apiOrderRes = new ApiOrderRes();
            apiOrderRes.setPayState(CommonEnum.YesOrNo.YES);
            return apiOrderRes;
        });
    }

    @Override
    public Page<ApiOrderVO> apiList(Long accountId, ApiOrderReq apiOrderReq) {
        //参数转换
        OrderQuery spuOrderQuery = TransferUtils.transfer(apiOrderReq, OrderQuery.class);
        //分页查询
        Page<OrderVO> apiSpuOrderPageVOPage = queryService.orderVOList(spuOrderQuery);
        return TransferUtils.transferPage(apiSpuOrderPageVOPage,ApiOrderVO.class);
    }

    @Override
    public ApiOrderAggVO apiDetail(Long accountId, String outOrderNo) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setOutOrderNo(outOrderNo);
        List<String> orderNoList = queryService.orderNoList(orderQuery);
        if(ObjectUtil.isEmpty(orderNoList)){
            ThrowsException.exception(BaseErrorCode.PARAM, "外部订单号错误");
        }
        OrderAgg orderAgg = orderDomain.orderAgg(orderNoList.get(0));
        ApiOrderAggVO apiOrderAggVO = new ApiOrderAggVO();
        apiOrderAggVO.setOrder(TransferUtils.transfer(orderAgg.getOrder(), ApiOrderVO.class));
        apiOrderAggVO.setOrderItem(TransferUtils.transfers(orderAgg.getSkuOrderList(), ApiSkuOrderVO.class));
        return apiOrderAggVO;
    }

    @Override
    public void apiConfirm(Long accountId, ApiOrderConfirmReq confirmReq) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setOutOrderNo(confirmReq.getOutOrderNo());
        orderQuery.resetQuerySingle();
        OrderVO order =  CollUtil.getFirst(queryService.orderVOList(orderQuery).getRecords());
        if(order == null){
            ThrowsException.exception(BaseErrorCode.PARAM, "外部订单号错误");
        }
        //查询SKU订单ID
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderNo(order.getOrderNo());
        skuOrderQuery.setSkuIdList(confirmReq.getSkuIdList());
        List<SkuOrderVO> skuOrderVOList = queryService.skuOrderVOList(skuOrderQuery).getRecords();
        List<String> skuOrderNoList = skuOrderVOList.stream().map(SkuOrderVO::getSkuOrderNo).collect(Collectors.toList());
        if(ObjectUtil.isNotEmpty(skuOrderNoList)){
            orderService.receiveSkuOrder(order.getOrderNo(), skuOrderNoList);
        }
    }

    @Override
    public Long apiFreight(Long accountId, ApiOrderFreightReq orderReq) {
        ShipVO shipVO = new ShipVO();
        shipVO.setShipName(orderReq.getShipName());
        shipVO.setShipPhone(orderReq.getShipPhone());
        shipVO.setShipArea(orderReq.getShipArea());
        shipVO.setShipAddress(orderReq.getShipAddress());
        shipVO.setShipProvinceCode(orderReq.getShipProvinceCode());
        shipVO.setShipCityCode(orderReq.getShipCityCode());
        shipVO.setShipAreaCode(orderReq.getShipAreaCode());
        shipVO.setShipZipCode(orderReq.getShipZipCode());
        List<OrderItemCommand> orderItemCommandList = TransferUtils.transfers(orderReq.getOrderGoodsList(), new Function<ApiOrderSubmitItemReq, OrderItemCommand>() {
            @Override
            public OrderItemCommand apply(ApiOrderSubmitItemReq apiOrderItemReq) {
                OrderItemCommand orderItemCommand = new OrderItemCommand();
                orderItemCommand.setSkuId(apiOrderItemReq.getSkuId());
                orderItemCommand.setCount(apiOrderItemReq.getCount());
                return orderItemCommand;
            }
        });
        OrderCreateCommand orderCreateCommand = new OrderCreateCommand();
        orderCreateCommand.setChannelId(accountId);
        orderCreateCommand.setShipVO(shipVO);
        orderCreateCommand.setOrderGoodsList(orderItemCommandList);
        return orderService.channelRealOrderFreight(orderCreateCommand);
    }

    @Override
    public List<OrderStateVO> apiOrderState(Long accountId, List<String> outOrderNoList) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setOutOrderNoList(outOrderNoList);
        List<Long> orderIdList = queryService.orderIdList(orderQuery);
        if(ObjectUtil.isEmpty(orderIdList)){
            ThrowsException.exception(BaseErrorCode.PARAM, "外部订单号错误");
        }
        return queryService.accountOrderState(accountId, orderIdList);
    }

    @Override
    public OrderRelationVO orderRelation(Long orderId, Long spuId) {
        return queryService.orderRelation(orderId, spuId);
    }

    @Override
    // TODO[#171-seata] 原 Seata @GlobalTransactional 降级为本地事务(Base 未接 Seata); 会员支付成功编排走单体本地事务, 待 Seata 装配后恢复分布式全局事务
    @Transactional(rollbackFor = Exception.class)
    public void memberPaySuccess(String orderNo) {
        orderService.memberPaySuccess(orderNo);
    }

    @Override
    public void orderChannelPay(String orderNo) {
        orderService.orderBalancePay(orderNo);
    }

    @Override
    public void orderMemberPay(List<String> orderNoList) {
        orderDomain.batchUpdateOrderState(orderNoList, OrderEnum.State.MEMBER_WAIT_PAY, OrderEnum.State.CHANNEL_WAIT_PAY, null);
        orderNoList.forEach(orderNo -> {
            OrderDTO orderDTO = orderDomain.orderAgg(orderNo).getOrder();
            localMessageApi.sendOrderNewRecordEvent(Collections.singletonList(orderDTO), OrderEnum.State.MEMBER_WAIT_PAY, OrderEnum.State.CHANNEL_WAIT_PAY, AccountEnum.Identity.PLATFORM.getCode(), AccountEnum.Identity.PLATFORM);
        });
    }

    @Override
    public void save(OrderStateRecordRPC orderStateRecordRPC) {
        // 将RPC传输模型转换为领域实体
        OrderStateRecordEntity entity = new OrderStateRecordEntity();
        BeanUtils.copyProperties(orderStateRecordRPC, entity);

        // 调用领域服务完成保存
        orderDomain.createStateRecord(entity);
    }

    /**
     * 会订货订单状态回调处理
     *
     * <p>迁移自 plugin-hdh {@code HdhEvent.handleStatusCallback} —— 原实现直连 biz-order
     * domain (orderRepository/orderDomain) 违反跨服务域红线。业务逻辑下沉本 facade 实现,
     * plugin 侧仅经 {@link OrderFacade} 转发, 恢复 slug21 设计原意 (回调经 OrderFacade)。</p>
     *
     * <p>流程: 解析外部订单号 → 查订单 → 三方状态码映射 → 状态机校验转换 →
     * (到货态)按包裹外部SKU反查内部SKU发货。</p>
     *
     * @param callbackRequest 回调请求参数 (含外部订单号 + 三方状态码 + 包裹列表)
     * @return 处理成功返回 {@code true}, 否则 {@code false}
     */
    @Override
    public boolean handleStatusCallback(OrderCallbackRequest callbackRequest) {
        // 1. 解析外部订单号
        String userOrderNum = callbackRequest.getUserOrderNum();
        if (StrUtil.isBlank(userOrderNum)) {
            log.warn("回调外部订单号为空");
            return false;
        }

        // 2. 查询订单（确保存在）—— 经 domain 聚合读, 不直连 repository
        OrderAgg orderAgg = orderDomain.orderAgg(userOrderNum);
        OrderDTO orderDTO = orderAgg == null ? null : orderAgg.getOrder();
        if (orderDTO == null) {
            log.warn("未查询到订单，外部订单号：{}", userOrderNum);
            return false;
        }
        log.info("查询到订单，ID：{}，当前状态：{}", orderDTO.getId(), orderDTO.getOrderState());

        // 3. 三方状态码映射为我方状态码
        Integer thirdPartyCode = callbackRequest.getOrderStatusCode();
        OrderEnum.State ourCode = ThirdPartyOrderEnum.ThirdPartyStateMapping.getOurCodeByThirdPartyCode(thirdPartyCode);
        if (ourCode == null) {
            log.warn("三方状态码映射失败，三方码：{}，订单ID：{}", thirdPartyCode, orderDTO.getId());
            return false;
        }

        // 4. 状态机校验转换合法性
        OrderEnum.State currentState = orderDTO.getOrderState();
        if (currentState == null) {
            log.warn("状态解析失败，当前状态码：{}，目标状态码：{}，订单ID：{}", null, ourCode, orderDTO.getId());
            return false;
        }
        if (currentState == ourCode) {
            log.info("订单状态未变更，无需处理，订单ID：{}，状态：{}", orderDTO.getId(), currentState.getValue());
            return true;
        }
        OrderEnum.State validatedTarget;
        try {
            validatedTarget = COrderStateMachine.transition(currentState, ourCode);
        } catch (IllegalArgumentException e) {
            log.error("状态转换非法，订单ID：{}，当前状态：{}，目标状态：{}，原因：{}",
                    orderDTO.getId(), currentState.getValue(), ourCode.getValue(), e.getMessage());
            return false;
        }
        if (ourCode == OrderEnum.State.WAIT_RECEIVE) {
            List<PkgInfo> pkgList = callbackRequest.getPkgList();
            pkgList.forEach(pkgInfo -> {
                String expressNum = pkgInfo.getExpressNum();
                String expressCompany = pkgInfo.getExpressCompany();
                List<ItemInfo> itemList = pkgInfo.getItemList();
                List<String> skuIdList = itemList.stream().map(ItemInfo::getSkuId).collect(Collectors.toList());
                orderDeliver(skuIdList, orderDTO, expressCompany, expressNum, itemList);
            });
        }
        log.info("订单状态更新成功，订单ID：{}，{}→{}", orderDTO.getId(), currentState.getValue(), validatedTarget.getValue());
        return true;
    }

    /**
     * 会订货发货：外部SKU反查内部SKU, 按回调数量构建发货命令
     *
     * @param outSkuIdList       外部SKU ID列表
     * @param order              交易单(主键给发货命令, 单号给子表关联查询)
     * @param expressCompanyName 快递公司名称
     * @param expressNo          快递单号
     * @param itemList           回调商品信息列表 (含外部SKU + 发货数量)
     */
    private void orderDeliver(List<String> outSkuIdList, OrderDTO order, String expressCompanyName, String expressNo,
                              List<ItemInfo> itemList) {
        Long orderId = order == null ? null : order.getId();
        if (CollUtil.isEmpty(outSkuIdList) || orderId == null || StrUtil.isBlank(expressNo)
                || CollUtil.isEmpty(itemList)) {
            log.warn("发货参数不完整，订单ID：{}，外部SKU列表：{}，快递单号：{}，商品信息：{}", orderId, outSkuIdList, expressNo, itemList);
            return;
        }

        // 外部SKU → 内部SKU 映射 (经 domain 端口, 跨域走 GoodsApi)
        List<ApiSkuVO> apiSkuVOList = goodsApi.querySkuIdListByOutId(outSkuIdList);
        if (CollUtil.isEmpty(apiSkuVOList)) {
            log.warn("未查询到外部SKU对应的内部信息，外部SKU列表：{}，订单ID：{}", outSkuIdList, orderId);
            return;
        }
        Map<Long, String> innerIdToOutSkuMap = apiSkuVOList.stream()
                .filter(sku -> StrUtil.isNotBlank(sku.getOutSkuId()))
                .collect(Collectors.toMap(ApiSkuVO::getId, ApiSkuVO::getOutSkuId,
                        (existing, replacement) -> {
                            log.warn("内部SKU对应多个外部SKU，保留第一个。内部SKU：{}，外部SKU1：{}", existing, replacement);
                            return existing;
                        }));

        Map<String, Integer> outSkuToNumberMap = itemList.stream()
                .filter(item -> StrUtil.isNotBlank(item.getSkuId()) && item.getNumber() != null && item.getNumber() > 0)
                .collect(Collectors.toMap(ItemInfo::getSkuId, ItemInfo::getNumber,
                        (existing, replacement) -> {
                            log.warn("外部SKU对应多个数量，取合计。数量1：{}，数量2：{}", existing, replacement);
                            return existing + replacement;
                        }));

        // 查订单下SKU信息 (经 queryService, 不直连 repository)
        List<Long> innerSkuIdList = new ArrayList<>(innerIdToOutSkuMap.keySet());
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderNo(order.getOrderNo());
        skuOrderQuery.setSkuIdList(innerSkuIdList);
        List<SkuOrderVO> skuOrderVOList = queryService.skuOrderVOList(skuOrderQuery).getRecords();
        if (CollUtil.isEmpty(skuOrderVOList)) {
            log.warn("订单下无匹配的SKU信息，订单ID：{}，内部SKU列表：{}", orderId, innerSkuIdList);
            return;
        }

        DeliverCommand deliverCommand = new DeliverCommand();
        deliverCommand.setSpuOrderId(orderId);
        deliverCommand.setExpressCompanyName(expressCompanyName);
        deliverCommand.setExpressNo(expressNo);
        deliverCommand.setExpressMobile("");

        List<DeliverItemCommand> deliverItemList = skuOrderVOList.stream().map(skuOrder -> {
            Long innerSkuId = skuOrder.getSkuId();
            String outSkuId = innerIdToOutSkuMap.get(innerSkuId);
            if (StrUtil.isBlank(outSkuId)) {
                log.warn("内部SKU未找到对应外部SKU，跳过发货。内部SKU：{}，订单ID：{}", innerSkuId, orderId);
                return null;
            }
            Integer deliverNum = outSkuToNumberMap.get(outSkuId);
            if (deliverNum == null || deliverNum <= 0) {
                log.warn("外部SKU未找到有效发货数量，跳过发货。外部SKU：{}，内部SKU：{}，订单ID：{}", outSkuId, innerSkuId, orderId);
                return null;
            }
            DeliverItemCommand itemCommand = new DeliverItemCommand();
            itemCommand.setSkuId(innerSkuId);
            itemCommand.setCount(deliverNum);
            return itemCommand;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        if (!CollUtil.isEmpty(deliverItemList)) {
            deliverCommand.setDeliverItemCommandList(deliverItemList);
            orderDomain.deliverCreate(deliverCommand);
            log.info("订单发货命令已提交，订单ID：{}，快递单号：{}，发货商品数：{}", orderId, expressNo, deliverItemList.size());
        } else {
            log.warn("订单无有效发货商品，跳过发货。订单ID：{}", orderId);
        }
    }
}
