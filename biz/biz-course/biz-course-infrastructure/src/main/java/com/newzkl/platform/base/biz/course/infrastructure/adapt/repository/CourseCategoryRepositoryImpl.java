package com.newzkl.platform.base.biz.course.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseCategoryRepository;
import com.newzkl.platform.base.biz.course.infrastructure.dao.CourseCategoryDAO;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CourseCategoryDO;
import com.newzkl.platform.base.biz.course.model.category.query.CourseCategoryQuery;
import com.newzkl.platform.base.biz.course.model.category.req.CourseCategoryReq;
import com.newzkl.platform.base.biz.course.model.category.res.CourseCategoryRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程分类仓储实现
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class CourseCategoryRepositoryImpl extends RepositorySupport implements CourseCategoryRepository {

    private final CourseCategoryDAO courseCategoryDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(CourseCategoryReq req) {
        CourseCategoryDO categoryDO = TransferUtils.transfer(req, CourseCategoryDO::new);
        courseCategoryDAO.insertOrUpdate(categoryDO);
        return categoryDO.getId();
    }

    @Override
    public CourseCategoryRes detail(Long id) {
        if (id == null) {
            return null;
        }
        return TransferUtils.transfer(courseCategoryDAO.selectById(id), CourseCategoryRes::new);
    }

    @Override
    public Page<CourseCategoryRes> pageList(CourseCategoryQuery query) {
        Page<CourseCategoryDO> page = courseCategoryDAO.selectPage(RepositorySupport.page(query),
                courseCategoryDAO.getLw(query));
        return TransferUtils.transferPage(page, CourseCategoryRes::new);
    }

    @Override
    public List<CourseCategoryRes> listAllEnabled() {
        BaseLambdaQueryWrapper<CourseCategoryDO> wrapper = new BaseLambdaQueryWrapper<CourseCategoryDO>()
                .notNullEq(CourseCategoryDO::getIsEnabled, 1);
        wrapper.orderByAsc(CourseCategoryDO::getSort).orderByDesc(CourseCategoryDO::getId);
        return TransferUtils.transfers(courseCategoryDAO.selectList(wrapper), CourseCategoryRes::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEnabled(Long id, Integer isEnabled) {
        CourseCategoryDO categoryDO = new CourseCategoryDO();
        categoryDO.setId(id);
        categoryDO.setIsEnabled(isEnabled);
        return courseCategoryDAO.updateById(categoryDO) > 0;
    }

    @Override
    public boolean existsByName(String categoryName, Long excludeId) {
        if (categoryName == null || categoryName.isBlank()) {
            return false;
        }
        BaseLambdaQueryWrapper<CourseCategoryDO> wrapper = new BaseLambdaQueryWrapper<CourseCategoryDO>()
                .notEmptyEq(CourseCategoryDO::getCategoryName, categoryName)
                .notNullNe(CourseCategoryDO::getId, excludeId);
        return courseCategoryDAO.selectCount(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Long> idList) {
        return courseCategoryDAO.deleteByIds(idList) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recover(Long id) {
        return recoverDeleteById(id, CourseCategoryDO.class);
    }
}
