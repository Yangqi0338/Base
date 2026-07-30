package com.newzkl.platform.base.biz.course.domain.adapt.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.course.model.follow.query.UserFollowQuery;
import com.newzkl.platform.base.biz.course.model.follow.req.UserFollowReq;
import com.newzkl.platform.base.biz.course.model.follow.res.UserFollowRes;

import java.util.List;

/**
 * 用户关注讲师仓储端口
 *
 * @author KC
 */
public interface UserLecturerFollowRepository {

    /**
     * 新增关注记录
     *
     * @param req 关注请求
     * @return 记录主键
     */
    Long save(UserFollowReq req);

    /**
     * 判断是否已关注
     *
     * @param userId     用户ID
     * @param lecturerId 讲师ID
     * @return 是否已关注
     */
    boolean exists(Long userId, Long lecturerId);

    /**
     * 取消关注(逻辑删除关注记录)
     *
     * @param userId     用户ID
     * @param lecturerId 讲师ID
     * @return 是否取关成功
     */
    boolean cancel(Long userId, Long lecturerId);

    /**
     * 分页查用户关注的讲师列表
     *
     * <p>关注记录与讲师信息的拼装在领域服务完成, 本方法只返回关注记录本体分页。</p>
     *
     * @param query 查询条件
     * @return 分页结果
     */
    Page<UserFollowRes> pageList(UserFollowQuery query);

    /**
     * 查用户已关注的讲师ID列表
     *
     * @param userId         用户ID
     * @param lecturerIdList 待判定的讲师ID列表
     * @return 已关注的讲师ID列表, 永远非 null
     */
    List<Long> listFollowedLecturerIds(Long userId, List<Long> lecturerIdList);
}
