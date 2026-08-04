package com.newzkl.platform.base.biz.content.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章数据实体
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.infrastructure.entity.ArticleDO}。</p>
 *
 * <p><b>不继承 </b>{@code BaseDO}: 旧 {@code article} 表列由旧 DO 逐字声明,
 * 无 {@code update_time}/{@code del_flag} 等基类列, 继承会令自动填充拼入不存在的列。</p>
 *
 * @author KC
 */
@Data
@TableName(value = "article", autoResultMap = true)
public class ArticleDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章标题
     */
    @TableField("title")
    private String title;

    /**
     * 文章内容
     */
    @TableField("content")
    private String content;

    /**
     * 封面图URL
     */
    @TableField("cover_image")
    private String coverImage;

    /**
     * 海报轮播图URL列表(JSON数组)
     */
    @TableField(value = "poster_images", typeHandler = JacksonTypeHandler.class)
    private List<String> posterImages;

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
     * 发布人id
     */
    @TableField("issuer_id")
    private Long issuerId;

    /**
     * 发布人
     */
    @TableField("issuer")
    private String issuer;
}
