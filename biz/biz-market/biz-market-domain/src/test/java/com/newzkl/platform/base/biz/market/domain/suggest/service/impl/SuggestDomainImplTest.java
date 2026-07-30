package com.newzkl.platform.base.biz.market.domain.suggest.service.impl;

import com.newzkl.platform.base.biz.market.domain.suggest.repository.SuggestRepository;
import com.newzkl.platform.base.biz.market.model.suggest.req.CommitTagReq;
import com.newzkl.platform.base.biz.market.model.suggest.req.TagConfigReq;
import com.newzkl.platform.base.biz.market.model.suggest.vo.CommitTagVO;
import com.newzkl.platform.base.biz.market.model.suggest.vo.TagConfigVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@code SuggestDomainImpl} 委派行为测试 (仓储端口 mock, 不连库)
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class SuggestDomainImplTest {

    @Mock
    private SuggestRepository suggestRepository;

    @InjectMocks
    private SuggestDomainImpl suggestDomain;

    @Test
    @DisplayName("saveTagConfig / commitTag 原样委派仓储")
    void writeMethodsDelegate() {
        TagConfigReq tagConfigReq = new TagConfigReq();
        CommitTagReq commitTagReq = new CommitTagReq();

        suggestDomain.saveTagConfig(tagConfigReq);
        suggestDomain.commitTag(commitTagReq);

        verify(suggestRepository).saveTagConfig(tagConfigReq);
        verify(suggestRepository).commitTag(commitTagReq);
    }

    @Test
    @DisplayName("queryTagConfig 透传 operator 视角标志")
    void queryTagConfigPassesOperatorFlag() {
        TagConfigVO vo = new TagConfigVO();
        when(suggestRepository.queryTagConfig(false)).thenReturn(vo);

        assertThat(suggestDomain.queryTagConfig(false)).isSameAs(vo);
        verify(suggestRepository).queryTagConfig(false);
    }

    @Test
    @DisplayName("queryCommitTag / queryOperatorAllSuggest 原样返回仓储结果")
    void readMethodsReturnRepositoryResult() {
        CommitTagVO commitTagVO = new CommitTagVO();
        List<CommitTagVO> list = List.of(commitTagVO);
        when(suggestRepository.queryCommitTag()).thenReturn(commitTagVO);
        when(suggestRepository.queryOperatorAllSuggest("138")).thenReturn(list);

        assertThat(suggestDomain.queryCommitTag()).isSameAs(commitTagVO);
        assertThat(suggestDomain.queryOperatorAllSuggest("138")).isSameAs(list);
    }
}
