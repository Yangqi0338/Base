package com.newzkl.platform.base.biz.course.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.course.infrastructure.entity.CourseCategoryDO;
import com.newzkl.platform.base.biz.course.model.category.query.CourseCategoryQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 课程分类 DAO
 *
 * @author KC
 */
@Mapper
public interface CourseCategoryDAO extends BaseMapper<CourseCategoryDO> {

    /**
     * 构建分页查询条件
     *
     * @param query 查询条件
     * @return 查询条件包装
     */
    default BaseLambdaQueryWrapper<CourseCategoryDO> getLw(CourseCategoryQuery query) {
        return new BaseLambdaQueryWrapper<CourseCategoryDO>()
                .notEmptyIn(CourseCategoryDO::getId, query.getIdList())
                .notEmptyLike(CourseCategoryDO::getCategoryName, query.getCategoryName())
                .notNullEq(CourseCategoryDO::getIsEnabled, query.getIsEnabled())
                .between(CourseCategoryDO::getCreateTime, query.getCreateTime())
                .orderByAsc(CourseCategoryDO::getSort)
                .orderByDesc(CourseCategoryDO::getId);
    }

    /**
     * 恢复已逻辑删除的分类
     *
     * <p>逻辑删除后 {@code del_flag} 为 NULL, MyBatis-Plus 常规更新会被逻辑删除条件过滤,
     * 故以注解 SQL 直接复位。</p>
     *
     * @param id 分类主键
     * @return 受影响行数
     */
    @Update("UPDATE course_category SET del_flag = 0, update_time = NOW() WHERE id = #{id} AND del_flag IS NULL")
    int recoverById(@Param("id") Long id);
}
