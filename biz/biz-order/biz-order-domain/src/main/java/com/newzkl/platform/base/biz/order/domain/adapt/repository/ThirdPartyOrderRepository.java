package com.newzkl.platform.base.biz.order.domain.adapt.repository;




import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;

import java.util.List;

public interface ThirdPartyOrderRepository {
    ThirdPartyOrderRecordDTO saveRecord(ThirdPartyOrderRecordDTO request);
    ThirdPartyOrderRecordDTO findRecordByBizOrderNo(String bizOrderNo);
    List<ThirdPartyOrderRecordDTO> findByStatusAndNextRetryTimeBefore(PlatformTypeEnum platformType, CommonEnum.RequestStatusEnum status, String interfaceName);
}