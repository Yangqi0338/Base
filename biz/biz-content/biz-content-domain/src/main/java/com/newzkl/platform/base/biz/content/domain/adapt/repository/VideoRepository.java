package com.newzkl.platform.base.biz.content.domain.adapt.repository;

import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import com.newzkl.platform.base.biz.content.model.video.entity.Video;
import com.newzkl.platform.base.biz.content.model.video.query.VideoPageQuery;
import com.newzkl.platform.base.biz.content.model.video.query.VideoQuery;
import com.newzkl.platform.base.biz.content.model.video.req.VideoReq;
import com.newzkl.platform.base.biz.content.model.video.res.VideoRes;
import com.newzkl.platform.base.biz.content.model.common.res.ContentPage;
import com.newzkl.platform.base.biz.content.model.video.entity.Video;

import java.util.List;

/**
 * 视频仓储接口
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.repository.IVideoRepository}, 去 I 前缀。
 * 分页壳由旧 {@code Page} 换为 {@code ContentPage}。</p>
 *
 * @author KC
 */
public interface VideoRepository {

    /**
     * 保存视频
     *
     * @param req 视频入参
     */
    void save(VideoReq req);

    /**
     * 按主键删除视频
     *
     * @param id 主键ID
     */
    void deleteById(Long id);

    /**
     * 按主键更新视频
     *
     * @param req 视频入参(含主键ID)
     */
    void update(VideoReq req);

    /**
     * 分页查询视频
     *
     * @param query 分页查询条件
     * @return 分页结果(未填分类名/发布人/互动数)
     */
    ContentPage<VideoRes> getVideoPage(VideoPageQuery query);

    /**
     * 查询视频领域实体列表
     *
     * <p>供推荐聚合使用, 返回领域实体供上层随机重排。</p>
     *
     * @param query 列表查询条件
     * @return 视频领域实体列表
     */
    List<Video> getVideoList(VideoQuery query);

    /**
     * 查询视频出参列表(按当前登录角色施加可见性)
     *
     * <p>迁移自旧 domain 层可见性判断: C端角色(MEMBER)仅可见 {@code isVisible=1}。
     * 因 domain 层不依赖通用工具(SecurityUtils/TransferUtils), 角色判断与出参转换下沉至此。</p>
     *
     * @param query 列表查询条件
     * @return 视频出参列表
     */
    List<VideoRes> listVideoResForMember(VideoQuery query);

    /**
     * 按主键查询视频(无可见性校验)
     *
     * <p>供更新/删除前取旧值。</p>
     *
     * @param id 主键ID
     * @return 视频领域实体
     */
    Video getById(Long id);

    /**
     * 按主键查询视频(按当前登录角色施加可见性校验)
     *
     * <p>迁移自旧 {@code getVideoById(id, isVisibleHack)}: C端角色(MEMBER)访问不可见视频抛异常。
     * 角色判断下沉至此。</p>
     *
     * @param id 主键ID
     * @return 视频领域实体
     */
    Video getVideoByIdForView(Long id);

    /**
     * 取当前登录角色对应的推荐人群集合
     *
     * <p>迁移自旧 domain 层 {@code RecommendGroupsCheckUtil.getRecommendGroups(SecurityUtils.getIdentity())}。
     * 因 domain 层不依赖 SecurityUtils, 下沉至此。</p>
     *
     * @return 推荐人群名称集合
     */
    List<RecommendGroupEnum> currentRecommendGroups();

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
