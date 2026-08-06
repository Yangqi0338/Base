package com.newzkl.platform.base.biz.course.action.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.newzkl.platform.base.biz.course.domain.service.UserLecturerFollowDomain;
import com.newzkl.platform.base.biz.course.model.follow.query.UserFollowQuery;
import com.newzkl.platform.base.biz.course.model.follow.req.UserFollowReq;
import com.newzkl.platform.base.biz.course.model.follow.res.UserFollowRes;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户关注讲师管理
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/lecturer")
@RequiredArgsConstructor
public class UserLecturerFollowController {

    private final UserLecturerFollowDomain userLecturerFollowDomain;

    /**
     * 关注讲师
     *
     * @param req 关注请求
     * @return 是否成功
     */
    @PostMapping("/follow")
    public PlatformResult<Boolean> follow(@Validated @RequestBody UserFollowReq req) {
        return PlatformResult.success(userLecturerFollowDomain.follow(req));
    }

    /**
     * 取消关注讲师
     *
     * @param req 关注请求
     * @return 是否成功
     */
    @PostMapping("/cancel")
    public PlatformResult<Boolean> cancel(@Validated @RequestBody UserFollowReq req) {
        return PlatformResult.success(userLecturerFollowDomain.cancelFollow(req));
    }

    /**
     * 分页查关注讲师列表
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public PlatformResult<IPage<UserFollowRes>> page(@RequestBody UserFollowQuery query) {
        return PlatformResult.success(userLecturerFollowDomain.pageQueryFollowList(query));
    }
}
