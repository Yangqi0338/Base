package com.newzkl.platform.base.biz.course.domain.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.adapt.api.UserAccountApi;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.LecturerRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.UserLecturerFollowRepository;
import com.newzkl.platform.base.biz.course.domain.service.UserLecturerFollowDomain;
import com.newzkl.platform.base.biz.course.model.follow.query.UserFollowQuery;
import com.newzkl.platform.base.biz.course.model.follow.req.UserFollowReq;
import com.newzkl.platform.base.biz.course.model.follow.res.UserFollowRes;
import com.newzkl.platform.base.biz.course.model.lecturer.res.LecturerRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户关注讲师领域服务实现
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.lecturer.service.impl.UserLecturerFollowDomainServiceImpl}
 * 与应用服务 {@code UserLecturerFollowServiceImpl} 的合并结果。</p>
 *
 * <p>关注/取关同时维护讲师侧 {@code followCount} 累计值, 与旧实现一致。
 * 列表的讲师展示字段由讲师仓储逐条回读拼装(旧实现走 mapper XML 联表, 新架构避免自建 XML)。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class UserLecturerFollowDomainImpl implements UserLecturerFollowDomain {

    private final UserLecturerFollowRepository userLecturerFollowRepository;

    private final LecturerRepository lecturerRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean follow(UserFollowReq req) {
        LecturerRes lecturer = lecturerRepository.detail(req.getLecturerId());
        ThrowsException.isNull(lecturer, BaseErrorCode.NODATA, "讲师");
        if (userLecturerFollowRepository.exists(req.getUserId(), req.getLecturerId())) {
            return true;
        }
        userLecturerFollowRepository.save(req);
        lecturerRepository.addFollowCount(req.getLecturerId(), 1);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelFollow(UserFollowReq req) {
        Long userId = resolveUserId(req);
        if (!userLecturerFollowRepository.exists(userId, req.getLecturerId())) {
            return true;
        }
        boolean cancelled = userLecturerFollowRepository.cancel(userId, req.getLecturerId());
        if (cancelled) {
            lecturerRepository.addFollowCount(req.getLecturerId(), -1);
        }
        return cancelled;
    }

    @Override
    public IPage<UserFollowRes> pageQueryFollowList(UserFollowQuery query) {
        ThrowsException.isNull(query.getUserId(), BaseErrorCode.USER_NOT_LOGIN, "关注列表");
        Page<UserFollowRes> page = userLecturerFollowRepository.pageList(query);
        fillLecturer(page.getRecords());
        return page;
    }

    /**
     * 回填讲师展示字段
     *
     * @param records 关注记录列表
     */
    private void fillLecturer(List<UserFollowRes> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        for (UserFollowRes item : records) {
            LecturerRes lecturer = lecturerRepository.detail(item.getLecturerId());
            if (lecturer == null) {
                continue;
            }
            item.setLecturerName(lecturer.getLecturerName());
            item.setMainAccount(lecturer.getMainAccount());
            item.setMainAccountId(lecturer.getMainAccountId());
            item.setCourseCount(lecturer.getCourseCount());
            item.setFollowCount(lecturer.getFollowCount());
            item.setPersonalIntro(lecturer.getPersonalIntro());
            item.setCoverImageUrl(lecturer.getCoverImageUrl());
            item.setAvatarUrl(lecturer.getAvatarUrl());
            item.setCategoryId(lecturer.getLecturerCategoryId() == null
                    ? null : String.valueOf(lecturer.getLecturerCategoryId()));
            item.setCategoryName(lecturer.getLecturerCategoryName());
        }
    }

    /**
     * 解析关注操作的用户ID, 缺省取当前登录用户
     *
     * @param req 关注请求
     * @return 用户ID
     */
    private Long resolveUserId(UserFollowReq req) {
        ThrowsException.isNull(req.getLecturerId(), BaseErrorCode.PARAM, "讲师ID");
        return req.getUserId();
    }
}
