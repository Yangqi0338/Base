package com.newzkl.platform.base.biz.goods.application.goods.service.approval.impl;

import com.alibaba.fastjson2.JSON;
import com.newzkl.platform.base.biz.goods.application.goods.service.approval.ApprovalService;
import com.newzkl.platform.base.biz.goods.domain.spu.service.ExecuteLogDomain;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuDomain;
import com.newzkl.platform.base.biz.goods.domain.spu.service.SpuWorkflowDomain;
import com.newzkl.platform.base.biz.goods.model.enums.AuditEnum;
import com.newzkl.platform.base.biz.goods.model.goods.req.audit.ApprovalResultReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.audit.AuditDataSpuVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品上传审批应用服务实现
 *
 * <p>职责为编排: 取审批流数据 → 落审批人改动 → 按审批状态委托 {@code SpuDomain} 变更 SPU 状态 →
 * 记录销售价操作日志。全部落库动作均由领域层完成, 本层不写查询。</p>
 *
 * <p>对应源实现 {@code GoodsAuditFacadeImpl#spuCreateEvent} (Dubbo 消费审批 MQ 事件),
 * Base 侧收敛为应用服务, 由 plugin-audit 侧触发。</p>
 *
 * <p><b>缺口清单 (TODO[capability-gap])</b> —— 以下均为<b>跨域副作用</b>, 缺失不影响商品域主流程
 * (SPU 状态照常流转), 故不抛异常, 仅告警日志占位:</p>
 * <ul>
 *   <li>审核通过后<b>通知供应商</b> —— 需 user 域 {@code supplierFacade#spuCreateAuditSuccess}, Base 无对等 port</li>
 *   <li>审核通过后<b>创建商品结算计划</b> —— 需 user 域供应商结算周期配置 + sale 域 {@code settleFacade#settleGoodsSave}</li>
 *   <li>审核通过后<b>邀请人等级自动升级</b> —— 需 user 域 {@code accountFacade#levelUp}</li>
 *   <li>审核失败/终止后<b>返还商品位</b> —— 需 finance 域 {@code balancePayApi#goodsAuditFailAddGoodsSeat}</li>
 *   <li>操作日志 type 码 —— Base 无 {@code ExecuteEnum}, 此处内联源枚举
 *       {@code ExecuteEnum.Type.SPU_SALE_PRICE} 的字面量 0, 待枚举补齐后替换</li>
 * </ul>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    /**
     * 操作日志类型: SPU 销售价变动
     *
     * <p>TODO[capability-gap]: 源为 {@code ExecuteEnum.Type.SPU_SALE_PRICE.getCode()},
     * Base 尚无 {@code ExecuteEnum}, 暂内联字面量保持行为一致。</p>
     */
    private static final Integer EXECUTE_TYPE_SPU_SALE_PRICE = 0;

    private final SpuWorkflowDomain spuWorkflowDomain;
    private final SpuDomain spuDomain;
    private final ExecuteLogDomain executeLogDomain;

    /**
     * 处理商品上传审批结果
     *
     * <p>通过: 落审批人对业务数据的改动, 再让 SPU 审核通过落库, 并记录一条销售价操作日志。
     * 未通过 / 终止: 分别落拒绝原因与终止状态。其余状态不做处理。</p>
     *
     * @param req 审批结果请求 (id 为审批流 ID, state 取 {@code AuditEnum.State} 名称)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approval(ApprovalResultReq req) {
        AuditDataSpuVO messageData = spuWorkflowDomain.detail(req.getId());
        SpuVO spuVO = JSON.parseObject(messageData.getSpuCreateInfoJson(), SpuVO.class);

        if (AuditEnum.State.SUCCESS.name().equals(req.getState())) {
            // 审核通过: 先落审批人改动, 再落 SPU 通过状态
            spuWorkflowDomain.editBusinessData(req.getId(), req.getEditCommand());
            spuDomain.spuAuditSuccess(spuVO, messageData.getSkuSalePriceJson());
            // 记录 SKU 销售价操作日志
            executeLogDomain.executeLogSave(EXECUTE_TYPE_SPU_SALE_PRICE,
                    messageData.getSpuId(),
                    messageData.getAdminUserName(),
                    messageData.getSpuCreateInfoJson(),
                    messageData.getSkuSalePriceJson());
            log.warn("TODO[capability-gap]: 商品审核通过的跨域副作用未执行 (通知供应商 / 结算计划 / 邀请人升级), spuId={}",
                    messageData.getSpuId());
        } else if (AuditEnum.State.FAIL.name().equals(req.getState())) {
            spuDomain.spuAuditFail(spuVO, req.getReason());
            log.warn("TODO[capability-gap]: 商品审核失败的商品位返还未执行, spuId={}", messageData.getSpuId());
        } else if (AuditEnum.State.STOP.name().equals(req.getState())) {
            spuDomain.spuAuditStop(spuVO);
            log.warn("TODO[capability-gap]: 商品审核终止的商品位返还未执行, spuId={}", messageData.getSpuId());
        } else {
            log.info("商品上传审批: 无需处理的审批状态, state={}", req.getState());
        }
    }
}
