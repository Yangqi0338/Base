package com.newzkl.platform.base.biz.course.model.lecturer.res;

import com.newzkl.platform.base.common.ddd.model.res.BaseRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 讲师出参
 *
 * <p>迁移自 {@code com.zkl.scm.user.domain.lecturer.model.res.LecturerRes}。
 * 偏离说明: 源 {@code createBy}/{@code updateBy} 由 {@code BaseDO#executor} 承担, 不再暴露。</p>
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LecturerRes extends BaseRes {

    /**
     * 讲师名称
     */
    private String lecturerName;

    /**
     * 主体账号, 展示用(如手机号/账号字符串)
     */
    private String mainAccount;

    /**
     * 主体账号ID, 关联主体账号表主键
     */
    private Long mainAccountId;

    /**
     * 讲师分类ID
     */
    private Long lecturerCategoryId;

    /**
     * 讲师分类名称, 冗余存储
     */
    private String lecturerCategoryName;

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
     * 是否启用: 1-启用, 0-禁用
     */
    private Integer isEnabled;

    /**
     * 启用状态描述: 启用/禁用
     */
    private String isEnabledDesc;

    /**
     * 当前登录用户是否已关注该讲师
     */
    private Boolean isFollow = false;
}
