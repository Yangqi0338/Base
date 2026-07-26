package com.newzkl.platform.base.biz.goods.domain.video.service.impl;

import com.newzkl.platform.base.biz.goods.domain.video.adapt.repository.ShortVideoRepository;
import com.newzkl.platform.base.biz.goods.model.goods.query.video.ShortVideoQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.video.ShortVideoReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.video.ShortVideoVO;
import com.newzkl.platform.base.common.core.model.exception.ScmException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 短视频领域服务单元测试 (仓储端口 mock, 不连库)。
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class ShortVideoDomainImplTest {

    @Mock
    private ShortVideoRepository shortVideoRepository;

    @InjectMocks
    private ShortVideoDomainImpl shortVideoDomain;

    @Test
    void add_mergesSpuIdIntoListAndReturnsId() {
        ShortVideoReq req = new ShortVideoReq();
        req.setPath("video/a.mp4");
        req.setSpuId(9L);
        when(shortVideoRepository.insert(any(ShortVideoReq.class))).thenReturn(66L);

        Long id = shortVideoDomain.add(req);

        assertThat(id).isEqualTo(66L);
        ArgumentCaptor<ShortVideoReq> captor = ArgumentCaptor.forClass(ShortVideoReq.class);
        verify(shortVideoRepository).insert(captor.capture());
        assertThat(captor.getValue().getSpuIdList()).containsExactly(9L);
    }

    @Test
    void add_throwsWhenNoSpuRelated() {
        ShortVideoReq req = new ShortVideoReq();
        req.setPath("video/a.mp4");

        assertThatThrownBy(() -> shortVideoDomain.add(req))
                .isInstanceOf(ScmException.class)
                .hasMessageContaining("关联的SPU不能为空");
    }

    @Test
    void edit_throwsWhenNoSpuRelated() {
        ShortVideoReq req = new ShortVideoReq();
        req.setId(1L);
        req.setPath("video/a.mp4");

        assertThatThrownBy(() -> shortVideoDomain.edit(req))
                .isInstanceOf(ScmException.class)
                .hasMessageContaining("关联的SPU不能为空");
    }

    @Test
    void edit_delegatesToRepositoryWithMergedSpuIdList() {
        ShortVideoReq req = new ShortVideoReq();
        req.setId(1L);
        req.setPath("video/a.mp4");
        req.setSpuIdList(List.of(3L));
        req.setSpuId(4L);

        shortVideoDomain.edit(req);

        ArgumentCaptor<ShortVideoReq> captor = ArgumentCaptor.forClass(ShortVideoReq.class);
        verify(shortVideoRepository).edit(captor.capture());
        assertThat(captor.getValue().getSpuIdList()).containsExactlyInAnyOrder(3L, 4L);
    }

    @Test
    void detail_returnsRepositoryResult() {
        ShortVideoVO vo = new ShortVideoVO();
        vo.setId(1L);
        vo.setPath("video/a.mp4");
        when(shortVideoRepository.detail(1L)).thenReturn(vo);

        ShortVideoVO actual = shortVideoDomain.detail(1L);

        assertThat(actual).isNotNull();
        assertThat(actual.getPath()).isEqualTo("video/a.mp4");
    }

    @Test
    void detail_throwsWhenNotFound() {
        when(shortVideoRepository.detail(2L)).thenReturn(null);

        assertThatThrownBy(() -> shortVideoDomain.detail(2L))
                .isInstanceOf(ScmException.class);
    }

    @Test
    void del_delegatesToRepository() {
        shortVideoDomain.del(5L);
        verify(shortVideoRepository).del(5L);
    }

    @Test
    void page_delegatesToRepository() {
        ShortVideoQuery query = new ShortVideoQuery();
        shortVideoDomain.page(query);
        verify(shortVideoRepository).page(query);
    }
}
