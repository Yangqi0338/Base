package com.newzkl.platform.base.biz.content.action.controller;

import com.newzkl.platform.base.biz.content.domain.service.VideoCategoryDomain;
import com.newzkl.platform.base.biz.content.model.common.res.ContentPage;
import com.newzkl.platform.base.biz.content.model.util.RecommendGroupsCheckUtil;
import com.newzkl.platform.base.biz.content.model.videocategory.query.VideoCategoryPageQuery;
import com.newzkl.platform.base.biz.content.model.videocategory.req.VideoCategoryReq;
import com.newzkl.platform.base.biz.content.model.videocategory.res.VideoCategoryRes;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 平台-视频分类
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.interfaces.controller.VideoCategoryController},
 * 端点路径与 HTTP 方法逐字保留。</p>
 *
 * @author KC
 */
@Slf4j
@RestController
@RequestMapping("/admin/video/category")
@RequiredArgsConstructor
public class VideoCategoryController {

    private final VideoCategoryDomain videoCategoryDomain;

    /**
     * 分页查询视频分类列表
     *
     * <p>TODO[fe-contract]: 出参壳由旧 {@code Page} 改为 {@code ContentPage}, 字段名一致。</p>
     *
     * @param query 分页查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<ContentPage<VideoCategoryRes>> getCategoryPage(@RequestBody VideoCategoryPageQuery query) {
        return PlatformResult.success(videoCategoryDomain.getCategoryPage(query));
    }

    /**
     * 获取分类详情
     *
     * @param id 主键ID
     * @return 分类详情
     */
    @GetMapping("/getCategoryDetail")
    public PlatformResult<VideoCategoryRes> getCategoryDetail(@RequestParam Long id) {
        return PlatformResult.success(videoCategoryDomain.getById(id));
    }

    /**
     * 创建视频分类
     *
     * @param req 视频分类入参
     * @return 创建结果
     */
    @PostMapping("/create")
    public PlatformResult<Void> createCategory(@Valid @RequestBody VideoCategoryReq req) {
        videoCategoryDomain.createCategory(req);
        return PlatformResult.success();
    }

    /**
     * 修改视频分类
     *
     * @param req 视频分类入参(含主键ID)
     * @return 修改结果
     */
    @PostMapping("/update")
    public PlatformResult<Void> updateCategory(@Valid @RequestBody VideoCategoryReq req) {
        videoCategoryDomain.updateCategory(req);
        return PlatformResult.success();
    }

    /**
     * 批量修改视频分类
     *
     * @param reqList 视频分类入参集合
     * @return 修改结果
     */
    @PostMapping("/batchUpdate")
    public PlatformResult<Void> batchUpdateCategory(@Valid @RequestBody List<VideoCategoryReq> reqList) {
        reqList.forEach(videoCategoryDomain::updateCategory);
        return PlatformResult.success();
    }

    /**
     * 删除视频分类
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @GetMapping("/delete")
    public PlatformResult<Void> deleteCategory(@RequestParam Long id) {
        videoCategoryDomain.deleteCategory(id);
        return PlatformResult.success();
    }

    /**
     * 获取分类列表
     *
     * <p>推荐人群由当前登录角色换算, 旧 {@code SecurityUtils.getRole()} 换为
     * {@code SecurityUtils.getRoleId()}(两者同为角色ID语义)。</p>
     *
     * @return 分类列表
     */
    @GetMapping("/list")
    public PlatformResult<List<VideoCategoryRes>> getCategoryList() {
        return PlatformResult.success(videoCategoryDomain.getCategoryList(
                RecommendGroupsCheckUtil.getRecommendGroups(SecurityUtils.getRole())));
    }
}
