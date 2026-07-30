package com.newzkl.platform.base.biz.user.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.model.relation.req.UserFollowPageReq;
import com.newzkl.platform.base.biz.user.model.relation.vo.UserFollowVO;

import java.util.List;

/**
 * 用户关注领域服务
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.follow.service.UserFollowDomain}。
 * 本域自足, 仅读写关注关系, 不富化被关注者昵称等别域数据。</p>
 *
 * <p>迁移说明: 源 getFollowingPage/getFollowerPage 返回 MyBatis-Plus Page,
 * 本仓按 {@code rules/Architecture.md} 保留 {@code Page} 分页壳直返, total/pages 元数据不丢。</p>
 *
 * @author KC
 */
public interface UserFollowDomain {

    /**
     * 关注用户
     *
     * <p>不可关注自己; 已关注时幂等回 true。</p>
     *
     * @param follower  关注者ID
     * @param following 被关注者ID
     * @return 是否成功
     */
    boolean follow(Long follower, Long following);

    /**
     * 取消关注
     *
     * @param follower  关注者ID
     * @param following 被关注者ID
     * @return 是否成功
     */
    boolean unfollow(Long follower, Long following);

    /**
     * 查询是否已关注
     *
     * @param follower  关注者ID
     * @param following 被关注者ID
     * @return 是否已关注
     */
    boolean isFollowed(Long follower, Long following);

    /**
     * 查询关注列表（我关注的人）
     *
     * @param userId 关注者ID
     * @return 关注列表
     */
    List<UserFollowVO> getFollowingList(Long userId);

    /**
     * 分页查询关注列表（我关注的人）
     *
     * @param query 分页查询请求
     * @return 关注分页
     */
    Page<UserFollowVO> getFollowingPage(UserFollowPageReq query);

    /**
     * 查询粉丝列表（关注我的人）
     *
     * @param userId 被关注者ID
     * @return 粉丝列表
     */
    List<UserFollowVO> getFollowerList(Long userId);

    /**
     * 分页查询粉丝列表（关注我的人）
     *
     * @param query 分页查询请求
     * @return 粉丝分页
     */
    Page<UserFollowVO> getFollowerPage(UserFollowPageReq query);

    /**
     * 统计关注数（我关注的人数）
     *
     * @param userId 关注者ID
     * @return 关注数
     */
    int getFollowingCount(Long userId);

    /**
     * 统计粉丝数（关注我的人数）
     *
     * @param userId 被关注者ID
     * @return 粉丝数
     */
    int getFollowerCount(Long userId);
}
