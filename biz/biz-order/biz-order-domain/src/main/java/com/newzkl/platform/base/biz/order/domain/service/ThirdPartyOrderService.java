package com.newzkl.platform.base.biz.order.domain.service;


import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;

public interface ThirdPartyOrderService {

    <T> ThirdPartyOrderRecordDTO createRecord(PlatformTypeEnum platformType, String bizOrderNo, String interfaceName, T requestObject, Object responseObject, CommonEnum.RequestStatusEnum requestStatus, String errorMessage);
}
