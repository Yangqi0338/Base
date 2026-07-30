package com.newzkl.platform.base.biz.course.domain.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseCategoryRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseRepository;
import com.newzkl.platform.base.biz.course.domain.service.CourseCategoryDomain;
import com.newzkl.platform.base.biz.course.model.category.query.CourseCategoryQuery;
import com.newzkl.platform.base.biz.course.model.category.req.CourseCategoryReq;
import com.newzkl.platform.base.biz.course.model.category.res.CourseCategoryRes;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 课程分类领域服务实现
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.service.impl.CourseCategoryDomainServiceImpl}
 * 与应用服务 {@code CourseCategoryServiceImpl} 的合并结果: 旧两层(应用服务做校验 + 领域服务做持久)
 * 在新架构收敛到单一领域服务, 校验与编排均落此。</p>
 *
 * <p>偏离说明: 源 {@code operator} 参数由 controller 硬编码 {@code "system"}, 新架构操作人由
 * {@code BaseDO#executor} 自动填充, 故领域方法不再接收 operator。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class CourseCategoryDomainImpl implements CourseCategoryDomain {

    private final CourseCategoryRepository courseCategoryRepository;

    private final CourseRepository courseRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseCategoryRes add(CourseCategoryReq req) {
        ThrowsException.isTrue(courseCategoryRepository.existsByName(req.getCategoryName(), null),
                BaseErrorCode.EXIST_DATA, "分类名称");
        req.setId(null);
        req.setCategoryCode(BusinessCodeUtil.generate(BusinessType.COURSE_CATEGORY));
        Long id = courseCategoryRepository.save(req);
        return courseCategoryRepository.detail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseCategoryRes edit(CourseCategoryReq req) {
        ThrowsException.isNull(req.getId(), BaseErrorCode.PARAM, "分类主键");
        CourseCategoryRes exist = courseCategoryRepository.detail(req.getId());
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "课程分类");
        ThrowsException.isTrue(courseCategoryRepository.existsByName(req.getCategoryName(), req.getId()),
                BaseErrorCode.EXIST_DATA, "分类名称");
        req.setCategoryCode(exist.getCategoryCode());
        courseCategoryRepository.save(req);
        return courseCategoryRepository.detail(req.getId());
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
    public CourseCategoryRes getById(Long id) {
        CourseCategoryRes res = courseCategoryRepository.detail(id);
        ThrowsException.isNull(res, BaseErrorCode.NODATA, "课程分类");
        return res;
    }

    @Override
    public IPage<CourseCategoryRes> pageQuery(CourseCategoryQuery query) {
        return courseCategoryRepository.pageList(query);
    }

    @Override
    public List<CourseCategoryRes> listAllEnabled() {
        return courseCategoryRepository.listAllEnabled();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return batchDelete(Collections.singletonList(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> idList) {
        ThrowsException.isTrue(idList == null || idList.isEmpty(), BaseErrorCode.PARAM, "分类主键列表");
        for (Long id : idList) {
            CourseCategoryRes exist = courseCategoryRepository.detail(id);
            ThrowsException.isNull(exist, BaseErrorCode.NODATA, "课程分类");
            ThrowsException.isTrue(courseRepository.countByCategoryId(id) > 0,
                    BaseErrorCode.OPERATE_FAIL, "分类下存在课程, 不允许删除");
        }
        return courseCategoryRepository.delete(idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recover(Long id) {
        ThrowsException.isNull(id, BaseErrorCode.PARAM, "分类主键");
        return courseCategoryRepository.recover(id);
    }

    /**
     * 校验存在后更新启用状态
     *
     * @param id        分类主键
     * @param isEnabled 启用状态 1-启用 0-禁用
     * @return 是否成功
     */
    private boolean updateEnabled(Long id, Integer isEnabled) {
        CourseCategoryRes exist = courseCategoryRepository.detail(id);
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "课程分类");
        return courseCategoryRepository.updateEnabled(id, isEnabled);
    }
}
