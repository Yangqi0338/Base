package com.newzkl.platform.base.biz.course.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 讲师数据对象
 *
 * @author KC
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName
public class LecturerDO extends BaseDO {

    /**
     * 讲师名称
     * @ext 即渠道商名称
     */
    private String lecturerName;

    /**
     * 主体账号
     * @ext 展示用字符串
     */
    private String mainAccount;

    /**
     * 主体账号ID
     * @ext 关联主体账号表主键
     */
    private Long mainAccountId;

    /**
     * 讲师分类ID
     * @ext 关联讲师分类表主键
     */
    private Long lecturerCategoryId;

    /**
     * 讲师分类名称
     * @ext 冗余存储便于展示
     */
    private String lecturerCategoryName;

    /**
     * 课程数量
     */
    private Integer courseCount;

    /**
     * 被关注人数累计值
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
     * 是否启用
     * @ext 0-禁用, 1-启用
     */
    private CommonEnum.YesOrNo isEnabled;

    /**
     * 是否金牌
     * @ext 0-否, 1-是
     */
    private CommonEnum.YesOrNo isTop;
}
