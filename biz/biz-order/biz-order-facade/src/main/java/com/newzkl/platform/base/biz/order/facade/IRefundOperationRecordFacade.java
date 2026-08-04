package com.newzkl.platform.base.biz.order.facade;


import com.newzkl.platform.base.biz.order.facade.model.order.RefundOperationRecordRPC;

/**
 * @author sijiwang
 */
public interface IRefundOperationRecordFacade {

    Long save(RefundOperationRecordRPC refundOperationRecordRPC);
}
