package com.newzkl.platform.base.biz.user.domain.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.UserFollowRepository;
import com.newzkl.platform.base.biz.user.domain.service.UserFollowDomain;
import com.newzkl.platform.base.biz.user.model.relation.req.UserFollowPageReq;
import com.newzkl.platform.base.biz.user.model.relation.vo.UserFollow;
import com.newzkl.platform.base.biz.user.model.relation.vo.UserFollowVO;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.PlatformException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户关注领域服务实现
 *
 * <p>迁移自旧 {@code UserFollowDomainImpl}。语义迁移: {@code ScmException(CUSTOM, msg, true)} 三参
 * → {@code PlatformException} 两参; Page 出参保留分页壳直返。</p>
 *
 * @author KC
 */
@Slf4j
@Service("userFollowDomainImpl")
@RequiredArgsConstructor
public class UserFollowDomainImpl implements UserFollowDomain {

    private final UserFollowRepository userFollowRepository;

    /**
     * 领域实体转视图对象（关注列表用, 回被关注者ID）
     *
     * @param entity 关注实体
     * @return 视图对象
     */
    private UserFollowVO toFollowingVO(UserFollow entity) {
        UserFollowVO vo = new UserFollowVO();
        vo.setId(entity.getId());
        vo.setUserId(entity.getFollowing());
        vo.setFollowTime(entity.getCreateTime());
        return vo;
    }

    /**
     * 领域实体转视图对象（粉丝列表用, 回关注者ID）
     *
     * @param entity 关注实体
     * @return 视图对象
     */
    private UserFollowVO toFollowerVO(UserFollow entity) {
        UserFollowVO vo = new UserFollowVO();
        vo.setId(entity.getId());
        vo.setUserId(entity.getFollower());
        vo.setFollowTime(entity.getCreateTime());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean follow(Long follower, Long following) {
        if (follower.equals(following)) {
            throw new PlatformException(BaseErrorCode.CUSTOM, "不能关注自己");
        }

        if (userFollowRepository.isFollowed(follower, following)) {
            log.info("用户{}已关注用户{}", follower, following);
            return true;
        }

        UserFollow userFollow = new UserFollow();
        userFollow.setFollower(follower);
        userFollow.setFollowing(following);
        userFollowRepository.save(userFollow);

        log.info("用户{}关注用户{}成功", follower, following);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unfollow(Long follower, Long following) {
        boolean result = userFollowRepository.delete(follower, following);
        log.info("用户{}取消关注用户{}，结果：{}", follower, following, result);
        return result;
    }

    @Override
    public boolean isFollowed(Long follower, Long following) {
        return userFollowRepository.isFollowed(follower, following);
    }

    @Override
    public List<UserFollowVO> getFollowingList(Long userId) {
        return userFollowRepository.findFollowingList(userId).stream()
                .map(this::toFollowingVO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserFollowVO> getFollowingPage(UserFollowPageReq query) {
        return TransferUtils.transferPage(userFollowRepository.findFollowingPage(query), this::toFollowingVO);
    }

    @Override
    public List<UserFollowVO> getFollowerList(Long userId) {
        return userFollowRepository.findFollowerList(userId).stream()
                .map(this::toFollowerVO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserFollowVO> getFollowerPage(UserFollowPageReq query) {
        return TransferUtils.transferPage(userFollowRepository.findFollowerPage(query), this::toFollowerVO);
    }

    @Override
    public int getFollowingCount(Long userId) {
        return userFollowRepository.countFollowing(userId);
    }

    @Override
    public int getFollowerCount(Long userId) {
        return userFollowRepository.countFollower(userId);
    }
}
