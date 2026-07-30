package com.newzkl.platform.base.biz.content.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 视频分类数据实体
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.entity.VideoCategoryDO}。</p>
 *
 * <p><b>不继承 </b>{@code BaseDO}: 旧 {@code video_category} 表<b>无</b>
 * {@code update_time} / {@code del_flag} / {@code executor} 列, 继承会拼入不存在的列。</p>
 *
 * @author KC
 */
@Data
@TableName("video_category")
public class VideoCategoryDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    @TableField("name")
    private String name;

    /**
     * 排序值(小于100)
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 视频数量
     */
    @TableField("video_count")
    private Integer videoCount;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 是否启用(0-禁用, 1-启用)
     */
    @TableField("is_enabled")
    private Integer isEnabled;

    /**
     * 推荐人群(逗号分隔)
     *
     * @see RecommendGroupEnum
     */
    @TableField("recommend_groups")
    private String recommendGroups;

    /**
     * 权重值
     */
    private Integer weight;
}
