package com.newzkl.platform.base.biz.content.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.domain.adapt.repository.VideoCategoryRepository;
import com.newzkl.platform.base.biz.content.domain.service.VideoCategoryDomain;
import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import com.newzkl.platform.base.biz.content.model.util.RecommendGroupsCheckUtil;
import com.newzkl.platform.base.biz.content.model.videocategory.entity.VideoCategory;
import com.newzkl.platform.base.biz.content.model.videocategory.query.VideoCategoryPageQuery;
import com.newzkl.platform.base.biz.content.model.videocategory.req.VideoCategoryReq;
import com.newzkl.platform.base.biz.content.model.videocategory.res.VideoCategoryRes;
import com.newzkl.platform.base.biz.content.model.videocategory.vo.VideoCategoryWeightVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 视频分类领域服务实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.service.impl.IVideoCategoryDomainImpl}。
 * 旧 {@code ScmException(-1, "分类不存在")} 映射为 {@code BaseErrorCode.NODATA}。</p>
 *
 * @author KC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoCategoryDomainImpl implements VideoCategoryDomain {

    private final VideoCategoryRepository videoCategoryRepository;

    @Override
    public Page<VideoCategoryRes> getCategoryPage(VideoCategoryPageQuery query) {
        return videoCategoryRepository.getCategoryPage(query);
    }

    @Override
    public void createCategory(VideoCategoryReq req) {
        // 校验推荐人群
        RecommendGroupsCheckUtil.validateRecommendGroups(req.getRecommendGroups());
        videoCategoryRepository.save(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(VideoCategoryReq req) {
        // 校验分类是否存在
        VideoCategory existingCategory = videoCategoryRepository.getById(req.getId());
        if (existingCategory == null) {
            ThrowsException.exception(BaseErrorCode.NODATA, "分类");
        }
        // 校验推荐人群
        if (req.getRecommendGroups() != null && !req.getRecommendGroups().trim().isEmpty()) {
            RecommendGroupsCheckUtil.validateRecommendGroups(req.getRecommendGroups());
        }
        videoCategoryRepository.update(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        videoCategoryRepository.delete(id);
    }

    @Override
    public void updateVideoCount(Long id, Integer num) {
        videoCategoryRepository.updateVideoCount(id, num);
    }

    @Override
    public VideoCategoryRes getById(Long id) {
        return videoCategoryRepository.getResById(id);
    }

    @Override
    public List<VideoCategoryRes> getCategoryList(List<RecommendGroupEnum> recommendGroups) {
        return videoCategoryRepository.getCategoryList(recommendGroups);
    }

    @Override
    public List<VideoCategoryWeightVO> getCategoryWeightList() {
        return videoCategoryRepository.getCategoryWeightList();
    }
}
