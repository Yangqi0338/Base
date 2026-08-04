package com.newzkl.platform.base.biz.content.domain.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.newzkl.platform.base.biz.content.domain.adapt.repository.ArticleCategoryRepository;
import com.newzkl.platform.base.biz.content.domain.adapt.repository.ArticleRepository;
import com.newzkl.platform.base.biz.content.domain.service.ArticleDomain;
import com.newzkl.platform.base.biz.content.model.article.query.ArticlePageQuery;
import com.newzkl.platform.base.biz.content.model.article.query.ArticleQuery;
import com.newzkl.platform.base.biz.content.model.article.req.ArticleReq;
import com.newzkl.platform.base.biz.content.model.article.res.ArticleRes;
import com.newzkl.platform.base.biz.content.model.article.vo.ArticleVO;
import com.newzkl.platform.base.biz.content.model.articlecategory.entity.ArticleCategory;
import com.newzkl.platform.base.biz.content.model.common.res.ContentPage;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文章领域服务实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.service.impl.IArticleDomainImpl}, 去 I 前缀。
 * 旧 {@code ScmException(-1, "分类不存在")} 映射为 {@code BaseErrorCode.NODATA}。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class ArticleDomainImpl implements ArticleDomain {

    private final ArticleRepository articleRepository;

    private final ArticleCategoryRepository articleCategoryRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createArticle(ArticleReq req) {
        // 检查分类是否存在
        if (articleCategoryRepository.getById(req.getCategoryId()) == null) {
            ThrowsException.exception(BaseErrorCode.NODATA, "分类");
        }

        articleRepository.save(req);

        // 更新分类文章数量
        articleCategoryRepository.updateArticleCount(req.getCategoryId(), 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ArticleReq req) {
        ArticleRes oldArticle = articleRepository.getArticleById(req.getId());

        articleRepository.update(req);

        if (req.getCategoryId() != null && !ObjectUtil.equal(oldArticle.getCategoryId(), req.getCategoryId())) {
            // 更新分类文章数量
            articleCategoryRepository.updateArticleCount(oldArticle.getCategoryId(), -1);
            articleCategoryRepository.updateArticleCount(req.getCategoryId(), 1);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(Long id) {
        ArticleRes oldArticle = articleRepository.getArticleById(id);
        articleRepository.deleteById(id);
        // 更新分类文章数量
        articleCategoryRepository.updateArticleCount(oldArticle.getCategoryId(), -1);
    }

    @Override
    public ContentPage<ArticleRes> getArticlePage(ArticlePageQuery query) {
        ContentPage<ArticleRes> articlePage = articleRepository.getArticlePage(query);

        // 如果分页结果为空,直接返回
        if (articlePage.getRecords().isEmpty()) {
            return articlePage;
        }

        // 收集所有分类ID(去重)
        List<Long> categoryIds = articlePage.getRecords().stream()
                .map(ArticleRes::getCategoryId)
                .distinct()
                .collect(Collectors.toList());

        // 批量查询分类并构建映射
        Map<Long, ArticleCategory> categoryMap = articleCategoryRepository.getByIdList(categoryIds).stream()
                .collect(Collectors.toMap(
                        ArticleCategory::getId,
                        category -> category,
                        (existing, replacement) -> existing
                ));

        // 填充分类名称
        articlePage.getRecords().forEach(article -> {
            ArticleCategory category = categoryMap.get(article.getCategoryId());
            if (category != null) {
                article.setCategoryName(category.getName());
            }
        });

        return articlePage;
    }

    @Override
    public List<ArticleVO> getArticleList(ArticleQuery query) {
        return articleRepository.getArticleList(query);
    }

    @Override
    public ArticleRes getArticleById(Long id) {
        ArticleRes articleRes = articleRepository.getArticleById(id);
        ArticleCategory category = articleCategoryRepository.getById(articleRes.getCategoryId());
        if (category != null) {
            articleRes.setCategoryName(category.getName());
        }
        return articleRes;
    }

    @Override
    public List<ArticleVO> getArticleHotList() {
        // 查询可见文章总数
        long totalCount = articleRepository.countVisibleArticles();
        if (totalCount == 0) {
            return Collections.emptyList();
        }

        // 计算需要返回的数量: >=25 返回 25, 否则向下取 5 的倍数
        int limit;
        if (totalCount >= 25) {
            limit = 25;
        } else {
            limit = (int) (totalCount / 5) * 5;
        }

        if (limit <= 0) {
            return Collections.emptyList();
        }

        // 随机获取文章, 只返回 id 和标题
        return articleRepository.getRandomArticles(limit);
    }
}
