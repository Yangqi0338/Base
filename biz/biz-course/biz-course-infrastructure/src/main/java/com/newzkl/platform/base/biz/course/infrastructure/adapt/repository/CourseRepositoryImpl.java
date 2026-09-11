package com.newzkl.platform.base.biz.course.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseRepository;
import com.newzkl.platform.base.biz.course.infrastructure.convert.CourseUnitConverter;
import com.newzkl.platform.base.biz.course.infrastructure.dao.CourseDAO;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CourseDO;
import com.newzkl.platform.base.biz.course.model.course.query.CourseQuery;
import com.newzkl.platform.base.biz.course.model.course.req.CourseDetailReq;
import com.newzkl.platform.base.biz.course.model.course.req.CourseReq;
import com.newzkl.platform.base.biz.course.model.course.res.CourseRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.core.mybatis.support.RepositorySupport;
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
public class CourseRepositoryImpl extends RepositorySupport implements CourseRepository {

    private final CourseDAO courseDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveBase(CourseReq req) {
        // 价格已 Money↔Money 同名同型, TransferUtils 直拷, 无需排除+手工换算
        CourseDO courseDO = TransferUtils.transfer(req, CourseDO::new);
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
    public CourseRes getByCourseNo(String courseNo) {
        if (courseNo == null || courseNo.isBlank()) {
            return null;
        }
        BaseLambdaQueryWrapper<CourseDO> wrapper = new BaseLambdaQueryWrapper<CourseDO>()
                .notEmptyEq(CourseDO::getCourseNo, courseNo);
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
                .notNullEq(CourseDO::getIsEnabled, CommonEnum.YesOrNo.YES);
        wrapper.orderByDesc(CourseDO::getId);
        return TransferUtils.transfers(courseDAO.selectList(wrapper), this::toRes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEnabled(Long id, CommonEnum.YesOrNo isEnabled) {
        CourseDO courseDO = new CourseDO();
        courseDO.setId(id);
        courseDO.setIsEnabled(isEnabled);
        return courseDAO.updateById(courseDO) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPurchaseCount(Long id) {
        // TODO 空值可能+1会报错
        return courseDAO.update(new LambdaUpdateWrapper<CourseDO>().eq(CourseDO::getId, id)
                .setIncrBy(CourseDO::getPurchaseCount, 1)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateChapterStat(Long id, Integer chapterCount, Long totalDurationCentisecond) {
        CourseDO courseDO = new CourseDO();
        courseDO.setId(id);
        courseDO.setChapterCount(chapterCount);
        courseDO.setTotalDurationCentisecond(totalDurationCentisecond);
        return courseDAO.updateById(courseDO) > 0;
    }

    @Override
    public List<Long> listIdsByTitleAndCategory(String title, Long categoryId) {
        CourseQuery query = new CourseQuery();
        query.setCategoryId(categoryId);
        query.setTitle(title);
        return listOneField(courseDAO, courseDAO.getLw(query), CourseDO::getId);
    }

    @Override
    public long countByCategoryId(Long categoryId) {
        CourseQuery query = new CourseQuery();
        query.setCategoryId(categoryId);
        return courseDAO.selectCount(courseDAO.getLw(query));
    }

    @Override
    public long countByLecturerId(Long lecturerId) {
        CourseQuery query = new CourseQuery();
        query.setLecturerId(lecturerId);
        return courseDAO.selectCount(courseDAO.getLw(query));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Long> idList) {
        return courseDAO.deleteByIds(idList) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recover(Long id) {
        return recoverDeleteById(id,CourseDO.class);
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
        // 价格 Money→Money 直拷; 仅时长(百分秒→秒)仍需换算
        CourseRes res = TransferUtils.transfer(courseDO, CourseRes::new);
        res.setTotalDurationSeconds(CourseUnitConverter.centisecondToSeconds(courseDO.getTotalDurationCentisecond()));
        return res;
    }
}
