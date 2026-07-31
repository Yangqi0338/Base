package com.newzkl.platform.base.biz.order.domain.adapt.repository;




import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRequest;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ThirdPartyOrderRepository {
    ThirdPartyOrderRequest save(ThirdPartyOrderRequest request);
    Optional<ThirdPartyOrderRequest> findByBizOrderNo(String bizOrderNo);
    List<ThirdPartyOrderRequest> findByStatusAndNextRetryTimeBefore(PlatformTypeEnum platformType, CommonEnum.RequestStatusEnum status, String interfaceName);
}