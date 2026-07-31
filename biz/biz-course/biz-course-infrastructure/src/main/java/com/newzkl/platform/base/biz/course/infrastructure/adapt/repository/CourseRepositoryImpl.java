package com.newzkl.platform.base.biz.course.infrastructure.adapt.repository;

import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseRepository;
import com.newzkl.platform.base.biz.course.infrastructure.convert.CourseUnitConverter;
import com.newzkl.platform.base.biz.course.infrastructure.dao.CourseDAO;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CourseDO;
import com.newzkl.platform.base.biz.course.model.course.query.CourseQuery;
import com.newzkl.platform.base.biz.course.model.course.req.CourseDetailReq;
import com.newzkl.platform.base.biz.course.model.course.req.CourseReq;
import com.newzkl.platform.base.biz.course.model.course.res.CourseRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程仓储实现
 *
 * <p>价格入参单位元({@code Double})、库中单位分({@code Long}), 时长冗余字段库中单位百分秒,
 * 换算全部经 {@link CourseUnitConverter} 于读写边界完成。价格字段名在 Req/Res 与 DO 中同名异型,
 * Hutool 会静默按数值直拷造成量级错误, 故一律排除自动拷贝后显式换算填充。</p>
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {

    /**
     * 价格字段名, 同名异型(元 Double / 分 Long), 排除 Hutool 自动拷贝
     */
    private static final String[] PRICE_PROPS = {"originalPrice", "sellPrice"};

    private final CourseDAO courseDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveBase(CourseReq req) {
        CopyOptions opts = CopyOptions.create().setIgnoreProperties(PRICE_PROPS);
        CourseDO courseDO = TransferUtils.transfer(req, CourseDO::new, opts);
        courseDO.setOriginalPrice(CourseUnitConverter.yuanToFen(req.getOriginalPrice()));
        courseDO.setSellPrice(CourseUnitConverter.yuanToFen(req.getSellPrice()));
        courseDAO.insertOrUpdate(courseDO);
        return courseDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveDetail(CourseDetailReq req) {
        CourseDO courseDO = new CourseDO();
        courseDO.setId(req.getId());
        courseDO.setCoverImage(req.getCoverImage());
        courseDO.setCarouselImages(req.getCarouselImages());
        courseDO.setVideoUrl(req.getVideoUrl());
        courseDO.setDetails(req.getDetails());
        return courseDAO.updateById(courseDO) > 0;
    }

    @Override
    public CourseRes detail(Long id) {
        if (id == null) {
            return null;
        }
        return toRes(courseDAO.selectById(id));
    }

    @Override
    public CourseRes getByCourseNum(String courseNum) {
        if (courseNum == null || courseNum.isBlank()) {
            return null;
        }
        BaseLambdaQueryWrapper<CourseDO> wrapper = new BaseLambdaQueryWrapper<CourseDO>()
                .notEmptyEq(CourseDO::getCourseNum, courseNum);
        return toRes(courseDAO.selectOne(wrapper.last("limit 1")));
    }

    @Override
    public Page<CourseRes> pageList(CourseQuery query) {
        Page<CourseDO> page = courseDAO.selectPage(RepositorySupport.page(query), courseDAO.getLw(query));
        return TransferUtils.transferPage(page, this::toRes);
    }

    @Override
    public List<CourseRes> listByCategoryId(Long categoryId) {
        BaseLambdaQueryWrapper<CourseDO> wrapper = new BaseLambdaQueryWrapper<CourseDO>()
                .notNullEq(CourseDO::getCategoryId, categoryId)
                .notNullEq(CourseDO::getIsEnabled, 1);
        wrapper.orderByDesc(CourseDO::getId);
        return TransferUtils.transfers(courseDAO.selectList(wrapper), this::toRes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEnabled(Long id, Integer isEnabled) {
        CourseDO courseDO = new CourseDO();
        courseDO.setId(id);
        courseDO.setIsEnabled(isEnabled);
        return courseDAO.updateById(courseDO) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPurchaseCount(Long id) {
        return courseDAO.addPurchaseCount(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateChapterStat(Long id, Integer chapterCount, Long totalDurationCentisecond) {
        CourseDO courseDO = new CourseDO();
        courseDO.setId(id);
        courseDO.setChapterCount(chapterCount);
        courseDO.setTotalDurationCentisecond(totalDurationCentisecond);
        courseDO.setTotalDurationDesc(CourseUnitConverter.formatCentisecond(totalDurationCentisecond));
        return courseDAO.updateById(courseDO) > 0;
    }

    @Override
    public long countByCategoryId(Long categoryId) {
        BaseLambdaQueryWrapper<CourseDO> wrapper = new BaseLambdaQueryWrapper<CourseDO>()
                .notNullEq(CourseDO::getCategoryId, categoryId);
        return courseDAO.selectCount(wrapper);
    }

    @Override
    public long countByLecturerId(Long lecturerId) {
        BaseLambdaQueryWrapper<CourseDO> wrapper = new BaseLambdaQueryWrapper<CourseDO>()
                .notNullEq(CourseDO::getLecturerId, lecturerId);
        return courseDAO.selectCount(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Long> idList) {
        return courseDAO.deleteByIds(idList) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recover(Long id) {
        return courseDAO.recoverById(id) > 0;
    }

    /**
     * DO 转 Res, 分转元、百分秒转秒、启用描述在此填充
     *
     * @param courseDO 课程数据对象
     * @return 课程视图, 入参为空返回 null
     */
    private CourseRes toRes(CourseDO courseDO) {
        if (courseDO == null) {
            return null;
        }
        CopyOptions opts = CopyOptions.create().setIgnoreProperties(PRICE_PROPS);
        CourseRes res = TransferUtils.transfer(courseDO, CourseRes::new, opts);
        res.setOriginalPrice(CourseUnitConverter.fenToYuan(courseDO.getOriginalPrice()));
        res.setSellPrice(CourseUnitConverter.fenToYuan(courseDO.getSellPrice()));
        res.setTotalDurationSeconds(CourseUnitConverter.centisecondToSeconds(courseDO.getTotalDurationCentisecond()));
        res.setIsEnabledDesc(toEnabledDesc(courseDO.getIsEnabled()));
        return res;
    }

    /**
     * 启用状态转描述
     *
     * @param isEnabled 启用状态
     * @return 描述文案
     */
    private String toEnabledDesc(Integer isEnabled) {
        return isEnabled != null && isEnabled == 1 ? "启用" : "禁用";
    }
}
