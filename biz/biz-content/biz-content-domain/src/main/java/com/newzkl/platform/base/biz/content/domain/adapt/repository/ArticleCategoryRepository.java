package com.newzkl.platform.base.biz.content.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.model.articlecategory.entity.ArticleCategory;
import com.newzkl.platform.base.biz.content.model.articlecategory.query.ArticleCategoryPageQuery;
import com.newzkl.platform.base.biz.content.model.articlecategory.req.ArticleCategoryReq;
import com.newzkl.platform.base.biz.content.model.articlecategory.res.ArticleCategoryRes;
import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;

import java.util.List;

/**
 * 文章分类仓储接口
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.repository.IArticleCategoryRepository}</p>
 *
 * @author KC
 */
public interface ArticleCategoryRepository {

    /**
     * 分页查询文章分类
     *
     * @param query 分页查询条件
     * @return 分页结果
     */
    Page<ArticleCategoryRes> getCategoryPage(ArticleCategoryPageQuery query);

    /**
     * 按主键查询文章分类
     *
     * @param id 主键ID
     * @return 文章分类, 不存在返回 null
     */
    ArticleCategory getById(Long id);

    /**
     * 按主键查询文章分类出参
     *
     * @param id 主键ID
     * @return 文章分类出参, 不存在返回 null
     */
    ArticleCategoryRes getResById(Long id);

    /**
     * 按主键集合批量查询文章分类
     *
     * @param idList 主键ID集合
     * @return 文章分类集合
     */
    List<ArticleCategory> getByIdList(List<Long> idList);

    /**
     * 新增文章分类
     *
     * @param req 文章分类入参
     */
    void save(ArticleCategoryReq req);

    /**
     * 按主键修改文章分类
     *
     * @param req 文章分类入参(含主键ID)
     */
    void update(ArticleCategoryReq req);

    /**
     * 按主键删除文章分类
     *
     * @param id 主键ID
     */
    void delete(Long id);

    /**
     * 增量更新分类下的文章数量
     *
     * @param id  分类主键ID
     * @param num 增量值, 可为负数
     */
    void updateArticleCount(Long id, Integer num);

    /**
     * 按推荐人群查询启用的文章分类列表
     *
     * @param recommendGroups 推荐人群名称集合, 任一命中即返回
     * @return 文章分类出参集合
     */
    List<ArticleCategoryRes> getCategoryList(List<RecommendGroupEnum> recommendGroups);
}
