package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;

import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.ShortVideoDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.VideoSpuRelationDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.ShortVideoDO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.VideoSpuRelationDO;
import com.newzkl.platform.base.biz.goods.model.goods.req.video.ShortVideoReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.video.ShortVideoVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 短视频仓储实现单元测试 (DAO mock, 不连库)
 *
 * @author KC
 */
@ExtendWith(MockitoExtension.class)
class ShortVideoRepositoryImplTest {

    @Mock
    private ShortVideoDAO shortVideoDAO;

    @Mock
    private VideoSpuRelationDAO videoSpuRelationDAO;

    @InjectMocks
    private ShortVideoRepositoryImpl shortVideoRepository;

    @Test
    void insert_returnsGeneratedIdAndWritesRelations() {
        doAnswer(invocation -> {
            ShortVideoDO arg = invocation.getArgument(0);
            arg.setId(88L);
            return 1;
        }).when(shortVideoDAO).insert(any(ShortVideoDO.class));

        ShortVideoReq req = new ShortVideoReq();
        req.setPath("video/a.mp4");
        req.setCoverPath("img/a.jpg");
        req.setSpuIdList(List.of(11L, 12L));

        Long id = shortVideoRepository.insert(req);

        assertThat(id).isEqualTo(88L);
        ArgumentCaptor<VideoSpuRelationDO> captor = ArgumentCaptor.forClass(VideoSpuRelationDO.class);
        verify(videoSpuRelationDAO, times(2)).insert(captor.capture());
        assertThat(captor.getAllValues()).extracting(VideoSpuRelationDO::getSpuId)
                .containsExactlyInAnyOrder(11L, 12L);
        assertThat(captor.getAllValues()).allSatisfy(relation -> {
            assertThat(relation.getVideoId()).isEqualTo(88L);
            assertThat(relation.getType()).isEqualTo(1);
        });
    }

    @Test
    void edit_updatesVideoAndRebuildsRelations() {
        ShortVideoReq req = new ShortVideoReq();
        req.setId(88L);
        req.setPath("video/b.mp4");
        req.setSpuIdList(List.of(21L));

        shortVideoRepository.edit(req);

        verify(shortVideoDAO).updateById(any(ShortVideoDO.class));
        verify(videoSpuRelationDAO).delete(any());
        verify(videoSpuRelationDAO).insert(any(VideoSpuRelationDO.class));
    }

    @Test
    void del_removesVideoAndRelations() {
        shortVideoRepository.del(88L);

        verify(shortVideoDAO).deleteById(88L);
        verify(videoSpuRelationDAO).delete(any());
    }

    @Test
    void detail_mapsDoAndFillsSpuIdList() {
        ShortVideoDO videoDO = new ShortVideoDO();
        videoDO.setId(88L);
        videoDO.setPath("video/a.mp4");
        videoDO.setCoverPath("img/a.jpg");
        when(shortVideoDAO.selectById(88L)).thenReturn(videoDO);

        VideoSpuRelationDO relation = new VideoSpuRelationDO();
        relation.setVideoId(88L);
        relation.setSpuId(11L);
        relation.setType(1);
        when(videoSpuRelationDAO.selectList(any())).thenReturn(List.of(relation));

        ShortVideoVO vo = shortVideoRepository.detail(88L);

        assertThat(vo).isNotNull();
        assertThat(vo.getPath()).isEqualTo("video/a.mp4");
        assertThat(vo.getSpuIdList()).containsExactly(11L);
    }

    @Test
    void detail_returnsNullWhenAbsent() {
        when(shortVideoDAO.selectById(99L)).thenReturn(null);

        assertThat(shortVideoRepository.detail(99L)).isNull();
    }
}
