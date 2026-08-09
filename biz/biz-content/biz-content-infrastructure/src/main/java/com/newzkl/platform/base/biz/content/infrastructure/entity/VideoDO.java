package com.newzkl.platform.base.biz.content.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.common.core.model.enums.CommonEnum;
import com.newzkl.platform.base.common.core.mybatis.entity.BaseDO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 视频数据实体
 *
 * @author KC
 */
@Data
@TableName
public class VideoDO extends BaseDO {

    /**
     * 视频名称
     */
    private String name;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 是否显示
     * @ext 0-不显示, 1-显示
     */
    private CommonEnum.YesOrNo isVisible;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 视频封面地址
     */
    private String videoCoverUrl;

    /**
     * 发布人id
     */
    private Long issuerId;
}
