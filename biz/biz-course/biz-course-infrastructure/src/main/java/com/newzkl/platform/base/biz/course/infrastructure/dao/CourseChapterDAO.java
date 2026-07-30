package com.newzkl.platform.base.biz.course.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CourseChapterDO;
import com.newzkl.platform.base.biz.course.model.chapter.query.CourseChapterQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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
        return new BaseLambdaQueryWrapper<CourseChapterDO>()
                .notEmptyIn(CourseChapterDO::getId, query.getIdList())
                .notNullEq(CourseChapterDO::getCourseId, query.getCourseId())
                .notEmptyLike(CourseChapterDO::getTitle, query.getTitle())
                .notNullEq(CourseChapterDO::getIsFree, query.getIsFree())
                .notNullEq(CourseChapterDO::getIsEnabled, query.getIsEnabled())
                .between(CourseChapterDO::getCreateTime, query.getCreateTime())
                .orderByAsc(CourseChapterDO::getChapterNum);
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
}
