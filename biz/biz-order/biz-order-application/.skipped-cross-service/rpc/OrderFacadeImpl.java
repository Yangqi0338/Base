package com.newzkl.platform.base.biz.order.application.order.rpc;

import com.zkl.scm.goods.rpc.facade.IDistributionRpcFacade;
import com.zkl.scm.model.constants.ApiPage;
import com.zkl.scm.openapi.req.*;
import com.zkl.scm.openapi.res.ApiOrderRes;
import com.zkl.scm.openapi.vo.ApiOrderAggVO;
import com.zkl.scm.openapi.vo.ApiOrderVO;
import com.zkl.scm.openapi.vo.SpuOrderRelationVO;
import com.zkl.scm.openapi.vo.SpuOrderStateVO;
import com.zkl.scm.sale.facade.IOrderFacade;
import com.newzkl.platform.base.biz.order.model.order.req.CreateOrderReq;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sijiwang
 */
@DubboService
@Component
public class OrderFacadeImpl implements IOrderFacade {

    @DubboReference
    private IDistributionRpcFacade distributionRpcFacade;

    @Override
    public ApiOrderRes apiSubmitOrder(Long accountId, ApiOrderSubmitReq orderReq) {
        CreateOrderReq orderCreateCommand = new CreateOrderReq();
        // todo 这里需要适配
        // 新版的下单流程是以铺货表为主要信息来源的，乐态没有铺货，需要适配一下
        // 新版订单的收货地址，以地址id为主要信息来源的，乐态是具体的地址信息，需要适配一下
        // 其他的状态 例如 支付状态等，需要根据老版本进行适配
//        orderCreateCommand.setChannelId(accountId);
//        orderCreateCommand.setOrderType(OrderEnum.OrderType.Channel.getCode());
//        orderCreateCommand.setShipVO(TransferUtils.transfer(orderReq, apiShipVO -> {
//            ShipVO shipVO = new ShipVO();
//            shipVO.setShipName(apiShipVO.getShipName());
//            shipVO.setShipPhone(apiShipVO.getShipPhone());
//            shipVO.setShipArea(apiShipVO.getShipArea());
//            shipVO.setShipAddress(apiShipVO.getShipAddress());
//            shipVO.setShipProvinceCode(apiShipVO.getShipProvinceCode());
//            shipVO.setShipCityCode(apiShipVO.getShipCityCode());
//            shipVO.setShipAreaCode(apiShipVO.getShipAreaCode());
//            shipVO.setShipZipCode(apiShipVO.getShipZipCode());
//            return shipVO;
//        }));
//        orderCreateCommand.setOrderGoodsList(TransferUtils.transfers(orderReq.getOrderGoodsList(), apiOrderSubmitItemReq -> {
//            OrderItemCommand orderItemCommand = new OrderItemCommand();
//            orderItemCommand.setSkuId(apiOrderSubmitItemReq.getSkuId());
//            orderItemCommand.setCount(apiOrderSubmitItemReq.getCount());
//            return orderItemCommand;
//        }));
//        orderCreateCommand.setRemark(orderReq.getRemark());
//        orderCreateCommand.setOutOrderNo(orderReq.getOutOrderNo());
//        OrderCreateRes orderCreateRes = commitOrder.commitOrder(orderCreateCommand, new MemberOrderCreateCommand());
//        return TransferUtils.transfer(orderCreateRes, orderCreateRes1 -> {
//            ApiOrderRes apiOrderRes = new ApiOrderRes();
//            apiOrderRes.setPayState(CommonEnum.Switch.ON.getCode());
//            return apiOrderRes;
//        });
        return null;
    }

    @Override
    public ApiPage<ApiOrderVO> apiList(Long accountId, ApiOrderReq spuOrderQuery) {
        return null;
    }

    @Override
    public ApiOrderAggVO apiDetail(Long accountId, String outOrderNo) {
        return null;
    }

    @Override
    public void apiConfirm(Long accountId, ApiOrderConfirmReq confirmReq) {

    }

    @Override
    public Integer apiFreight(Long accountId, ApiOrderFreightReq orderReq) {
        return null;
    }

    @Override
    public List<SpuOrderStateVO> apiOrderState(Long accountId, List<String> outOrderNoList) {
        return null;
    }

    @Override
    public SpuOrderRelationVO spuOrderRelation(Long orderId, Long spuId) {
        return null;
    }

    @Override
    public void memberPaySuccess(Long orderId) {

    }

    @Override
    public void orderChannelPay(Long orderId) {

    }

    @Override
    public boolean handleStatusCallback(OrderCallbackRequest callbackRequest) {
        return false;
    }

    @Override
    public void orderMemberPay(List<Long> orderIdList) {

    }

    @Override
    public void closeOrder(Long orderId) {

    }
}
