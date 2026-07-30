package com.newzkl.platform.base.biz.course.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CourseDO;
import com.newzkl.platform.base.biz.course.model.course.query.CourseQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 课程 DAO
 *
 * @author KC
 */
@Mapper
public interface CourseDAO extends BaseMapper<CourseDO> {

    /**
     * 构建分页查询条件
     *
     * @param query 查询条件
     * @return 查询条件包装
     */
    default BaseLambdaQueryWrapper<CourseDO> getLw(CourseQuery query) {
        return new BaseLambdaQueryWrapper<CourseDO>()
                .notEmptyIn(CourseDO::getId, query.getIdList())
                .notEmptyLike(CourseDO::getTitle, query.getTitle())
                .notNullEq(CourseDO::getCategoryId, query.getCategoryId())
                .notNullEq(CourseDO::getLecturerId, query.getLecturerId())
                .notNullEq(CourseDO::getIsEnabled, query.getIsEnabled())
                .between(CourseDO::getCreateTime, query.getCreateTime())
                .orderByDesc(CourseDO::getId);
    }

    /**
     * 实际购买数递增
     *
     * @param id 课程主键
     * @return 受影响行数
     */
    @Update("UPDATE course SET purchase_count = IFNULL(purchase_count, 0) + 1, update_time = NOW() "
            + "WHERE id = #{id} AND del_flag = 0")
    int addPurchaseCount(@Param("id") Long id);

    /**
     * 恢复已逻辑删除的课程
     *
     * @param id 课程主键
     * @return 受影响行数
     */
    @Update("UPDATE course SET del_flag = 0, update_time = NOW() WHERE id = #{id} AND del_flag IS NULL")
    int recoverById(@Param("id") Long id);
}
