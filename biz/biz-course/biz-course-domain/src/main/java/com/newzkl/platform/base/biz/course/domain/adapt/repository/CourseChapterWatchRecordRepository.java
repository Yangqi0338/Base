package com.newzkl.platform.base.biz.course.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.model.watch.query.CourseChapterWatchRecordQuery;
import com.newzkl.platform.base.biz.course.model.watch.req.CourseChapterWatchRecordReq;
import com.newzkl.platform.base.biz.course.model.watch.res.CourseChapterWatchRecordRes;
import com.newzkl.platform.base.biz.course.model.watch.res.CourseWatchStatisticRes;

import java.util.List;

/**
 * 课程章节观看记录仓储端口
 *
 * @author KC
 */
public interface CourseChapterWatchRecordRepository {

    /**
     * 新增观看记录
     *
     * @param req 观看记录请求
     * @return 记录主键
     */
    Long save(CourseChapterWatchRecordReq req);

    /**
     * 累加观看次数并刷新观看时间
     *
     * @param id 记录主键
     * @return 是否更新成功
     */
    boolean addWatchTimes(Long id);

    /**
     * 按用户与章节查观看记录
     *
     * @param userId          用户ID
     * @param courseChapterId 章节ID
     * @return 观看记录, 不存在返回 null
     */
    CourseChapterWatchRecordRes getByUserIdAndChapterId(Long userId, Long courseChapterId);

    /**
     * 分页查观看记录
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<CourseChapterWatchRecordRes> pageList(CourseChapterWatchRecordQuery query);

    /**
     * 查用户在某课程下已观看的章节记录
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 记录列表, 永远非 null
     */
    List<CourseChapterWatchRecordRes> listWatchedByUserIdAndCourseId(Long userId, Long courseId);

    /**
     * 批量统计用户在多个课程下的已观看章节数
     *
     * @param userId       用户ID
     * @param courseIdList 课程ID列表
     * @return 统计列表, 永远非 null
     */
    List<CourseWatchStatisticRes> batchStatWatchedChapter(Long userId, List<Long> courseIdList);

    /**
     * 逻辑删除观看记录
     *
     * @param idList 记录主键列表
     * @return 是否删除成功
     */
    boolean delete(List<Long> idList);
}
