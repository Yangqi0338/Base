package com.newzkl.platform.base.biz.goods.domain.service.impl;

import com.newzkl.platform.base.biz.goods.domain.adapt.repository.ThirdPartyGoodsRepository;
import com.newzkl.platform.base.biz.goods.domain.service.ThirdPartyGoodsDomain;
import com.newzkl.platform.base.biz.goods.facade.model.thirdparty.ThirdPartyGoodsRecordDTO;
import com.newzkl.platform.base.common.ddd.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 第三方商品同步领域服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ThirdPartyGoodsDomainImpl implements ThirdPartyGoodsDomain {

    private final ThirdPartyGoodsRepository goodsRepository;

    @Override
    @Transactional
    public void recordAction(PlatformTypeEnum platformType, String outSpuId, String interfaceName,
                             String requestJson, String responseJson, CommonEnum.RequestStatusEnum requestStatus, String errorMessage) {
        ThirdPartyGoodsRecordDTO record = ThirdPartyGoodsRecordDTO
                .init(platformType, outSpuId, interfaceName, requestJson, responseJson, requestStatus, errorMessage);
        goodsRepository.saveRecord(record);
    }
}
