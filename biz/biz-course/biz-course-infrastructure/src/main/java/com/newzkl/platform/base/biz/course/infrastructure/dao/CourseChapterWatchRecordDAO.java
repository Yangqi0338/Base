package com.newzkl.platform.base.biz.course.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CourseChapterWatchRecordDO;
import com.newzkl.platform.base.biz.course.model.watch.query.CourseChapterWatchRecordQuery;
import com.newzkl.platform.base.biz.course.model.watch.res.CourseWatchStatisticRes;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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
        BaseLambdaQueryWrapper<CourseChapterWatchRecordDO> wrapper =
                new BaseLambdaQueryWrapper<CourseChapterWatchRecordDO>()
                        .notEmptyIn(CourseChapterWatchRecordDO::getId, query.getIdList())
                        .notNullEq(CourseChapterWatchRecordDO::getUserId, query.getUserId())
                        .notNullEq(CourseChapterWatchRecordDO::getCourseId, query.getCourseId())
                        .notNullEq(CourseChapterWatchRecordDO::getCourseChapterId, query.getCourseChapterId())
                        .notNullEq(CourseChapterWatchRecordDO::getIsWatched, query.getIsWatched())
                        .between(CourseChapterWatchRecordDO::getCreateTime, query.getCreateTime());
        wrapper.orderByDesc(CourseChapterWatchRecordDO::getId);
        return wrapper;
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

    /**
     * 批量统计用户在多个课程下的已观看章节数
     *
     * <p>只统计已观看({@code is_watched = 1})且未删除({@code del_flag = 0})的记录, 按课程分组计数。</p>
     *
     * @param userId       用户ID
     * @param courseIdList 课程ID列表, 调用方保证非空
     * @return 统计列表
     */
    @Select("<script>"
            + "SELECT user_id AS userId, course_id AS courseId, COUNT(*) AS watchedChapterCount "
            + "FROM course_chapter_watch_record "
            + "WHERE del_flag = 0 AND is_watched = 1 AND user_id = #{userId} "
            + "AND course_id IN "
            + "<foreach collection='courseIdList' item='cid' open='(' separator=',' close=')'>#{cid}</foreach> "
            + "GROUP BY user_id, course_id"
            + "</script>")
    List<CourseWatchStatisticRes> batchStatWatchedChapter(@Param("userId") Long userId,
                                                          @Param("courseIdList") List<Long> courseIdList);
}
