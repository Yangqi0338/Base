package com.newzkl.platform.base.biz.order.domain.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.RefundRepository;
import com.newzkl.platform.base.biz.order.domain.service.RefundDomain;
import com.newzkl.platform.base.biz.order.model.order.dto.SkuOrder;
import com.newzkl.platform.base.biz.order.model.order.dto.SpuOrder;
import com.newzkl.platform.base.biz.order.model.order.req.ApplyPlatformReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundPageReq;
import com.newzkl.platform.base.biz.order.model.order.req.RefundReq;
import com.newzkl.platform.base.biz.order.model.order.res.RefundAuditRes;
import com.newzkl.platform.base.biz.order.model.order.res.RefundCreateRes;
import com.newzkl.platform.base.biz.order.model.order.vo.ApiRefundFreightAddressVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundExcelVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundFreightVO;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 售后单领域服务实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.sale.domain.refund.service.impl.RefundDomainServiceImpl}。
 * 校验统一抛 {@code PlatformException}, 创建/更新时间交由 MyBatis-Plus 自动填充。</p>
 *
 * <p><b>当前实现范围</b>: 售后单的查询与导出能力已完整实现 (走
 * {@code RefundRepository} 端口)。售后<b>状态流转</b>类方法暂未实现, 调用即抛
 * {@code UnsupportedOperationException} —— 见下方缺口清单。这样做是为了让 Spring 容器
 * 能装配本 bean 使应用启动, 而不是用静默的空实现假装功能可用。</p>
 *
 * <p><b>缺口清单 (TODO[domain-gap])</b>: 以下方法依赖 biz-order 尚未建立的构件 ——
 * ① 无订单主聚合 ({@code OrderDomain}/{@code OrderAgg}), 拿不到 SPU/SKU 订单实体与其状态机;
 * ② 无退款打款端口 (原 {@code PayBaseResult} 在 biz-finance-model, 本模块未依赖);
 * ③ 无 {@code LocalMessageFacade}/本地消息表 MQ 端口, 售后事件无法可靠投递。</p>
 * <ul>
 *   <li>{@code refundCreate} —— 缺 ①: 需校验 SPU/SKU 订单状态并冻结可退数量</li>
 *   <li>{@code agreeAudit} / {@code refuseAudit} / {@code agreeAuditV2} —— 缺 ①③</li>
 *   <li>{@code applyPlatform} / {@code platformExecute} —— 缺 ①③</li>
 *   <li>{@code confirmRefundFreight} / {@code refuseRefundFreight} / {@code outRefuseRefundFreight} —— 缺 ①②③</li>
 *   <li>{@code submitRefundFreight} —— 缺 ①</li>
 *   <li>{@code stopAudit} —— 缺 ①③</li>
 *   <li>{@code sellAfterRefundNotify} —— 缺 ②③: 打款结果回写</li>
 * </ul>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class RefundDomainImpl implements RefundDomain {

    /**
     * 状态流转类方法的统一缺口说明前缀
     */
    private static final String GAP = "TODO[domain-gap]: ";

    private final RefundRepository refundRepository;

    @Override
    public RefundCreateRes refundCreate(RefundReq refundCommand, SpuOrder spuOrder, List<SkuOrder> skuOrders) {
        throw new UnsupportedOperationException(GAP + "售后创建需订单主聚合校验 SPU/SKU 订单状态与可退数量, biz-order 暂无 OrderDomain/OrderAgg");
    }

    @Override
    public RefundAuditRes agreeAudit(Long refundId, Integer role) {
        throw new UnsupportedOperationException(GAP + "同意售后需订单主聚合改单 + MQ 通知端口, 两者暂缺");
    }

    @Override
    public RefundAuditRes refuseAudit(Long refundId, String reason) {
        throw new UnsupportedOperationException(GAP + "拒绝售后需订单主聚合改单 + MQ 通知端口, 两者暂缺");
    }

    @Override
    public RefundAuditRes agreeAuditV2(Long refundId, RoleEnum.CompanyRole role) {
        throw new UnsupportedOperationException(GAP + "平台侧同意售后需订单主聚合改单 + MQ 通知端口, 两者暂缺");
    }

    @Override
    public void applyPlatform(ApplyPlatformReq applyPlatformCommand) {
        throw new UnsupportedOperationException(GAP + "申请平台介入需订单主聚合与售后状态机, 暂缺");
    }

    @Override
    public RefundAuditRes confirmRefundFreight(Long refundId) {
        throw new UnsupportedOperationException(GAP + "确认退货收货需触发退款打款, 缺退款打款端口");
    }

    @Override
    public void submitRefundFreight(RefundFreightVO refundFreightVO) {
        throw new UnsupportedOperationException(GAP + "提交退货物流需订单主聚合校验售后归属, 暂缺");
    }

    @Override
    public void platformExecute(Long refundId, Integer execute) {
        throw new UnsupportedOperationException(GAP + "平台介入处理需订单主聚合改单 + MQ 通知端口, 两者暂缺");
    }

    @Override
    public void refuseRefundFreight(Long refundId) {
        throw new UnsupportedOperationException(GAP + "拒绝收货需订单主聚合改单 + MQ 通知端口, 两者暂缺");
    }

    @Override
    public void stopAudit(RoleEnum.CompanyRole roleId, Long accountId, Long refundId) {
        throw new UnsupportedOperationException(GAP + "终止售后需订单主聚合回滚可退数量 + MQ 通知端口, 两者暂缺");
    }

    @Override
    public void sellAfterRefundNotify(Long refundId, Long channelId, String outRefundId) {
        throw new UnsupportedOperationException(GAP + "售后打款结果回写需退款打款端口与本地消息表, 两者暂缺");
    }

    @Override
    public void outRefuseRefundFreight(Long refundId) {
        throw new UnsupportedOperationException(GAP + "外部供应商拒绝收货需订单主聚合改单 + MQ 通知端口, 两者暂缺");
    }

    @Override
    public ApiRefundFreightAddressVO getOutRefundAddress(String spuOrderNo, Long spuId) {
        if (StrUtil.isBlank(spuOrderNo)) {
            throw new PlatformException(BaseErrorCode.PARAM, "SPU订单号不能为空");
        }
        if (spuId == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "SPU_ID不能为空");
        }
        return refundRepository.getOutRefundAddress(spuOrderNo, spuId);
    }

    @Override
    public RefundVO refundVO(Long refundId) {
        if (refundId == null) {
            throw new PlatformException(BaseErrorCode.PARAM, "售后单ID不能为空");
        }
        return refundRepository.refundVO(refundId);
    }

    @Override
    public RefundVO refundVoBySpuOrderId(String spuOrderNo) {
        if (StrUtil.isBlank(spuOrderNo)) {
            throw new PlatformException(BaseErrorCode.PARAM, "SPU订单号不能为空");
        }
        return refundRepository.refundVoBySpuOrderId(spuOrderNo);
    }

    @Override
    public Page<RefundVO> refundVOList(RefundPageReq refundQuery) {
        if (ObjectUtil.isNull(refundQuery)) {
            throw new PlatformException(BaseErrorCode.PARAM, "分页参数不能为空");
        }
        return refundRepository.refundVOList(refundQuery);
    }

    @Override
    public List<RefundExcelVO> exportRefund(RefundPageReq refundQuery) {
        if (ObjectUtil.isNull(refundQuery)) {
            throw new PlatformException(BaseErrorCode.PARAM, "导出查询参数不能为空");
        }
        return refundRepository.exportRefund(refundQuery);
    }
}
