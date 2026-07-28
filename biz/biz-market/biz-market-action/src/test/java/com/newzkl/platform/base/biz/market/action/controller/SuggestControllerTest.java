package com.newzkl.platform.base.biz.market.action.controller;

import com.newzkl.platform.base.biz.market.domain.suggest.service.SuggestDomain;
import com.newzkl.platform.base.biz.market.model.suggest.req.TagConfigReq;
import com.newzkl.platform.base.biz.market.model.suggest.vo.CommitTagVO;
import com.newzkl.platform.base.biz.market.model.suggest.vo.TagConfigVO;
import com.newzkl.platform.base.common.ddd.model.res.PlatformResult;
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
 * {@link SuggestController} 单元测试 (领域服务 mock, 不起 Spring 容器)。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class SuggestControllerTest {

    @Mock
    private SuggestDomain suggestDomain;

    @InjectMocks
    private SuggestController suggestController;

    @Test
    @DisplayName("saveTagConfig 委派领域服务并返回成功")
    void writeEndpointsDelegate() {
        TagConfigReq tagConfigReq = new TagConfigReq();

        assertThat(suggestController.saveTagConfig(tagConfigReq).isSuccess()).isTrue();

        verify(suggestDomain).saveTagConfig(tagConfigReq);
    }

    @Test
    @DisplayName("queryTagConfig 走运营商视角 (operator=true)")
    void queryTagConfigUsesOperatorTrue() {
        TagConfigVO vo = new TagConfigVO();
        when(suggestDomain.queryTagConfig(true)).thenReturn(vo);

        PlatformResult<TagConfigVO> result = suggestController.queryTagConfig();

        assertThat(result.getData()).isSameAs(vo);
        verify(suggestDomain).queryTagConfig(true);
    }

    @Test
    @DisplayName("queryOperatorAllSuggest: 哨兵值 \"0\" 转 null, 其余原样透传")
    void queryOperatorAllSuggestNormalizesMobile() {
        List<CommitTagVO> list = List.of(new CommitTagVO());
        when(suggestDomain.queryOperatorAllSuggest(null)).thenReturn(list);
        when(suggestDomain.queryOperatorAllSuggest("13800000000")).thenReturn(List.of());

        assertThat(suggestController.queryOperatorAllSuggest("0").getData()).isSameAs(list);
        assertThat(suggestController.queryOperatorAllSuggest("13800000000").getData()).isEmpty();

        verify(suggestDomain).queryOperatorAllSuggest(null);
        verify(suggestDomain).queryOperatorAllSuggest("13800000000");
    }

    @Test
    @DisplayName("normalizeMobile: 哨兵值转 null, null 保持 null")
    void normalizeMobileSentinel() {
        assertThat(SuggestController.normalizeMobile("0")).isNull();
        assertThat(SuggestController.normalizeMobile(null)).isNull();
        assertThat(SuggestController.normalizeMobile("138")).isEqualTo("138");
    }
}
