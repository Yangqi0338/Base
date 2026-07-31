package com.newzkl.platform.base.biz.course.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newzkl.platform.base.biz.course.infrastructure.entity.LecturerDO;
import com.newzkl.platform.base.biz.course.model.lecturer.query.LecturerQuery;
import com.newzkl.platform.base.common.ddd.infrastructure.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 讲师 DAO
 *
 * @author KC
 */
@Mapper
public interface LecturerDAO extends BaseMapper<LecturerDO> {

    /**
     * 构建分页查询条件
     *
     * @param query 查询条件
     * @return 查询条件包装
     */
    default BaseLambdaQueryWrapper<LecturerDO> getLw(LecturerQuery query) {
        BaseLambdaQueryWrapper<LecturerDO> wrapper = new BaseLambdaQueryWrapper<LecturerDO>()
                .notEmptyIn(LecturerDO::getId, query.getIdList())
                .notEmptyLike(LecturerDO::getLecturerName, query.getLecturerName())
                .notEmptyLike(LecturerDO::getMainAccount, query.getMainAccount())
                .notEmptyIn(LecturerDO::getMainAccountId, query.getMainAccountIds())
                .notNullEq(LecturerDO::getLecturerCategoryId, query.getLecturerCategoryId())
                .notNullEq(LecturerDO::getIsEnabled, query.getIsEnabled())
                .between(LecturerDO::getCreateTime, query.getCreateTime());
        wrapper.orderByDesc(LecturerDO::getId);
        return wrapper;
    }

    /**
     * 关注数增减
     *
     * <p>以 SQL 原子增减避免读改写竞态, 结果不小于 0。</p>
     *
     * @param id    讲师主键
     * @param delta 增量, 取关传负数
     * @return 受影响行数
     */
    @Update("UPDATE lecturer SET follow_count = GREATEST(IFNULL(follow_count, 0) + #{delta}, 0), "
            + "update_time = NOW() WHERE id = #{id} AND del_flag = 0")
    int addFollowCount(@Param("id") Long id, @Param("delta") int delta);
}
