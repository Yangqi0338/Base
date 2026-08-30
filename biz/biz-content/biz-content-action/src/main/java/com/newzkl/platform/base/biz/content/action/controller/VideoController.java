package com.newzkl.platform.base.biz.content.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.domain.service.VideoDomain;
import com.newzkl.platform.base.biz.content.model.video.query.VideoPageQuery;
import com.newzkl.platform.base.biz.content.model.video.query.VideoQuery;
import com.newzkl.platform.base.biz.content.model.video.req.VideoReq;
import com.newzkl.platform.base.biz.content.model.video.res.VideoRes;
import com.newzkl.platform.base.biz.content.model.video.vo.RecommendVideoVO;
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
 * 平台-视频
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.interfaces.controller.VideoController},
 * 端点路径与 HTTP 方法逐字保留。{@code ScmResult} 换为 {@code PlatformResult},
 * {@code jakarta.validation} 换为 {@code jakarta.validation}。</p>
 *
 * <p><b>契约变更</b>: 旧 {@code getRecommendList} 经 application 层
 * {@code IContentManageService.getRecommendList} 转发, Base 无 application 层,
 * 直接委托 {@code VideoDomain.getRecommendList}, 出参结构不变。</p>
 *
 * <p><b>跨域降级</b>: {@code getFollowingVideoPage}/{@code likeVideoList} 及各出参的关注/点赞/
 * 发布人昵称字段依赖用户域/商品域 facade, Base 暂无, 详见 {@code VideoDomainImpl} TODO[cross-domain]。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/admin/video")
@RequiredArgsConstructor
@FuncPermission("平台-视频")
public class VideoController {

    private final VideoDomain videoDomain;

    /**
     * 创建视频
     *
     * @param req 视频入参
     * @return 创建结果
     */
    @PostMapping("/create")
    @FuncPermission("创建视频")
    public PlatformResult<Void> createVideo(@Valid @RequestBody VideoReq req) {
        videoDomain.createVideo(req);
        return PlatformResult.success();
    }

    /**
     * 更新视频
     *
     * @param req 视频入参(含主键ID)
     * @return 更新结果
     */
    @PostMapping("/update")
    @FuncPermission("更新视频")
    public PlatformResult<Void> updateVideo(@RequestBody VideoReq req) {
        videoDomain.updateVideo(req);
        return PlatformResult.success();
    }

    /**
     * 删除视频
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @GetMapping("/delete")
    @FuncPermission("删除视频")
    public PlatformResult<Void> deleteVideo(@RequestParam Long id) {
        videoDomain.deleteVideo(id);
        return PlatformResult.success();
    }

    /**
     * 分页查询视频
     *
     * <p>TODO[fe-contract]: 出参壳由旧 mybatis-plus {@code Page} 换为 {@code ContentPage},
     * 字段名一致(records/total/size/current/pages)。</p>
     *
     * @param query 分页查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<Page<VideoRes>> getVideoPage(@RequestBody VideoPageQuery query) {
        return PlatformResult.success(videoDomain.getVideoPage(query));
    }

    /**
     * 视频列表
     *
     * @param query 列表查询条件
     * @return 视频出参列表
     */
    @PostMapping("/list")
    public PlatformResult<List<VideoRes>> getVideoList(@RequestBody VideoQuery query) {
        return PlatformResult.success(videoDomain.getVideoList(query));
    }

    /**
     * 获取视频详情
     *
     * @param id 主键ID
     * @return 视频详情
     */
    @GetMapping("/getVideoById")
    public PlatformResult<VideoRes> getVideoById(@RequestParam Long id) {
        return PlatformResult.success(videoDomain.getVideoById(id));
    }

    /**
     * 视频推荐列表
     *
     * @return 推荐视频视图列表
     */
    @PostMapping("/getRecommendList")
    public PlatformResult<List<RecommendVideoVO>> getRecommendList() {
        return PlatformResult.success(videoDomain.getRecommendList());
    }

    /**
     * 分页查询关注的人发布的视频
     *
     * <p>TODO[cross-domain]: 依赖用户域关注 facade, Base 暂无, 当前返回空分页。</p>
     *
     * @param query 分页查询条件
     * @return 分页结果
     */
    @PostMapping("/getFollowingVideoPage")
    public PlatformResult<Page<VideoRes>> getFollowingVideoPage(@RequestBody VideoPageQuery query) {
        return PlatformResult.success(videoDomain.getFollowingVideoPage(query));
    }

    /**
     * 点赞视频列表
     *
     * <p>TODO[cross-domain]: 依赖用户域交互 facade, Base 暂无, 当前返回空列表。</p>
     *
     * @param query 分页查询条件
     * @return 视频出参列表
     */
    @PostMapping("/likeVideoList")
    public PlatformResult<List<VideoRes>> likeVideoList(@RequestBody VideoPageQuery query) {
        return PlatformResult.success(videoDomain.likeVideoList(query));
    }

    /**
     * 用户发布视频列表
     *
     * @param query 分页查询条件
     * @return 视频出参列表
     */
    @PostMapping("/userIssueVideoList")
    public PlatformResult<List<VideoRes>> userIssueVideoList(@RequestBody VideoPageQuery query) {
        return PlatformResult.success(videoDomain.userIssueVideoList(query));
    }
}
