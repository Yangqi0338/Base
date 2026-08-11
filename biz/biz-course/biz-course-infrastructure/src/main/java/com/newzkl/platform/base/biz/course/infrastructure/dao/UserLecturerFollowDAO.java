package com.newzkl.platform.base.biz.course.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.infrastructure.entity.UserLecturerFollowDO;
import com.newzkl.platform.base.biz.course.model.follow.query.UserFollowQuery;
import com.newzkl.platform.base.biz.course.model.follow.res.UserFollowRes;
import com.newzkl.platform.base.common.core.mybatis.support.BaseLambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户关注讲师 DAO
 *
 * @author KC
 */
@Mapper
public interface UserLecturerFollowDAO extends BaseMapper<UserLecturerFollowDO> {

    /**
     * 构建分页查询条件
     *
     * <p>逻辑删除即取关, MyBatis-Plus 默认过滤, 故本条件天然只返回在关注状态的记录。</p>
     *
     * @param query 查询条件
     * @return 查询条件包装
     */
    default BaseLambdaQueryWrapper<UserLecturerFollowDO> getLw(UserFollowQuery query) {
        BaseLambdaQueryWrapper<UserLecturerFollowDO> wrapper = new BaseLambdaQueryWrapper<UserLecturerFollowDO>()
                .notEmptyIn(UserLecturerFollowDO::getId, query.getIdList())
                .notNullEq(UserLecturerFollowDO::getUserId, query.getUserId())
                .between(UserLecturerFollowDO::getCreateTime, query.getCreateTime());
        wrapper.orderByDesc(UserLecturerFollowDO::getFollowTime);
        return wrapper;
    }

    /**
     * 分页查关注讲师列表(join lecturer 表, 支持 lecturerName 模糊过滤)
     *
     * <p>迁移自 {@code com.zkl.scm.user.infrastructure.dao.UserLecturerFollowDAO#pageQueryFollowList}。
     * 字段由一听 join 查询一次带回(Base lecturer 表已冗余存讲师展示字段, 无需 join channel/lecturer_category)。</p>
     *
     * @param page  分页参数
     * @param query 查询条件(含 userId/lecturerName)
     * @return 分页关注列表
     */
    IPage<UserFollowRes> pageQueryFollowList(Page<UserFollowRes> page, @Param("query") UserFollowQuery query);

    /**
     * 查用户已关注的讲师ID列表
     *
     * <p>只返回未删除({@code del_flag = 0}, 即仍在关注)的记录, 在给定讲师ID范围内取交集。</p>
     *
     * @param userId         用户ID
     * @param lecturerIdList 待判定的讲师ID列表, 调用方保证非空
     * @return 已关注的讲师ID列表
     */
    @Select("<script>"
            + "SELECT lecturer_id FROM user_lecturer_follow "
            + "WHERE del_flag = 0 AND user_id = #{userId} "
            + "AND lecturer_id IN "
            + "<foreach collection='lecturerIdList' item='lid' open='(' separator=',' close=')'>#{lid}</foreach>"
            + "</script>")
    List<Long> listFollowedLecturerIds(@Param("userId") Long userId,
                                       @Param("lecturerIdList") List<Long> lecturerIdList);
}
