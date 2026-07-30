package com.newzkl.platform.base.biz.order.application.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.application.service.RefundService;
import com.newzkl.platform.base.biz.order.domain.service.RefundDomain;
import com.newzkl.platform.base.biz.order.model.order.req.ApplyPlatformReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundReq;
import com.newzkl.platform.base.biz.order.model.order.vo.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundFreightVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundVO;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 售后应用服务实现
 *
 * <p>职责为编排, 全部落库与状态流转委托 {@code RefundDomain}, 本层不写查询。</p>
 *
 * <p><b>当前实现范围</b>: 查询与导出类方法已完整实现 (透传领域层)。审核、收货、
 * 平台介入等<b>状态流转</b>类方法暂未实现, 调用即抛 {@code UnsupportedOperationException}
 * —— 根因在领域层: {@code RefundDomainImpl} 对应方法同样是缺口 (缺订单主聚合 / 退款打款端口 /
 * 本地消息表 MQ 端口)。本层不做绕过实现, 避免绕开领域校验改变业务语义。</p>
 *
 * <p><b>缺口清单 (TODO[domain-gap])</b></p>
 * <ul>
 *   <li>{@code refundCreateApi} / {@code refundCreate} —— 需订单主聚合取 SPU/SKU 订单实体后才能建单</li>
 *   <li>{@code supplierAudit} / {@code channelAudit} —— 需领域层 {@code agreeAudit}/{@code refuseAudit}</li>
 *   <li>{@code supplierConfirmRefundFreight} / {@code merchantConfirmRefundFreight} —— 需退款打款端口</li>
 *   <li>{@code applyPlatform} / {@code platformExecute} / {@code refuseRefundFreight}
 *       / {@code stopAudit} / {@code submitRefundFreight} / {@code outRefuseRefundFreight} —— 领域层同名方法均为缺口</li>
 * </ul>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    /**
     * 缺口说明统一前缀
     */
    private static final String GAP = "TODO[domain-gap]: ";

    private final RefundDomain refundDomain;

    @Override
    public Long refundCreateApi(RefundReq refundCommand) {
        throw new UnsupportedOperationException(GAP + "API 建售后单需订单主聚合取 SPU/SKU 订单实体, biz-order 暂无 OrderDomain/OrderAgg");
    }

    @Override
    public Long refundCreate(RefundReq refundCommand) {
        throw new UnsupportedOperationException(GAP + "建售后单需订单主聚合取 SPU/SKU 订单实体, biz-order 暂无 OrderDomain/OrderAgg");
    }

    @Override
    public void supplierAudit(Long refundId, Integer execute, boolean isAudit) {
        throw new UnsupportedOperationException(GAP + "供应商审核依赖 RefundDomain.agreeAudit/refuseAudit, 领域层为缺口");
    }

    @Override
    public void channelAudit(Long refundId, String spuOrderNo, Integer execute, String reason, boolean isAudit) {
        throw new UnsupportedOperationException(GAP + "渠道审核依赖 RefundDomain.agreeAuditV2/refuseAudit, 领域层为缺口");
    }

    @Override
    public void supplierConfirmRefundFreight(Long refundId) {
        throw new UnsupportedOperationException(GAP + "供应商确认收货会触发退款打款, 缺退款打款端口");
    }

    @Override
    public void merchantConfirmRefundFreight(Long id) {
        throw new UnsupportedOperationException(GAP + "商户确认收货会触发退款打款, 缺退款打款端口");
    }

    @Override
    public void applyPlatform(ApplyPlatformReq applyPlatformCommand) {
        throw new UnsupportedOperationException(GAP + "申请平台介入依赖 RefundDomain.applyPlatform, 领域层为缺口");
    }

    @Override
    public void submitRefundFreight(RefundFreightVO refundFreightVO) {
        throw new UnsupportedOperationException(GAP + "提交退货物流依赖 RefundDomain.submitRefundFreight, 领域层为缺口");
    }

    @Override
    public void platformExecute(Long refundId, Integer execute) {
        throw new UnsupportedOperationException(GAP + "平台介入处理依赖 RefundDomain.platformExecute, 领域层为缺口");
    }

    @Override
    public void refuseRefundFreight(Long refundId) {
        throw new UnsupportedOperationException(GAP + "拒绝收货依赖 RefundDomain.refuseRefundFreight, 领域层为缺口");
    }

    @Override
    public void stopAudit(RoleEnum.CompanyRole roleId, Long accountId, Long refundId) {
        throw new UnsupportedOperationException(GAP + "终止售后依赖 RefundDomain.stopAudit, 领域层为缺口");
    }

    @Override
    public void outRefuseRefundFreight(Long refundId) {
        throw new UnsupportedOperationException(GAP + "外部供应商拒绝收货依赖 RefundDomain.outRefuseRefundFreight, 领域层为缺口");
    }

    @Override
    public ApiRefundFreightAddressVO getOutRefundAddress(String spuOrderNo, Long spuId) {
        return refundDomain.getOutRefundAddress(spuOrderNo, spuId);
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
}
