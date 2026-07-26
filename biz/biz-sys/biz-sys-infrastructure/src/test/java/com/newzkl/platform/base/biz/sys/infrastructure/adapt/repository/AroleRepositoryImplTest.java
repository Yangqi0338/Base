package com.newzkl.platform.base.biz.sys.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.sys.infrastructure.dao.AroleDAO;
import com.newzkl.platform.base.biz.sys.infrastructure.entity.AroleDO;
import com.newzkl.platform.base.biz.sys.model.arole.query.AroleQuery;
import com.newzkl.platform.base.biz.sys.model.arole.req.AroleReq;
import com.newzkl.platform.base.biz.sys.model.arole.res.AroleRes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 后台角色仓储实现单元测试 (DAO mock, 不连库)。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class AroleRepositoryImplTest {

    @Mock
    private AroleDAO aroleDAO;

    @InjectMocks
    private AroleRepositoryImpl aroleRepository;

    @Test
    void aroleSave_transfersReqToDoAndReturnsId() {
        doAnswer(invocation -> {
            AroleDO arg = invocation.getArgument(0);
            arg.setId(999L);
            return true;
        }).when(aroleDAO).insertOrUpdate(any(AroleDO.class));

        AroleReq req = new AroleReq();
        req.setName("运营");
        req.setComment("运营角色");

        Long id = aroleRepository.aroleSave(req);

        assertThat(id).isEqualTo(999L);
    }

    @Test
    void aroleVO_mapsDoFieldsToRes() {
        AroleDO aroleDO = new AroleDO();
        aroleDO.setId(1L);
        aroleDO.setName("平台管理员");
        aroleDO.setComment("备注");
        when(aroleDAO.selectById(1L)).thenReturn(aroleDO);

        AroleRes res = aroleRepository.aroleVO(1L);

        assertThat(res).isNotNull();
        assertThat(res.getId()).isEqualTo(1L);
        assertThat(res.getName()).isEqualTo("平台管理员");
        assertThat(res.getComment()).isEqualTo("备注");
    }

    @Test
    void aroleList_returnsMappedRecordsFromPage() {
        AroleDO aroleDO = new AroleDO();
        aroleDO.setId(1L);
        aroleDO.setName("平台管理员");
        Page<AroleDO> page = new Page<>();
        page.setRecords(List.of(aroleDO));
        when(aroleDAO.selectPage(any(), any())).thenReturn(page);

        AroleQuery query = new AroleQuery();
        query.reset();
        List<AroleRes> list = aroleRepository.aroleList(query);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getName()).isEqualTo("平台管理员");
    }

    @Test
    void aroleDelete_delegatesToDao() {
        aroleRepository.aroleDelete(List.of(1L, 2L));
        verify(aroleDAO).deleteByIds(anyList());
    }
}
