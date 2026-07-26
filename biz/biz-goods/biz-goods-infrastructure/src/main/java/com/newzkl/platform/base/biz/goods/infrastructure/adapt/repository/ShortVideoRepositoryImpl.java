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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品-短视频仓储实现。
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class ShortVideoRepositoryImpl implements ShortVideoRepository {

    /**
     * 视频类型: 短视频。
     */
    private static final Integer TYPE_SHORT = 1;

    private final ShortVideoDAO shortVideoDAO;

    private final VideoSpuRelationDAO videoSpuRelationDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(ShortVideoReq req) {
        ShortVideoDO videoDO = TransferUtils.transfer(req, ShortVideoDO::new);
        shortVideoDAO.insert(videoDO);
        saveRelation(videoDO.getId(), req.getSpuIdList(), false);
        return videoDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(ShortVideoReq req) {
        ShortVideoDO videoDO = TransferUtils.transfer(req, ShortVideoDO::new);
        shortVideoDAO.updateById(videoDO);
        saveRelation(req.getId(), req.getSpuIdList(), true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(Long id) {
        shortVideoDAO.deleteById(id);
        videoSpuRelationDAO.delete(videoSpuRelationDAO.getLwByVideo(List.of(id), TYPE_SHORT));
    }

    @Override
    public ShortVideoVO detail(Long id) {
        ShortVideoDO videoDO = shortVideoDAO.selectById(id);
        if (videoDO == null) {
            return null;
        }
        ShortVideoVO vo = TransferUtils.transfer(videoDO, ShortVideoVO::new);
        List<VideoSpuRelationDO> relationList = videoSpuRelationDAO
                .selectList(videoSpuRelationDAO.getLwByVideo(List.of(id), TYPE_SHORT));
        vo.setSpuIdList(relationList.stream().map(VideoSpuRelationDO::getSpuId).toList());
        vo.setSpuId(CollUtil.getFirst(vo.getSpuIdList()));
        return vo;
    }

    @Override
    public Page<ShortVideoVO> page(ShortVideoQuery query) {
        return shortVideoDAO.queryPage(RepositorySupport.page(query), query);
    }

    /**
     * 写入视频-SPU 关联关系。
     *
     * @param videoId   视频 ID
     * @param spuIdList 关联 SPU ID 列表
     * @param doDelete  是否先清理旧关系 (编辑场景)
     */
    private void saveRelation(Long videoId, List<Long> spuIdList, boolean doDelete) {
        if (videoId == null) {
            return;
        }
        if (doDelete) {
            videoSpuRelationDAO.delete(videoSpuRelationDAO.getLwByVideo(List.of(videoId), TYPE_SHORT));
        }
        if (CollUtil.isEmpty(spuIdList)) {
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
}
