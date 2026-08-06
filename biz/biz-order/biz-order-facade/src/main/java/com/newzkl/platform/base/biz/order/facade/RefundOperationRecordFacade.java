package com.newzkl.platform.base.biz.order.facade;


import com.newzkl.platform.base.biz.order.facade.model.order.RefundOperationRecordRPC;

/**
 * @author sijiwang
 */
public interface RefundOperationRecordFacade {

    Long save(RefundOperationRecordRPC refundOperationRecordRPC);
}
