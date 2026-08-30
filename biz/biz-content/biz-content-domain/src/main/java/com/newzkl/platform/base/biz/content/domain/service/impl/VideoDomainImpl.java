package com.newzkl.platform.base.biz.content.domain.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.domain.adapt.repository.VideoCategoryRepository;
import com.newzkl.platform.base.biz.content.domain.adapt.repository.VideoRepository;
import com.newzkl.platform.base.biz.content.domain.service.VideoDomain;
import com.newzkl.platform.base.biz.content.model.video.entity.Video;
import com.newzkl.platform.base.biz.content.model.video.query.VideoPageQuery;
import com.newzkl.platform.base.biz.content.model.video.query.VideoQuery;
import com.newzkl.platform.base.biz.content.model.video.req.VideoReq;
import com.newzkl.platform.base.biz.content.model.video.res.VideoRes;
import com.newzkl.platform.base.biz.content.model.video.vo.RecommendVideoVO;
import com.newzkl.platform.base.biz.content.model.videocategory.entity.VideoCategory;
import com.newzkl.platform.base.biz.content.model.videocategory.res.VideoCategoryRes;
import com.newzkl.platform.base.biz.content.model.videocategory.vo.VideoCategoryWeightVO;
import cn.hutool.core.bean.BeanUtil;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 视频领域服务实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.service.impl.IVideoDomainImpl}, 去 I 前缀。</p>
 *
 * <p><b>TODO[cross-domain] 跨域降级</b>: 旧实现注入 3 个 Dubbo facade——
 * {@code IUserFollowFacade}(关注关系)、{@code IAccountFacade}(发布人昵称头像)、
 * {@code IInteractionFacade}(点赞状态)。Base 现无这些 facade 且 {@code biz-content} 不依赖跨域模块, 故:</p>
 * <ul>
 *   <li>{@code fillFollowAndLikeStatus}/{@code fillIssuerNickname}/{@code fillFollowStatusForRecommend}
 *       等跨域填充方法移除, 出参 {@code isFollowed}/{@code isLike}/{@code issuer}/{@code issuerImg} 不再填充;</li>
 *   <li>{@code getFollowingVideoPage} 无法取关注列表, 返回空分页(保持契约结构);</li>
 *   <li>{@code likeVideoList} 委托仓储, 仓储已降级返回空列表。</li>
 * </ul>
 * <p>核心 CRUD、分类名填充、推荐分类按权重取数逻辑保留。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class VideoDomainImpl implements VideoDomain {

    private final VideoRepository videoRepository;

    private final VideoCategoryRepository videoCategoryRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createVideo(VideoReq req) {
        // 检查分类是否存在
        if (videoCategoryRepository.getById(req.getCategoryId()) == null) {
            ThrowsException.exception(BaseErrorCode.NODATA, "分类");
        }

        videoRepository.save(req);

        // 更新分类视频数量
        videoCategoryRepository.updateVideoCount(req.getCategoryId(), 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVideo(VideoReq req) {
        Video oldVideo = videoRepository.getById(req.getId());

        videoRepository.update(req);

        if (req.getCategoryId() != null && !ObjectUtil.equal(oldVideo.getCategoryId(), req.getCategoryId())) {
            // 更新分类视频数量
            videoCategoryRepository.updateVideoCount(oldVideo.getCategoryId(), -1);
            videoCategoryRepository.updateVideoCount(req.getCategoryId(), 1);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVideo(Long id) {
        Video oldVideo = videoRepository.getById(id);
        videoRepository.deleteById(id);
        // 更新分类视频数量
        videoCategoryRepository.updateVideoCount(oldVideo.getCategoryId(), -1);
    }

    @Override
    public Page<VideoRes> getVideoPage(VideoPageQuery query) {
        Page<VideoRes> videoPage = videoRepository.getVideoPage(query);

        // 如果分页结果为空, 直接返回
        if (videoPage.getRecords().isEmpty()) {
            return videoPage;
        }

        // 收集所有分类ID(去重)
        List<Long> categoryIds = videoPage.getRecords().stream()
                .map(VideoRes::getCategoryId)
                .distinct()
                .collect(Collectors.toList());

        // 批量查询分类并构建映射
        Map<Long, VideoCategory> categoryMap = videoCategoryRepository.getByIdList(categoryIds).stream()
                .collect(Collectors.toMap(
                        VideoCategory::getId,
                        category -> category,
                        (existing, replacement) -> existing
                ));

        // 填充分类名称
        videoPage.getRecords().forEach(video -> {
            VideoCategory category = categoryMap.get(video.getCategoryId());
            if (category != null) {
                video.setCategoryName(category.getName());
            }
        });

        // TODO[cross-domain]: 旧此处 fillFollowAndLikeStatus(关注/点赞) + fillIssuerNickname(发布人昵称头像),
        // 依赖用户域 IUserFollowFacade/IInteractionFacade/IAccountFacade, Base 无对应 facade, 暂不填充。
        return videoPage;
    }

    @Override
    public List<VideoRes> getVideoList(VideoQuery query) {
        // 旧 domain 层角色可见性判断(MEMBER 仅看 isVisible=1) 与出参转换下沉至仓储, 见 listVideoResForMember
        List<VideoRes> result = videoRepository.listVideoResForMember(query);
        // TODO[cross-domain]: 旧此处 fillIssuerNickname 填发布人昵称头像, 依赖 IAccountFacade, 暂不填充。
        return result;
    }

    @Override
    public VideoRes getVideoById(Long id) {
        // 旧 domain 层角色可见性校验下沉至仓储 getVideoByIdForView
        Video video = videoRepository.getVideoByIdForView(id);
        VideoRes videoRes = BeanUtil.copyProperties(video, VideoRes.class);
        VideoCategory category = videoCategoryRepository.getById(videoRes.getCategoryId());
        if (category != null) {
            videoRes.setCategoryName(category.getName());
        }
        // TODO[cross-domain]: 旧此处 fillIssuerNicknameSingle 填发布人昵称头像, 依赖 IAccountFacade, 暂不填充。
        return videoRes;
    }

    @Override
    public List<RecommendVideoVO> getRecommendList() {
        // 获取所有的视频分类(旧传 SecurityUtils.getIdentity(), 角色->推荐人群下沉至仓储 currentRecommendGroups)
        List<VideoCategoryRes> categoryList = videoCategoryRepository.getCategoryList(
                videoRepository.currentRecommendGroups());

        if (categoryList.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取每个分类应该获取的视频数量
        List<VideoCategoryWeightVO> categoryWeightList = videoCategoryRepository.getCategoryWeightList();
        Map<Long, VideoCategoryWeightVO> map = categoryWeightList.stream()
                .collect(Collectors.toMap(VideoCategoryWeightVO::getId, videoCategoryWeight -> videoCategoryWeight));

        // 从每个分类中随机获取对应数量的视频
        List<Video> allVideos = new ArrayList<>();
        for (VideoCategoryRes category : categoryList) {
            VideoQuery query = new VideoQuery();
            query.setCategoryId(category.getId());
            query.setLimit(map.get(category.getId()).getWeight()); // 只查询需要的数量
            query.setRandom(true); // 随机获取
            query.setIsVisible(1);
            query.setOneself(false);
            List<Video> videos = videoRepository.getVideoList(query);
            allVideos.addAll(videos);
        }

        // 如果没有视频, 直接返回空列表
        if (allVideos.isEmpty()) {
            return new ArrayList<>();
        }

        // 随机排序所有选中的视频
        Collections.shuffle(allVideos);

        // 转换为 RecommendVideoVO
        // TODO[cross-domain]: 旧此处 fillFollowStatusForRecommend(关注) + fillIssuerNicknameForRecommend(昵称),
        // 依赖 IUserFollowFacade/IAccountFacade, Base 无对应 facade, 暂不填充。
        return BeanUtil.copyToList(allVideos, RecommendVideoVO.class);
    }

    @Override
    public Page<VideoRes> getFollowingVideoPage(VideoPageQuery query) {
        // TODO[cross-domain]: 旧实现依赖 IUserFollowFacade.getFollowingIds 取关注列表, 再按 issuerIds 查视频。
        // Base 无用户域关注 facade, 无法确定关注对象, 返回空分页保持契约结构。
        return new Page<>();
    }

    @Override
    public List<VideoRes> likeVideoList(VideoPageQuery query) {
        // 仓储层已跨域降级返回空列表(依赖 IInteractionFacade 取用户点赞记录)
        return videoRepository.likeVideoList(query);
    }

    @Override
    public List<VideoRes> userIssueVideoList(VideoPageQuery query) {
        List<VideoRes> list = videoRepository.userIssueVideoList(query);
        // TODO[cross-domain]: 旧此处 fillFollowAndLikeStatus + fillIssuerNickname,
        // 依赖用户域 facade, Base 无对应 facade, 暂不填充。
        return list;
    }
}
