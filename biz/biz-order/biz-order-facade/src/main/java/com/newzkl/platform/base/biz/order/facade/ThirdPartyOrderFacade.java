package com.newzkl.platform.base.biz.order.facade;




import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRequest;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;

import java.util.List;

public interface ThirdPartyOrderFacade {

    public <T> ThirdPartyOrderRequest createRequestRecord(PlatformTypeEnum platformType, String bizOrderNo, String interfaceName, T requestObject, Object responseObject, CommonEnum.RequestStatusEnum requestStatus, String errorMessage);

    List<ThirdPartyOrderRequest> findByStatusAndNextRetryTimeBefore(PlatformTypeEnum platformType, CommonEnum.RequestStatusEnum status, String interfaceName);

    ThirdPartyOrderRequest save(ThirdPartyOrderRequest request);

}