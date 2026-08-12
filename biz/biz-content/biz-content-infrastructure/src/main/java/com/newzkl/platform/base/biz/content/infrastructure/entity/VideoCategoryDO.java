package com.newzkl.platform.base.biz.content.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 视频分类数据实体
 *
 * @author KC
 */
@Data
@TableName
public class VideoCategoryDO extends BaseDO {

    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序值
     * @ext 小于100
     */
    private Integer sort;

    /**
     * 视频数量
     */

    /**
     * 是否启用
     * @ext 0-禁用, 1-启用
     */
    private CommonEnum.YesOrNo isEnabled;

    /**
     * 推荐人群
     * @ext 逗号分隔
     *
     * @see RecommendGroupEnum
     */
    private String recommendGroups;

    /**
     * 权重值
     */
    private Integer weight;
}
