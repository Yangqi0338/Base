package com.newzkl.platform.base.biz.order.infrastructure.adapt.repository.thirdparty;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.order.domain.adapt.repository.ThirdPartyOrderRepository;
import com.newzkl.platform.base.biz.order.facade.model.order.ThirdPartyOrderRecordDTO;
import com.newzkl.platform.base.biz.order.infrastructure.dao.ThirdPartyOrderRecordDAO;
import com.newzkl.platform.base.biz.order.infrastructure.entity.ThirdPartyOrderRecordDO;
import com.newzkl.platform.base.biz.order.model.req.query.ThirdPartyOrderRecordQuery;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ThirdPartyOrderRepositoryImpl implements ThirdPartyOrderRepository {

    private final ThirdPartyOrderRecordDAO recordDAO;

    @Override
    public ThirdPartyOrderRecordDTO saveRecord(ThirdPartyOrderRecordDTO request) {
        ThirdPartyOrderRecordDO po = TransferUtils.transfer(request, ThirdPartyOrderRecordDO.class);
        if (request.getId() == null) {
            recordDAO.insert(po);
            request.setId(po.getId());
        } else {
            recordDAO.updateById(po);
        }
        return request;
    }

    @Override
    public ThirdPartyOrderRecordDTO findRecordByBizOrderNo(String bizOrderNo) {
        ThirdPartyOrderRecordQuery query = ThirdPartyOrderRecordQuery.builder()
                .bizOrderNo(bizOrderNo)
                .build();
        ThirdPartyOrderRecordDO recordDO = recordDAO.selectOne(recordDAO.getLw(query));
        return TransferUtils.transfer(recordDO, ThirdPartyOrderRecordDTO.class);
    }

    @Override
    public Page<ThirdPartyOrderRecordDTO> selectPage(ThirdPartyOrderRecordQuery query) {
        Page<ThirdPartyOrderRecordDO> list = recordDAO.selectPage(RepositorySupport.page(query), recordDAO.getLw(query));
        return TransferUtils.transferPage(list, ThirdPartyOrderRecordDTO.class);
    }
}