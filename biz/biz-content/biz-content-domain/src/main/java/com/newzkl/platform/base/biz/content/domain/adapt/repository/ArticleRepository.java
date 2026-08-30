package com.newzkl.platform.base.biz.content.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.model.article.query.ArticlePageQuery;
import com.newzkl.platform.base.biz.content.model.article.query.ArticleQuery;
import com.newzkl.platform.base.biz.content.model.article.req.ArticleReq;
import com.newzkl.platform.base.biz.content.model.article.res.ArticleRes;
import com.newzkl.platform.base.biz.content.model.article.vo.ArticleVO;

import java.util.List;

/**
 * 文章仓储接口
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.repository.IArticleRepository},
 * 去 I 前缀。贴 {@code ArticleCategoryRepository} 范本, 仓储层直接返回出参/视图类型,
 * domain 层不再依赖 {@code TransferUtils}。分页壳由旧 {@code Page} 换为 {@code ContentPage}。</p>
 *
 * @author KC
 */
public interface ArticleRepository {

    /**
     * 保存文章
     *
     * @param req 文章入参
     */
    void save(ArticleReq req);

    /**
     * 按主键删除文章
     *
     * @param id 主键ID
     */
    void deleteById(Long id);

    /**
     * 按主键更新文章
     *
     * @param req 文章入参(含主键ID)
     */
    void update(ArticleReq req);

    /**
     * 分页查询文章
     *
     * @param query 分页查询条件
     * @return 分页结果(未填分类名)
     */
    Page<ArticleRes> getArticlePage(ArticlePageQuery query);

    /**
     * 查询可见文章列表
     *
     * @param query 列表查询条件
     * @return 文章视图列表
     */
    List<ArticleVO> getArticleList(ArticleQuery query);

    /**
     * 按主键查询文章出参
     *
     * @param id 主键ID
     * @return 文章出参, 不存在返回 null
     */
    ArticleRes getArticleById(Long id);

    /**
     * 统计可见文章总数
     *
     * @return 可见文章数量
     */
    long countVisibleArticles();

    /**
     * 随机获取指定数量的可见文章(仅 id 与标题)
     *
     * @param limit 数量上限
     * @return 文章视图列表
     */
    List<ArticleVO> getRandomArticles(int limit);
}
