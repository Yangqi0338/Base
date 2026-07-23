package com.newzkl.platform.base.biz.user.model.relation.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户关注领域实体。
 *
 * @author sijiwang
 */
@Data
public class UserFollow {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 关注者ID
     */
    private Long follower;

    /**
     * 被关注者ID
     */
    private Long following;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
