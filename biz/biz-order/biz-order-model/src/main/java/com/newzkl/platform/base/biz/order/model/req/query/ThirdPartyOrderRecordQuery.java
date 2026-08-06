package com.newzkl.platform.base.biz.order.model.req.query;

import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ThirdPartyOrderRecordQuery {

    private String bizOrderNo;

    private String interfaceName;

    private PlatformTypeEnum platformType;

    private CommonEnum.RequestStatusEnum requestStatus;

    /**
     * 下次重试时间上限(lt), 用于扫描超时未重试的记录
     */
    private LocalDateTime nextRetryTimeBefore;
}