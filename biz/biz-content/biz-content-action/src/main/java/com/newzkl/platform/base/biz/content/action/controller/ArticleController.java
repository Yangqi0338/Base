package com.newzkl.platform.base.biz.content.action.controller;

import com.newzkl.platform.base.biz.content.domain.service.ArticleDomain;
import com.newzkl.platform.base.biz.content.model.article.query.ArticlePageQuery;
import com.newzkl.platform.base.biz.content.model.article.query.ArticleQuery;
import com.newzkl.platform.base.biz.content.model.article.req.ArticleReq;
import com.newzkl.platform.base.biz.content.model.article.res.ArticleRes;
import com.newzkl.platform.base.biz.content.model.article.vo.ArticleVO;
import com.newzkl.platform.base.biz.content.model.common.res.ContentPage;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
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
 * 平台-文章
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.interfaces.controller.ArticleController},
 * 端点路径与 HTTP 方法逐字保留。{@code ScmResult} 换为 {@code PlatformResult},
 * {@code jakarta.validation} 换为 {@code jakarta.validation}。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleDomain articleDomain;

    /**
     * 创建文章
     *
     * @param req 文章入参
     * @return 创建结果
     */
    @PostMapping("/create")
    public PlatformResult<Void> createArticle(@Valid @RequestBody ArticleReq req) {
        articleDomain.createArticle(req);
        return PlatformResult.success();
    }

    /**
     * 更新文章
     *
     * @param req 文章入参(含主键ID)
     * @return 更新结果
     */
    @PostMapping("/update")
    public PlatformResult<Void> updateArticle(@RequestBody ArticleReq req) {
        articleDomain.updateArticle(req);
        return PlatformResult.success();
    }

    /**
     * 删除文章
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @GetMapping("/delete")
    public PlatformResult<Void> deleteArticle(@RequestParam Long id) {
        articleDomain.deleteArticle(id);
        return PlatformResult.success();
    }

    /**
     * 分页查询文章
     *
     * <p>TODO[fe-contract]: 出参壳由旧 mybatis-plus {@code Page} 换为 {@code ContentPage},
     * 字段名一致(records/total/size/current/pages)。</p>
     *
     * @param query 分页查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<ContentPage<ArticleRes>> getArticlePage(@RequestBody ArticlePageQuery query) {
        return PlatformResult.success(articleDomain.getArticlePage(query));
    }

    /**
     * 文章列表
     *
     * @param query 列表查询条件
     * @return 文章视图列表
     */
    @PostMapping("/list")
    public PlatformResult<List<ArticleVO>> getArticleList(@RequestBody ArticleQuery query) {
        return PlatformResult.success(articleDomain.getArticleList(query));
    }

    /**
     * 获取文章详情
     *
     * @param id 主键ID
     * @return 文章详情
     */
    @GetMapping("/getArticleById")
    public PlatformResult<ArticleRes> getArticleById(@RequestParam Long id) {
        return PlatformResult.success(articleDomain.getArticleById(id));
    }

    /**
     * 文章热榜列表
     *
     * @return 文章视图列表
     */
    @GetMapping("/hotList")
    public PlatformResult<List<ArticleVO>> getArticleHotList() {
        return PlatformResult.success(articleDomain.getArticleHotList());
    }
}
