package com.newzkl.platform.base.biz.course.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CourseChapterDO;
import com.newzkl.platform.base.biz.course.model.chapter.query.CourseChapterQuery;
import com.newzkl.platform.base.biz.course.model.chapter.res.CourseChapterStatRes;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 课程章节 DAO
 *
 * @author KC
 */
@Mapper
public interface CourseChapterDAO extends BaseMapper<CourseChapterDO> {

    /**
     * 构建分页查询条件
     *
     * @param query 查询条件
     * @return 查询条件包装
     */
    default BaseLambdaQueryWrapper<CourseChapterDO> getLw(CourseChapterQuery query) {
        BaseLambdaQueryWrapper<CourseChapterDO> wrapper = new BaseLambdaQueryWrapper<CourseChapterDO>()
                .notEmptyIn(CourseChapterDO::getId, query.getIdList())
                .notNullEq(CourseChapterDO::getCourseId, query.getCourseId())
                .notEmptyLike(CourseChapterDO::getTitle, query.getTitle())
                .notNullEq(CourseChapterDO::getIsFree, query.getIsFree())
                .notNullEq(CourseChapterDO::getIsEnabled, query.getIsEnabled())
                .between(CourseChapterDO::getCreateTime, query.getCreateTime());
        wrapper.orderByAsc(CourseChapterDO::getChapterNum);
        return wrapper;
    }

    /**
     * 恢复已逻辑删除的章节
     *
     * @param id 章节主键
     * @return 受影响行数
     */
    @Update("UPDATE course_chapter SET del_flag = 0, update_time = NOW() "
            + "WHERE id = #{id} AND del_flag IS NULL")
    int recoverById(@Param("id") Long id);

    /**
     * 批量统计课程有效章节数与总时长
     *
     * <p>只统计启用({@code is_enabled = 1})且未删除({@code del_flag = 0})的章节。
     * {@code totalDurationSeconds} 由仓储实现按百分秒换算填充, 此处只返回主键/章节数/总百分秒。</p>
     *
     * @param courseIdList 课程主键列表, 调用方保证非空
     * @return 统计列表
     */
    @Select("<script>"
            + "SELECT course_id AS courseId, COUNT(*) AS chapterCount, "
            + "IFNULL(SUM(duration_centisecond), 0) AS totalDurationCentisecond "
            + "FROM course_chapter "
            + "WHERE del_flag = 0 AND is_enabled = 1 "
            + "AND course_id IN "
            + "<foreach collection='courseIdList' item='cid' open='(' separator=',' close=')'>#{cid}</foreach> "
            + "GROUP BY course_id"
            + "</script>")
    List<CourseChapterStatRes> batchStat(@Param("courseIdList") List<Long> courseIdList);
}
