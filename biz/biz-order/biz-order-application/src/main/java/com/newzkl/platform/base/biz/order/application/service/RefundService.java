package com.newzkl.platform.base.biz.order.application.service;


import com.newzkl.platform.base.biz.order.model.req.RefundCommand;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;

/**
 * @author muc_fang
 * @Description:
 * @date 2023/12/811:16
 */
public interface RefundService {
    /**
     * 售后单创建
     * @param refundCommand
     * @return
     */
    Long refundCreateApi(RefundCommand refundCommand);

    /**
     * 售后单创建
     * @param refundCommand
     * @return
     */
    Long refundCreate(RefundCommand refundCommand);

    void supplierAudit(Long refundId, CommonEnum.YesOrNo execute, boolean isAudit);

    void channelAudit(Long refundId,Long spuOrderId, CommonEnum.YesOrNo execute, String reason,boolean isAudit);

    void supplierConfirmRefundFreight(Long refundId);
}
