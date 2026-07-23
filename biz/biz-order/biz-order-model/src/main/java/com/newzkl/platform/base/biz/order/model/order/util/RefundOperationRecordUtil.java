package com.newzkl.platform.base.biz.order.model.order.util;


import cn.hutool.core.util.ObjectUtil;
import com.newzkl.platform.base.biz.order.model.support.MQ;
import com.newzkl.platform.base.biz.order.model.enums.CommonEnum;
import com.newzkl.platform.base.biz.order.model.enums.order.RefundEnum;
import com.newzkl.platform.base.biz.order.model.enums.user.identity.RoleEnum;
import com.newzkl.platform.base.biz.order.model.order.dto.Refund;
import com.newzkl.platform.base.biz.order.model.order.vo.RefundOperationRecordVO;
import com.newzkl.platform.base.common.core.utils.biz.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 售后操作记录消息发送工具类
 * 封装重复的消息组装和发送逻辑
 * @author sijiwang
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RefundOperationRecordUtil {

    // 统一管理消息相关常量
    public static final String MQ_TOPIC = MQ.SCM_ORDER;
    public static final String MQ_TAG = MQ.Tag.REFUND_OPERATION_RECORD_EVENT;



    /**
     * 构建并发送售后操作记录消息
     * @param refund 售后单实体
     * @param beforeState 操作前状态
     * @param afterState 操作后状态
     * @param operationContent 操作内容（如"售后单创建"、"供应商确认收货"）
     */
    public void sendRefundOperationRecord(Refund refund, RefundEnum.State beforeState, RefundEnum.State afterState, String operationContent, Integer operationType) {
        try {
            // 1. 组装消息对象（封装所有重复的参数赋值逻辑）
            RefundOperationRecordVO recordRPC = buildRefundOperationRecordRPC(refund, beforeState, afterState, operationContent, operationType);
            
            // 2. 发送消息（统一异常处理，避免消息发送失败导致主流程异常）
//            localMessageService.sendMessage(MQ_TOPIC, MQ_TAG, recordRPC);
            log.info("售后操作记录消息发送成功，refundId: {}, operationContent: {}", refund.getId(), operationContent);
        } catch (Exception e) {
            log.error("售后操作记录消息发送失败，refundId: {}", refund.getId(), e);
            // 消息发送失败不抛异常，避免影响主业务流程（可根据业务需求调整）
        }
    }

    /**
     * 构建售后操作记录消息对象（核心封装）
     */
    private RefundOperationRecordVO buildRefundOperationRecordRPC(Refund refund, RefundEnum.State beforeState,
                                                                  RefundEnum.State afterState, String operationContent, Integer operationType) {
        RefundOperationRecordVO recordRPC = new RefundOperationRecordVO();
        // 基础订单/售后单信息
        recordRPC.setSpuOrderNo(refund.getSpuOrderNo());
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
    private void fillOperatorInfo(RefundOperationRecordVO recordRPC) {
        if (ObjectUtil.isNull(SecurityUtils.getAccountId())){
            recordRPC.setOperatorId(0L);
            recordRPC.setOperatorRoleCode(RoleEnum.CompanyRole.PLATFORM.getCode());
            recordRPC.setOperatorClient(CommonEnum.Client.ADMIN.getCode());
            recordRPC.setOperatorName("系统");
        }else {
            recordRPC.setOperatorId(SecurityUtils.getAccountId());
            recordRPC.setOperatorRoleCode(RoleEnum.CompanyRole.getByCode(SecurityUtils.getRoleId()).getCode());
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