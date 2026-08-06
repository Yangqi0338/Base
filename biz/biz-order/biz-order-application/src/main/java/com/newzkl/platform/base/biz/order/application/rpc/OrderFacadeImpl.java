package com.newzkl.platform.base.biz.order.application.rpc;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.application.service.CommitOrder;
import com.newzkl.platform.base.biz.order.application.service.OrderService;
import com.newzkl.platform.base.biz.order.application.service.QueryService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.LocalMessageApi;
import com.newzkl.platform.base.biz.order.domain.service.IOrderDomain;
import com.newzkl.platform.base.biz.order.facade.IOrderFacade;
import com.newzkl.platform.base.biz.order.facade.model.api.order.*;
import com.newzkl.platform.base.biz.order.facade.model.order.OrderStateRecordRPC;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderRelationVO;
import com.newzkl.platform.base.biz.order.facade.model.order.SpuOrderStateVO;
import com.newzkl.platform.base.biz.order.model.dto.OrderAgg;
import com.newzkl.platform.base.biz.order.model.dto.OrderStateRecordEntity;
import com.newzkl.platform.base.biz.order.model.dto.SpuOrderDTO;
import com.newzkl.platform.base.biz.order.model.req.MemberOrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.OrderCreateCommand;
import com.newzkl.platform.base.biz.order.model.req.OrderItemCommand;
import com.newzkl.platform.base.biz.order.model.req.query.OrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SkuOrderQuery;
import com.newzkl.platform.base.biz.order.model.req.query.SpuOrderQuery;
import com.newzkl.platform.base.biz.order.model.res.OrderCreateRes;
import com.newzkl.platform.base.biz.order.model.vo.OrderVO;
import com.newzkl.platform.base.biz.order.model.vo.ShipVO;
import com.newzkl.platform.base.biz.order.model.vo.SkuOrderVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author fang
 */
@DubboService
@Component
@RequiredArgsConstructor
public class OrderFacadeImpl implements IOrderFacade {

    private final OrderService orderService;
    private final IOrderDomain orderDomain;
    private final LocalMessageApi localMessageApi;
    private final QueryService queryService;
    private final CommitOrder commitOrder;

    @Override
    public ApiOrderRes apiSubmitOrder(Long accountId, ApiOrderSubmitReq orderReq) {
        OrderCreateCommand orderCreateCommand = new OrderCreateCommand();
        orderCreateCommand.setChannelId(accountId);
        orderCreateCommand.setOrderType(OrderEnum.OrderType.CHANNEL);
        orderCreateCommand.setShipVO(TransferUtils.transfer(orderReq, apiShipVO -> {
            ShipVO shipVO = new ShipVO();
            shipVO.setShipName(apiShipVO.getShipName());
            shipVO.setShipPhone(apiShipVO.getShipPhone());
            shipVO.setShipArea(apiShipVO.getShipArea());
            shipVO.setShipAddress(apiShipVO.getShipAddress());
            shipVO.setShipProvinceCode(apiShipVO.getShipProvinceCode());
            shipVO.setShipCityCode(apiShipVO.getShipCityCode());
            shipVO.setShipAreaCode(apiShipVO.getShipAreaCode());
            shipVO.setShipZipCode(apiShipVO.getShipZipCode());
            return shipVO;
        }));
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
        OrderQuery spuOrderQuery = TransferUtils.transfer(apiOrderReq, new Function<ApiOrderReq, OrderQuery>() {
            @Override
            public OrderQuery apply(ApiOrderReq apiSpuOrderReq) {
                OrderQuery spuOrderQuery = new OrderQuery();
                spuOrderQuery.setOutOrderNo(apiSpuOrderReq.getOutOrderNo());
                spuOrderQuery.setCreateStartTime(apiSpuOrderReq.getCreateBeginTime());
                spuOrderQuery.setCreateEndTime(apiSpuOrderReq.getCreateEndTime());
                spuOrderQuery.setPageNo(apiSpuOrderReq.getPageNo());
                spuOrderQuery.setPageSize(apiSpuOrderReq.getPageSize());
                return spuOrderQuery;
            }
        });
        //分页查询
        Page<OrderVO> apiSpuOrderPageVOPage = queryService.orderVOList(spuOrderQuery);
        return TransferUtils.transferPage(apiSpuOrderPageVOPage,ApiOrderVO.class);
    }

    @Override
    public ApiOrderAggVO apiDetail(Long accountId, String outOrderNo) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setOutOrderNo(outOrderNo);
        List<Long> orderIdList = queryService.orderIdList(orderQuery);
        if(ObjectUtil.isEmpty(orderIdList)){
            ThrowsException.exception(BaseErrorCode.PARAM, "外部订单号错误");
        }
        OrderAgg orderAgg = orderDomain.orderAgg(orderIdList.get(0));
        ApiOrderAggVO apiOrderAggVO = new ApiOrderAggVO();
        apiOrderAggVO.setOrder(TransferUtils.transfer(orderAgg.getOrder(), ApiOrderVO.class));
        apiOrderAggVO.setOrderItem(TransferUtils.transfers(orderAgg.getSkuOrderList(), ApiSkuOrderVO.class));
        return apiOrderAggVO;
    }

    @Override
    public void apiConfirm(Long accountId, ApiOrderConfirmReq confirmReq) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setOutOrderNo(confirmReq.getOutOrderNo());
        List<Long> orderIdList = queryService.orderIdList(orderQuery);
        if(ObjectUtil.isEmpty(orderIdList)){
            ThrowsException.exception(BaseErrorCode.PARAM, "外部订单号错误");
        }
        //查询SKU订单ID
        SkuOrderQuery skuOrderQuery = new SkuOrderQuery();
        skuOrderQuery.setOrderId(orderIdList.get(0));
        skuOrderQuery.setSkuIdList(confirmReq.getSkuIdList());
        List<SkuOrderVO> skuOrderVOList = queryService.skuOrderVOList(skuOrderQuery).getRecords();
        Map<Long, List<SkuOrderVO>> skuOrderVOMap = skuOrderVOList.stream().collect(Collectors.groupingBy(SkuOrderVO::getSpuOrderId));
        for (Long spuOrderId : skuOrderVOMap.keySet()) {
            List<Long> skuOrderIdList = skuOrderVOMap.get(spuOrderId).stream().map(item -> item.getId()).collect(Collectors.toList());
            if(ObjectUtil.isNotEmpty(skuOrderIdList)){
                orderService.receiveSkuOrder(spuOrderId, skuOrderIdList);
            }
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
    public List<SpuOrderStateVO> apiOrderState(Long accountId, List<String> outOrderNoList) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setOutOrderNoList(outOrderNoList);
        List<Long> orderIdList = queryService.orderIdList(orderQuery);
        if(ObjectUtil.isEmpty(orderIdList)){
            ThrowsException.exception(BaseErrorCode.PARAM, "外部订单号错误");
        }
        return queryService.accountOrderState(accountId, orderIdList);
    }

    @Override
    public SpuOrderRelationVO spuOrderRelation(Long orderId, Long spuId) {
        return queryService.spuOrderRelation(orderId,spuId);
    }

    @Override
    // TODO[#171-seata] 原 Seata @GlobalTransactional 降级为本地事务(Base 未接 Seata); 会员支付成功编排走单体本地事务, 待 Seata 装配后恢复分布式全局事务
    @Transactional(rollbackFor = Exception.class)
    public void memberPaySuccess(Long orderId) {
        orderService.memberPaySuccess(orderId);
    }

    @Override
    public void orderChannelPay(Long orderId) {
        orderService.orderBalancePay(orderId);
    }

    @Override
    public void orderMemberPay(List<Long> orderIdList) {
        orderDomain.batchUpdateOrderState(orderIdList, OrderEnum.State.MEMBER_WAIT_PAY, OrderEnum.State.CHANNEL_WAIT_PAY, null);
        orderIdList.forEach(orderId -> {
            SpuOrderQuery spuOrderQuery = new SpuOrderQuery();
            spuOrderQuery.setOrderId(orderId);
            List<SpuOrderVO> spuOrders = orderDomain.spuOrderPage(spuOrderQuery).getRecords();
            localMessageApi.sendOrderNewRecordEvent(TransferUtils.transfers(spuOrders, SpuOrderDTO.class), OrderEnum.State.MEMBER_WAIT_PAY, OrderEnum.State.CHANNEL_WAIT_PAY, RoleEnum.CompanyRole.PLATFORM.getCode(),RoleEnum.CompanyRole.PLATFORM);
        });
    }

    @Override
    public void closeOrder(Long orderId) {
        orderService.closeOrder(orderId);
    }

    @Override
    public void save(OrderStateRecordRPC orderStateRecordRPC) {
        // 将RPC传输模型转换为领域实体
        OrderStateRecordEntity entity = new OrderStateRecordEntity();
        BeanUtils.copyProperties(orderStateRecordRPC, entity);

        // 调用领域服务完成保存
        orderDomain.createStateRecord(entity);
    }
}
