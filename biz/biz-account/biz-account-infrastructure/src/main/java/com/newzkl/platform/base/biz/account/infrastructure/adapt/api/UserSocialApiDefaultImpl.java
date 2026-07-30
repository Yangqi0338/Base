package com.newzkl.platform.base.biz.account.infrastructure.adapt.api;

import com.newzkl.platform.base.biz.account.domain.adapt.api.UserSocialApi;
import org.springframework.stereotype.Component;

/**
 * {@code UserSocialApi} 默认兜底实现
 *
 * <p>infra-gap 清单:</p>
 * <ul>
 *   <li>关注关系 (isFollowed / countFollowing / countFollower): 能力在 biz-user 的
 *       {@code UserFollowRepository}, 跨服务链未接, 默认 false / 0</li>
 *   <li>累计获赞 (totalLikeCount): 能力在 biz-goods 的门店互动统计
 *       (旧 {@code IStoreTargetInteractionFacade#selectSummaryByPublisherId}), 未接, 默认 null</li>
 * </ul>
 *
 * <p>影响: {@code GET /user/account/userHomePage} 的关注数/粉丝数/是否关注/点赞数为默认值,
 * 昵称/头像/背景图/门店信息正常。入口 starter 侧远程 consumer 覆盖后自动恢复。</p>
 *
 * @author KC
 */
@Component
public class UserSocialApiDefaultImpl implements UserSocialApi {

    @Override
    public Boolean isFollowed(Long fromUserId, Long toUserId) {
        // TODO[infra-gap]: biz-user UserFollowRepository#isFollowed 跨服务未接
        return Boolean.FALSE;
    }

    @Override
    public Integer countFollowing(Long userId) {
        // TODO[infra-gap]: biz-user UserFollowRepository#countFollowing 跨服务未接
        return 0;
    }

    @Override
    public Integer countFollower(Long userId) {
        // TODO[infra-gap]: biz-user UserFollowRepository#countFollower 跨服务未接
        return 0;
    }

    @Override
    public Integer totalLikeCount(Long publisherId) {
        // TODO[infra-gap]: biz-goods 门店互动统计跨服务未接
        return null;
    }
}
