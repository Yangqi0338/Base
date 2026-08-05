package com.newzkl.platform.base.biz.goods.domain.adapt.repository;

import com.newzkl.platform.base.biz.goods.facade.model.thirdparty.ThirdPartyGoodsRecordDTO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;

import java.util.List;

public interface ThirdPartyGoodsRepository {

    ThirdPartyGoodsRecordDTO saveRecord(ThirdPartyGoodsRecordDTO request);

    List<ThirdPartyGoodsRecordDTO> findByStatus(PlatformTypeEnum platformType, CommonEnum.RequestStatusEnum status, String interfaceName);
}
