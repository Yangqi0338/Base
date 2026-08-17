package com.newzkl.platform.base.biz.content.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.content.domain.adapt.repository.VideoRepository;
import com.newzkl.platform.base.biz.content.infrastructure.dao.ContentVideoDAO;
import com.newzkl.platform.base.biz.content.infrastructure.entity.VideoDO;
import com.newzkl.platform.base.biz.content.model.common.res.ContentPage;
import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import com.newzkl.platform.base.biz.content.model.video.entity.Video;
import com.newzkl.platform.base.biz.content.model.video.query.VideoPageQuery;
import com.newzkl.platform.base.biz.content.model.video.query.VideoQuery;
import com.newzkl.platform.base.biz.content.model.video.req.VideoReq;
import com.newzkl.platform.base.biz.content.model.video.res.VideoRes;
import com.newzkl.platform.base.biz.content.model.util.RecommendGroupsCheckUtil;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.utils.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import com.newzkl.platform.base.common.ddd.model.enums.user.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频仓储实现
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.repository.VideoRepositoryImpl}。</p>
 *
 * <p><b>TODO[cross-domain] 跨域降级</b>: 旧实现注入 {@code IStoreTargetInteractionFacade}
 * (商品域, 取点赞/分享数) 与 {@code IInteractionFacade}(用户域, 取点赞记录分页)。Base 现无这两个
 * facade 且 {@code biz-content} 不依赖跨域模块, 故:</p>
 * <ul>
 *   <li>{@code buildLikeNumAndSharesNum} 逻辑移除, 出参 {@code likeNum}/{@code shareNum} 不再填充;</li>
 *   <li>{@code likeVideoList} 无法获取用户点赞记录, 返回空列表(保持契约结构)。</li>
 * </ul>
 * <p>旧分页 SQL 的跨库 JOIN 已去除, 改由 {@code BaseLambdaQueryWrapper} 等价条件重写。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class VideoRepositoryImpl implements VideoRepository {

    private final ContentVideoDAO contentVideoDAO;

    @Override
    public void save(VideoReq req) {
        VideoDO videoDO = TransferUtils.transfer(req, VideoDO::new);
        videoDO.setIsVisible(CommonEnum.YesOrNo.YES);
        contentVideoDAO.insert(videoDO);
    }

    @Override
    public void deleteById(Long id) {
        contentVideoDAO.deleteById(id);
    }

    @Override
    public void update(VideoReq req) {
        contentVideoDAO.updateById(TransferUtils.transfer(req, VideoDO::new));
    }

    @Override
    public ContentPage<VideoRes> getVideoPage(VideoPageQuery query) {
        // 旧: videoDAO.selectVideoPage 跨库 JOIN 取互动数, Base 分库不可用, 改纯条件重写; 互动数走跨域降级
        Page<VideoDO> page = contentVideoDAO.selectPage(RepositorySupport.page(query),
                new BaseLambdaQueryWrapper<VideoDO>()
                        .notEmptyLike(VideoDO::getName, query.getName())
                        .notEmptyEq(VideoDO::getIsVisible, query.getIsVisible())
                        .notEmptyEq(VideoDO::getCategoryId, query.getCategoryId())
                        .notEmptyEq(VideoDO::getIssuerId, query.getIssuerId())
                        .notEmptyIn(VideoDO::getIssuerId, query.getIssuerIds())
                        .between(query.getCreateTimeL() != null && query.getCreateTimeR() != null,
                                VideoDO::getCreateTime, query.getCreateTimeL(), query.getCreateTimeR())
                        .orderByDesc(VideoDO::getCreateTime));
        return ContentPage.of(page.getCurrent(), page.getSize(), page.getTotal(),
                TransferUtils.transfers(page.getRecords(), VideoRes::new));
    }

    @Override
    public List<Video> getVideoList(VideoQuery query) {
        // 旧: buildLikeNumAndSharesNum 填充点赞/分享数(跨域降级, 暂不填充)
        return TransferUtils.transfers(contentVideoDAO.selectList(buildListWrapper(query)), Video::new);
    }

    @Override
    public List<VideoRes> listVideoResForMember(VideoQuery query) {
        // 旧 domain 层: C端角色(MEMBER)仅可见 isVisible=1, 角色判断下沉至此
        if (RoleEnum.CompanyRole.MEMBER.equals(SecurityUtils.getRole())) {
            query.setIsVisible(1);
        }
        // 旧: buildLikeNumAndSharesNum 填充点赞/分享数(跨域降级, 暂不填充)
        return TransferUtils.transfers(contentVideoDAO.selectList(buildListWrapper(query)), VideoRes::new);
    }

    /**
     * 构建视频列表查询条件
     *
     * @param query 列表查询条件
     * @return 查询构造器
     */
    private BaseLambdaQueryWrapper<VideoDO> buildListWrapper(VideoQuery query) {
        BaseLambdaQueryWrapper<VideoDO> wrapper = new BaseLambdaQueryWrapper<VideoDO>()
                .notEmptyLike(VideoDO::getName, query.getName())
                .notEmptyEq(VideoDO::getIsVisible, query.getIsVisible())
                .notEmptyEq(VideoDO::getCategoryId, query.getCategoryId())
                .notEmptyIn(VideoDO::getId, query.getIdList())
                .notEmptyEq(VideoDO::getIssuerId, query.getIssuerId());

        if (Boolean.FALSE.equals(query.getOneself())) {
            wrapper.ne(VideoDO::getIssuerId, SecurityUtils.getAccountId());
        }
        // 如果需要随机排序, 使用 ORDER BY RAND()
        if (Boolean.TRUE.equals(query.getRandom())) {
            wrapper.last("ORDER BY RAND()");
            // 如果指定了 limit, 则限制查询数量
            if (query.getLimit() != null && query.getLimit() > 0) {
                wrapper.last("ORDER BY RAND() LIMIT " + query.getLimit());
            }
        } else {
            wrapper.orderByDesc(VideoDO::getCreateTime);
            // 如果指定了 limit, 则限制查询数量
            if (query.getLimit() != null && query.getLimit() > 0) {
                wrapper.last("LIMIT " + query.getLimit());
            }
        }
        return wrapper;
    }

    @Override
    public Video getById(Long id) {
        return TransferUtils.transfer(contentVideoDAO.selectById(id), Video::new);
    }

    @Override
    public Video getVideoByIdForView(Long id) {
        // 旧 domain 层: C端角色(MEMBER)访问不可见视频抛异常, 角色判断下沉至此
        boolean isVisibleHack = RoleEnum.CompanyRole.MEMBER.equals(SecurityUtils.getRole());
        VideoDO videoDO = contentVideoDAO.selectById(id);
        // 旧: CommonEnum.Switch.OFF.getCode() 判不可见, Base 无 CommonEnum.Switch, 用字面量 0
        if (videoDO == null || (isVisibleHack && Integer.valueOf(0).equals(videoDO.getIsVisible()))) {
            ThrowsException.exception(BaseErrorCode.NODATA, "视频");
        }
        return TransferUtils.transfer(videoDO, Video::new);
    }

    @Override
    public List<RecommendGroupEnum> currentRecommendGroups() {
        // 旧 domain: RecommendGroupsCheckUtil.getRecommendGroups(SecurityUtils.getRole())
        // 贴 Base 范本改用 getRoleId(), 语义一致(角色ID)
        return RecommendGroupsCheckUtil.getRecommendGroups(SecurityUtils.getRole());
    }

    @Override
    public List<VideoRes> likeVideoList(VideoPageQuery query) {
        // 旧实现依赖 IInteractionFacade.getUserInteractionsPage 分页取用户点赞记录, 再回查视频。
        // Base 无用户域交互 facade, 无法获取点赞记录, 返回空列表保持契约结构。
        // TODO[cross-domain]: 待用户域交互查询接入后, 恢复"点赞记录分页 -> 按 targetId 回查视频"逻辑。
        return new ArrayList<>();
    }

    @Override
    public List<VideoRes> userIssueVideoList(VideoPageQuery query) {
        // 旧: selectVideoPage 跨库 JOIN, 改纯条件分页; 只查可见视频
        query.setIsVisible(1);
        Page<VideoDO> page = contentVideoDAO.selectPage(RepositorySupport.page(query),
                new BaseLambdaQueryWrapper<VideoDO>()
                        .notEmptyLike(VideoDO::getName, query.getName())
                        .notEmptyEq(VideoDO::getIsVisible, query.getIsVisible())
                        .notEmptyEq(VideoDO::getCategoryId, query.getCategoryId())
                        .notEmptyEq(VideoDO::getIssuerId, query.getIssuerId())
                        .notEmptyIn(VideoDO::getIssuerId, query.getIssuerIds())
                        .orderByDesc(VideoDO::getCreateTime));
        // 旧: buildLikeNumAndSharesNum 填充点赞/分享数(跨域降级, 暂不填充)
        return TransferUtils.transfers(page.getRecords(), VideoRes::new);
    }
}
