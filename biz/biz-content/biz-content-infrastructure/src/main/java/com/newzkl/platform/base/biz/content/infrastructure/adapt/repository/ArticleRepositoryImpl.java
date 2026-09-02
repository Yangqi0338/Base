package com.newzkl.platform.base.biz.content.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.domain.adapt.repository.ArticleRepository;
import com.newzkl.platform.base.biz.content.infrastructure.dao.ContentArticleDAO;
import com.newzkl.platform.base.biz.content.infrastructure.entity.ArticleDO;
import com.newzkl.platform.base.biz.content.model.article.query.ArticlePageQuery;
import com.newzkl.platform.base.biz.content.model.article.query.ArticleQuery;
import com.newzkl.platform.base.biz.content.model.article.req.ArticleReq;
import com.newzkl.platform.base.biz.content.model.article.res.ArticleRes;
import com.newzkl.platform.base.biz.content.model.article.vo.ArticleVO;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.model.dto.AccountVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.repository.ArticleRepositoryImpl}。
 * 旧 {@code query.getPage()} 分页壳换为 {@code RepositorySupport.page(query)},
 * 出参壳换为 {@code ContentPage}。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ContentArticleDAO contentArticleDAO;

    @Override
    public void save(ArticleReq req) {
        ArticleDO articleDO = TransferUtils.transfer(req, ArticleDO.class);
        AccountVO accountVO = new AccountVO();
        accountVO.setId(req.getIssuerId());
        accountVO.setName(req.getIssuerName());
        articleDO.setIssuer(accountVO);
        contentArticleDAO.insert(articleDO);
    }

    @Override
    public void deleteById(Long id) {
        contentArticleDAO.deleteById(id);
    }

    @Override
    public void update(ArticleReq req) {
        ArticleDO articleDO = TransferUtils.transfer(req, ArticleDO::new);
        AccountVO accountVO = new AccountVO();
        accountVO.setId(req.getIssuerId());
        accountVO.setName(req.getIssuerName());
        articleDO.setIssuer(accountVO);
        contentArticleDAO.updateById(articleDO);
    }

    @Override
    public Page<ArticleRes> getArticlePage(ArticlePageQuery query) {
        Page<ArticleDO> page = contentArticleDAO.selectPage(RepositorySupport.page(query),
                new BaseLambdaQueryWrapper<ArticleDO>()
                        .notEmptyLike(ArticleDO::getTitle, query.getTitle())
                        .notEmptyEq(ArticleDO::getCategoryId, query.getCategoryId())
                        .between(query.getCreateTimeL() != null && query.getCreateTimeR() != null,
                                ArticleDO::getCreateTime, query.getCreateTimeL(), query.getCreateTimeR())
                        .orderByDesc(ArticleDO::getCreateTime));
        return TransferUtils.transferPage(page, ArticleRes.class);
    }

    @Override
    public List<ArticleVO> getArticleList(ArticleQuery query) {
        return TransferUtils.transfers(contentArticleDAO.selectList(new BaseLambdaQueryWrapper<ArticleDO>()
                .notEmptyLike(ArticleDO::getTitle, query.getTitle())
                .notEmptyEq(ArticleDO::getCategoryId, query.getCategoryId())
                .eq(ArticleDO::getIsVisible, 1)
                .orderByDesc(ArticleDO::getCreateTime)), ArticleVO::new);
    }

    @Override
    public ArticleRes getArticleById(Long id) {
        return TransferUtils.transfer(contentArticleDAO.selectById(id), ArticleRes::new);
    }

    @Override
    public long countVisibleArticles() {
        return contentArticleDAO.selectCount(new BaseLambdaQueryWrapper<ArticleDO>()
                .eq(ArticleDO::getIsVisible, 1));
    }

    @Override
    public List<ArticleVO> getRandomArticles(int limit) {
        return TransferUtils.transfers(contentArticleDAO.selectList(new BaseLambdaQueryWrapper<ArticleDO>()
                .select(ArticleDO::getId, ArticleDO::getTitle)
                .eq(ArticleDO::getIsVisible, 1)
                .last("ORDER BY RAND() LIMIT " + limit)), ArticleVO::new);
    }
}
