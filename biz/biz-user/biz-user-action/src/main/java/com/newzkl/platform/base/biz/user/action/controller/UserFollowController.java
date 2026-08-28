package com.newzkl.platform.base.biz.user.action.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.newzkl.platform.base.biz.user.domain.service.UserFollowDomain;
import com.newzkl.platform.base.biz.user.model.relation.req.FollowReq;
import com.newzkl.platform.base.biz.user.model.relation.query.UserFollowQuery;
import com.newzkl.platform.base.biz.user.model.relation.res.UserFollowRes;
import com.newzkl.platform.base.common.ddd.action.auth.FuncPermission;
import com.newzkl.platform.base.common.ddd.model.auth.SecurityUtils;
import com.newzkl.platform.base.common.core.model.res.PlatformResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户-关注
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.interfaces.controller.UserFollowController}。
 * 类级路径与方法级路径逐字沿用旧契约。</p>
 *
 * <p>迁移说明: 旧 followingPage/followerPage 回 {@code Page<UserFollowVO>}, 本仓保留
 * {@code Page} 分页壳直返。<b>前端契约变</b> (出参 list → records)。</p>
 *
 * @author KC
 */
@RestController
@RequestMapping("/user/follow")
@Slf4j
@RequiredArgsConstructor
@FuncPermission("用户关注")
public class UserFollowController {

    private final UserFollowDomain userFollowDomain;

    /**
     * 关注用户
     *
     * @param req 关注入参
     * @return 是否成功
     */
    @PostMapping("/follow")
    @FuncPermission("关注用户")
    public PlatformResult<Boolean> follow(@Validated @RequestBody FollowReq req) {
        Long follower = SecurityUtils.getAccountId();
        log.info("用户{}关注用户{}", follower, req.getFollowingId());
        return PlatformResult.success(userFollowDomain.follow(follower, req.getFollowingId()));
    }

    /**
     * 取消关注
     *
     * @param req 关注入参
     * @return 是否成功
     */
    @PostMapping("/unfollow")
    @FuncPermission("取消关注")
    public PlatformResult<Boolean> unfollow(@Validated @RequestBody FollowReq req) {
        Long follower = SecurityUtils.getAccountId();
        log.info("用户{}取消关注用户{}", follower, req.getFollowingId());
        return PlatformResult.success(userFollowDomain.unfollow(follower, req.getFollowingId()));
    }

    /**
     * 检查是否已关注
     *
     * @param req 关注入参
     * @return 是否已关注
     */
    @PostMapping("/isFollowed")
    public PlatformResult<Boolean> isFollowed(@Validated @RequestBody FollowReq req) {
        Long follower = SecurityUtils.getAccountId();
        return PlatformResult.success(userFollowDomain.isFollowed(follower, req.getFollowingId()));
    }

    /**
     * 关注列表（我关注的人）
     *
     * @return 关注列表
     */
    @PostMapping("/followingList")
    public PlatformResult<List<UserFollowRes>> getFollowingList() {
        Long userId = SecurityUtils.getAccountId();
        log.info("查询用户{}的关注列表", userId);
        return PlatformResult.success(userFollowDomain.getFollowingList(userId));
    }

    /**
     * 分页关注列表（我关注的人）
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 当前无前端仓在调此端点</p>
     *
     * @param query 分页查询
     * @return 关注分页
     */
    @PostMapping("/followingPage")
    public PlatformResult<Page<UserFollowRes>> getFollowingPage(@RequestBody UserFollowQuery query) {
        query.setUserId(SecurityUtils.getAccountId());
        log.info("分页查询用户{}的关注列表，pageNo={}, pageSize={}", query.getUserId(), query.getPageNo(), query.getPageSize());
        return PlatformResult.success(userFollowDomain.getFollowingPage(query));
    }

    /**
     * 粉丝列表（关注我的人）
     *
     * @return 粉丝列表
     */
    @PostMapping("/followerList")
    public PlatformResult<List<UserFollowRes>> getFollowerList() {
        Long userId = SecurityUtils.getAccountId();
        log.info("查询用户{}的粉丝列表", userId);
        return PlatformResult.success(userFollowDomain.getFollowerList(userId));
    }

    /**
     * 分页粉丝列表（关注我的人）
     *
     * <p>⚠️ 出参契约: 旧接口返 PageHelper 的 {@code PageInfo}
     * ({@code list}/{@code total}/{@code pageNum}/{@code pageSize}),
     * 本仓按 {@code rules/Architecture.md} 改为 MyBatis-Plus {@code Page}
     * ({@code records}/{@code total}/{@code current}/{@code size})。
     * <b>前端需把 {@code res.data.list} 改成 {@code res.data.records}</b> ——
     * 当前无前端仓在调此端点</p>
     *
     * @param query 分页查询
     * @return 粉丝分页
     */
    @PostMapping("/followerPage")
    public PlatformResult<Page<UserFollowRes>> getFollowerPage(@RequestBody UserFollowQuery query) {
        query.setUserId(SecurityUtils.getAccountId());
        log.info("分页查询用户{}的粉丝列表，pageNo={}, pageSize={}", query.getUserId(), query.getPageNo(), query.getPageSize());
        return PlatformResult.success(userFollowDomain.getFollowerPage(query));
    }

    /**
     * 关注数（我关注的人数）
     *
     * @param userId 用户ID, 不传取当前账号
     * @return 关注数
     */
    @GetMapping("/followingCount")
    public PlatformResult<Integer> getFollowingCount(@RequestParam(required = false) Long userId) {
        Long queryUserId = userId != null ? userId : SecurityUtils.getAccountId();
        return PlatformResult.success(userFollowDomain.getFollowingCount(queryUserId));
    }

    /**
     * 粉丝数（关注我的人数）
     *
     * @param userId 用户ID, 不传取当前账号
     * @return 粉丝数
     */
    @GetMapping("/followerCount")
    public PlatformResult<Integer> getFollowerCount(@RequestParam(required = false) Long userId) {
        Long queryUserId = userId != null ? userId : SecurityUtils.getAccountId();
        return PlatformResult.success(userFollowDomain.getFollowerCount(queryUserId));
    }
}
