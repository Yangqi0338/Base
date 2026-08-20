package com.newzkl.platform.base.biz.order.application.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import com.newzkl.platform.base.biz.order.application.service.QueryService;
import com.newzkl.platform.base.biz.order.application.service.RefundService;
import com.newzkl.platform.base.biz.order.domain.adapt.api.PayApi;
import com.newzkl.platform.base.biz.order.domain.adapt.api.LocalMessageApi;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundRepository;
import com.newzkl.platform.base.biz.order.domain.service.*;
import com.newzkl.platform.base.biz.order.model.dto.RefundDTO;
import com.newzkl.platform.base.biz.order.model.req.RefundCommand;
import com.newzkl.platform.base.biz.order.model.res.RefundAuditRes;
import com.newzkl.platform.base.biz.order.model.res.RefundCreateRes;
import com.newzkl.platform.base.biz.order.model.vo.RefundItemVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderAggVO;
import com.newzkl.platform.base.biz.order.model.vo.SpuOrderVO;
import com.newzkl.platform.base.common.core.model.money.Money;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.facade.MemberRefundRes;
import com.newzkl.platform.base.common.ddd.facade.SellAfterRefundReq;
import com.newzkl.platform.base.common.ddd.model.constant.OrderErrorCode;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.account.AccountEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.OrderEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/811:16
 */
@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final OrderDomain orderDomain;
    private final RefundDomain refundDomain;
    private final PayApi balancePayApi;
    private final LocalMessageApi localMessageApi;
    private final QueryService queryService;
    private final SettleDomain settleDomain;
    private final RefundRepository refundRepository;

    @Override
    public Long refundCreateApi(RefundCommand refundCommand) {
        if (StrUtil.isNotBlank(refundCommand.getRemark())&& refundCommand.getRemark().length()>300){
            ThrowsException.exception(BaseErrorCode.PARAM,"售后单备注不能超过300字！");
        }
        //查询订单
        SpuOrderAggVO spuOrderAggVO = queryService.spuOrderAggVO(refundCommand.getSpuOrderId());
        //售后单创建
        RefundCreateRes refundCreateRes = refundDomain.refundCreate(refundCommand, spuOrderAggVO);
        RefundDTO refund = refundCreateRes.getRefund();
        // 发送协商记录
        localMessageApi.sendRefundOperationRecord(refund, RefundEnum.State.CHANNEL_WAIT, RefundEnum.State.CHANNEL_WAIT, com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.LAUNCH_REFUND);
//        refundOperationRecordRPC.setOperationType(Tag.OperationType.CREATE);
        //如果是派发中订单, 售后自动通过
        if(OrderEnum.State.SENDING == spuOrderAggVO.getSpuOrderVO().getOrderState()) {
            supplierAudit(refundCreateRes.getRefundId(), CommonEnum.YesOrNo.YES, false);
        }
        return refundCreateRes.getRefundId();
    }

    @Override
    public Long refundCreate(RefundCommand refundCommand) {
        if (StrUtil.isNotBlank(refundCommand.getRemark())&& refundCommand.getRemark().length()>300){
            ThrowsException.exception(BaseErrorCode.PARAM,"售后单备注不能超过300字！");
        }
        //查询订单
        SpuOrderAggVO spuOrderAggVO = queryService.spuOrderAggVO(refundCommand.getSpuOrderId());
        SpuOrderVO spuOrderVO = spuOrderAggVO.getSpuOrderVO();
        if (Objects.isNull(spuOrderVO)){
            ThrowsException.exception(OrderErrorCode.NOT_EXISTS,"订单不存在！");
        }
        if (refundCommand.getRefundType() == RefundEnum.RefundType.MONEY){
            Set<OrderEnum.State> moneyOrderStates = OrderEnum.State.getMoneyOrderStates();
            if(!moneyOrderStates.contains(spuOrderVO.getOrderState())){
                ThrowsException.exception(BaseErrorCode.CUSTOM, "当前状态不能发起仅退款！");
            }
        }else if (refundCommand.getRefundType() == RefundEnum.RefundType.MONEY_GOODS){
            Set<OrderEnum.State> moneyGoodsOrderStates = OrderEnum.State.getMoneyGoodsOrderStates();
            if(!moneyGoodsOrderStates.contains(spuOrderVO.getOrderState())){
                ThrowsException.exception(BaseErrorCode.CUSTOM, "当前状态不能发起退货退款！");
            }
        }else {
            ThrowsException.exception(OrderErrorCode.REFUND_FAIL,"售后单类型错误！");
        }
        //售后单创建
        RefundCreateRes refundCreateRes = refundDomain.refundCreate(refundCommand, spuOrderAggVO);
        RefundDTO refund = refundCreateRes.getRefund();
        // 发送协商记录
        localMessageApi.sendRefundOperationRecord(refund, RefundEnum.State.CHANNEL_WAIT, RefundEnum.State.CHANNEL_WAIT, com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.LAUNCH_REFUND);
//        refundOperationRecordRPC.setOperationType(Tag.OperationType.CREATE);
        //如果是派发中订单, 售后自动通过
        if(OrderEnum.State.SENDING == spuOrderAggVO.getSpuOrderVO().getOrderState()) {
            supplierAudit(refundCreateRes.getRefundId(), CommonEnum.YesOrNo.YES, false);
        }
        return refundCreateRes.getRefundId();
    }

    @Override
    public void supplierAudit(Long refundId, CommonEnum.YesOrNo execute, boolean isAudit) {
        //审核
        RefundAuditRes refundAuditRes = null;
        if(CommonEnum.YesOrNo.YES == execute){
            refundAuditRes = refundDomain.agreeAuditV2(refundId, AccountEnum.Identity.SUPPLIER);

            com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum refundOperateTypeEnum = isAudit? com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.SUPPLIER_TIMEOUT_AGREE: com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.SUPPLIER_AGREE;
            localMessageApi.sendRefundOperationRecord(refundAuditRes.getRefund(), RefundEnum.State.SUPPLIER_WAIT,refundAuditRes.getNextState(), refundOperateTypeEnum);
        }else {refundAuditRes = refundDomain.refuseAudit(refundId, AccountEnum.Identity.SUPPLIER, "");
            localMessageApi.sendRefundOperationRecord(refundAuditRes.getRefund(), RefundEnum.State.SUPPLIER_WAIT,refundAuditRes.getNextState(), com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.SUPPLIER_REFUSE);
        }

        //售后通过处理
        if(refundAuditRes.isRefundPass()){
            doRefundPassForChannel(refundAuditRes);
            com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum refundOperateTypeEnum = isAudit? com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.REFUND_MONEY_TIMEOUT_SUCCESS: com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.REFUND_MONEY_SUCCESS;
            localMessageApi.sendRefundOperationRecord(refundAuditRes.getRefund(), refundAuditRes.getNextState(), RefundEnum.State.SUCCESS, com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.REFUND_MONEY_SUCCESS);
        }
        RefundDTO refund = refundAuditRes.getRefund();

    }
    @Override
    public void channelAudit(Long refundId, Long spuOrderId, CommonEnum.YesOrNo execute, String reason, boolean isAudit) {
        if (refundId == null && spuOrderId == null){
            ThrowsException.exception(BaseErrorCode.PARAM,"售后单id和spu订单id不能都为空！");
        }
        RefundDTO refund = null;
        if (refundId == null){
            refund = refundRepository.refundBySpuOrderId(spuOrderId);
        }else {
            refund =  refundRepository.refund(refundId);
        }
        if (Objects.isNull(refund)){
            ThrowsException.exception(BaseErrorCode.PARAM,"售后单不存在！");
        }
        refundId = refund.getId();
        //审核
        RefundAuditRes refundAuditRes = null;
        if(CommonEnum.YesOrNo.YES == execute){
            refundAuditRes = refundDomain.agreeAuditV2(refundId, AccountEnum.Identity.CHANNEL);
            com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum refundOperateTypeEnum = isAudit? com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.CHANNEL_TIMEOUT_AGREE: com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.CHANNEL_AGREE;
            localMessageApi.sendRefundOperationRecord(refundAuditRes.getRefund(), refund.getRefundState(), refundAuditRes.getNextState(), refundOperateTypeEnum);
        }else {
            refundAuditRes = refundDomain.refuseAudit(refundId, AccountEnum.Identity.CHANNEL, reason);
            localMessageApi.sendRefundOperationRecord(refundAuditRes.getRefund(), refund.getRefundState(), refundAuditRes.getNextState(), com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.CHANNEL_REFUSE);
        }

        //售后通过处理
        if(refundAuditRes.isRefundPass()){
            if(SpuEnum.ChannelType.SELECTION == refundAuditRes.getRefund().getSpuChannelType()){
                //供货商品
                doRefundPassForMemberAndSelection(refundAuditRes);
            }else if(SpuEnum.ChannelType.CUSTOM == refundAuditRes.getRefund().getSpuChannelType()){
                //自营商品
                doRefundPassForMemberAndCustom(refundAuditRes);
            }else {
                ThrowsException.exception(BaseErrorCode.PARAM);
            }
            com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum refundOperateTypeEnum = isAudit? com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.REFUND_MONEY_TIMEOUT_SUCCESS: com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.REFUND_MONEY_SUCCESS;
            localMessageApi.sendRefundOperationRecord(refundAuditRes.getRefund(), refundAuditRes.getNextState(), RefundEnum.State.SUCCESS,  refundOperateTypeEnum);
        }
    }

    /**
     * 售后通过: 渠道商订单-供货商品
     * @param refundAuditRes
     */
    public void doRefundPassForChannel(RefundAuditRes refundAuditRes) {
        //售后打款
        SellAfterRefundReq sellAfterRefundReq = new SellAfterRefundReq();
        RefundDTO refund = refundAuditRes.getRefund();
        sellAfterRefundReq.setAccountId(refund.getChannelId());
        sellAfterRefundReq.setRefundAmount(refund.getRefundAmount());
        sellAfterRefundReq.setServiceAmount(refund.getServiceAmount());
        sellAfterRefundReq.setSellAfterOrderNo(refund.getId());
        sellAfterRefundReq.setOrderNo(refund.getOrderId());
        MemberRefundRes memberRefundRes = balancePayApi.sellAfterRefund(sellAfterRefundReq);
        if (StrUtil.isNotBlank(memberRefundRes.getRefundWarnMsg())) {
            throw new PlatformException(OrderErrorCode.REFUND_FAIL, memberRefundRes.getRefundWarnMsg());
        }
        //售后已打款通知
        refundDomain.sellAfterRefundNotify(refund.getId(), refund.getChannelId(), memberRefundRes.getThirdTradeNo());
        //售后完成消息
        localMessageApi.sendRefundPassMessage(refund);
        //SKU订单售后通过通知订单
        if(ObjectUtil.isNotEmpty(refundAuditRes.getSkuOrderIdList())){
            orderDomain.tripSpuOrderChange(null, null, refundAuditRes.getSkuOrderIdList());
        }
        //处理待结算记录
        Map<Long, List<RefundItemVO>> refundItemMap = refund.getItem().stream().collect(Collectors.groupingBy(RefundItemVO::getSkuOrderId));
        for (Long skuOrderId : refundAuditRes.getSkuOrderIdList()) {
            Integer result = settleDomain.closeSettleOrder(skuOrderId, refund.getId());
            if(result == null){
                //无需操作
            }else if(result == 0){
                Money refundAmount = Money.ZERO;
                Long spuId = null;
                List<RefundItemVO> refundItemVOS = refundItemMap.get(skuOrderId);
                if(ObjectUtil.isNotEmpty(refundItemVOS)){
                    for (RefundItemVO refundItemVO : refundItemVOS) {
                        refundAmount = refundItemVO.getSupplierAmount();
                        spuId = refundItemVO.getSpuId();
                    }
                }
            }else if(result == 1){
                //无需操作
            }
        }
    }

    /**
     * 售后通过:C端订单-自营商品
     * @param refundAuditRes
     */
    public void doRefundPassForMemberAndCustom(RefundAuditRes refundAuditRes) {
        //C端打款
        SellAfterRefundReq sellAfterRefundReq = new SellAfterRefundReq();
        RefundDTO refund = refundAuditRes.getRefund();
        sellAfterRefundReq.setAccountId(refund.getChannelId());
        sellAfterRefundReq.setRefundAmount(refund.getRefundAmount());
        sellAfterRefundReq.setServiceAmount(refund.getServiceAmount());
        sellAfterRefundReq.setSellAfterOrderNo(refund.getId());
        sellAfterRefundReq.setOrderNo(refund.getOrderId());
        MemberRefundRes memberRefundRes = balancePayApi.sellAfterRefund(sellAfterRefundReq);
        if (StrUtil.isNotBlank(memberRefundRes.getRefundWarnMsg())) {
            throw new PlatformException(OrderErrorCode.REFUND_FAIL, memberRefundRes.getRefundWarnMsg());
        }
        //售后已打款通知
        refundDomain.sellAfterRefundNotify(refundAuditRes.getRefund().getId(), null, memberRefundRes.getThirdTradeNo());
        //售后完成消息
        localMessageApi.sendRefundPassMessage(refund);
        //SKU订单售后通过通知订单
        if(ObjectUtil.isNotEmpty(refundAuditRes.getSkuOrderIdList())){
            orderDomain.tripSpuOrderChange(null, null, refundAuditRes.getSkuOrderIdList());
        }
    }
    /**
     * 售后通过:C端订单-供货商品
     * @param refundAuditRes
     */
    public void doRefundPassForMemberAndSelection(RefundAuditRes refundAuditRes) {
        //C端打款
        SellAfterRefundReq sellAfterRefundReq = new SellAfterRefundReq();
        RefundDTO refund = refundAuditRes.getRefund();
        sellAfterRefundReq.setAccountId(refund.getChannelId());
        sellAfterRefundReq.setRefundAmount(refund.getRefundAmount());
        sellAfterRefundReq.setServiceAmount(refund.getServiceAmount());
        sellAfterRefundReq.setSellAfterOrderNo(refund.getId());
        sellAfterRefundReq.setOrderNo(refund.getOrderId());
        MemberRefundRes memberRefundRes = balancePayApi.sellAfterRefund(sellAfterRefundReq);
        if (StrUtil.isNotBlank(memberRefundRes.getRefundWarnMsg())) {
            throw new PlatformException(OrderErrorCode.REFUND_FAIL, memberRefundRes.getRefundWarnMsg());
        }
        //售后已打款通知
        refundDomain.sellAfterRefundNotify(refundAuditRes.getRefund().getId(), null, memberRefundRes.getThirdTradeNo());
        //售后完成消息
        // 供货商品需要供应商确认,这里就不发送消息 260129
//        RefundPassEvent refundPassEvent =  RefundUtil.refund2RefundPassEvent(refundAuditRes.getRefund());
//        localMessageFacade.sendMessage(Tag.REFUND_PASS, refundPassEvent, RefundPassEvent.class.getCanonicalName());
        //SKU订单售后通过通知订单
        if(ObjectUtil.isNotEmpty(refundAuditRes.getSkuOrderIdList())){
            orderDomain.tripSpuOrderChange(null, null, refundAuditRes.getSkuOrderIdList());
        }
    }
    @Override
    // TODO[#171-seata] 原 Seata @GlobalTransactional 降级为本地事务(Base 未接 Seata); 供应商确认退货运费编排走单体本地事务, 待 Seata 装配后恢复分布式全局事务
    @Transactional(rollbackFor = Exception.class)
    public void supplierConfirmRefundFreight(Long refundId) {
        //确认收货
        RefundAuditRes refundAuditRes = refundDomain.confirmRefundFreight(refundId);
        //售后通过处理
        if(refundAuditRes.isRefundPass()){
            doRefundPassForChannel(refundAuditRes);
        }
        RefundDTO refund = refundAuditRes.getRefund();
        localMessageApi.sendRefundOperationRecord(refund, RefundEnum.State.RECEIVE_WAIT, refund.getRefundState(), com.newzkl.platform.base.common.ddd.model.enums.order.RefundEnum.RefundOperateTypeEnum.SUPPLIER_CONFIRM_RECEIPT);
    }
}
