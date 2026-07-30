package com.newzkl.platform.base.biz.course.model.follow.res;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户关注讲师列表出参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.lecturer.model.res.UserFollowRes}。
 * 关注记录主键沿用旧字段名 {@code followId}(非 {@code id}), 故不继承
 * {@code BaseRes}, 保持前端契约不变。</p>
 *
 * @author KC
 */
@Data
public class UserFollowRes implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关注记录ID
     */
    private Long followId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 讲师ID
     */
    private Long lecturerId;

    /**
     * 关注时间
     */
    private LocalDateTime followTime;

    /**
     * 讲师名称, 即渠道商名称
     */
    private String lecturerName;

    /**
     * 主体账号, 展示用
     */
    private String mainAccount;

    /**
     * 主体账号ID
     */
    private Long mainAccountId;

    /**
     * 课程数量
     */
    private Integer courseCount;

    /**
     * 被关注人数
     */
    private Integer followCount;

    /**
     * 个人简介
     */
    private String personalIntro;

    /**
     * 封面图URL
     */
    private String coverImageUrl;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 渠道商头像
     *
     * <p>TODO[infra-gap]: 旧实现由 account 域回填, 跨域 provider 链未接线, 当前恒为 null。</p>
     */
    private String channelHeadImg;

    /**
     * 讲师分类ID
     */
    private String categoryId;

    /**
     * 讲师分类名称
     */
    private String categoryName;
}
