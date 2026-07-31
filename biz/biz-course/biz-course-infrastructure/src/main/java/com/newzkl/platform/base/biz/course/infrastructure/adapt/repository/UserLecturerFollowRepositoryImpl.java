package com.newzkl.platform.base.biz.course.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.UserLecturerFollowRepository;
import com.newzkl.platform.base.biz.course.infrastructure.dao.UserLecturerFollowDAO;
import com.newzkl.platform.base.biz.course.infrastructure.entity.UserLecturerFollowDO;
import com.newzkl.platform.base.biz.course.model.follow.query.UserFollowQuery;
import com.newzkl.platform.base.biz.course.model.follow.req.UserFollowReq;
import com.newzkl.platform.base.biz.course.model.follow.res.UserFollowRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 用户关注讲师仓储实现
 *
 * <p>取关即逻辑删除关注记录, {@code exists} 与分页天然只见在关注状态的记录(MyBatis-Plus 默认过滤已删)。
 * 关注记录主键沿用旧字段名 {@code followId}, DO 主键 {@code id} 经 biConsumer 映射, 讲师信息在领域服务拼装。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class UserLecturerFollowRepositoryImpl implements UserLecturerFollowRepository {

    private final UserLecturerFollowDAO userLecturerFollowDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(UserFollowReq req) {
        UserLecturerFollowDO followDO = new UserLecturerFollowDO();
        followDO.setUserId(req.getUserId());
        followDO.setLecturerId(req.getLecturerId());
        followDO.setFollowTime(LocalDateTime.now());
        userLecturerFollowDAO.insert(followDO);
        return followDO.getId();
    }

    @Override
    public boolean exists(Long userId, Long lecturerId) {
        if (userId == null || lecturerId == null) {
            return false;
        }
        BaseLambdaQueryWrapper<UserLecturerFollowDO> wrapper = new BaseLambdaQueryWrapper<UserLecturerFollowDO>()
                .notNullEq(UserLecturerFollowDO::getUserId, userId)
                .notNullEq(UserLecturerFollowDO::getLecturerId, lecturerId);
        return userLecturerFollowDAO.selectCount(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(Long userId, Long lecturerId) {
        if (userId == null || lecturerId == null) {
            return false;
        }
        BaseLambdaQueryWrapper<UserLecturerFollowDO> wrapper = new BaseLambdaQueryWrapper<UserLecturerFollowDO>()
                .notNullEq(UserLecturerFollowDO::getUserId, userId)
                .notNullEq(UserLecturerFollowDO::getLecturerId, lecturerId);
        return userLecturerFollowDAO.delete(wrapper) > 0;
    }

    @Override
    public Page<UserFollowRes> pageList(UserFollowQuery query) {
        Page<UserLecturerFollowDO> page = userLecturerFollowDAO.selectPage(RepositorySupport.page(query),
                userLecturerFollowDAO.getLw(query));
        return TransferUtils.transferPage(page, UserFollowRes::new, (followDO, res) -> {
            res.setFollowId(followDO.getId());
            res.setUserId(followDO.getUserId());
            res.setLecturerId(followDO.getLecturerId());
            res.setFollowTime(followDO.getFollowTime());
        });
    }

    @Override
    public List<Long> listFollowedLecturerIds(Long userId, List<Long> lecturerIdList) {
        if (userId == null || lecturerIdList == null || lecturerIdList.isEmpty()) {
            return Collections.emptyList();
        }
        return userLecturerFollowDAO.listFollowedLecturerIds(userId, lecturerIdList);
    }
}
