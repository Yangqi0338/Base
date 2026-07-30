package com.newzkl.platform.base.biz.content.model.articlecategory.entity;

import com.newzkl.platform.base.biz.content.model.enums.RecommendGroupEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章分类领域实体
 *
 * <p>迁移自旧 {@code com.zkl.scm.admin.domain.contentManage.model.entity.ArticleCategory}。
 * 本域分类为单层平铺结构, 无父子字段。</p>
 *
 * @author KC
 */
@Data
public class ArticleCategory implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 排序值(小于100, 相同时按创建时间倒序)
     */
    private Integer sort;

    /**
     * 文章数量
     */
    private Integer articleCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否启用(0-禁用, 1-启用)
     */
    private Integer isEnabled;

    /**
     * 推荐人群(逗号分隔)
     *
     * @see RecommendGroupEnum
     */
    private String recommendGroups;
}
