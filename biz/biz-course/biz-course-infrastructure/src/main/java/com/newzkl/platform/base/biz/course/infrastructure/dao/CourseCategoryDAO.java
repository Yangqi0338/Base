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
        BaseLambdaQueryWrapper<CourseCategoryDO> wrapper = new BaseLambdaQueryWrapper<CourseCategoryDO>()
                .notEmptyIn(CourseCategoryDO::getId, query.getIdList())
                .notEmptyLike(CourseCategoryDO::getCategoryName, query.getCategoryName())
                .notNullEq(CourseCategoryDO::getIsEnabled, query.getIsEnabled())
                .between(CourseCategoryDO::getCreateTime, query.getCreateTime());
        wrapper.orderByAsc(CourseCategoryDO::getSort).orderByDesc(CourseCategoryDO::getId);
        return wrapper;
    }
}
