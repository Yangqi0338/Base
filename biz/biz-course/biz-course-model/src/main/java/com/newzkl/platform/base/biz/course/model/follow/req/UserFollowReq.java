package com.newzkl.platform.base.biz.course.model.follow.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户关注/取关讲师入参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.lecturer.model.req.UserFollowOperateReq}。
 * {@code userId} 由 {@code UserAccountApi#currentUserId()} 覆写为当前登录用户,
 * 旧实现走 {@code SecurityUtils.getAccountId()}。</p>
 *
 * @author KC
 */
@Data
public class UserFollowReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull
    private Long userId;

    /**
     * 讲师ID
     */
    @NotNull
    private Long lecturerId;
}
