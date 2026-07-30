package com.newzkl.platform.base.biz.sys.infrastructure.adapt.repository;

import com.newzkl.platform.base.biz.sys.infrastructure.dao.DictItemDAO;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.DictItemDO;
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
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * 字典条目仓储实现单元测试 (DAO mock, 不连库)
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class DictItemRepositoryImplTest {

    @Mock
    private DictItemDAO dictItemDAO;

    @InjectMocks
    private DictItemRepositoryImpl dictItemRepository;

    @Test
    void itemSave_transfersReqAndReturnsId() {
        doAnswer(invocation -> {
            DictItemDO arg = invocation.getArgument(0);
            arg.setId(77L);
            return true;
        }).when(dictItemDAO).insertOrUpdate(any(DictItemDO.class));

        DictItemReq req = new DictItemReq();
        req.setDictId(10L);
        req.setItemKey("color");
        req.setItemValue("red");

        Long id = dictItemRepository.itemSave(req);

        assertThat(id).isEqualTo(77L);
    }

    @Test
    void itemList_mapsDoListToResByDictId() {
        DictItemDO itemDO = new DictItemDO();
        itemDO.setId(1L);
        itemDO.setDictId(10L);
        itemDO.setItemKey("color");
        itemDO.setItemValue("red");
        when(dictItemDAO.selectList(any())).thenReturn(List.of(itemDO));

        List<DictItemRes> list = dictItemRepository.itemList(10L);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getItemKey()).isEqualTo("color");
        assertThat(list.get(0).getDictId()).isEqualTo(10L);
    }
}
