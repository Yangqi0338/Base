package com.newzkl.platform.base.biz.course.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CourseChapterWatchRecordDO;
import com.newzkl.platform.base.biz.course.model.watch.query.CourseChapterWatchRecordQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 课程章节观看记录 DAO
 *
 * @author KC
 */
@Mapper
public interface CourseChapterWatchRecordDAO extends BaseMapper<CourseChapterWatchRecordDO> {

    /**
     * 构建分页查询条件
     *
     * @param query 查询条件
     * @return 查询条件包装
     */
    default BaseLambdaQueryWrapper<CourseChapterWatchRecordDO> getLw(CourseChapterWatchRecordQuery query) {
        return new BaseLambdaQueryWrapper<CourseChapterWatchRecordDO>()
                .notEmptyIn(CourseChapterWatchRecordDO::getId, query.getIdList())
                .notNullEq(CourseChapterWatchRecordDO::getUserId, query.getUserId())
                .notNullEq(CourseChapterWatchRecordDO::getCourseId, query.getCourseId())
                .notNullEq(CourseChapterWatchRecordDO::getCourseChapterId, query.getCourseChapterId())
                .notNullEq(CourseChapterWatchRecordDO::getIsWatched, query.getIsWatched())
                .between(CourseChapterWatchRecordDO::getCreateTime, query.getCreateTime())
                .orderByDesc(CourseChapterWatchRecordDO::getId);
    }

    /**
     * 累加观看次数并刷新观看时间
     *
     * @param id 记录主键
     * @return 受影响行数
     */
    @Update("UPDATE course_chapter_watch_record SET total_watch_times = IFNULL(total_watch_times, 0) + 1, "
            + "watch_time = NOW(), update_time = NOW() WHERE id = #{id} AND del_flag = 0")
    int addWatchTimes(@Param("id") Long id);
}
