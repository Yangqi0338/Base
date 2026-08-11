package com.newzkl.platform.base.biz.course.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CourseDO;
import com.newzkl.platform.base.biz.course.model.course.query.CourseQuery;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

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
        BaseLambdaQueryWrapper<CourseDO> wrapper = new BaseLambdaQueryWrapper<CourseDO>()
                .notEmptyIn(CourseDO::getId, query.getIdList())
                .notEmptyLike(CourseDO::getTitle, query.getTitle())
                .notNullEq(CourseDO::getCategoryId, query.getCategoryId())
                .notNullEq(CourseDO::getLecturerId, query.getLecturerId())
                .notNullEq(CourseDO::getIsEnabled, query.getIsEnabled())
                .between(CourseDO::getCreateTime, query.getCreateTime());
        wrapper.orderByDesc(CourseDO::getId);
        return wrapper;
    }
}
