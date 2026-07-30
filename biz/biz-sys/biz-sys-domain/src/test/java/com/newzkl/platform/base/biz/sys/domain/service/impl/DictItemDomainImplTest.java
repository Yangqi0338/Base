package com.newzkl.platform.base.biz.sys.domain.service.impl;

import com.newzkl.platform.base.biz.sys.domain.adapt.repository.DictItemRepository;
import com.newzkl.platform.base.biz.sys.model.dictitem.req.DictItemReq;
import com.newzkl.platform.base.biz.sys.model.dictitem.res.DictItemRes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 字典条目领域服务单元测试 (仓储端口 mock)
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class DictItemDomainImplTest {

    @Mock
    private DictItemRepository dictItemRepository;

    @InjectMocks
    private DictItemDomainImpl dictItemDomain;

    @Test
    void itemSave_delegatesToRepository() {
        DictItemReq req = new DictItemReq();
        req.setDictId(10L);
        req.setItemKey("k");
        when(dictItemRepository.itemSave(any(DictItemReq.class))).thenReturn(5L);

        Long id = dictItemDomain.itemSave(req);

        assertThat(id).isEqualTo(5L);
        verify(dictItemRepository).itemSave(req);
    }

    @Test
    void itemList_returnsRepositoryListByDictId() {
        DictItemRes res = new DictItemRes();
        res.setId(1L);
        res.setDictId(10L);
        when(dictItemRepository.itemList(10L)).thenReturn(List.of(res));

        List<DictItemRes> list = dictItemDomain.itemList(10L);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getDictId()).isEqualTo(10L);
    }

    @Test
    void itemDelete_delegatesToRepository() {
        dictItemDomain.itemDelete(List.of(1L, 2L));
        verify(dictItemRepository).itemDelete(anyList());
    }
}
