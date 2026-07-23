package com.newzkl.platform.base.biz.user.domain.adapt.repository;

import com.newzkl.platform.base.biz.user.model.relation.req.UserFollowPageReq;
import com.newzkl.platform.base.biz.user.model.relation.vo.UserFollow;

import java.util.List;
import java.util.Set;

/**
 * 用户关注仓储接口。
 *
 * <p>迁移说明：源 findFollowingPage/findFollowerPage 返回 MyBatis-Plus Page，降级为 List；
 * 分页在 infra 内构造。TODO[page-meta] total 等元数据跨层丢失。</p>
 *
 * @author sijiwang
 */
public interface UserFollowRepository {

    /**
     * 保存关注关系。
     *
     * @param userFollow 关注实体
     * @return 保存后的实体
     */
    UserFollow save(UserFollow userFollow);

    /**
     * 删除关注关系。
     *
     * @param follower  关注者ID
     * @param following 被关注者ID
     * @return 是否删除成功
     */
    boolean delete(Long follower, Long following);

    /**
     * 查询是否已关注。
     *
     * @param follower  关注者ID
     * @param following 被关注者ID
     * @return 是否已关注
     */
    boolean isFollowed(Long follower, Long following);

    /**
     * 查询关注列表（我关注的人）。
     *
     * @param follower 关注者ID
     * @return 关注列表
     */
    List<UserFollow> findFollowingList(Long follower);

    /**
     * 分页查询关注列表。
     *
     * @param query 分页查询请求
     * @return 当前页关注列表
     */
    List<UserFollow> findFollowingPage(UserFollowPageReq query);

    /**
     * 查询粉丝列表（关注我的人）。
     *
     * @param following 被关注者ID
     * @return 粉丝列表
     */
    List<UserFollow> findFollowerList(Long following);

    /**
     * 分页查询粉丝列表。
     *
     * @param query 分页查询请求
     * @return 当前页粉丝列表
     */
    List<UserFollow> findFollowerPage(UserFollowPageReq query);

    /**
     * 统计关注数（我关注的人数）。
     *
     * @param follower 关注者ID
     * @return 关注数
     */
    int countFollowing(Long follower);

    /**
     * 统计粉丝数（关注我的人数）。
     *
     * @param following 被关注者ID
     * @return 粉丝数
     */
    int countFollower(Long following);

    /**
     * 批量查询关注状态。
     *
     * @param followerId   关注者ID
     * @param followingIds 被关注者ID列表
     * @return 已关注的用户ID集合
     */
    Set<Long> batchIsFollowed(Long followerId, List<Long> followingIds);
}
