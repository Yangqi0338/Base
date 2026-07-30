package com.newzkl.platform.base.biz.course.domain.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.LecturerCategoryRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.LecturerRepository;
import com.newzkl.platform.base.biz.course.domain.service.LecturerDomain;
import com.newzkl.platform.base.biz.course.model.lecturer.query.LecturerQuery;
import com.newzkl.platform.base.biz.course.model.lecturer.req.LecturerReq;
import com.newzkl.platform.base.biz.course.model.lecturer.res.LecturerRes;
import com.newzkl.platform.base.biz.course.model.lecturercategory.res.LecturerCategoryRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

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
        return updateEnabled(id, 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disable(Long id) {
        return updateEnabled(id, 0);
    }

    @Override
    public LecturerRes getById(Long id) {
        LecturerRes res = lecturerRepository.detail(id);
        ThrowsException.isNull(res, BaseErrorCode.NODATA, "讲师");
        return res;
    }

    @Override
    public IPage<LecturerRes> pageQuery(LecturerQuery query) {
        return lecturerRepository.pageList(query);
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
    private boolean updateEnabled(Long id, Integer isEnabled) {
        LecturerRes exist = lecturerRepository.detail(id);
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "讲师");
        return lecturerRepository.updateEnabled(id, isEnabled);
    }
}
