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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * 短视频仓储实现单元测试 (DAO mock, 不连库)
 *
 * <p>每方法单一持久化动作, 跨表编排已上移 domain, 故本测试逐动作独立校验</p>
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
    void insertShortVideo_returnsGeneratedIdOnly() {
        doAnswer(invocation -> {
            ShortVideoDO arg = invocation.getArgument(0);
            arg.setId(88L);
            return 1;
        }).when(shortVideoDAO).insert(any(ShortVideoDO.class));

        ShortVideoReq req = new ShortVideoReq();
        req.setPath("video/a.mp4");
        req.setCoverPath("img/a.jpg");

        Long id = shortVideoRepository.insertShortVideo(req);

        assertThat(id).isEqualTo(88L);
        verifyNoInteractions(videoSpuRelationDAO);
    }

    @Test
    void saveRelation_writesOneRowPerSpu() {
        shortVideoRepository.saveRelation(88L, List.of(11L, 12L));

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
    void saveRelation_skipsWhenSpuIdListEmpty() {
        shortVideoRepository.saveRelation(88L, List.of());
        verifyNoInteractions(videoSpuRelationDAO);
    }

    @Test
    void updateShortVideo_updatesMainRecordOnly() {
        ShortVideoReq req = new ShortVideoReq();
        req.setId(88L);
        req.setPath("video/b.mp4");

        shortVideoRepository.updateShortVideo(req);

        verify(shortVideoDAO).updateById(any(ShortVideoDO.class));
        verifyNoInteractions(videoSpuRelationDAO);
    }

    @Test
    void deleteShortVideo_removesMainRecordOnly() {
        shortVideoRepository.deleteShortVideo(88L);

        verify(shortVideoDAO).deleteById(88L);
        verifyNoInteractions(videoSpuRelationDAO);
    }

    @Test
    void deleteRelationByVideo_removesRelations() {
        shortVideoRepository.deleteRelationByVideo(88L);
        verify(videoSpuRelationDAO).delete(any());
    }

    @Test
    void shortVideo_mapsMainRecordWithoutRelations() {
        ShortVideoDO videoDO = new ShortVideoDO();
        videoDO.setId(88L);
        videoDO.setPath("video/a.mp4");
        videoDO.setCoverPath("img/a.jpg");
        when(shortVideoDAO.selectById(88L)).thenReturn(videoDO);

        ShortVideoVO vo = shortVideoRepository.shortVideo(88L);

        assertThat(vo).isNotNull();
        assertThat(vo.getPath()).isEqualTo("video/a.mp4");
        assertThat(vo.getSpuIdList()).isNull();
        verifyNoInteractions(videoSpuRelationDAO);
    }

    @Test
    void shortVideo_returnsNullWhenAbsent() {
        when(shortVideoDAO.selectById(99L)).thenReturn(null);

        assertThat(shortVideoRepository.shortVideo(99L)).isNull();
    }

    @Test
    void relationSpuIdList_returnsSpuIds() {
        VideoSpuRelationDO relation = new VideoSpuRelationDO();
        relation.setVideoId(88L);
        relation.setSpuId(11L);
        relation.setType(1);
        when(videoSpuRelationDAO.selectList(any())).thenReturn(List.of(relation));

        List<Long> spuIdList = shortVideoRepository.relationSpuIdList(88L);

        assertThat(spuIdList).containsExactly(11L);
    }
}
