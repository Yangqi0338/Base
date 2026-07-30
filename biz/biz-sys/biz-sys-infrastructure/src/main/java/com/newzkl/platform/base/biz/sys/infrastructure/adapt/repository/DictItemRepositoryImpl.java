package com.newzkl.platform.base.biz.sys.infrastructure.adapt.repository;

import com.newzkl.platform.base.biz.sys.domain.adapt.repository.DictItemRepository;
import com.newzkl.platform.base.biz.sys.infrastructure.dao.DictItemDAO;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.DictItemDO;
import com.newzkl.platform.base.biz.sys.model.dictitem.req.DictItemReq;
import com.newzkl.platform.base.biz.sys.model.dictitem.res.DictItemRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典条目仓储实现
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class DictItemRepositoryImpl implements DictItemRepository {

    private final DictItemDAO dictItemDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long itemSave(DictItemReq req) {
        DictItemDO itemDO = TransferUtils.transfer(req, DictItemDO::new);
        dictItemDAO.insertOrUpdate(itemDO);
        return itemDO.getId();
    }

    @Override
    public List<DictItemRes> itemList(Long dictId) {
        return TransferUtils.transfers(dictItemDAO.selectList(dictItemDAO.getLwByDictId(dictId)), DictItemRes::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void itemDelete(List<Long> idList) {
        dictItemDAO.deleteByIds(idList);
    }
}
