package com.newzkl.platform.base.biz.sys.domain.service.impl;

import com.newzkl.platform.base.biz.sys.domain.adapt.repository.AroleRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 后台角色领域服务单元测试 (仓储端口 mock)。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class AroleDomainImplTest {

    @Mock
    private AroleRepository aroleRepository;

    @InjectMocks
    private AroleDomainImpl aroleDomain;

    @Test
    void aroleSave_delegatesToRepository() {
        AroleReq req = new AroleReq();
        req.setName("平台管理员");
        when(aroleRepository.aroleSave(any(AroleReq.class))).thenReturn(100L);

        Long id = aroleDomain.aroleSave(req);

        assertThat(id).isEqualTo(100L);
        verify(aroleRepository).aroleSave(req);
    }

    @Test
    void aroleVO_returnsRepositoryResult() {
        AroleRes res = new AroleRes();
        res.setId(1L);
        res.setName("平台管理员");
        when(aroleRepository.aroleVO(1L)).thenReturn(res);

        AroleRes actual = aroleDomain.aroleVO(1L);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo("平台管理员");
    }

    @Test
    void aroleList_returnsRepositoryList() {
        AroleRes res = new AroleRes();
        res.setId(1L);
        when(aroleRepository.aroleList(any(AroleQuery.class))).thenReturn(List.of(res));

        List<AroleRes> list = aroleDomain.aroleList(new AroleQuery());

        assertThat(list).hasSize(1);
    }

    @Test
    void aroleDelete_delegatesToRepository() {
        aroleDomain.aroleDelete(List.of(1L, 2L));
        verify(aroleRepository).aroleDelete(anyList());
    }
}
