package com.newzkl.platform.base.biz.order.application.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkl.scm.finance.model.purse.req.SellAfterRefundReq;
import com.zkl.scm.finance.model.purse.res.MemberRefundRes;
import com.zkl.scm.finance.rpc.facade.pay.IBalancePayFacade;
import com.zkl.scm.goods.rpc.model.order.RefundPassEvent;
import com.zkl.scm.infrastructure.mq.biz.service.ILocalMessageService;
import com.zkl.scm.model.constants.common.MQ;
import com.newzkl.platform.base.biz.order.model.enums.goods.SpuEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundOperateTypeEnum;
import com.newzkl.platform.base.biz.order.model.enums.user.identity.RoleEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import com.newzkl.platform.base.biz.order.model.enums.order.OrderErrorCode;
import com.newzkl.platform.base.biz.order.application.service.IQueryOrderService;

import com.newzkl.platform.base.biz.order.application.service.IRefundService;
import com.newzkl.platform.base.biz.order.application.service.IUpdateOrderService;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.IRefundRepository;
import com.newzkl.platform.base.biz.order.domain.service.IRefundDomain;
import com.newzkl.platform.base.biz.order.domain.service.ISettleDomain;
import com.newzkl.platform.base.biz.order.model.order.dto.Refund;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.ApplyPlatformReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundReq;
import com.newzkl.platform.base.biz.order.model.order.res.RefundAuditRes;
import com.newzkl.platform.base.biz.order.model.order.res.RefundCreateRes;
import com.newzkl.platform.base.biz.order.model.order.util.RefundOperationRecordUtil;
import com.newzkl.platform.base.biz.order.model.order.vo.*;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sijiwang
 */
@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements IRefundService {

    private final IQueryOrderService orderDomain;
    private final IRefundDomain refundDomain;
    @DubboReference
    private IBalancePayFacade balancePayFacade;
    private final ILocalMessageService localMessageService;
    private final ISettleDomain settleDomain;
    private final IRefundRepository refundRepository;
    private final IUpdateOrderService updateOrderDomain;

    private final RefundOperationRecordUtil refundOperationRecordUtil;
    @Override
    public Long refundCreateApi(RefundReq refundCommand) {
        if (StrUtil.isNotBlank(refundCommand.getRemark()) && refundCommand.getRemark().length() > 300) {
            throw new ScmException(BaseErrorCode.PARAM, "售后单备注不能超过300字！");
        }
        // 查询订单
        SpuOrder spuOrderAggVO = orderDomain.selectSpuOrder(refundCommand.getSpuOrderNo());
        List<SkuOrder> skuOrders = orderDomain.getBySpuOrderNo(refundCommand.getSpuOrderNo());

        // 售后单创建
        RefundCreateRes refundCreateRes = refundDomain.refundCreate(refundCommand, spuOrderAggVO, skuOrders);
        Refund refund = refundCreateRes.getRefund();
        // 发送协商记录
        refundOperationRecordUtil.sendRefundOperationRecord(refund, RefundEnum.State.CHANNEL_WAIT,
                RefundEnum.State.CHANNEL_WAIT, RefundOperateTypeEnum.LAUNCH_REFUND.getDesc(),
                RefundOperateTypeEnum.LAUNCH_REFUND.getCode());
        // refundOperationRecordRPC.setOperationType(Tag.OperationType.CREATE.getCode());
        // 如果是派发中订单, 售后自动通过
        if (OrderEnum.State.SENDING.getCode().equals(spuOrderAggVO.getOrderState())) {
            supplierAudit(refundCreateRes.getRefundId(), RoleEnum.Switch.ON.getCode(), false);
        }
        return refundCreateRes.getRefundId();
    }

    @Override
    public Long refundCreate(RefundReq refundCommand) {
        if (StrUtil.isNotBlank(refundCommand.getRemark()) && refundCommand.getRemark().length() > 300) {
            throw new ScmException(BaseErrorCode.PARAM, "售后单备注不能超过300字！");
        }
        // 查询订单
        SpuOrder spuOrderVO = orderDomain.selectSpuOrder(refundCommand.getSpuOrderNo());
        List<SkuOrder> skuOrders = orderDomain.getBySpuOrderNo(refundCommand.getSpuOrderNo());
        if (Objects.isNull(spuOrderVO)) {
            throw new ScmException(OrderErrorCode.NOT_EXISTS, "订单不存在！");
        }
        if (refundCommand.getRefundType().equals(RefundEnum.RefundType.MONEY.getCode())) {
            Set<Integer> moneyOrderStates = OrderEnum.State.getMoneyOrderStates();
            if (!moneyOrderStates.contains(spuOrderVO.getOrderState())) {
                throw new ScmException(BaseErrorCode.CUSTOM, "当前状态不能发起仅退款！");
            }
        } else if (refundCommand.getRefundType().equals(RefundEnum.RefundType.MONEY_GOODS.getCode())) {
            Set<Integer> moneyGoodsOrderStates = OrderEnum.State.getMoneyGoodsOrderStates();
            if (!moneyGoodsOrderStates.contains(spuOrderVO.getOrderState())) {
                throw new ScmException(BaseErrorCode.CUSTOM, "当前状态不能发起退货退款！");
            }
        } else {
            throw new ScmException(OrderErrorCode.REFUND_FAIL, "售后单类型错误！");
        }
        // 售后单创建
        RefundCreateRes refundCreateRes = refundDomain.refundCreate(refundCommand, spuOrderVO, skuOrders);
        Refund refund = refundCreateRes.getRefund();
        // 发送协商记录
        refundOperationRecordUtil.sendRefundOperationRecord(refund, RefundEnum.State.CHANNEL_WAIT,
                RefundEnum.State.CHANNEL_WAIT, RefundOperateTypeEnum.LAUNCH_REFUND.getDesc(),
                RefundOperateTypeEnum.LAUNCH_REFUND.getCode());
        // refundOperationRecordRPC.setOperationType(Tag.OperationType.CREATE.getCode());
        // 如果是派发中订单, 售后自动通过
        if (OrderEnum.State.SENDING.getCode().equals(spuOrderVO.getOrderState())) {
            supplierAudit(refundCreateRes.getRefundId(), RoleEnum.Switch.ON.getCode(), false);
        }
        return refundCreateRes.getRefundId();
    }

    @Override
    public void supplierAudit(Long refundId, Integer execute, boolean isAudit) {
        // 审核
        RefundAuditRes refundAuditRes = null;
        if (RoleEnum.Switch.ON.getCode().equals(execute)) {
            refundAuditRes = refundDomain.agreeAuditV2(refundId, RoleEnum.CompanyRole.SUPPLIER);

            RefundOperateTypeEnum refundOperateTypeEnum = isAudit ? RefundOperateTypeEnum.SUPPLIER_TIMEOUT_AGREE
                    : RefundOperateTypeEnum.SUPPLIER_AGREE;
            refundOperationRecordUtil.sendRefundOperationRecord(refundAuditRes.getRefund(),
                    RefundEnum.State.SUPPLIER_WAIT, refundAuditRes.getNextState(),
                    refundOperateTypeEnum.getDesc(), refundOperateTypeEnum.getCode());
        } else {
            refundAuditRes = refundDomain.refuseAudit(refundId, "");
            refundOperationRecordUtil.sendRefundOperationRecord(refundAuditRes.getRefund(),
                    RefundEnum.State.SUPPLIER_WAIT, refundAuditRes.getNextState(),
                    RefundOperateTypeEnum.SUPPLIER_REFUSE.getDesc(), RefundOperateTypeEnum.SUPPLIER_REFUSE.getCode());
        }

        // 售后通过处理
        if (refundAuditRes.isRefundPass()) {
            doRefundPassForChannel(refundAuditRes);
            RefundOperateTypeEnum refundOperateTypeEnum = isAudit ? RefundOperateTypeEnum.REFUND_MONEY_TIMEOUT_SUCCESS
                    : RefundOperateTypeEnum.REFUND_MONEY_SUCCESS;
            refundOperationRecordUtil.sendRefundOperationRecord(refundAuditRes.getRefund(),
                    refundAuditRes.getNextState(), RefundEnum.State.SUCCESS,
                    RefundOperateTypeEnum.REFUND_MONEY_SUCCESS.getDesc(),
                    RefundOperateTypeEnum.REFUND_MONEY_SUCCESS.getCode());
        }
    }

    @Override
    public void channelAudit(Long refundId, String spuOrderNo, Integer execute, String reason, boolean isAudit) {
        if (refundId == null && spuOrderNo == null) {
            throw new ScmException(BaseErrorCode.PARAM, "售后单id和spu订单id不能都为空！");
        }
        RefundVO refund = null;
        if (refundId == null) {
            refund = refundRepository.refundVoBySpuOrderId(spuOrderNo);
        } else {
            refund = refundRepository.refundVO(refundId);
        }
        if (Objects.isNull(refund)) {
            throw new ScmException(BaseErrorCode.PARAM, "售后单不存在！");
        }
        refundId = refund.getId();
        // 审核
        RefundAuditRes refundAuditRes = null;
        if (RoleEnum.Switch.ON.getCode().equals(execute)) {
            refundAuditRes = refundDomain.agreeAuditV2(refundId, RoleEnum.CompanyRole.CHANNEL);
            RefundOperateTypeEnum refundOperateTypeEnum = isAudit ? RefundOperateTypeEnum.CHANNEL_TIMEOUT_AGREE
                    : RefundOperateTypeEnum.CHANNEL_AGREE;
            refundOperationRecordUtil.sendRefundOperationRecord(refundAuditRes.getRefund(), refund.getRefundState(),
                    refundAuditRes.getNextState(), refundOperateTypeEnum.getDesc(), refundOperateTypeEnum.getCode());
        } else {
            refundAuditRes = refundDomain.refuseAudit(refundId, reason);
            refundOperationRecordUtil.sendRefundOperationRecord(refundAuditRes.getRefund(), refund.getRefundState(),
                    refundAuditRes.getNextState(), RefundOperateTypeEnum.CHANNEL_REFUSE.getDesc(),
                    RefundOperateTypeEnum.CHANNEL_REFUSE.getCode());
        }

        // 售后通过处理
        if (refundAuditRes.isRefundPass()) {
            if (SpuEnum.ChannelType.SELECTION == refundAuditRes.getRefund().getSpuChannelType()) {
                // 供货商品
                doRefundPassForMemberAndSelection(refundAuditRes);
            } else if (SpuEnum.ChannelType.CUSTOM == refundAuditRes.getRefund().getSpuChannelType()) {
                // 自营商品
                doRefundPassForMemberAndCustom(refundAuditRes);
            } else {
                throw new ScmException(BaseErrorCode.PARAM);
            }
            RefundOperateTypeEnum refundOperateTypeEnum = isAudit ? RefundOperateTypeEnum.REFUND_MONEY_TIMEOUT_SUCCESS
                    : RefundOperateTypeEnum.REFUND_MONEY_SUCCESS;
            refundOperationRecordUtil.sendRefundOperationRecord(refundAuditRes.getRefund(),
                    refundAuditRes.getNextState(), RefundEnum.State.SUCCESS, refundOperateTypeEnum.getDesc(),
                    refundOperateTypeEnum.getCode());
        }
    }

    @Override
    public void supplierConfirmRefundFreight(Long refundId) {
        // 确认收货
        RefundAuditRes refundAuditRes = refundDomain.confirmRefundFreight(refundId);
        // 售后通过处理
        if (refundAuditRes.isRefundPass()) {
            doRefundPassForChannel(refundAuditRes);
        }
        Refund refund = refundAuditRes.getRefund();
        refundOperationRecordUtil.sendRefundOperationRecord(refund, RefundEnum.State.RECEIVE_WAIT,
                refund.getRefundState(), RefundOperateTypeEnum.SUPPLIER_CONFIRM_RECEIPT.getDesc(),
                RefundOperateTypeEnum.SUPPLIER_CONFIRM_RECEIPT.getCode());
    }

    @Override
    public void merchantConfirmRefundFreight(Long id) {
        // 确认收货
        RefundAuditRes refundAuditRes = refundDomain.confirmRefundFreight(id);
        // 售后通过处理
        if (refundAuditRes.isRefundPass()) {
            if (SpuEnum.ChannelType.SELECTION == refundAuditRes.getRefund().getSpuChannelType()) {
                // 供货商品
                doRefundPassForMemberAndSelection(refundAuditRes);
            } else if (SpuEnum.ChannelType.CUSTOM == refundAuditRes.getRefund().getSpuChannelType()) {
                // 自营商品
                doRefundPassForMemberAndCustom(refundAuditRes);
            } else {
                throw new ScmException(BaseErrorCode.PARAM);
            }
        }
    }

    @Override
    public RefundVO refundVO(Long refundId) {
        return refundDomain.refundVO(refundId);
    }

    @Override
    public RefundVO refundVoBySpuOrderId(String spuOrderNo) {
        return refundDomain.refundVoBySpuOrderId(spuOrderNo);
    }

    @Override
    public Page<RefundVO> refundVOList(RefundPageReq refundQuery) {
        return refundDomain.refundVOList(refundQuery);
    }

    @Override
    public List<RefundExcelVO> exportRefund(RefundPageReq refundQuery) {
        return refundDomain.exportRefund(refundQuery);
    }

    /**
     * 申请平台介入
     */
    @Override
    public void applyPlatform(ApplyPlatformReq applyPlatformCommand) {
        refundDomain.applyPlatform(applyPlatformCommand);
    }

    /**
     * 提交退货物流
     */
    @Override
    public void submitRefundFreight(RefundFreightVO refundFreightVO) {
        refundDomain.submitRefundFreight(refundFreightVO);
    }

    /**
     * 平台介入处理
     */
    @Override
    public void platformExecute(Long refundId, Integer execute) {
        refundDomain.platformExecute(refundId, execute);
    }

    /**
     * 拒绝收货
     */
    @Override
    public void refuseRefundFreight(Long refundId) {
        refundDomain.refuseRefundFreight(refundId);
    }

    /**
     * 终止售后
     */
    @Override
    public void stopAudit(RoleEnum.CompanyRole roleId, Long accountId, Long refundId) {
        refundDomain.stopAudit(roleId, accountId, refundId);
    }

    /**
     * 获取外部订单售后地址
     */
    @Override
    public ApiRefundFreightAddressVO getOutRefundAddress(String spuOrderNo, Long spuId) {
        return refundDomain.getOutRefundAddress(spuOrderNo, spuId);
    }

    /**
     * 外部供应商拒绝退货收货
     */
    @Override
    public void outRefuseRefundFreight(Long refundId) {
        refundDomain.outRefuseRefundFreight(refundId);
    }

    /**
     * 售后通过: 渠道商订单-供货商品
     * 
     * @param refundAuditRes
     */
    private void doRefundPassForChannel(RefundAuditRes refundAuditRes) {
        // 售后打款
        SellAfterRefundReq sellAfterRefundReq = new SellAfterRefundReq();
        Refund refund = refundAuditRes.getRefund();
        sellAfterRefundReq.setAccountId(refund.getChannelId());
        sellAfterRefundReq.setRefundAmount(refund.getRefundAmount());
        sellAfterRefundReq.setServiceAmount(refund.getServiceAmount());
        sellAfterRefundReq.setSellAfterOrderNo(refund.getId());
        sellAfterRefundReq.setOrderNo(refund.getId());
        MemberRefundRes memberRefundRes = balancePayFacade.sellAfterRefund(sellAfterRefundReq);
        if (StrUtil.isNotBlank(memberRefundRes.getRefundWarnMsg())) {
            throw new ScmException(OrderErrorCode.REFUND_FAIL, memberRefundRes.getRefundWarnMsg());
        }
        // 售后已打款通知
        refundDomain.sellAfterRefundNotify(refund.getId(), refund.getChannelId(), memberRefundRes.getThirdTradeNo());

        localMessageService.sendMessage(MQ.SCM_ORDER, MQ.Tag.REFUND_PASS, refund);
        // SKU订单售后通过通知订单
        if (ObjectUtil.isNotEmpty(refundAuditRes.getSkuOrderNoList())) {
            updateOrderDomain.tripSpuOrderChange(null, null, refundAuditRes.getSkuOrderNoList());
        }
        // 处理待结算记录
        Map<String, List<RefundItemVO>> refundItemMap = refund.getItem().stream()
                .collect(Collectors.groupingBy(RefundItemVO::getSkuOrderNo));
        for (String skuOrderId : refundAuditRes.getSkuOrderNoList()) {
            Integer result = settleDomain.closeSettleOrder(skuOrderId, refund.getId());
            if (result == null) {
                // 无需操作
            } else if (result == 0) {
                Integer refundAmount = 0;
                Long spuId = null;
                List<RefundItemVO> refundItemVOS = refundItemMap.get(skuOrderId);
                if (ObjectUtil.isNotEmpty(refundItemVOS)) {
                    for (RefundItemVO refundItemVO : refundItemVOS) {
                        refundAmount = refundItemVO.getSupplierAmount();
                        spuId = refundItemVO.getSpuId();
                    }
                }
            } else if (result == 1) {
                // 无需操作
            }
        }
    }

    /**
     * 售后通过:C端订单-自营商品
     * 
     * @param refundAuditRes
     */
    private void doRefundPassForMemberAndCustom(RefundAuditRes refundAuditRes) {
        // C端打款
        SellAfterRefundReq sellAfterRefundReq = new SellAfterRefundReq();
        Refund refund = refundAuditRes.getRefund();
        sellAfterRefundReq.setAccountId(refund.getChannelId());
        sellAfterRefundReq.setRefundAmount(refund.getRefundAmount());
        sellAfterRefundReq.setServiceAmount(refund.getServiceAmount());
        sellAfterRefundReq.setSellAfterOrderNo(refund.getId());
        sellAfterRefundReq.setOrderNo(refund.getId());
        MemberRefundRes memberRefundRes = balancePayFacade.sellAfterRefund(sellAfterRefundReq);
        if (StrUtil.isNotBlank(memberRefundRes.getRefundWarnMsg())) {
            throw new ScmException(OrderErrorCode.REFUND_FAIL, memberRefundRes.getRefundWarnMsg());
        }
        // 售后已打款通知
        refundDomain.sellAfterRefundNotify(refundAuditRes.getRefund().getId(), null, memberRefundRes.getThirdTradeNo());
        // 售后完成消息
        RefundPassEvent refundPassEvent = TransferUtils.transfer(refundAuditRes.getRefund(), RefundPassEvent::new);
        localMessageService.sendMessage(MQ.SCM_ORDER, MQ.Tag.REFUND_PASS, refundPassEvent);
        // SKU订单售后通过通知订单
        if (ObjectUtil.isNotEmpty(refundAuditRes.getSkuOrderNoList())) {
            updateOrderDomain.tripSpuOrderChange(null, null, refundAuditRes.getSkuOrderNoList());
        }
    }

    /**
     * 售后通过:C端订单-供货商品
     * 
     * @param refundAuditRes
     */
    private void doRefundPassForMemberAndSelection(RefundAuditRes refundAuditRes) {
        // C端打款
        SellAfterRefundReq sellAfterRefundReq = new SellAfterRefundReq();
        Refund refund = refundAuditRes.getRefund();
        sellAfterRefundReq.setAccountId(refund.getChannelId());
        sellAfterRefundReq.setRefundAmount(refund.getRefundAmount());
        sellAfterRefundReq.setServiceAmount(refund.getServiceAmount());
        sellAfterRefundReq.setSellAfterOrderNo(refund.getId());
        sellAfterRefundReq.setOrderNo(refund.getId());
        MemberRefundRes memberRefundRes = balancePayFacade.sellAfterRefund(sellAfterRefundReq);
        if (StrUtil.isNotBlank(memberRefundRes.getRefundWarnMsg())) {
            throw new ScmException(OrderErrorCode.REFUND_FAIL, memberRefundRes.getRefundWarnMsg());
        }
        // 售后已打款通知
        refundDomain.sellAfterRefundNotify(refundAuditRes.getRefund().getId(), null, memberRefundRes.getThirdTradeNo());
        // 售后完成消息
        // 供货商品需要供应商确认,这里就不发送消息 260129
        // RefundPassEvent refundPassEvent =
        // RefundUtil.refund2RefundPassEvent(refundAuditRes.getRefund());
        // localMessageFacade.sendMessage(Tag.REFUND_PASS, refundPassEvent,
        // RefundPassEvent.class.getCanonicalName());
        // SKU订单售后通过通知订单
        if (ObjectUtil.isNotEmpty(refundAuditRes.getSkuOrderNoList())) {
            updateOrderDomain.tripSpuOrderChange(null, null, refundAuditRes.getSkuOrderNoList());
        }
    }
}
