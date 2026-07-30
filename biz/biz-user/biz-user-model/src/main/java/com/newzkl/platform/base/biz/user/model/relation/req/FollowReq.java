package com.newzkl.platform.base.biz.user.model.relation.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 关注操作请求
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.follow.model.req.FollowReq}。
 * 关注者由鉴权上下文取当前账号, 入参只带被关注者。</p>
 *
 * @author KC
 */
@Data
public class FollowReq {

    /**
     * 被关注者ID
     */
    @NotNull(message = "被关注用户ID不能为空")
    private Long followingId;
}
