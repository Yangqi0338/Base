package com.newzkl.platform.base.biz.order.application.rpc;


import com.newzkl.platform.base.biz.order.domain.service.IRefundOperationRecordDomainService;
import com.newzkl.platform.base.biz.order.facade.IRefundOperationRecordFacade;
import com.newzkl.platform.base.biz.order.facade.model.order.RefundOperationRecordRPC;
import com.newzkl.platform.base.biz.order.model.dto.RefundOperationRecordEntity;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sijiwang
 */
@DubboService
@Component
public class RefundOperationRecordFacadeImpl implements IRefundOperationRecordFacade {

    @Autowired
    private IRefundOperationRecordDomainService refundOperationRecordDomainService;

    @Override
    public void save(RefundOperationRecordRPC refundOperationRecordRPC) {
        RefundOperationRecordEntity entity = new RefundOperationRecordEntity();
        BeanUtils.copyProperties(refundOperationRecordRPC, entity);
        refundOperationRecordDomainService.create(entity);
    }
}
