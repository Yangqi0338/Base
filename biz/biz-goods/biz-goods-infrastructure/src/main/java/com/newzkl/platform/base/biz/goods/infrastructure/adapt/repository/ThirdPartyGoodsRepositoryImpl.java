package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;

import com.newzkl.platform.base.biz.goods.domain.adapt.repository.ThirdPartyGoodsRepository;
import com.newzkl.platform.base.biz.goods.facade.model.thirdparty.ThirdPartyGoodsRecordDTO;
import com.newzkl.platform.base.biz.goods.infrastructure.dao.ThirdPartyGoodsRecordDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.entity.ThirdPartyGoodsRecordDO;
import com.newzkl.platform.base.biz.goods.model.biz.req.query.ThirdPartyGoodsRecordQuery;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.ddd.model.enums.order.PlatformTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ThirdPartyGoodsRepositoryImpl implements ThirdPartyGoodsRepository {

    private final ThirdPartyGoodsRecordDAO recordDAO;

    @Override
    public ThirdPartyGoodsRecordDTO saveRecord(ThirdPartyGoodsRecordDTO request) {
        ThirdPartyGoodsRecordDO po = TransferUtils.transfer(request, ThirdPartyGoodsRecordDO.class);
        if (request.getId() == null) {
            recordDAO.insert(po);
            request.setId(po.getId());
        } else {
            recordDAO.updateById(po);
        }
        return request;
    }

    @Override
    public List<ThirdPartyGoodsRecordDTO> findByStatus(PlatformTypeEnum platformType, CommonEnum.RequestStatusEnum status, String interfaceName) {
        ThirdPartyGoodsRecordQuery query = ThirdPartyGoodsRecordQuery.builder()
                .platformType(platformType)
                .requestStatus(status)
                .interfaceName(interfaceName)
                .build();
        List<ThirdPartyGoodsRecordDO> list = recordDAO.selectList(recordDAO.getLw(query));
        return TransferUtils.transfers(list, ThirdPartyGoodsRecordDTO.class);
    }
}
