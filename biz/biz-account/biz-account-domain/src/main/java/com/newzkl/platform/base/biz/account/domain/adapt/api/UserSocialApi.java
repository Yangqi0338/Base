package com.newzkl.platform.base.biz.account.domain.adapt.api;

/**
 * 用户社交域出站端口 (outbound port)
 *
 * <p>迁移: 原直连 {@code com.zkl.scm.user.domain.social.repository.IUserFollowRepository}
 * 与 {@code com.zkl.scm.goods.rpc.facade.IStoreTargetInteractionFacade};
 * 中台化后跨域只经端口, 由入口 starter 侧远程 consumer 覆盖默认实现。
 * 关注关系落 biz-user, 互动统计落 biz-goods, 均不得从 biz-account 直连。</p>
 *
 * @author KC
 */
public interface UserSocialApi {

    /**
     * 判断来源用户是否已关注目标用户
     *
     * @param fromUserId 来源(当前登录)用户ID
     * @param toUserId   目标(被查看)用户ID
     * @return 已关注返回 true
     */
    Boolean isFollowed(Long fromUserId, Long toUserId);

    /**
     * 统计用户的关注数 (我关注的人数)
     *
     * @param userId 用户ID
     * @return 关注数, 无则 0
     */
    Integer countFollowing(Long userId);

    /**
     * 统计用户的粉丝数 (关注我的人数)
     *
     * @param userId 用户ID
     * @return 粉丝数, 无则 0
     */
    Integer countFollower(Long userId);

    /**
     * 统计发布者累计获赞数
     *
     * @param publisherId 发布者用户ID
     * @return 累计获赞数, 无则 null
     */
    Integer totalLikeCount(Long publisherId);
}
