package com.newzkl.platform.base.biz.user.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.adapt.repository.UserFollowRepository;
import com.newzkl.platform.base.biz.user.infrastructure.dao.UserFollowDAO;
import com.newzkl.platform.base.biz.user.infrastructure.entity.UserFollowDO;
import com.newzkl.platform.base.biz.user.model.relation.query.UserFollowQuery;
import com.newzkl.platform.base.biz.user.model.relation.dto.UserFollowDTO;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户关注仓储实现
 *
 * <p>迁移说明：findFollowingPage/findFollowerPage 保留 Page 分页壳直返，
 * infra 手动 new Page 执行分页。</p>
 *
 * @author sijiwang
 */
@Repository
@RequiredArgsConstructor
public class UserFollowRepositoryImpl implements UserFollowRepository {

    @Resource
    private UserFollowDAO userFollowDAO;

    /**
     * 数据对象转领域实体
     *
     * @param doObj 数据对象
     * @return 领域实体
     */
    private UserFollowDTO toDomain(UserFollowDO doObj) {
        if (doObj == null) {
            return null;
        }
        UserFollowDTO entity = new UserFollowDTO();
        entity.setId(doObj.getId());
        entity.setFollower(doObj.getFollowerId());
        entity.setFollowing(doObj.getFollowingId());
        entity.setCreateTime(doObj.getCreateTime());
        return entity;
    }

    /**
     * 领域实体转数据对象
     *
     * @param entity 领域实体
     * @return 数据对象
     */
    private UserFollowDO toDO(UserFollowDTO entity) {
        UserFollowDO doObj = new UserFollowDO();
        doObj.setId(entity.getId());
        doObj.setFollowerId(entity.getFollower());
        doObj.setFollowingId(entity.getFollowing());
        doObj.setCreateTime(entity.getCreateTime());
        return doObj;
    }

    @Override
    public UserFollowDTO save(UserFollowDTO userFollow) {
        UserFollowDO doObj = toDO(userFollow);
        if (doObj.getCreateTime() == null) {
            doObj.setCreateTime(LocalDateTime.now());
        }
        userFollowDAO.insert(doObj);
        return toDomain(doObj);
    }

    @Override
    public boolean delete(Long follower, Long following) {
        int rows = userFollowDAO.delete(new LambdaQueryWrapper<UserFollowDO>()
                .eq(UserFollowDO::getFollowerId, follower)
                .eq(UserFollowDO::getFollowingId, following));
        return rows > 0;
    }

    @Override
    public boolean isFollowed(Long follower, Long following) {
        Long count = userFollowDAO.selectCount(new LambdaQueryWrapper<UserFollowDO>()
                .eq(UserFollowDO::getFollowerId, follower)
                .eq(UserFollowDO::getFollowingId, following));
        return count != null && count > 0;
    }

    @Override
    public List<UserFollowDTO> findFollowingList(Long follower) {
        List<UserFollowDO> doList = userFollowDAO.selectList(new LambdaQueryWrapper<UserFollowDO>()
                .eq(UserFollowDO::getFollowerId, follower)
                .orderByDesc(UserFollowDO::getId));
        return doList.stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Page<UserFollowDTO> findFollowingPage(UserFollowQuery query) {
        Page<UserFollowDO> page = new Page<>(query.getPageNo(), query.getPageSize());
        Page<UserFollowDO> doPage = userFollowDAO.selectPage(page, new LambdaQueryWrapper<UserFollowDO>()
                .eq(UserFollowDO::getFollowerId, query.getUserId())
                .orderByDesc(UserFollowDO::getId));
        return TransferUtils.transferPage(doPage, this::toDomain);
    }

    @Override
    public List<UserFollowDTO> findFollowerList(Long following) {
        List<UserFollowDO> doList = userFollowDAO.selectList(new LambdaQueryWrapper<UserFollowDO>()
                .eq(UserFollowDO::getFollowingId, following)
                .orderByDesc(UserFollowDO::getId));
        return doList.stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Page<UserFollowDTO> findFollowerPage(UserFollowQuery query) {
        Page<UserFollowDO> page = new Page<>(query.getPageNo(), query.getPageSize());
        Page<UserFollowDO> doPage = userFollowDAO.selectPage(page, new LambdaQueryWrapper<UserFollowDO>()
                .eq(UserFollowDO::getFollowingId, query.getUserId())
                .orderByDesc(UserFollowDO::getId));
        return TransferUtils.transferPage(doPage, this::toDomain);
    }

    @Override
    public int countFollowing(Long follower) {
        Long count = userFollowDAO.selectCount(new LambdaQueryWrapper<UserFollowDO>()
                .eq(UserFollowDO::getFollowerId, follower));
        return count != null ? count.intValue() : 0;
    }

    @Override
    public int countFollower(Long following) {
        Long count = userFollowDAO.selectCount(new LambdaQueryWrapper<UserFollowDO>()
                .eq(UserFollowDO::getFollowingId, following));
        return count != null ? count.intValue() : 0;
    }

    @Override
    public Set<Long> batchIsFollowed(Long followerId, List<Long> followingIds) {
        if (followerId == null || followingIds == null || followingIds.isEmpty()) {
            return new HashSet<>();
        }
        List<UserFollowDO> followList = userFollowDAO.selectList(new LambdaQueryWrapper<UserFollowDO>()
                .eq(UserFollowDO::getFollowerId, followerId)
                .in(UserFollowDO::getFollowingId, followingIds));
        return followList.stream()
                .map(UserFollowDO::getFollowingId)
                .collect(Collectors.toSet());
    }
}
