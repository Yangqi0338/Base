package com.newzkl.platform.base.biz.course.domain.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.adapt.api.UserAccountApi;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseCategoryRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseChapterRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseChapterWatchRecordRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseRepository;
import com.newzkl.platform.base.biz.course.domain.service.CourseDomain;
import com.newzkl.platform.base.biz.course.model.category.res.CourseCategoryRes;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterStatRes;
import com.newzkl.platform.base.biz.course.model.course.query.CourseQuery;
import com.newzkl.platform.base.biz.course.model.course.req.CourseDetailReq;
import com.newzkl.platform.base.biz.course.model.course.req.CourseReq;
import com.newzkl.platform.base.biz.course.model.course.res.AppCourseRes;
import com.newzkl.platform.base.biz.course.model.course.res.CourseRes;
import com.newzkl.platform.base.biz.course.model.watch.res.CourseWatchStatisticRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.core.utils.generator.BusinessCodeUtil;
import com.newzkl.platform.base.common.core.utils.generator.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程领域服务实现
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.service.impl.CourseDomainServiceImpl}
 * 与应用服务 {@code CourseServiceImpl} 的合并结果。</p>
 *
 * <p>价格入参单位元, 由仓储层换算为分落库; 章节统计冗余字段由 {@link CourseDomainImpl#refreshChapterStat}
 * 在章节变更后回填, 与旧实现一致。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class CourseDomainImpl implements CourseDomain {

    private final CourseRepository courseRepository;

    private final CourseCategoryRepository courseCategoryRepository;

    private final CourseChapterRepository courseChapterRepository;

    private final CourseChapterWatchRecordRepository courseChapterWatchRecordRepository;

    private final UserAccountApi userAccountApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseRes add(CourseReq req) {
        checkCategory(req.getCategoryId());
        req.setId(null);
        req.setCourseNum(BusinessCodeUtil.generate(BusinessType.COURSE));
        Long id = courseRepository.saveBase(req);
        return courseRepository.detail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseRes editBase(CourseReq req) {
        ThrowsException.isNull(req.getId(), BaseErrorCode.PARAM, "课程主键");
        CourseRes exist = courseRepository.detail(req.getId());
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "课程");
        checkCategory(req.getCategoryId());
        req.setCourseNum(exist.getCourseNum());
        courseRepository.saveBase(req);
        return courseRepository.detail(req.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseRes editDetail(CourseDetailReq req) {
        ThrowsException.isNull(req.getId(), BaseErrorCode.PARAM, "课程主键");
        CourseRes exist = courseRepository.detail(req.getId());
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "课程");
        courseRepository.saveDetail(req);
        return courseRepository.detail(req.getId());
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
    public CourseRes getById(Long id) {
        CourseRes res = courseRepository.detail(id);
        ThrowsException.isNull(res, BaseErrorCode.NODATA, "课程");
        return res;
    }

    @Override
    public CourseRes getByCourseNum(String courseNum) {
        ThrowsException.isBlank(courseNum, "课程编码");
        CourseRes res = courseRepository.getByCourseNum(courseNum);
        ThrowsException.isNull(res, BaseErrorCode.NODATA, "课程");
        return res;
    }

    @Override
    public IPage<CourseRes> pageQuery(CourseQuery query) {
        return courseRepository.pageList(query);
    }

    @Override
    public IPage<AppCourseRes> appPageQuery(CourseQuery query) {
        Page<CourseRes> source = courseRepository.pageList(query);
        Page<AppCourseRes> target = new Page<>(source.getCurrent(), source.getSize(), source.getTotal());
        List<AppCourseRes> records = TransferUtils.transfers(source.getRecords(), AppCourseRes::new);
        target.setRecords(fillWatchCount(records));
        return target;
    }

    @Override
    public List<CourseRes> listByCategoryId(Long categoryId) {
        ThrowsException.isNull(categoryId, BaseErrorCode.PARAM, "课程分类主键");
        return courseRepository.listByCategoryId(categoryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return batchDelete(Collections.singletonList(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> idList) {
        ThrowsException.isTrue(idList == null || idList.isEmpty(), BaseErrorCode.PARAM, "课程主键列表");
        for (Long id : idList) {
            ThrowsException.isNull(courseRepository.detail(id), BaseErrorCode.NODATA, "课程");
        }
        return courseRepository.delete(idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recover(Long id) {
        ThrowsException.isNull(id, BaseErrorCode.PARAM, "课程主键");
        return courseRepository.recover(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPurchaseCount(Long courseId) {
        ThrowsException.isNull(courseId, BaseErrorCode.PARAM, "课程主键");
        return courseRepository.addPurchaseCount(courseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refreshChapterStat(Long courseId) {
        if (courseId == null) {
            return false;
        }
        List<CourseChapterStatRes> statList =
                courseChapterRepository.batchStat(Collections.singletonList(courseId));
        int chapterCount = 0;
        long totalDuration = 0L;
        if (!statList.isEmpty()) {
            CourseChapterStatRes stat = statList.get(0);
            chapterCount = stat.getChapterCount() == null ? 0 : stat.getChapterCount();
            totalDuration = stat.getTotalDurationCentisecond() == null
                    ? 0L : stat.getTotalDurationCentisecond();
        }
        return courseRepository.updateChapterStat(courseId, chapterCount, totalDuration);
    }

    /**
     * 回填 C 端列表的已观看章节数
     *
     * <p>当前用户取不到时(未登录/兜底实现)统一回填 0, 不阻断列表查询。</p>
     *
     * @param records C 端课程列表
     * @return 回填后的课程列表
     */
    private List<AppCourseRes> fillWatchCount(List<AppCourseRes> records) {
        if (records == null || records.isEmpty()) {
            return new ArrayList<>();
        }
        Long userId = userAccountApi.currentUserId();
        if (userId == null) {
            records.forEach(item -> item.setWatchCount(0));
            return records;
        }
        List<Long> courseIdList = records.stream().map(CourseRes::getId).toList();
        List<CourseWatchStatisticRes> statList =
                courseChapterWatchRecordRepository.batchStatWatchedChapter(userId, courseIdList);
        Map<Long, Integer> statMap = new HashMap<>(statList.size());
        statList.forEach(item -> statMap.put(item.getCourseId(),
                item.getWatchedChapterCount() == null ? 0 : item.getWatchedChapterCount()));
        records.forEach(item -> item.setWatchCount(statMap.getOrDefault(item.getId(), 0)));
        return records;
    }

    /**
     * 校验课程分类存在
     *
     * @param categoryId 课程分类主键
     */
    private void checkCategory(Long categoryId) {
        ThrowsException.isNull(categoryId, BaseErrorCode.PARAM, "课程分类主键");
        CourseCategoryRes category = courseCategoryRepository.detail(categoryId);
        ThrowsException.isNull(category, BaseErrorCode.NODATA, "课程分类");
    }

    /**
     * 校验存在后更新启用状态
     *
     * @param id        课程主键
     * @param isEnabled 启用状态 1-启用 0-禁用
     * @return 是否成功
     */
    private boolean updateEnabled(Long id, CommonEnum.YesOrNo isEnabled) {
        CourseRes exist = courseRepository.detail(id);
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "课程");
        return courseRepository.updateEnabled(id, isEnabled);
    }
}
