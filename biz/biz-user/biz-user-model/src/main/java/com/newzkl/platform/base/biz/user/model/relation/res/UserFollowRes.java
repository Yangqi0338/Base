package com.newzkl.platform.base.biz.user.model.relation.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户关注视图对象
 *
 * <p>迁移自旧 {@code com.zkl.scm.user.domain.follow.model.vo.UserFollowVO}。
 * {@code userId} 语义随查询方向变化: 关注列表回被关注者ID, 粉丝列表回关注者ID。</p>
 *
 * @author KC
 */
@Data
public class UserFollowRes extends BaseRes {

    /**
     * 关联用户ID（关注列表=被关注者；粉丝列表=关注者）
     */
    private Long userId;

    /**
     * 关注时间
     */
    private LocalDateTime followTime;
}
