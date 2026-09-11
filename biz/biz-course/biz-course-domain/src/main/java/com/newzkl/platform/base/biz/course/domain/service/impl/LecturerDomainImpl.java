package com.newzkl.platform.base.biz.course.domain.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.LecturerCategoryRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.LecturerRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.UserLecturerFollowRepository;
import com.newzkl.platform.base.biz.course.domain.service.LecturerDomain;
import com.newzkl.platform.base.biz.course.model.lecturer.query.LecturerQuery;
import com.newzkl.platform.base.biz.course.model.lecturer.req.LecturerReq;
import com.newzkl.platform.base.biz.course.model.lecturer.res.LecturerRes;
import com.newzkl.platform.base.biz.course.model.lecturercategory.query.LecturerCategoryQuery;
import com.newzkl.platform.base.biz.course.model.lecturercategory.req.LecturerCategoryReq;
import com.newzkl.platform.base.biz.course.model.lecturercategory.res.LecturerCategoryRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 讲师领域服务实现
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.lecturer.service.impl.LecturerDomainServiceImpl}
 * 与应用服务 {@code LecturerServiceImpl} 的合并结果。</p>
 *
 * <p>{@code lecturerCategoryName} 为冗余展示字段, 由讲师分类回读后落库, 与旧实现一致。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class LecturerDomainImpl implements LecturerDomain {

    private final LecturerRepository lecturerRepository;
    private final LecturerCategoryRepository lecturerCategoryRepository;
    private final CourseRepository courseRepository;
    private final UserLecturerFollowRepository userLecturerFollowRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LecturerRes add(LecturerReq req) {
        ThrowsException.isTrue(lecturerRepository.existsByMainAccountId(req.getMainAccountId(), null),
                BaseErrorCode.EXIST_DATA, "该主体账号的讲师");
        req.setId(null);
        Long id = lecturerRepository.save(req);
        fillCategoryName(id, req.getLecturerCategoryId());
        return lecturerRepository.detail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LecturerRes edit(LecturerReq req) {
        ThrowsException.isNull(req.getId(), BaseErrorCode.PARAM, "讲师主键");
        LecturerRes exist = lecturerRepository.detail(req.getId());
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "讲师");
        ThrowsException.isTrue(lecturerRepository.existsByMainAccountId(req.getMainAccountId(), req.getId()),
                BaseErrorCode.EXIST_DATA, "该主体账号的讲师");
        lecturerRepository.save(req);
        fillCategoryName(req.getId(), req.getLecturerCategoryId());
        return lecturerRepository.detail(req.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enable(Long id) {
        return updateEnabled(id, CommonEnum.YesOrNo.YES);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disable(Long id) {
        return updateEnabled(id, CommonEnum.YesOrNo.NO);
    }

    @Override
    public LecturerRes getById(Long id) {
        LecturerRes res = lecturerRepository.detail(id);
        ThrowsException.isNull(res, BaseErrorCode.NODATA, "讲师");
        return res;
    }

    @Override
    public Page<LecturerRes> pageQuery(LecturerQuery query) {
        Page<LecturerRes> pageList = lecturerRepository.pageList(query);
        List<LecturerRes> lecturerRecords = pageList.getRecords();

        Long userId = SecurityUtils.getAccountId();
        List<Long> ids = lecturerRecords.stream().map(LecturerRes::getId).collect(Collectors.toList());
        List<Long> lecturerIds = userLecturerFollowRepository.listFollowedLecturerIds(userId, ids);
        lecturerRecords.forEach(lecturer -> {
            if (lecturerIds.contains(lecturer.getId())){
                lecturer.setIsFollow(true);
            }
        });

        return pageList;
    }

    @Override
    public List<LecturerRes> listAllEnabled() {
        return lecturerRepository.listAllEnabled();
    }

    @Override
    public LecturerRes getByMainAccountId(Long mainAccountId) {
        ThrowsException.isNull(mainAccountId, BaseErrorCode.PARAM, "主体账号ID");
        return lecturerRepository.getByMainAccountId(mainAccountId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        LecturerRes exist = lecturerRepository.detail(id);
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "讲师");
        ThrowsException.isTrue(courseRepository.countByLecturerId(id) > 0,
                BaseErrorCode.OPERATE_FAIL, "讲师下存在课程, 不允许删除");
        return lecturerRepository.delete(Collections.singletonList(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFollowCount(Long id, int delta) {
        return lecturerRepository.addFollowCount(id, delta);
    }

    /**
     * 回填讲师分类名称冗余字段
     *
     * @param lecturerId         讲师主键
     * @param lecturerCategoryId 讲师分类主键
     */
    private void fillCategoryName(Long lecturerId, Long lecturerCategoryId) {
        if (lecturerId == null || lecturerCategoryId == null) {
            return;
        }
        LecturerCategoryRes category = lecturerCategoryRepository.detail(lecturerCategoryId);
        ThrowsException.isNull(category, BaseErrorCode.NODATA, "讲师分类");
        lecturerRepository.updateCategoryName(lecturerId, category.getCategoryName());
    }

    /**
     * 校验存在后更新启用状态
     *
     * @param id        讲师主键
     * @param isEnabled 启用状态 1-启用 0-禁用
     * @return 是否成功
     */
    private boolean updateEnabled(Long id, CommonEnum.YesOrNo isEnabled) {
        LecturerRes exist = lecturerRepository.detail(id);
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "讲师");
        return lecturerRepository.updateEnabled(id, isEnabled);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LecturerCategoryRes addCategory(LecturerCategoryReq req) {
        ThrowsException.isTrue(lecturerCategoryRepository.existsByName(req.getCategoryName(), null),
                BaseErrorCode.EXIST_DATA, "讲师分类名称");
        req.setId(null);
        Long id = lecturerCategoryRepository.save(req);
        return lecturerCategoryRepository.detail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LecturerCategoryRes editCategory(LecturerCategoryReq req) {
        ThrowsException.isNull(req.getId(), BaseErrorCode.PARAM, "分类主键");
        LecturerCategoryRes exist = lecturerCategoryRepository.detail(req.getId());
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "讲师分类");
        ThrowsException.isTrue(lecturerCategoryRepository.existsByName(req.getCategoryName(), req.getId()),
                BaseErrorCode.EXIST_DATA, "讲师分类名称");
        lecturerCategoryRepository.save(req);
        return lecturerCategoryRepository.detail(req.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableCategory(Long id) {
        return updateEnabled(id, CommonEnum.YesOrNo.YES);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableCategory(Long id) {
        return updateEnabled(id, CommonEnum.YesOrNo.NO);
    }

    @Override
    public LecturerCategoryRes getCategoryById(Long id) {
        LecturerCategoryRes res = lecturerCategoryRepository.detail(id);
        ThrowsException.isNull(res, BaseErrorCode.NODATA, "讲师分类");
        return res;
    }

    @Override
    public IPage<LecturerCategoryRes> pageCategoryQuery(LecturerCategoryQuery query) {
        return lecturerCategoryRepository.pageList(query);
    }

    @Override
    public List<LecturerCategoryRes> listAllCategoryEnabled() {
        return lecturerCategoryRepository.listAllEnabled();
    }

    /**
     * 校验存在后更新启用状态
     *
     * @param id        分类主键
     * @param isEnabled 启用状态 1-启用 0-禁用
     * @return 是否成功
     */
    private boolean updateCategoryEnabled(Long id, CommonEnum.YesOrNo isEnabled) {
        LecturerCategoryRes exist = lecturerCategoryRepository.detail(id);
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "讲师分类");
        return lecturerCategoryRepository.updateEnabled(id, isEnabled);
    }
}
