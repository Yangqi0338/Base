package com.newzkl.platform.base.biz.content.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.model.video.query.VideoPageQuery;
import com.newzkl.platform.base.biz.content.model.video.query.VideoQuery;
import com.newzkl.platform.base.biz.content.model.video.req.VideoReq;
import com.newzkl.platform.base.biz.content.model.video.res.VideoRes;
import com.newzkl.platform.base.biz.content.model.video.vo.RecommendVideoVO;

import java.util.List;

/**
 * 视频领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.service.IVideoDomain}, 去 I 前缀。
 * 分页壳由旧 {@code Page} 换为 {@code ContentPage}。</p>
 *
 * @author KC
 */
public interface VideoDomain {

    /**
     * 创建视频
     *
     * @param req 视频入参
     */
    void createVideo(VideoReq req);

    /**
     * 更新视频
     *
     * @param req 视频入参(含主键ID)
     */
    void updateVideo(VideoReq req);

    /**
     * 删除视频
     *
     * @param id 主键ID
     */
    void deleteVideo(Long id);

    /**
     * 分页查询视频
     *
     * @param query 分页查询条件
     * @return 分页结果
     */
    Page<VideoRes> getVideoPage(VideoPageQuery query);

    /**
     * 查询视频列表
     *
     * @param query 列表查询条件
     * @return 视频出参列表
     */
    List<VideoRes> getVideoList(VideoQuery query);

    /**
     * 按主键获取视频详情
     *
     * @param id 主键ID
     * @return 视频详情
     */
    VideoRes getVideoById(Long id);

    /**
     * 获取推荐视频列表
     *
     * @return 推荐视频视图列表
     */
    List<RecommendVideoVO> getRecommendList();

    /**
     * 分页查询关注的人发布的视频
     *
     * @param query 分页查询条件
     * @return 分页结果
     */
    Page<VideoRes> getFollowingVideoPage(VideoPageQuery query);

    /**
     * 点赞视频列表
     *
     * @param query 分页查询条件
     * @return 视频出参列表
     */
    List<VideoRes> likeVideoList(VideoPageQuery query);

    /**
     * 用户发布视频列表
     *
     * @param query 分页查询条件
     * @return 视频出参列表
     */
    List<VideoRes> userIssueVideoList(VideoPageQuery query);
}
