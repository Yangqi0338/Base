package com.newzkl.platform.base.biz.order.domain.adapt.repository;




import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.biz.order.model.req.query.ThirdPartyOrderRecordQuery;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;

import java.util.List;

public interface ThirdPartyOrderRepository {
    ThirdPartyOrderRecordDTO saveRecord(ThirdPartyOrderRecordDTO request);
    ThirdPartyOrderRecordDTO findRecordByBizOrderNo(String bizOrderNo);
    Page<ThirdPartyOrderRecordDTO> selectPage(ThirdPartyOrderRecordQuery query);
}