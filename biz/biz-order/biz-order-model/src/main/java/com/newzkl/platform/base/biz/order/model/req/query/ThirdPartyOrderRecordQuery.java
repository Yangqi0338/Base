package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ThirdPartyOrderRecordQuery {

    private String bizOrderNo;

    private String interfaceName;

    private PlatformTypeEnum platformType;

    private CommonEnum.RequestStatusEnum requestStatus;
}