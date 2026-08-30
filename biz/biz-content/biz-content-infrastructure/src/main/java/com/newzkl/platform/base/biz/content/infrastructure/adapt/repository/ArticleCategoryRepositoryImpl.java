package com.newzkl.platform.base.biz.content.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.domain.adapt.repository.ArticleCategoryRepository;
import com.newzkl.platform.base.biz.content.infrastructure.dao.ContentArticleCategoryDAO;
import com.newzkl.platform.base.biz.content.infrastructure.entity.ArticleCategoryDO;
import com.newzkl.platform.base.biz.content.model.articlecategory.entity.ArticleCategory;
import com.newzkl.platform.base.biz.content.model.articlecategory.query.ArticleCategoryPageQuery;
import com.newzkl.platform.base.biz.content.model.articlecategory.req.ArticleCategoryReq;
import com.newzkl.platform.base.biz.content.model.articlecategory.res.ArticleCategoryRes;
import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章分类仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.repository.ArticleCategoryRepositoryImpl}</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class ArticleCategoryRepositoryImpl implements ArticleCategoryRepository {

    private final ContentArticleCategoryDAO contentArticleCategoryDAO;

    @Override
    public Page<ArticleCategoryRes> getCategoryPage(ArticleCategoryPageQuery query) {
        Page<ArticleCategoryDO> page = contentArticleCategoryDAO.selectPage(RepositorySupport.page(query),
                new BaseLambdaQueryWrapper<ArticleCategoryDO>()
                        .notEmptyLike(ArticleCategoryDO::getName, query.getName())
                        .orderByAsc(ArticleCategoryDO::getSort)
                        .orderByDesc(ArticleCategoryDO::getCreateTime));
        return TransferUtils.transferPage(page, ArticleCategoryRes.class);
    }

    @Override
    public ArticleCategory getById(Long id) {
        return TransferUtils.transfer(contentArticleCategoryDAO.selectById(id), ArticleCategory::new);
    }

    @Override
    public ArticleCategoryRes getResById(Long id) {
        return TransferUtils.transfer(contentArticleCategoryDAO.selectById(id), ArticleCategoryRes::new);
    }

    @Override
    public List<ArticleCategory> getByIdList(List<Long> idList) {
        return TransferUtils.transfers(contentArticleCategoryDAO.selectByIds(idList), ArticleCategory::new);
    }

    @Override
    public void save(ArticleCategoryReq req) {
        contentArticleCategoryDAO.insert(TransferUtils.transfer(req, ArticleCategoryDO::new));
    }

    @Override
    public void update(ArticleCategoryReq req) {
        contentArticleCategoryDAO.updateById(TransferUtils.transfer(req, ArticleCategoryDO::new));
    }

    @Override
    public void delete(Long id) {
        contentArticleCategoryDAO.deleteById(id);
    }

    @Override
    public void updateArticleCount(Long id, Integer num) {
        contentArticleCategoryDAO.update(new LambdaUpdateWrapper<ArticleCategoryDO>()
                .setSql(" article_count = article_count + (" + num + ")")
                .eq(ArticleCategoryDO::getId, id));
    }

    @Override
    public List<ArticleCategoryRes> getCategoryList(List<RecommendGroupEnum> recommendGroups) {
        LambdaQueryWrapper<ArticleCategoryDO> queryWrapper = new BaseLambdaQueryWrapper<ArticleCategoryDO>()
                .likeList(ArticleCategoryDO::getRecommendGroups, recommendGroups)
                .notEmptyEq(ArticleCategoryDO::getIsEnabled, CommonEnum.YesOrNo.YES)
                .orderByAsc(ArticleCategoryDO::getSort)
                .orderByDesc(ArticleCategoryDO::getId);
        return TransferUtils.transfers(contentArticleCategoryDAO.selectList(queryWrapper), ArticleCategoryRes::new);
    }
}
