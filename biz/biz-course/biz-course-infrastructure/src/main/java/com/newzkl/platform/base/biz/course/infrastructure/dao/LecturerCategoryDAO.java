package com.newzkl.platform.base.biz.course.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.course.infrastructure.entity.LecturerCategoryDO;
import com.newzkl.platform.base.biz.course.model.lecturercategory.query.LecturerCategoryQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 讲师分类 DAO
 *
 * @author KC
 */
@Mapper
public interface LecturerCategoryDAO extends BaseMapper<LecturerCategoryDO> {

    /**
     * 构建分页查询条件
     *
     * @param query 查询条件
     * @return 查询条件包装
     */
    default BaseLambdaQueryWrapper<LecturerCategoryDO> getLw(LecturerCategoryQuery query) {
        return new BaseLambdaQueryWrapper<LecturerCategoryDO>()
                .notEmptyIn(LecturerCategoryDO::getId, query.getIdList())
                .notEmptyLike(LecturerCategoryDO::getCategoryName, query.getCategoryName())
                .notNullEq(LecturerCategoryDO::getIsEnabled, query.getIsEnabled())
                .between(LecturerCategoryDO::getCreateTime, query.getCreateTime())
                .orderByDesc(LecturerCategoryDO::getId);
    }
}
