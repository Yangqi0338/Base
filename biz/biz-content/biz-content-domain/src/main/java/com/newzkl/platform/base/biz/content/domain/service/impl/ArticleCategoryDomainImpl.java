package com.newzkl.platform.base.biz.content.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.domain.adapt.repository.ArticleCategoryRepository;
import com.newzkl.platform.base.biz.content.domain.service.ArticleCategoryDomain;
import com.newzkl.platform.base.biz.content.model.articlecategory.entity.ArticleCategory;
import com.newzkl.platform.base.biz.content.model.articlecategory.query.ArticleCategoryPageQuery;
import com.newzkl.platform.base.biz.content.model.articlecategory.req.ArticleCategoryReq;
import com.newzkl.platform.base.biz.content.model.articlecategory.res.ArticleCategoryRes;
import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import com.newzkl.platform.base.biz.content.model.util.RecommendGroupsCheckUtil;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文章分类领域服务实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.service.impl.IArticleCategoryDomainImpl}。
 * 旧 {@code ScmException(-1, "分类不存在")} 映射为 {@code BaseErrorCode.NODATA}。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class ArticleCategoryDomainImpl implements ArticleCategoryDomain {

    private final ArticleCategoryRepository articleCategoryRepository;

    @Override
    public Page<ArticleCategoryRes> getCategoryPage(ArticleCategoryPageQuery query) {
        return articleCategoryRepository.getCategoryPage(query);
    }

    @Override
    public void createCategory(ArticleCategoryReq req) {
        // 校验推荐人群
        RecommendGroupsCheckUtil.validateRecommendGroups(req.getRecommendGroups());
        articleCategoryRepository.save(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(ArticleCategoryReq req) {
        // 校验分类是否存在
        ArticleCategory existingCategory = articleCategoryRepository.getById(req.getId());
        if (existingCategory == null) {
            ThrowsException.exception(BaseErrorCode.NODATA, "分类");
        }
        // 校验推荐人群
        if (req.getRecommendGroups() != null && !req.getRecommendGroups().trim().isEmpty()) {
            RecommendGroupsCheckUtil.validateRecommendGroups(req.getRecommendGroups());
        }
        articleCategoryRepository.update(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        articleCategoryRepository.delete(id);
    }

    @Override
    public void updateArticleCount(Long id, Integer num) {
        articleCategoryRepository.updateArticleCount(id, num);
    }

    @Override
    public ArticleCategoryRes getById(Long id) {
        return articleCategoryRepository.getResById(id);
    }

    @Override
    public List<ArticleCategoryRes> getCategoryList(List<RecommendGroupEnum> recommendGroups) {
        return articleCategoryRepository.getCategoryList(recommendGroups);
    }
}
