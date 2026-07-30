package com.newzkl.platform.base.biz.course.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.model.follow.query.UserFollowQuery;
import com.newzkl.platform.base.biz.course.model.follow.req.UserFollowReq;
import com.newzkl.platform.base.biz.course.model.follow.res.UserFollowRes;

/**
 * 用户关注讲师领域服务
 *
 * <p>{@code userId} 由 {@code UserAccountApi#currentUserId()} 取当前登录用户。</p>
 *
 * @author KC
 */
public interface UserLecturerFollowDomain {

    /**
     * 关注讲师, 已关注时幂等返回成功
     *
     * @param req 关注请求
     * @return 是否成功
     */
    boolean follow(UserFollowReq req);

    /**
     * 取消关注
     *
     * @param req 关注请求
     * @return 是否成功
     */
    boolean cancelFollow(UserFollowReq req);

    /**
     * 分页查当前用户关注的讲师列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<UserFollowRes> pageQueryFollowList(UserFollowQuery query);
}
