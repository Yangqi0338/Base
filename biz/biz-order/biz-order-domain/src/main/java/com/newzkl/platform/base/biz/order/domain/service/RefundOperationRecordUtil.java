package com.newzkl.platform.base.biz.order.domain.service;

import cn.hutool.core.util.ObjectUtil;

import com.newzkl.platform.base.biz.order.domain.adapt.api.RefundOperationRecordApi;
import com.newzkl.platform.base.biz.order.facade.model.order.RefundOperationRecordRPC;
import com.newzkl.platform.base.biz.order.model.dto.Refund;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.RoleEnum;
import com.newzkl.platform.base.common.ddd.model.enums.finance.RefundEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.RefundOperateTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 售后操作记录消息发送工具类
 * 封装重复的消息组装和发送逻辑
 *
 * <p>迁移: 原直连 {@code @DubboReference ILocalMessageFacade} + MQ/Tag 常量违依赖硬线,
 * 改走 {@link RefundOperationRecordApi} 出站端口, MQ topic/tag 与投递由 infra 实现
 *
 * @author sijiwang
 */
@Slf4j
@Component
public class RefundOperationRecordUtil {

    @Autowired
    private RefundOperationRecordApi refundOperationRecordApi;

    /**
     * 构建并发送售后操作记录消息
     * @param refund 售后单实体
     * @param beforeState 操作前状态
     * @param afterState 操作后状态
     * @param operationContent 操作内容（如"售后单创建"、"供应商确认收货"）
     */
    public void sendRefundOperationRecord(Refund refund, RefundEnum.State beforeState, RefundEnum.State afterState, String operationContent, RefundOperateTypeEnum operationType) {
        try {
            // 1. 组装消息对象（封装所有重复的参数赋值逻辑）
            RefundOperationRecordRPC recordRPC = buildRefundOperationRecordRPC(refund, beforeState, afterState, operationContent, operationType);

            // 2. 发送消息（统一异常处理，避免消息发送失败导致主流程异常）
            refundOperationRecordApi.sendRefundOperationRecord(recordRPC);
            log.info("售后操作记录消息发送成功，refundId: {}, operationContent: {}", refund.getId(), operationContent);
        } catch (Exception e) {
            log.error("售后操作记录消息发送失败，refundId: {}", refund.getId(), e);
            // 消息发送失败不抛异常，避免影响主业务流程（可根据业务需求调整）
        }
    }

    /**
     * 重载方法：支持手动指定spuOrderId（适配部分场景）
     */
    public void sendRefundOperationRecord(Long spuOrderId, Long refundId, RefundEnum.State beforeState, RefundEnum.State afterState,
                                          String operationContent, Integer refundAmount, String reason) {
        try {
            RefundOperationRecordRPC recordRPC = new RefundOperationRecordRPC();
            recordRPC.setSpuOrderId(spuOrderId);
            recordRPC.setRefundId(refundId);
            fillOperatorInfo(recordRPC); // 填充操作人信息
            recordRPC.setBeforeState(beforeState);
            recordRPC.setAfterState(afterState);
            recordRPC.setOperationContent(operationContent);
            recordRPC.setRefundAmount(refundAmount);
            recordRPC.setReason(reason);

            refundOperationRecordApi.sendRefundOperationRecord(recordRPC);
            log.info("售后操作记录消息发送成功，refundId: {}, operationContent: {}", refundId, operationContent);
        } catch (Exception e) {
            log.error("售后操作记录消息发送失败，refundId: {}", refundId, e);
        }
    }

    /**
     * 构建售后操作记录消息对象（核心封装）
     */
    private RefundOperationRecordRPC buildRefundOperationRecordRPC(Refund refund, RefundEnum.State beforeState,
                                                                   RefundEnum.State afterState, String operationContent,RefundOperateTypeEnum operationType) {
        RefundOperationRecordRPC recordRPC = new RefundOperationRecordRPC();
        // 基础订单/售后单信息
        recordRPC.setSpuOrderId(refund.getSpuOrderId());
        recordRPC.setRefundId(refund.getId());
        // 操作人信息（封装重复的SecurityUtils调用）
        fillOperatorInfo(recordRPC);
        // 状态信息
        recordRPC.setBeforeState(beforeState);
        recordRPC.setAfterState(afterState);
        recordRPC.setOperationType(operationType);
        // 操作内容
        recordRPC.setOperationContent(operationContent);
        recordRPC.setRefundAmount(refund.getRefundAmount());
        recordRPC.setReason(buildReason(refund));

        return recordRPC;
    }

    /**
     * 填充操作人公共信息（抽离重复逻辑）
     */
    private void fillOperatorInfo(RefundOperationRecordRPC recordRPC) {
        if (ObjectUtil.isNull(SecurityUtils.getAccountId())){
            recordRPC.setOperatorId(0L);
            recordRPC.setOperatorRoleCode(RoleEnum.CompanyRole.PLATFORM);
            recordRPC.setOperatorClient(CommonEnum.Client.ADMIN.getCode());
            recordRPC.setOperatorName("系统");
        }else {
            recordRPC.setOperatorId(SecurityUtils.getAccountId());
            recordRPC.setOperatorRoleCode(SecurityUtils.getRole());
            recordRPC.setOperatorClient(SecurityUtils.getClient().name());
            recordRPC.setOperatorName(SecurityUtils.getUsername());
        }

    }

    /**
     * 构建操作原因（统一格式）
     */
    private String buildReason(Refund refund) {
        return refund.getReason() + " : " + refund.getRemark();
    }
}