package com.newzkl.platform.base.biz.order.domain.adapt.repository;




import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.biz.order.model.req.query.ThirdPartyOrderRecordQuery;

public interface ThirdPartyOrderRepository {
    ThirdPartyOrderRecordDTO saveRecord(ThirdPartyOrderRecordDTO request);
    ThirdPartyOrderRecordDTO findRecordByBizOrderNo(String bizOrderNo);
    Page<ThirdPartyOrderRecordDTO> selectPage(ThirdPartyOrderRecordQuery query);
}