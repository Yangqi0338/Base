package com.newzkl.platform.base.biz.content.action.controller;

import com.newzkl.platform.base.biz.content.domain.service.ArticleCategoryDomain;
import com.newzkl.platform.base.biz.content.model.articlecategory.query.ArticleCategoryPageQuery;
import com.newzkl.platform.base.biz.content.model.articlecategory.req.ArticleCategoryReq;
import com.newzkl.platform.base.biz.content.model.articlecategory.res.ArticleCategoryRes;
import com.newzkl.platform.base.biz.content.model.common.req.RecommendGroupReq;
import com.newzkl.platform.base.biz.content.model.common.res.ContentPage;
import com.newzkl.platform.base.biz.content.model.util.RecommendGroupsCheckUtil;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 平台-文章分类
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.interfaces.controller.ArticleCategoryController},
 * 端点路径与 HTTP 方法逐字保留。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/article/category")
@RequiredArgsConstructor
@FuncPermission("平台-文章分类")
public class ArticleCategoryController {

    private final ArticleCategoryDomain articleCategoryDomain;

    /**
     * 分页查询文章分类列表
     *
     * <p>TODO[fe-contract]: 出参壳由旧 {@code Page} 改为 {@code ContentPage}, 字段名一致。</p>
     *
     * @param query 分页查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<ContentPage<ArticleCategoryRes>> getCategoryPage(@RequestBody ArticleCategoryPageQuery query) {
        return PlatformResult.success(articleCategoryDomain.getCategoryPage(query));
    }

    /**
     * 获取分类详情
     *
     * @param id 主键ID
     * @return 分类详情
     */
    @GetMapping("/getCategoryDetail")
    public PlatformResult<ArticleCategoryRes> getCategoryDetail(@RequestParam Long id) {
        return PlatformResult.success(articleCategoryDomain.getById(id));
    }

    /**
     * 创建文章分类
     *
     * @param req 文章分类入参
     * @return 创建结果
     */
    @PostMapping("/create")
    @FuncPermission("创建文章分类")
    public PlatformResult<Void> createCategory(@Valid @RequestBody ArticleCategoryReq req) {
        articleCategoryDomain.createCategory(req);
        return PlatformResult.success();
    }

    /**
     * 修改文章分类
     *
     * @param req 文章分类入参(含主键ID)
     * @return 修改结果
     */
    @PostMapping("/update")
    @FuncPermission("修改文章分类")
    public PlatformResult<Void> updateCategory(@Valid @RequestBody ArticleCategoryReq req) {
        articleCategoryDomain.updateCategory(req);
        return PlatformResult.success();
    }

    /**
     * 删除文章分类
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @GetMapping("/delete")
    @FuncPermission("删除文章分类")
    public PlatformResult<Void> deleteCategory(@RequestParam Long id) {
        articleCategoryDomain.deleteCategory(id);
        return PlatformResult.success();
    }

    /**
     * 获取分类列表
     *
     * <p>推荐人群由当前登录角色换算, 旧 {@code SecurityUtils.getIdentity()} 换为
     * {@code SecurityUtils.getRoleId()}(两者同为角色ID语义)。入参 {@code req} 旧实现同样未使用,
     * 为保持前端契约(POST 带 body)原样保留。</p>
     *
     * @param req 推荐人群入参(旧实现未使用)
     * @return 分类列表
     */
    @PostMapping("/list")
    public PlatformResult<List<ArticleCategoryRes>> getCategoryList(@RequestBody RecommendGroupReq req) {
        return PlatformResult.success(articleCategoryDomain.getCategoryList(
                RecommendGroupsCheckUtil.getRecommendGroups(SecurityUtils.getIdentity())));
    }
}
