package com.newzkl.platform.base.biz.goods.domain.report.service.impl;

import com.newzkl.platform.base.biz.goods.domain.report.repository.ReportRepository;
import com.newzkl.platform.base.biz.goods.model.assembler.ReportAssembler;
import com.newzkl.platform.base.biz.goods.model.goods.query.report.ReportQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.report.ReportReq;
import com.newzkl.platform.base.biz.goods.model.goods.res.report.ReportRes;
import com.newzkl.platform.base.biz.goods.model.goods.vo.report.ReportVO;
import com.newzkl.platform.base.biz.goods.model.goods.vo.spu.SpuSimpleVO;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 报告领域服务单元测试 (仓储端口 mock, 不连库)
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class ReportDomainImplTest {

    @Mock
    private ReportRepository repository;

    @Mock
    private ReportAssembler assembler;

    @InjectMocks
    private ReportDomainImpl reportDomain;

    @Test
    void detail_assemblesSpuListWhenSpuIdListNotEmpty() {
        ReportRes res = new ReportRes();
        res.setId(1L);
        res.setSpuIdList(List.of(7L, 8L));
        when(repository.report(1L)).thenReturn(res);
        when(repository.spuSimpleList(List.of(7L, 8L))).thenReturn(List.of(new SpuSimpleVO()));

        ReportRes actual = reportDomain.detail(1L);

        assertThat(actual).isSameAs(res);
        assertThat(actual.getSpuList()).hasSize(1);
        verify(repository).spuSimpleList(List.of(7L, 8L));
    }

    @Test
    void detail_skipsSpuListWhenSpuIdListEmpty() {
        ReportRes res = new ReportRes();
        res.setId(2L);
        when(repository.report(2L)).thenReturn(res);

        reportDomain.detail(2L);

        verify(repository, never()).spuSimpleList(any());
    }

    @Test
    void detail_throwsWhenNotFound() {
        when(repository.report(3L)).thenReturn(null);

        assertThatThrownBy(() -> reportDomain.detail(3L))
                .isInstanceOf(PlatformException.class);
    }

    @Test
    void add_delegatesToRepositoryWithAssembledVO() {
        ReportReq req = new ReportReq();
        ReportVO vo = new ReportVO();
        when(assembler.req2VO(req)).thenReturn(vo);
        when(repository.insert(vo)).thenReturn(66L);

        Long id = reportDomain.add(req);

        assertThat(id).isEqualTo(66L);
        verify(repository).insert(vo);
    }

    @Test
    void edit_throwsWhenTargetMissing() {
        ReportReq req = new ReportReq();
        req.setId(9L);
        when(repository.countByQuery(any(ReportQuery.class))).thenReturn(0);

        assertThatThrownBy(() -> reportDomain.edit(req))
                .isInstanceOf(PlatformException.class);
        verify(repository, never()).updateByQuery(any(), any());
    }

    @Test
    void edit_updatesWhenTargetExists() {
        ReportReq req = new ReportReq();
        req.setId(9L);
        ReportVO vo = new ReportVO();
        when(repository.countByQuery(any(ReportQuery.class))).thenReturn(1);
        when(assembler.req2VO(req)).thenReturn(vo);

        reportDomain.edit(req);

        verify(repository).updateByQuery(eq(vo), any(ReportQuery.class));
    }

    @Test
    void del_delegatesToRepository() {
        reportDomain.del(5L);
        verify(repository).del(5L);
    }

    @Test
    void queryPageList_delegatesToRepository() {
        ReportQuery query = new ReportQuery();
        reportDomain.queryPageList(query);
        verify(repository).queryPage(query);
    }
}
