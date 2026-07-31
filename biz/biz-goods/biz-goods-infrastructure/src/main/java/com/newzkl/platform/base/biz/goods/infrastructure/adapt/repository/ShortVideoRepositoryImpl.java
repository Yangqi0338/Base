package com.newzkl.platform.base.biz.goods.infrastructure.adapt.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.goods.domain.video.adapt.repository.ShortVideoRepository;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.ShortVideoDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.dao.VideoSpuRelationDAO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.ShortVideoDO;
import com.newzkl.platform.base.biz.goods.infrastructure.goods.entity.VideoSpuRelationDO;
import com.newzkl.platform.base.biz.goods.model.goods.query.video.ShortVideoQuery;
import com.newzkl.platform.base.biz.goods.model.goods.req.video.ShortVideoReq;
import com.newzkl.platform.base.biz.goods.model.goods.vo.video.ShortVideoVO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品-短视频仓储实现
 *
 * <p>每方法单一持久化动作 (一次 IO); 跨表写/读组装的事务与编排上移 {@code ShortVideoDomain}</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class ShortVideoRepositoryImpl implements ShortVideoRepository {

    /**
     * 视频类型: 短视频
     */
    private static final Integer TYPE_SHORT = 1;

    private final ShortVideoDAO shortVideoDAO;

    private final VideoSpuRelationDAO videoSpuRelationDAO;

    @Override
    public Long insertShortVideo(ShortVideoReq req) {
        ShortVideoDO videoDO = TransferUtils.transfer(req, ShortVideoDO::new);
        shortVideoDAO.insert(videoDO);
        return videoDO.getId();
    }

    @Override
    public void updateShortVideo(ShortVideoReq req) {
        ShortVideoDO videoDO = TransferUtils.transfer(req, ShortVideoDO::new);
        shortVideoDAO.updateById(videoDO);
    }

    @Override
    public void deleteShortVideo(Long id) {
        shortVideoDAO.deleteById(id);
    }

    @Override
    public void saveRelation(Long videoId, List<Long> spuIdList) {
        if (videoId == null || CollUtil.isEmpty(spuIdList)) {
            return;
        }
        spuIdList.forEach(spuId -> {
            VideoSpuRelationDO relationDO = new VideoSpuRelationDO();
            relationDO.setVideoId(videoId);
            relationDO.setSpuId(spuId);
            relationDO.setType(TYPE_SHORT);
            videoSpuRelationDAO.insert(relationDO);
        });
    }

    @Override
    public void deleteRelationByVideo(Long videoId) {
        if (videoId == null) {
            return;
        }
        videoSpuRelationDAO.delete(videoSpuRelationDAO.getLwByVideo(List.of(videoId), TYPE_SHORT));
    }

    @Override
    public ShortVideoVO shortVideo(Long id) {
        ShortVideoDO videoDO = shortVideoDAO.selectById(id);
        if (videoDO == null) {
            return null;
        }
        return TransferUtils.transfer(videoDO, ShortVideoVO::new);
    }

    @Override
    public List<Long> relationSpuIdList(Long videoId) {
        List<VideoSpuRelationDO> relationList = videoSpuRelationDAO
                .selectList(videoSpuRelationDAO.getLwByVideo(List.of(videoId), TYPE_SHORT));
        return relationList.stream().map(VideoSpuRelationDO::getSpuId).toList();
    }

    @Override
    public Page<ShortVideoVO> page(ShortVideoQuery query) {
        return shortVideoDAO.queryPage(RepositorySupport.page(query), query);
    }
}
