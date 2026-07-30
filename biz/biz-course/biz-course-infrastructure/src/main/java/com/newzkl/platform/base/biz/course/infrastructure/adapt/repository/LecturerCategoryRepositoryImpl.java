package com.newzkl.platform.base.biz.course.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.LecturerCategoryRepository;
import com.newzkl.platform.base.biz.course.infrastructure.dao.LecturerCategoryDAO;
import com.newzkl.platform.base.biz.course.infrastructure.entity.LecturerCategoryDO;
import com.newzkl.platform.base.biz.course.model.lecturercategory.query.LecturerCategoryQuery;
import com.newzkl.platform.base.biz.course.model.lecturercategory.req.LecturerCategoryReq;
import com.newzkl.platform.base.biz.course.model.lecturercategory.res.LecturerCategoryRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 讲师分类仓储实现
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class LecturerCategoryRepositoryImpl implements LecturerCategoryRepository {

    private final LecturerCategoryDAO lecturerCategoryDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(LecturerCategoryReq req) {
        LecturerCategoryDO categoryDO = TransferUtils.transfer(req, LecturerCategoryDO::new);
        lecturerCategoryDAO.insertOrUpdate(categoryDO);
        return categoryDO.getId();
    }

    @Override
    public LecturerCategoryRes detail(Long id) {
        if (id == null) {
            return null;
        }
        return TransferUtils.transfer(lecturerCategoryDAO.selectById(id), LecturerCategoryRes::new);
    }

    @Override
    public Page<LecturerCategoryRes> pageList(LecturerCategoryQuery query) {
        Page<LecturerCategoryDO> page = lecturerCategoryDAO.selectPage(RepositorySupport.page(query),
                lecturerCategoryDAO.getLw(query));
        return TransferUtils.transferPage(page, LecturerCategoryRes::new);
    }

    @Override
    public List<LecturerCategoryRes> listAllEnabled() {
        BaseLambdaQueryWrapper<LecturerCategoryDO> wrapper = new BaseLambdaQueryWrapper<LecturerCategoryDO>()
                .notNullEq(LecturerCategoryDO::getIsEnabled, 1);
        wrapper.orderByDesc(LecturerCategoryDO::getId);
        return TransferUtils.transfers(lecturerCategoryDAO.selectList(wrapper), LecturerCategoryRes::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEnabled(Long id, Integer isEnabled) {
        LecturerCategoryDO categoryDO = new LecturerCategoryDO();
        categoryDO.setId(id);
        categoryDO.setIsEnabled(isEnabled);
        return lecturerCategoryDAO.updateById(categoryDO) > 0;
    }

    @Override
    public boolean existsByName(String categoryName, Long excludeId) {
        if (categoryName == null || categoryName.isBlank()) {
            return false;
        }
        BaseLambdaQueryWrapper<LecturerCategoryDO> wrapper = new BaseLambdaQueryWrapper<LecturerCategoryDO>()
                .notEmptyEq(LecturerCategoryDO::getCategoryName, categoryName)
                .notNullNe(LecturerCategoryDO::getId, excludeId);
        return lecturerCategoryDAO.selectCount(wrapper) > 0;
    }
}
