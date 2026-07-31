package com.newzkl.platform.base.biz.course.infrastructure.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.domain.adapt.repository.CourseChapterWatchRecordRepository;
import com.newzkl.platform.base.biz.course.infrastructure.dao.CourseChapterWatchRecordDAO;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CourseChapterWatchRecordDO;
import com.newzkl.platform.base.biz.course.model.watch.query.CourseChapterWatchRecordQuery;
import com.newzkl.platform.base.biz.course.model.watch.req.CourseChapterWatchRecordReq;
import com.newzkl.platform.base.biz.course.model.watch.res.CourseChapterWatchRecordRes;
import com.newzkl.platform.base.biz.course.model.watch.res.CourseWatchStatisticRes;
import com.newzkl.platform.base.common.core.utils.common.TransferUtils;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import com.newzkl.platform.base.common.ddd.infrastructure.support.RepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 课程章节观看记录仓储实现
 *
 * @author KC
 */
@Repository
@RequiredArgsConstructor
public class CourseChapterWatchRecordRepositoryImpl implements CourseChapterWatchRecordRepository {

    private final CourseChapterWatchRecordDAO courseChapterWatchRecordDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(CourseChapterWatchRecordReq req) {
        CourseChapterWatchRecordDO recordDO = TransferUtils.transfer(req, CourseChapterWatchRecordDO::new);
        recordDO.setIsWatched(1);
        recordDO.setTotalWatchTimes(1);
        recordDO.setWatchTime(LocalDateTime.now());
        courseChapterWatchRecordDAO.insert(recordDO);
        return recordDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addWatchTimes(Long id) {
        return courseChapterWatchRecordDAO.addWatchTimes(id) > 0;
    }

    @Override
    public CourseChapterWatchRecordRes getByUserIdAndChapterId(Long userId, Long courseChapterId) {
        if (userId == null || courseChapterId == null) {
            return null;
        }
        BaseLambdaQueryWrapper<CourseChapterWatchRecordDO> wrapper =
                new BaseLambdaQueryWrapper<CourseChapterWatchRecordDO>()
                        .notNullEq(CourseChapterWatchRecordDO::getUserId, userId)
                        .notNullEq(CourseChapterWatchRecordDO::getCourseChapterId, courseChapterId);
        return toRes(courseChapterWatchRecordDAO.selectOne(wrapper.last("limit 1")));
    }

    @Override
    public Page<CourseChapterWatchRecordRes> pageList(CourseChapterWatchRecordQuery query) {
        Page<CourseChapterWatchRecordDO> page = courseChapterWatchRecordDAO.selectPage(
                RepositorySupport.page(query), courseChapterWatchRecordDAO.getLw(query));
        return TransferUtils.transferPage(page, this::toRes);
    }

    @Override
    public List<CourseChapterWatchRecordRes> listWatchedByUserIdAndCourseId(Long userId, Long courseId) {
        BaseLambdaQueryWrapper<CourseChapterWatchRecordDO> wrapper =
                new BaseLambdaQueryWrapper<CourseChapterWatchRecordDO>()
                        .notNullEq(CourseChapterWatchRecordDO::getUserId, userId)
                        .notNullEq(CourseChapterWatchRecordDO::getCourseId, courseId)
                        .notNullEq(CourseChapterWatchRecordDO::getIsWatched, 1);
        return TransferUtils.transfers(courseChapterWatchRecordDAO.selectList(wrapper), this::toRes);
    }

    @Override
    public List<CourseWatchStatisticRes> batchStatWatchedChapter(Long userId, List<Long> courseIdList) {
        if (userId == null || courseIdList == null || courseIdList.isEmpty()) {
            return Collections.emptyList();
        }
        return courseChapterWatchRecordDAO.batchStatWatchedChapter(userId, courseIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Long> idList) {
        return courseChapterWatchRecordDAO.deleteByIds(idList) > 0;
    }

    /**
     * DO 转 Res, 观看状态描述在此填充
     *
     * @param recordDO 观看记录数据对象
     * @return 观看记录视图, 入参为空返回 null
     */
    private CourseChapterWatchRecordRes toRes(CourseChapterWatchRecordDO recordDO) {
        if (recordDO == null) {
            return null;
        }
        CourseChapterWatchRecordRes res = TransferUtils.transfer(recordDO, CourseChapterWatchRecordRes::new);
        res.setIsWatchedDesc(recordDO.getIsWatched() != null && recordDO.getIsWatched() == 1 ? "已观看" : "未观看");
        return res;
    }
}
