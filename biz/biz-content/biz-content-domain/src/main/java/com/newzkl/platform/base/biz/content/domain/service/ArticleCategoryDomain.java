package com.newzkl.platform.base.biz.content.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.model.articlecategory.query.ArticleCategoryPageQuery;
import com.newzkl.platform.base.biz.content.model.articlecategory.req.ArticleCategoryReq;
import com.newzkl.platform.base.biz.content.model.articlecategory.res.ArticleCategoryRes;
import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;

import java.util.List;

/**
 * 文章分类领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.service.IArticleCategoryDomain}</p>
 *
 * @author KC
 */
public interface ArticleCategoryDomain {

    /**
     * 分页查询文章分类
     *
     * <p>TODO[fe-contract]: 出参壳由旧 mybatis-plus {@code Page} 改为 {@code ContentPage},
     * 字段名一致, 前端仍建议回归。</p>
     *
     * @param query 分页查询条件
     * @return 分页结果
     */
    Page<ArticleCategoryRes> getCategoryPage(ArticleCategoryPageQuery query);

    /**
     * 创建文章分类
     *
     * @param req 文章分类入参
     */
    void createCategory(ArticleCategoryReq req);

    /**
     * 修改文章分类
     *
     * @param req 文章分类入参(含主键ID)
     */
    void updateCategory(ArticleCategoryReq req);

    /**
     * 删除文章分类
     *
     * @param id 主键ID
     */
    void deleteCategory(Long id);

    /**
     * 增量更新分类下的文章数量
     *
     * @param id  分类主键ID
     * @param num 增量值, 可为负数
     */
    void updateArticleCount(Long id, Integer num);

    /**
     * 获取文章分类详情
     *
     * @param id 主键ID
     * @return 文章分类详情, 不存在返回 null
     */
    ArticleCategoryRes getById(Long id);

    /**
     * 按推荐人群查询启用的文章分类列表
     *
     * @param recommendGroups 推荐人群名称集合
     * @return 文章分类列表
     */
    List<ArticleCategoryRes> getCategoryList(List<RecommendGroupEnum> recommendGroups);
}
