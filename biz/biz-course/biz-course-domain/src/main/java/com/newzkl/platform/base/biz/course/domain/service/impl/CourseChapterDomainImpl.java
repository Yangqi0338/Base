package com.newzkl.platform.base.biz.course.domain.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseChapterRepository;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseRepository;
import com.newzkl.platform.base.biz.course.domain.service.CourseChapterDomain;
import com.newzkl.platform.base.biz.course.domain.service.CourseChapterWatchRecordDomain;
import com.newzkl.platform.base.biz.course.domain.service.CourseDomain;
import com.newzkl.platform.base.biz.course.model.chapter.query.CourseChapterQuery;
import com.newzkl.platform.base.biz.course.model.chapter.req.CourseChapterReq;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterRes;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterStatRes;
import com.newzkl.platform.base.biz.course.model.course.res.CourseRes;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.model.exception.BaseErrorCode;
import com.newzkl.platform.base.common.core.model.exception.ThrowsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程章节领域服务实现
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.course.service.impl.CourseChapterDomainServiceImpl}
 * 与应用服务 {@code CourseChapterServiceImpl} 的合并结果。</p>
 *
 * <p>时长入参单位秒(保留 2 位小数), 由仓储层换算为百分秒落库; 章节增删改后回刷课程侧
 * 章节数与总时长冗余字段, 与旧实现一致。</p>
 *
 * @author KC
 */
@Service
@RequiredArgsConstructor
public class CourseChapterDomainImpl implements CourseChapterDomain {

    private final CourseChapterRepository courseChapterRepository;

    private final CourseRepository courseRepository;

    private final CourseDomain courseDomain;

    private final CourseChapterWatchRecordDomain courseChapterWatchRecordDomain;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseChapterRes add(CourseChapterReq req) {
        checkCourse(req.getCourseId());
        ThrowsException.isTrue(
                courseChapterRepository.existsChapterNum(req.getCourseId(), req.getChapterNum(), null),
                BaseErrorCode.EXIST_DATA, "该课程下同章节数的章节");
        req.setId(null);
        Long id = courseChapterRepository.save(req);
        courseDomain.refreshChapterStat(req.getCourseId());
        return courseChapterRepository.detail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseChapterRes edit(CourseChapterReq req) {
        ThrowsException.isNull(req.getId(), BaseErrorCode.PARAM, "章节主键");
        CourseChapterRes exist = courseChapterRepository.detail(req.getId());
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "课程章节");
        checkCourse(req.getCourseId());
        ThrowsException.isTrue(
                courseChapterRepository.existsChapterNum(req.getCourseId(), req.getChapterNum(), req.getId()),
                BaseErrorCode.EXIST_DATA, "该课程下同章节数的章节");
        courseChapterRepository.save(req);
        courseDomain.refreshChapterStat(req.getCourseId());
        return courseChapterRepository.detail(req.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enable(Long id) {
        CourseChapterRes exist = getExist(id);
        return courseChapterRepository.updateEnabled(exist.getId(), CommonEnum.YesOrNo.YES);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disable(Long id) {
        CourseChapterRes exist = getExist(id);
        return courseChapterRepository.updateEnabled(exist.getId(), CommonEnum.YesOrNo.NO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setFree(Long id) {
        CourseChapterRes exist = getExist(id);
        return courseChapterRepository.updateFree(exist.getId(), CommonEnum.YesOrNo.YES);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setCharge(Long id) {
        CourseChapterRes exist = getExist(id);
        return courseChapterRepository.updateFree(exist.getId(), CommonEnum.YesOrNo.NO);
    }

    @Override
    public CourseChapterRes getById(Long id) {
        CourseChapterRes res = getExist(id);
        courseChapterWatchRecordDomain.bufferWatch(res);
        return res;
    }

    @Override
    public IPage<CourseChapterRes> pageQuery(CourseChapterQuery query) {
        return courseChapterRepository.pageList(query);
    }

    @Override
    public List<CourseChapterRes> listByCourseNum(String courseNum) {
        ThrowsException.isBlank(courseNum, "课程编码");
        CourseRes course = courseRepository.getByCourseNum(courseNum);
        ThrowsException.isNull(course, BaseErrorCode.NODATA, "课程");
        return courseChapterRepository.listByCourseId(course.getId());
    }

    @Override
    public Map<Long, CourseChapterStatRes> batchStatChapter(List<Long> courseIdList) {
        if (courseIdList == null || courseIdList.isEmpty()) {
            return new HashMap<>(0);
        }
        List<CourseChapterStatRes> statList = courseChapterRepository.batchStat(courseIdList);
        Map<Long, CourseChapterStatRes> statMap = new HashMap<>(statList.size());
        statList.forEach(item -> statMap.put(item.getCourseId(), item));
        return statMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return batchDelete(Collections.singletonList(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Long> idList) {
        ThrowsException.isTrue(idList == null || idList.isEmpty(), BaseErrorCode.PARAM, "章节主键列表");
        List<Long> courseIdList = idList.stream().map(id -> getExist(id).getCourseId()).distinct().toList();
        boolean deleted = courseChapterRepository.delete(idList);
        courseIdList.forEach(courseDomain::refreshChapterStat);
        return deleted;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recover(Long id) {
        ThrowsException.isNull(id, BaseErrorCode.PARAM, "章节主键");
        boolean recovered = courseChapterRepository.recover(id);
        CourseChapterRes chapter = courseChapterRepository.detail(id);
        if (chapter != null) {
            courseDomain.refreshChapterStat(chapter.getCourseId());
        }
        return recovered;
    }

    /**
     * 取存在的章节, 不存在抛业务异常
     *
     * @param id 章节主键
     * @return 章节视图
     */
    private CourseChapterRes getExist(Long id) {
        CourseChapterRes exist = courseChapterRepository.detail(id);
        ThrowsException.isNull(exist, BaseErrorCode.NODATA, "课程章节");
        return exist;
    }

    /**
     * 校验所属课程存在
     *
     * @param courseId 课程主键
     */
    private void checkCourse(Long courseId) {
        ThrowsException.isNull(courseId, BaseErrorCode.PARAM, "课程主键");
        ThrowsException.isNull(courseRepository.detail(courseId), BaseErrorCode.NODATA, "课程");
    }
}
