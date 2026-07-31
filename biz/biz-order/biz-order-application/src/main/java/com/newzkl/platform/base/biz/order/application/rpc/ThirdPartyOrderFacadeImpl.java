package com.newzkl.platform.base.biz.order.application.rpc;


import com.newzkl.platform.base.biz.order.domain.adapt.repository.ThirdPartyOrderRepository;
import com.newzkl.platform.base.biz.order.domain.service.ThirdPartyOrderService;
import com.newzkl.platform.base.biz.order.facade.ThirdPartyOrderFacade;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRequest;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@DubboService
@Component
public class ThirdPartyOrderFacadeImpl implements ThirdPartyOrderFacade {
    
    @Autowired
    private ThirdPartyOrderService thirdPartyOrderService;

    @Autowired
    private ThirdPartyOrderRepository orderRepository;
    
    @Override
    public <T> ThirdPartyOrderRequest createRequestRecord(PlatformTypeEnum platformType, String bizOrderNo,
                                                          String interfaceName, T requestObject, Object responseObject, CommonEnum.RequestStatusEnum requestStatus,
                                                          String errorMessage) {
        return thirdPartyOrderService.createRequestRecord(platformType, bizOrderNo, interfaceName, requestObject, responseObject, requestStatus, errorMessage);
    }

    @Override
    public List<ThirdPartyOrderRequest> findByStatusAndNextRetryTimeBefore(PlatformTypeEnum platformType,
                                                                           CommonEnum.RequestStatusEnum status, String interfaceName) {
        return orderRepository.findByStatusAndNextRetryTimeBefore(platformType, status, interfaceName);
    }

    @Override
    public ThirdPartyOrderRequest save(ThirdPartyOrderRequest request) {
        return orderRepository.save(request);
    }
}
