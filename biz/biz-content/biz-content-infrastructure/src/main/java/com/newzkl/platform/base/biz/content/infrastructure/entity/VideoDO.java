package com.newzkl.platform.base.biz.content.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 视频数据实体
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.entity.VideoDO}。</p>
 *
 * <p><b>不继承 </b>{@code BaseDO}: 旧 {@code video} 表列由旧 DO 逐字声明, 无基类列。
 * 主键沿用旧 {@code IdType.ASSIGN_ID} 雪花策略。旧表 {@code like_num}/{@code share_num}
 * 不落列, 由跨域统计填充(迁移后降级, 见 {@code VideoDO} 未含该两列)。</p>
 *
 * @author KC
 */
@Data
@TableName(value = "video")
public class VideoDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 视频名称
     */
    @TableField("name")
    private String name;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 是否显示:0-不显示,1-显示
     */
    @TableField("is_visible")
    private Integer isVisible;

    /**
     * 创建人ID
     */
    @TableField("creator_id")
    private Long creatorId;

    /**
     * 创建人姓名
     */
    @TableField("creator_name")
    private String creatorName;

    /**
     * 视频地址
     */
    @TableField("video_url")
    private String videoUrl;

    /**
     * 视频封面地址
     */
    @TableField("video_cover_url")
    private String videoCoverUrl;

    /**
     * 发布人id
     */
    @TableField("issuer_id")
    private Long issuerId;
}
