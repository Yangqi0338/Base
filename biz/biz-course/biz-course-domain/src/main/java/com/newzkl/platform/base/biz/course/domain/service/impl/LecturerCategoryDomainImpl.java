package com.newzkl.platform.base.biz.course.domain.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.LecturerCategoryRepository;
import com.newzkl.platform.base.biz.course.domain.service.LecturerCategoryDomain;
import com.newzkl.platform.base.biz.course.model.lecturercategory.query.LecturerCategoryQuery;
import com.newzkl.platform.base.biz.course.model.lecturercategory.req.LecturerCategoryReq;
import com.newzkl.platform.base.biz.course.model.lecturercategory.res.LecturerCategoryRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 讲师分类领域服务实现
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.lecturer.service.impl.LecturerCategoryDomainServiceImpl}
 * 与应用服务 {@code LecturerCategoryServiceImpl} 的合并结果。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class LecturerCategoryDomainImpl implements LecturerCategoryDomain {

    private final LecturerCategoryRepository lecturerCategoryRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LecturerCategoryRes add(LecturerCategoryReq req) {
        ThrowsException.isTrue(lecturerCategoryRepository.existsByName(req.getCategoryName(), null),
                BaseErrorCode.EXIST_DATA, "讲师分类名称");
        req.setId(null);
        Long id = lecturerCategoryRepository.save(req);
        return lecturerCategoryRepository.detail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LecturerCategoryRes edit(LecturerCategoryReq req) {
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
    public boolean enable(Long id) {
        return updateEnabled(id, 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disable(Long id) {
        return updateEnabled(id, 0);
    }

    @Override
    public LecturerCategoryRes getById(Long id) {
        LecturerCategoryRes res = lecturerCategoryRepository.detail(id);
        ThrowsException.isNull(res, BaseErrorCode.NODATA, "讲师分类");
        return res;
    }

    @Override
    public IPage<LecturerCategoryRes> pageQuery(LecturerCategoryQuery query) {
        return lecturerCategoryRepository.pageList(query);
    }

    @Override
    public List<LecturerCategoryRes> listAllEnabled() {
        return lecturerCategoryRepository.listAllEnabled();
    }

    /**
     * 校验存在后更新启用状态
     *
     * @param id        分类主键
     * @param isEnabled 启用状态 1-启用 0-禁用
     * @return 是否成功
     */
    private boolean updateEnabled(Long id, Integer isEnabled) {
        LecturerCategoryRes exist = lecturerCategoryRepository.detail(id);
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "讲师分类");
        return lecturerCategoryRepository.updateEnabled(id, isEnabled);
    }
}
