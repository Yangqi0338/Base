package com.newzkl.platform.base.biz.content.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.model.article.query.ArticlePageQuery;
import com.newzkl.platform.base.biz.content.model.article.query.ArticleQuery;
import com.newzkl.platform.base.biz.content.model.article.req.ArticleReq;
import com.newzkl.platform.base.biz.content.model.article.res.ArticleRes;
import com.newzkl.platform.base.biz.content.model.article.vo.ArticleVO;

import java.util.List;

/**
 * 文章领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.service.IArticleDomain}, 去 I 前缀。</p>
 *
 * @author KC
 */
public interface ArticleDomain {

    /**
     * 创建文章
     *
     * @param req 文章入参
     */
    void createArticle(ArticleReq req);

    /**
     * 更新文章
     *
     * @param req 文章入参(含主键ID)
     */
    void updateArticle(ArticleReq req);

    /**
     * 删除文章
     *
     * @param id 主键ID
     */
    void deleteArticle(Long id);

    /**
     * 分页查询文章
     *
     * <p>TODO[fe-contract]: 出参壳由旧 mybatis-plus {@code Page} 换为 {@code ContentPage},
     * 字段名一致(current/size/total/records)。</p>
     *
     * @param query 分页查询条件
     * @return 分页结果
     */
    Page<ArticleRes> getArticlePage(ArticlePageQuery query);

    /**
     * 查询文章列表
     *
     * @param query 列表查询条件
     * @return 文章视图列表
     */
    List<ArticleVO> getArticleList(ArticleQuery query);

    /**
     * 按主键获取文章详情
     *
     * @param id 主键ID
     * @return 文章详情
     */
    ArticleRes getArticleById(Long id);

    /**
     * 文章热榜列表
     *
     * @return 文章视图列表
     */
    List<ArticleVO> getArticleHotList();
}
