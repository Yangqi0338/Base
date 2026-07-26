package com.newzkl.platform.base.biz.sys.domain.service.impl;

import com.newzkl.platform.base.biz.sys.domain.adapt.repository.DictItemRepository;
import com.newzkl.platform.base.biz.sys.domain.service.DictItemDomain;
import com.newzkl.platform.base.biz.sys.model.dictitem.req.DictItemReq;
import com.newzkl.platform.base.biz.sys.model.dictitem.res.DictItemRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典条目领域服务实现。
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class DictItemDomainImpl implements DictItemDomain {

    private final DictItemRepository dictItemRepository;

    @Override
    public Long itemSave(DictItemReq req) {
        return dictItemRepository.itemSave(req);
    }

    @Override
    public List<DictItemRes> itemList(Long dictId) {
        return dictItemRepository.itemList(dictId);
    }

    @Override
    public void itemDelete(List<Long> idList) {
        dictItemRepository.itemDelete(idList);
    }
}
