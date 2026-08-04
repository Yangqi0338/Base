package com.newzkl.platform.base.biz.order.application.rpc;


import com.newzkl.platform.base.biz.order.domain.service.IRefundDomain;
import com.newzkl.platform.base.biz.order.facade.IRefundOperationRecordFacade;
import com.newzkl.platform.base.biz.order.facade.model.order.RefundOperationRecordRPC;
import com.newzkl.platform.base.biz.order.model.dto.RefundOperationRecordDTO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sijiwang
 */
@DubboService
@Component
public class RefundOperationRecordFacadeImpl implements IRefundOperationRecordFacade {

    @Autowired
    private IRefundDomain refundDomain;

    @Override
    public Long save(RefundOperationRecordRPC refundOperationRecordRPC) {
        return refundDomain.createRecord(TransferUtils.transfer(refundOperationRecordRPC,RefundOperationRecordDTO.class));
    }
}
