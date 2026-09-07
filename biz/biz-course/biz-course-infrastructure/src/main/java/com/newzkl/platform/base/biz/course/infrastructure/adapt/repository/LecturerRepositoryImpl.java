package com.newzkl.platform.base.biz.course.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.LecturerRepository;
import com.newzkl.platform.base.biz.course.infrastructure.dao.LecturerDAO;
import com.newzkl.platform.base.biz.course.infrastructure.entity.LecturerDO;
import com.newzkl.platform.base.biz.course.model.lecturer.query.LecturerQuery;
import com.newzkl.platform.base.biz.course.model.lecturer.req.LecturerReq;
import com.newzkl.platform.base.biz.course.model.lecturer.res.LecturerRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 讲师仓储实现
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class LecturerRepositoryImpl implements LecturerRepository {

    private final LecturerDAO lecturerDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(LecturerReq req) {
        LecturerDO lecturerDO = TransferUtils.transfer(req, LecturerDO::new);
        lecturerDAO.insertOrUpdate(lecturerDO);
        return lecturerDO.getId();
    }

    @Override
    public LecturerRes detail(Long id) {
        if (id == null) {
            return null;
        }
        return TransferUtils.transfer(lecturerDAO.selectById(id), LecturerRes::new);
    }

    @Override
    public Page<LecturerRes> pageList(LecturerQuery query) {
        Page<LecturerDO> page = lecturerDAO.selectPage(RepositorySupport.page(query), lecturerDAO.getLw(query));
        return TransferUtils.transferPage(page, LecturerRes::new);
    }

    @Override
    public List<LecturerRes> listAllEnabled() {
        BaseLambdaQueryWrapper<LecturerDO> wrapper = new BaseLambdaQueryWrapper<LecturerDO>()
                .notNullEq(LecturerDO::getIsEnabled, 1);
        wrapper.orderByDesc(LecturerDO::getId);
        List<LecturerRes> list = TransferUtils.transfers(lecturerDAO.selectList(wrapper), LecturerRes::new);;
        return list;
    }

    @Override
    public LecturerRes getByMainAccountId(Long mainAccountId) {
        if (mainAccountId == null) {
            return null;
        }
        BaseLambdaQueryWrapper<LecturerDO> wrapper = new BaseLambdaQueryWrapper<LecturerDO>()
                .notNullEq(LecturerDO::getMainAccountId, mainAccountId);
        wrapper.last("LIMIT 1");
        return TransferUtils.transfer(lecturerDAO.selectOne(wrapper), LecturerRes::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEnabled(Long id, CommonEnum.YesOrNo isEnabled) {
        LecturerDO lecturerDO = new LecturerDO();
        lecturerDO.setId(id);
        lecturerDO.setIsEnabled(isEnabled);
        return lecturerDAO.updateById(lecturerDO) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCategoryName(Long id, String lecturerCategoryName) {
        LecturerDO lecturerDO = new LecturerDO();
        lecturerDO.setId(id);
        lecturerDO.setLecturerCategoryName(lecturerCategoryName);
        return lecturerDAO.updateById(lecturerDO) > 0;
    }

    @Override
    public boolean existsByMainAccountId(Long mainAccountId, Long excludeId) {
        if (mainAccountId == null) {
            return false;
        }
        BaseLambdaQueryWrapper<LecturerDO> wrapper = new BaseLambdaQueryWrapper<LecturerDO>()
                .notNullEq(LecturerDO::getMainAccountId, mainAccountId)
                .notNullNe(LecturerDO::getId, excludeId);
        return lecturerDAO.selectCount(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFollowCount(Long id, int delta) {
        return lecturerDAO.addFollowCount(id, delta) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Long> idList) {
        return lecturerDAO.deleteByIds(idList) > 0;
    }
}
